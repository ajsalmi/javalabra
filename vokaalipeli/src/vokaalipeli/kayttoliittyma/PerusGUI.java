package vokaalipeli.kayttoliittyma;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.swing.JButton;
import vokaalipeli.laskenta.Ikkunafunktio;
import vokaalipeli.peli.Vokaalipeli;

/**
 * Luokka koordinoi Taajuuskäyrä-olion luomista, parametrien kysymistä
 * käyttäjältä ja niiden antamista Vokaalipeli-luokan oliolle. Tämä on ainoa
 * luokka joka kommunikoi Vokaalipeli-luokan ilmentymän kanssa, rajapinnan
 * kautta.
 *
 * @author A J Salmi
 * @see Vokaalipeli
 * @see Paaikkuna
 * @see ParametrienKyselyIkkuna
 */
public class PerusGUI implements Kayttoliittyma {

    private ParametrienKyselyIkkuna kyselyIkkuna;
    private Paaikkuna paaikkuna;
    private int korkeus;
    private int leveys;
    private Vokaalipeli peli;

    /**
     * Konstruktori luo Taajuuskäyrä-luokan olion saamiensa mittojen mukaisesti,
     * kysyy kaikkia tarvittavia parametreja käyttäjältä ja
     *
     * @param peli vokaalipeli
     * @param leveys luotavan pääikkunan leveys
     * @param korkeus luotavan pääikkunan korkeus
     * @see Vokaalipeli
     * @see Taajuuskayra
     */
    public PerusGUI(Vokaalipeli peli, int leveys, int korkeus) {
        this.korkeus = korkeus;
        this.leveys = leveys;
        this.peli = peli;
    }

    public void asetaArvot(double[] arvot) {
        this.paaikkuna.asetaArvotTaajuuskayralle(arvot);
    }

    /**
     * Runnable-rajapinnan toteuttavan luokan run()-metodi, jossa luodaan ensin
     * kyselyikkunan ja liitetään sen käynnistysnappiin tapahtumakuuntelijan,
     * joka puolestaan asettaa Vokaalipelille tarvittavat parametrit.
     *
     * @see Runnable
     */
    @Override
    public void run() {
        this.kyselyIkkuna = new ParametrienKyselyIkkuna();

        kyselyIkkuna.getKaynnistysnappi().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int ikkunanKoko = kyselyIkkuna.getIkkunanKoko();
                peli.setAikaikkunanKoko(ikkunanKoko);

                Ikkunafunktio funktio = kyselyIkkuna.getIkkunafunktio();
                peli.setIkkunafunktio(funktio);

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
                    new Infoikkuna(Info.EI_TUETTU_FORMAATTI, kyselyIkkuna.getKaynnistysnappi());
                }
            }
        });
    }

    private void asetaKuuntelijat() {
        JButton vokaalinvaihtoNappi = paaikkuna.getNappipaneeli().getVokaalinVaihtoNappi();
        vokaalinvaihtoNappi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paaikkuna.asetaVokaali(peli.annaUusiVokaali());
            }
        });
        
        
    }
}
