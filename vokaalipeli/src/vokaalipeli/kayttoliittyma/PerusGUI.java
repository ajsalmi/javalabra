package vokaalipeli.kayttoliittyma;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.sound.sampled.AudioFormat;
import javax.swing.JButton;
import vokaalipeli.laskenta.Ikkunafunktio;
import vokaalipeli.peli.Vokaalipeli;

/**
 * Luokka koordinoi Taajuuskäyrä-olion luomista, parametrien kysymistä
 * käyttäjältä ja niiden antamista Vokaalipeli-luokan oliolle. Ainoa
 * luokka käyttöliittymä-paketissa joka tietää Vokaalipelin olemassaolosta.
 *
 * @author A J Salmi
 * @see Vokaalipeli
 * @see Paaikkuna
 * @see ParametrienKyselyIkkuna
 */
public class PerusGUI implements Kayttoliittyma {

    private int korkeus;
    private int leveys;
    private ParametrienKyselyIkkuna kyselyIkkuna;
    private Paaikkuna paaikkuna;
    private Vokaalipeli peli;

    /**
     * Konstruktorissa asetetaan oliomuuttujiin korkeus, leveys ja peli
     * parametreina saadut arvot.
     *
     * @param peli vokaalipeli
     * @param leveys luotavan pääikkunan leveys
     * @param korkeus luotavan pääikkunan korkeus
     * @see Vokaalipeli
     */
    public PerusGUI(Vokaalipeli peli, int leveys, int korkeus) {
        this.korkeus = korkeus;
        this.leveys = leveys;
        this.peli = peli;
    }

    /**
     * Kayttoliittyma-rajapinnan vaatima metodi. Asettaa pääikkunaan oliomuuttujana
     * olevalle taajuuskäyrälle saamansa arvot.
     * 
     * @param arvot 
     * @see Kayttoliittyma
     */
    public void asetaArvot(double[] arvot) {
        this.paaikkuna.getTaajuuskayra().setArvot(arvot);
    }

    /**
     * Kayttoliittyma-rajapinnan vaatima metodi sen tarkistukseen onko arvojen 
     * asettaminen valmiina.
     * 
     * @return tieto siitä onko arvojen asettaminen valmiina
     */
    public boolean arvojenAsettaminenValmis() {
        return this.paaikkuna.getTaajuuskayra().getPiirtoValmiina();
    }    
    
    /**
     * Kayttoliittyma-rajapinnan vvatima metodi. Siinä luodaan ensin kyselyikkuna 
     * ja liitetään sen käynnistysnappiin tapahtumakuuntelija, joka puolestaan 
     * asettaa tapahtuman toteutuessa Vokaalipelille tarvittavat parametrit.
     *
     * @see Kayttoliittyma
     */
    public void kaynnista() {
        this.kyselyIkkuna = new ParametrienKyselyIkkuna();

        kyselyIkkuna.getKaynnistysNappi().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int ikkunanKoko = kyselyIkkuna.getIkkunanKoko();
                peli.asetaAikaikkunanKoko(ikkunanKoko);

                Ikkunafunktio funktio = kyselyIkkuna.getIkkunafunktio();
                peli.asetaIkkunafunktio(funktio);

                int naytteenottoTaajuus = kyselyIkkuna.getNaytteenottoTaajuus();
                int tavuaPerNayte = kyselyIkkuna.getTavuaPerNayte();
                boolean signed = kyselyIkkuna.getEtumerkillisyys();
                boolean bigEndian = kyselyIkkuna.getTavujarjestys();
                AudioFormat formaatti = new AudioFormat(naytteenottoTaajuus, tavuaPerNayte, 1, signed, bigEndian);

                try {
                    peli.setAanilahde(new Mikrofoni(formaatti));
                    Taajuuskayra taajuuskayra = new Taajuuskayra(korkeus - 70, leveys - 20, 60000, naytteenottoTaajuus / 2);
                    paaikkuna = new Paaikkuna(taajuuskayra, leveys, korkeus);
                    asetaKuuntelijat();
                    kyselyIkkuna.dispose();
                    kyselyIkkuna = null;
                } catch (IllegalArgumentException ex) {
                    new Infoikkuna(Info.EI_TUETTU_FORMAATTI, kyselyIkkuna.getKaynnistysNappi());
                }
            }
        });
    }

    private void asetaKuuntelijat() {
        JButton vokaalinvaihtoNappi = paaikkuna.getNappipaneeli().getVokaalinVaihtoNappi();
        vokaalinvaihtoNappi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Taajuuskayra kayra = paaikkuna.getTaajuuskayra();
                peli.muutaKorjausKerrointa(kayra.getKorjausKerroin());
                kayra.setVokaali(peli.annaUusiVokaali());
            }
        });
    }
}
