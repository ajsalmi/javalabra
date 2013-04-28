package vokaalipeli.peli;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;
import javax.sound.sampled.AudioFormat;
import vokaalipeli.domain.Vokaali;
import vokaalipeli.domain.VokaaliInventorio;
import vokaalipeli.kayttoliittyma.AaniLahde;
import vokaalipeli.kayttoliittyma.Kayttoliittyma;
import vokaalipeli.kayttoliittyma.Taajuuskayra;
import vokaalipeli.laskenta.FastFourierMuokkaaja;
import vokaalipeli.laskenta.Ikkunafunktio;
import vokaalipeli.laskenta.LaskentaKeskus;

/**
 * Luokka sisältää pelin ydintoiminnallisuuden. 
 * 
 * [Tällä luokalla on liikaa vastuita ja tehtäviä. Taulukkomuotoisten
 * aikaikkunoiden käsittelyä voisi siirtää laskennalle ja/tai arvon lukemisen
 * äänilähteelle. Myös uuden vokaalin arpomista voisi harkita siirrettäväksi
 * vaikka vokaali-inventoriolle.]
 * 
 * @author A J Salmi
 */
public class Vokaalipeli implements Runnable {

    private Kayttoliittyma kayttoliittyma;
    private VokaaliInventorio kielenVokaalit;
    private Vokaali edellinenVokaali;
    private double formanttienKorjauskerroin = 1.0; 
    private AaniLahde aanilahde;
    private LaskentaKeskus laskentakeskus;

    public Vokaalipeli() {
        this.kielenVokaalit = new VokaaliInventorionLuoja().luoVokaalit("suomi");
    }

    /**
     * Metodi palauttaa vokaali-inventoriosta vokaalin. Se ei saa olla sama kuin
     * edellinen vokaali. 
     * 
     * @return uusi vokaali
     */
    public Vokaali annaUusiVokaali() {
        int vokaalienLkm = kielenVokaalit.getVokaalienMaara();
        Random arpoja = new Random();
        Vokaali uusiVokaali = kielenVokaalit.annaVokaali(arpoja.nextInt(vokaalienLkm));
        while (edellinenVokaali == uusiVokaali) {
            uusiVokaali = kielenVokaalit.annaVokaali(arpoja.nextInt(vokaalienLkm));
        }
        this.edellinenVokaali = uusiVokaali;
        uusiVokaali = teeKorjaus(uusiVokaali);
        return uusiVokaali;
    }

    /**
     * Parametrina saadulla liukulukuarvolla kerrotaan aikaisempaa korjauskerrointa.
     * 
     * 
     * @param kerroin kerroin jolla aikaisempaa korjauskerrointa muutetaan 
     */
    public void muutaKorjausKerrointa (double kerroin){
        this.formanttienKorjauskerroin *= kerroin;
    }
    
    public void setAanilahde(AaniLahde aanilahde) {
        this.aanilahde = aanilahde;
    }
    
    /**
     * Luodaan aikaikkunan koon avulla uusi Laskentakeskus. Palauttaa
     * 
     * @param ikkunanKoko aikaikkunan koko
     * @return totuusarvo asettamisesta
     */
    public boolean asetaAikaikkunanKoko(int ikkunanKoko) {
        if (onKakkosenPotenssi(ikkunanKoko)) {
            this.laskentakeskus = new LaskentaKeskus(ikkunanKoko);
            return true;
        }
        return false;
    }

    public void setKayttoliittyma(Kayttoliittyma kayttis) {
        this.kayttoliittyma = kayttis;
    }

    
    public boolean aaniLahdeAsetettu() {
        if (this.aanilahde == null) {
            return false;
        }
        return true;
    }

    public void asetaIkkunafunktio(Ikkunafunktio funktio) {
        this.laskentakeskus.setIkkunaFunktio(funktio);
    }

    /**
     * Metodi käynnistää pelin ensin luomalla uuden ArrayListin käsiteltäville
     * aikaikkunoille, siirtyy while-looppiin ja pysyy niin kauan kunnes peli
     * lopetetaan. Loopin sisällä luetaan arvoja InputStreamistä, laitetaan ne
     * jokaiseen käsittelyssä olevaan aikaikkunaan sopivalla korjauksella (esim.
     * 'Hann window function') joka heikentää signaalia ikkunan molemmista
     * päistä. Kun ikkuna on täynnä, lähetetään se FFT-muokkaajalle käsittelyyn
     * saaduista arvoista (kompleksilukuja!) lasketut amplitudit lähetetään
     * käyttöliittymälle päivitykseen.
     *
     * @see FastFourierMuokkaaja
     * @see Taajuuskayra
     */

