package vokaalipeli.peli;

import java.io.IOException;
import java.util.ArrayList;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import vokaalipeli.domain.AaniLahde;
import vokaalipeli.kayttoliittyma.Taajuuskayra;
import vokaalipeli.laskenta.FastFourierMuokkaaja;

/**
 * Luokka sisältää keskeisimmät osat koko ohjelmasta. Se ottaa talteen 
 * äänisyötteen AudioInputStreamistä, lähettää sen muokattavaksi 
 * FastFourierMuokkaajalle ja lähettää arvot taajuuskäyrälle.
 * 
 * TODO: ...ja pitää myös kirjaa kullakin hetkellä tavoiteltavasta vokaalista
 * TODO: ...ja vastaa myös uuden vokaalin pyytämisestä VokaaliInventoriolta
 *
 * TODO: käytettävän ikkunafunktion asettaminen
 * 
 * @author A J Salmi
 */
public class Vokaalipeli {

    private FastFourierMuokkaaja muokkaaja;
    private Taajuuskayra kayra;
    private AudioInputStream striimi;
    private int aikaikkunanPituus;        // täytyy olla kakkosen potenssi
    private double siirtyma;           // paljonko aikaikkunaa siirretään kerralla
    private boolean jatkuu;

    public void setAanilahde(AaniLahde a) {
        this.striimi = a.getStriimi();
    }

    public AudioInputStream getStriimi() {
        return this.striimi;
    }

    /**
     * Pysäyttää pelin.
     */
    public void pysayta() {
        this.jatkuu = false;
    }

    /**
     * Metodi käynnistää pelin ensin luomalla uuden ArrayListin käsiteltäville 
     * aikaikkunoille, siirtyy whle-looppiin ja pysyy niin kauan kunnes peli pysäytetään.
     * Loopin sisällä luetaan arvoja InputStreamistä, laitetaan ne jokaiseen 
     * käsittelyssä olevaan aikaikkunaan sopivalla korjauksella ('Hann window function')
     * joka heikentää signaalia ikkunan molemmista päistä. Kun ikkuna on täynnä, 
     * lähetetään se FFT-muokkaajalle käsittelyyn saaduista arvoista (kompleksilukuja!)
     * lasketut amplitudit lähetetään taajuuskäyrälle päivitykseen.
     * 
     * @see FastFourierMuokkaaja
     * @see Taajuuskayra
     */
    public void kaynnista() {        
        this.jatkuu = true;

        AudioFormat formaatti = striimi.getFormat();
        this.siirtyma = formaatti.getSampleRate() / 180; // jaetaan arvojen saapumistaajuudella
        int bytesPerFrame = formaatti.getFrameSize();
        boolean bigEndian = formaatti.isBigEndian();

        int laskuri = 0;
        ArrayList<double[][]> kasiteltavatAikaikkunat = new ArrayList<>();

        while (this.jatkuu) {
            double luettuArvo = lueArvo(bigEndian, bytesPerFrame);

            if (laskuri >= siirtyma) {
                luoUusiAikaikkuna(kasiteltavatAikaikkunat);
                laskuri = 0;
            }

            int indeksi;
            for (int i = 0; i < kasiteltavatAikaikkunat.size(); i++) {
                double[][] ikkuna = kasiteltavatAikaikkunat.get(i);
                indeksi = (int) ikkuna[1][0];

                if (indeksi == aikaikkunanPituus -1) {
                    ikkuna[1] = new double[aikaikkunanPituus];
                    ikkuna = this.muokkaaja.muokkaaFFT(ikkuna, true);
                    double[] amplitudit = laskeAmplitudit(ikkuna);
                    kayra.setArvot(amplitudit);
                    kasiteltavatAikaikkunat.remove(i);
                }
                ikkuna[0][indeksi] = luettuArvo*laskeIkkunafunktio(indeksi, 0); // !!!! kysy käyttäjältä!!!
                ikkuna[1][0] = indeksi + 1; // talletetaan indeksi käyttämättömään imaginaaritaulukkoon
            }
            laskuri++;
        }
    }

    public void setAikaikkunanKoko(int ikkunanKoko) {
        if (onKakkosenPotenssi(ikkunanKoko)) {
            this.aikaikkunanPituus = ikkunanKoko;
        }
    }

