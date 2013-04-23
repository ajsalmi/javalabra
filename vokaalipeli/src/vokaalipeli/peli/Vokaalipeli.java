package vokaalipeli.peli;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import vokaalipeli.kayttoliittyma.AaniLahde;
import vokaalipeli.kayttoliittyma.Kayttoliittyma;
import vokaalipeli.laskenta.FastFourierMuokkaaja;
import vokaalipeli.laskenta.Ikkunafunktio;
import vokaalipeli.laskenta.IkkunafunktionLaskija;
import vokaalipeli.laskenta.KeskiarvonLaskija;

/**
 * Luokka sisältää keskeisimmät osat koko ohjelmasta. Se ottaa talteen
 * äänisyötteen AaniLahde-rajapinnan toteuttavan olion AudioInputStreamistä, 
 * lähettää sen muokattavaksi FastFourierMuokkaajalle ja lähettää arvot 
 * käyttikselle.
 *
 * TODO: ...ja pitää myös kirjaa kullakin hetkellä tavoiteltavasta vokaalista
 * TODO: ...ja vastaa myös uuden vokaalin pyytämisestä VokaaliInventoriolta ja 
 *          lähettämisestä käyttikselle
 *
 * @author A J Salmi
 */
public class Vokaalipeli {

    private Kayttoliittyma kayttis;
    private FastFourierMuokkaaja muokkaaja;
    private KeskiarvonLaskija keskiarvonLaskija;
    private AaniLahde aanilahde;
    private int aikaikkunanPituus;          // täytyy olla kakkosen potenssi
    private double siirtyma;                // paljonko aikaikkunaa siirretään kerralla
    private boolean jatkuu = true;          // tarvitaanko jos pelistä pääsee vain pois??
    private IkkunafunktionLaskija ikkunaFunktionLaskija;

    public void setAanilahde(AaniLahde a) {
        this.aanilahde = a;
    }

    public AudioInputStream getStriimi() {
        if (this.aanilahde == null) return null;        
        return this.aanilahde.getStriimi();
    }

    public void setIkkunafunktio(Ikkunafunktio funktio) {
        this.ikkunaFunktionLaskija = new IkkunafunktionLaskija(aikaikkunanPituus, funktio);
    }

    /**
     * Pysäyttää pelin.
     */
    public void pysayta() {
        this.jatkuu = false;
    }
    
    public void jatka(){
        this.jatkuu = true;
    }