    @Override
    public void run() {
        AudioFormat formaatti = aanilahde.getStriimi().getFormat();
        int aikaikkunanSiirtyma = (int) formaatti.getSampleRate() / 300; // uusi taulukko aloitetaan 300 krt/s
        int tavuaPerNayte = formaatti.getFrameSize();
        boolean bigEndian = formaatti.isBigEndian();
        
        int naytteitaKasitelty = 0;
        int aikaikkunanPituus = this.laskentakeskus.getAikaikkunanPituus();
        Queue <double[][]> kasiteltavat = new ArrayDeque<>();                   

        while (true) {     

            double luettuArvo = lueArvo(bigEndian, tavuaPerNayte);

            if (naytteitaKasitelty == aikaikkunanSiirtyma) {
                luoUusiAikaikkuna(kasiteltavat, aikaikkunanPituus);              
                naytteitaKasitelty = 0;
            }

            for (double[][] ikkuna : kasiteltavat) {                            
                int indeksi = (int) ikkuna[1][0];

                if (indeksi == aikaikkunanPituus - 1) { //  eli yksi aikaikkuna tuli täyteen
                    boolean arvotPiirrettyAjallaan = kayttoliittyma.arvojenAsettaminenValmis();
                    while (!kayttoliittyma.arvojenAsettaminenValmis()) {
                        try {
                            Thread.sleep(0, 1);
                        } catch (InterruptedException ex) {}
                    }

                    if (arvotPiirrettyAjallaan) {
                        double[] valmiitArvot = laskentakeskus.kasitteleValmisIkkuna(ikkuna);
                        kayttoliittyma.asetaArvot(valmiitArvot);
                    }
                    kasiteltavat.poll();
                }
                ikkuna[0][indeksi] = luettuArvo * laskentakeskus.annaIkkunakerroin(indeksi);
                ikkuna[1][0] = indeksi + 1; // talletetaan indeksi käyttämättömään imaginaariosataulukkoon
            }
            naytteitaKasitelty++;
        }
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
     * Metodi lukee striimistä seuraavan arvon, mikä saattaa vaatia käsittelyä 
     * riippuen äänisyötteen formaatista.
     *
     * @param bigEndian arvojen esitysjärjestys: 'big-endian' vai 'little-endian'
     * @param tavuaPerNayte tavujen määrä syötteessä: 1 (= 8-bittinen) tai 2 
     * (= 16-bittinen)
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
    private void luoUusiAikaikkuna(Queue<double[][]> kasiteltavatAikaikkunat, int aikaikkunanPituus) {
        double[][] lisattava = new double[2][1];
        lisattava[0] = new double[aikaikkunanPituus];
        kasiteltavatAikaikkunat.add(lisattava);
    }

    /**
     * Luo annetun vokaalin pohjalta uuden vokaalin, jonka jokainen formantti kerrotaan 
     * korjauskeroimella. Korjauskerroin kuvastaa ääntöväylän yksilöllisistä vaihteluista
     * johtuvaa formanttitaajuuksien siirtymää. 
     * 
     * (Myös Taajuuskäyrässä pidetään kirjaa korjauskertoimesta, mutta siellä uuden 
     * vokaalin saapuessa arvo asetetaan ykköseksi. Siellä siis muistetaan vain 
     * muutokset edellisestä vokaalin asettamisesta.)
     * 
     * @param vokaali vokaali josta tehdään muokattu versio
     * @return vokaali korjatuilla formanttitaajuuksilla
     * @see Taajuuskayra
     */    
    private Vokaali teeKorjaus(Vokaali vokaali) {        
        int[] vokaalinFormantit = vokaali.getFormantit();
        int[] korjatutFormantit = new int[3];
        for (int i = 0; i < 3; i++) {
            korjatutFormantit[i] = (int) (vokaalinFormantit[i] * formanttienKorjauskerroin);
        }
        return new Vokaali(vokaali.getNimi(), korjatutFormantit[0], korjatutFormantit[1], korjatutFormantit[2]);
    }
}