    public void setFastFourierMuokkaaja(FastFourierMuokkaaja muokkaaja) {
        this.muokkaaja = muokkaaja;
    }

/**
 * Metodi asettaa aikaikkunan siirtymän, eli kuika monen näytteen (='sample') jälkeen 
 * aloitetaan täyttää uutta ikkunaa. Koska ikkunoiden pituus pysyy samana, se myös 
 * määrittää suoraan taajuuden jolla aikaikkunoita valmistuu. Esim. jos näytteenottotaajuus 
 * on 16000Hz (=16000/s) ja uusi ikkuna aloitetaan 200 näytteen välein, niin valmiita ikkunoita
 * tulee 16000/200 = 80 kertaa sekunnissa (eli taajuuella 80Hz).
 * 
 * @param siirtyma aikaikkunan siirtymä
 */
    public void setSiirtyma(int siirtyma) {
        if (siirtyma < 1) {
            this.siirtyma = 1;
        } else {
            this.siirtyma = siirtyma;
        }
    }

    public void setTaajuuskayra(Taajuuskayra kayra) {
        this.kayra = kayra;
    }

    /**
     * Laskee onko annettu luku kakkosen potenssi.
     * 
     * @param luku tarkasteltava luku
     * @return tieto siitä onko annettu luku kakkosen potenssi 
     */
    private boolean onKakkosenPotenssi(int luku) {
        if (luku <= 0) {
            return false;
        }
        double logaritmi = Math.log(luku) / Math.log(2); // <-- logaritmien laskukaavasta
        if (((int) logaritmi) - logaritmi == 0) {
            return true;
        }
        return false;
    }

    /**
     * Metodi laskee amplitudit kaikille taulukon arvoille reaaliosan 
     * ja imaginaariosan euklidisena normina: (re^2 + im^2)^(0.5)
     * 
     * @param analysoitava analysoitava (reaali + imag) taulukko
     * @return eri taajuuksien amplitudit
     */
    private double[] laskeAmplitudit(double[][] analysoitava) {
        double[] amplitudit = new double[analysoitava[0].length / 2];
        double a;
        for (int i = 0; i < amplitudit.length; i++) {
            a = analysoitava[0][i + 1] * analysoitava[0][i + 1]
                    + analysoitava[1][i + 1] * analysoitava[1][i + 1];
            amplitudit[i] = Math.sqrt(a);
        }
        return amplitudit;
    }

    /**
     * Metodi laskee ikkunafunktion arvon . Ikkunafunktion tarkoitus on 
     * heikentää signaalia aikaikkunan reunoilta, koska silloin FFT-muunnoksella 
     * laskettu taajuuskäyrä on selkeämpi eikä  taajuuspiikki valuu ympärillään oleviin
     * taajuuksiin (ns. 'spectral leakage').
     * 
     * @param i sen taulukon indeksi, johon arvo talletetaan
     * @param muoto käytetty ikkunafunktio (0: ei mitään/suorakulmainen, 1: kolmio, 2:Hann window) 
     * @return ikkunafunktion korjauskerroin (nollan ja ykkösen välillä)
     */    
    private double laskeIkkunafunktio(int i, int muoto) {
        if (muoto==2) return 0.5 *(1 - Math.cos(2 * i * Math.PI / (aikaikkunanPituus - 1))); // Hann window
        if (muoto==1) return (1-Math.abs(i-(aikaikkunanPituus-1.0)/2)/((aikaikkunanPituus+1.0)/2)); // kolmio
        if (muoto==0)return 1; // suorakulmainen ikkuna
        return 0; 
    }

    /**
     * Metodi lukee striimistä seuraavan arvon, mitä ennen sen pitää mahdollisesti 
     * käsitellä sitä riippuen äänisyötteen formaatista.
     * 
     * @param bigEndian arvojen esitysjärjestys 'big-endian' vai 'little-endian'
     * @param tavuaPerNayte tavujen määrä syötteessä: 1 (= 8-bittinen) tai 2 (= 16-bittinen)
     * @return seuraava luettu arvo striimistä
     */
    private double lueArvo(boolean bigEndian, int tavuaPerNayte) {
        double arvo = 0;
        try {
            if (tavuaPerNayte == 2) {
                byte[] lukupari = new byte[2];
                                
                striimi.read(lukupari);
                if (bigEndian) {
                    arvo = Math.pow(2, 8) * lukupari[0] + lukupari[1];
                } else {
                    arvo = lukupari[0] + Math.pow(2, 8) * lukupari[1];
                }
            } else {    // toisinsanoen: if (tavuaPerNayte==1)
                arvo = striimi.read();
            }
        } catch (IOException exc) {
        }

        return arvo;
    }

    /**
     * Luo uuden tyhjän aikaikkunan ja laittaa sen käsiteltävien aikaikkunoiden listaan.
     * 
     * @param kasiteltavatAikaikkunat käsiteltävien aikaikkonoidne lista
     */
    private void luoUusiAikaikkuna(ArrayList<double[][]> kasiteltavatAikaikkunat) {
        double[][] lisattava = new double[2][1];
        lisattava[0] = new double[aikaikkunanPituus];
        kasiteltavatAikaikkunat.add(lisattava);
    }
}