    /**
     * Metodi käynnistää pelin ensin luomalla uuden ArrayListin käsiteltäville
     * aikaikkunoille, siirtyy whle-looppiin ja pysyy niin kauan kunnes peli
     * pysäytetään. Loopin sisällä luetaan arvoja InputStreamistä, laitetaan ne
     * jokaiseen käsittelyssä olevaan aikaikkunaan sopivalla korjauksella (esim. 'Hann
     * window function') joka heikentää signaalia ikkunan molemmista päistä. Kun
     * ikkuna on täynnä, lähetetään se FFT-muokkaajalle käsittelyyn saaduista
     * arvoista (kompleksilukuja!) lasketut amplitudit lähetetään
     * taajuuskäyrälle päivitykseen.
     *
     * @see FastFourierMuokkaaja
     * @see Taajuuskayra
     */
    public void kaynnista() {

        AudioFormat formaatti = aanilahde.getStriimi().getFormat();
        this.siirtyma = formaatti.getSampleRate() / 180; // jaetaan arvojen saapumistaajuudella
        int tavuaPerNayte = formaatti.getFrameSize();
        boolean bigEndian = formaatti.isBigEndian();

        int laskuri = 0;
        Queue<double[][]> kasiteltavat = new ArrayDeque<>();
        
        while (true) {  // ikuinen looppi !
            
            while(!this.jatkuu){
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                }
            }
            
            double luettuArvo = lueArvo(bigEndian, tavuaPerNayte);

            if (laskuri >= siirtyma) {
                luoUusiAikaikkuna(kasiteltavat);
                laskuri = 0;
            }

            for (double[][] ikkuna : kasiteltavat) {
                int indeksi = (int) ikkuna[1][0];

                if (indeksi == aikaikkunanPituus - 1) {
                    ikkuna[1] = new double[aikaikkunanPituus];
                    ikkuna = muokkaaja.muokkaaFFT(ikkuna, true);
                    double[] valmiitArvot = laskeAmplitudit(ikkuna);
                    valmiitArvot = keskiarvonLaskija.laske(valmiitArvot);
                    kayttis.asetaArvotKayralle(valmiitArvot);
                    kasiteltavat.poll();
                }
                ikkuna[0][indeksi] = luettuArvo * ikkunaFunktionLaskija.annaKerroin(indeksi);
                ikkuna[1][0] = indeksi + 1; // talletetaan indeksi käyttämättömään imaginaariosataulukkoon
            }
            laskuri++;
        }
    }

    public void setAikaikkunanKoko(int ikkunanKoko) {
        if (onKakkosenPotenssi(ikkunanKoko)) {
            this.aikaikkunanPituus = ikkunanKoko;
            this.keskiarvonLaskija = new KeskiarvonLaskija(ikkunanKoko/2); // luodaan keskiarvolla 1
            this.muokkaaja = new FastFourierMuokkaaja(aikaikkunanPituus);
        }
    }

    public void setKayttoliittyma(Kayttoliittyma kayttis) {
        this.kayttis = kayttis;
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
     * Metodi laskee amplitudit kaikille taulukon arvoille reaaliosan ja
     * imaginaariosan euklidisena normina: (re^2 + im^2)^(0.5)
     *
     * laskentaa? siis pakkaukseen "laskenta"?
     * 
     * @param analysoitava analysoitava (reaali- ja imag-) taulukko
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
     * Metodi lukee striimistä seuraavan arvon, mitä ennen sen pitää
     * mahdollisesti käsitellä sitä riippuen äänisyötteen formaatista.
     *
     * @param bigEndian arvojen esitysjärjestys 'big-endian' vai 'little-endian'
     * @param tavuaPerNayte tavujen määrä syötteessä: 1 (= 8-bittinen) tai 2 (=
     * 16-bittinen)
     * @return seuraava luettu arvo striimistä
     */
    private double lueArvo(boolean bigEndian, int tavuaPerNayte) {
        double arvo = 0;
        try {
            if (tavuaPerNayte == 2) {
                byte[] lukupari = new byte[2];
                aanilahde.getStriimi().read(lukupari);
                if (bigEndian) {
                    arvo = Math.pow(2, 8) * lukupari[0] + lukupari[1];
                } else {
                    arvo = lukupari[0] + Math.pow(2, 8) * lukupari[1];
                }
            } else {    // toisinsanoen: if (tavuaPerNayte==1)
                arvo = aanilahde.getStriimi().read();
            }
        } catch (IOException exc) {
        }
        return arvo;
    }

    /**
     * Luo uuden tyhjän aikaikkunan ja laittaa sen käsiteltävien aikaikkunoiden
     * jonoon. Koska syötteen arvot ovat reaalilukuja, ei imaaginaariosille
     * tarkoitettua osaa tarvita vielä. Sen tilalle talletetaan yhden mittainen
     * taulukko, johon laitetaan tieto siitä mihin indeksiin asti taulukkoa on 
     * täytetty.
     *
     * @param kasiteltavatAikaikkunat käsiteltävien aikaikkunoiden jono
     */   
    private void luoUusiAikaikkuna(Queue<double[][]> kasiteltavatAikaikkunat) {
        double[][] lisattava = new double[2][1];
        lisattava[0] = new double[aikaikkunanPituus];
        kasiteltavatAikaikkunat.add(lisattava);
    }
}
