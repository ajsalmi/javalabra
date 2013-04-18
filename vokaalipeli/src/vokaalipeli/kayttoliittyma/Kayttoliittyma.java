package vokaalipeli.kayttoliittyma;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.sound.sampled.AudioFormat;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.WindowConstants;
import vokaalipeli.laskenta.FastFourierMuokkaaja;
import vokaalipeli.peli.Vokaalipeli;

/**
 * Luokka koordinoi Taajuuskäyrä-olion luomista, parametrien kysymistä
 * käyttäjältä ja niiden antamista Vokaalipeli-luokan oliolle.
 *
 * @author A J Salmi
 */
public class Kayttoliittyma implements Runnable {

    private JFrame paaikkuna;
    private Taajuuskayra taajuuskayra;
    private int korkeus;
    private int leveys;
    private Vokaalipeli peli;
    // TODO: getterit käyrille ja mitoille ?

    /**
     * Konstruktori luo Taajuuskäyrä-luokan olion saamiensa mittojen mukaisesti,
     * kysyy kaikkia tarvittavia parametreja käyttäjältä ja
     *
     *
     * [Tämä on hieman vielä kesken, pitäisi eriyttää ikkunat omiksi luokikseen
     * jne.]
     *
     * @param peli vokaalipeli
     * @param leveys luotavan pääikkunan leveys
     * @param korkeus luotavan pääikkunan korkeus
     * @see Vokaalipeli
     * @see Taajuuskayra
     */
    public Kayttoliittyma(Vokaalipeli peli, int leveys, int korkeus) {
        this.korkeus = korkeus;
        this.leveys = leveys;
        this.peli = peli;
        this.taajuuskayra = new Taajuuskayra(korkeus - 45, leveys - 20);

        kysyKayttajaltaParametrit(); // nämä kaksi voisi yhdistää yhteiseen paikkaan "kysy kaikki mahdollinen"
        kysyAikaikkunanPituus(); // // nämä kaksi voisi yhdistää yhteiseen "kysy kaikki mahdollinen" 
    }

    /**
     * tämä todennäköisesti yhdistetään toiseen kyselyikkunaan ??? -->
     * kysyKayttajaltaParametrit()
     */
    private void kysyAikaikkunanPituus() {
        // TODO: kysy käyttäjältä aikaikkunan pituus (=> taajuusresoluutio)

        // vastaus saatu ---> 
        int ikkunanKoko = 1024 * 2 * 2 * 2 * 2;// <-- tämä tieto käyttäjältä
        this.peli.setAikaikkunanKoko(ikkunanKoko);
        this.peli.setTaajuuskayra(taajuuskayra);
        this.peli.setFastFourierMuokkaaja(new FastFourierMuokkaaja(ikkunanKoko));
    }

    /**
     * Runnable-rajapinnan toteuttavan luokan run()-metodi, jossa luodaan
     * Taajuuskayra-luokan oliota varten tyhjä ikkuna ja asetetaan se näkyviin.
     *
     * @see Runnable;
     */
    @Override
    public void run() {
        paaikkuna = new JFrame("Taajuuskäyrä");
        paaikkuna.setPreferredSize(new Dimension(this.leveys, this.korkeus));
        paaikkuna.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        luoKomponentit(paaikkuna.getContentPane());
        paaikkuna.pack();
        paaikkuna.setVisible(true);
    }

    public JFrame getFrame() {
        return this.paaikkuna;
    }

    /**
     * Metodi täyttää ikkunan halutuilla komponenteilla: taajuuskäyrä siihen
     * liittyvät napit.
     *
     * @param container
     */
    private void luoKomponentit(Container container) {
        container.add((Component) this.taajuuskayra);
        // TODO: esim. alapaneeliin napit maxArvon muokkaamiseen 
        // ja käyrän keskiarvojen määrän muokkaamiseen 
    }

    /**
     * Tämä (ml. alla oleva hirviö "luoKomponentitParametrienKysely(.)")
     * mahdolliseesti omaksi luokaksi ???
     */
    private void kysyKayttajaltaParametrit() {
        JFrame kyselyIkkuna = new JFrame();
        kyselyIkkuna.setPreferredSize(new Dimension(200, 480));
        kyselyIkkuna.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        luoKomponentitParametrienKysely(kyselyIkkuna.getContentPane());
        kyselyIkkuna.pack();
        kyselyIkkuna.setVisible(true);
    }

    /**
     * tämä varmaankin erilliseen luokkaan...
     *
     * @param container
     */
    private void luoKomponentitParametrienKysely(Container container) {
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        final ButtonGroup bg1 = new ButtonGroup();
        int[] eriTaajuudet = {8000, 11025, 16000, 22050, 44100};
        container.add(new JLabel("näytteenottotaajuus"));
        JRadioButton nappi;
        for (int taajuus : eriTaajuudet) {
            nappi = new JRadioButton(taajuus + " Hz");
            bg1.add(nappi);
            container.add(nappi);
        }
// --------------------------------------------------------        
        int[] sampleSizeInBits = {8, 16};
        container.add(new JLabel(" "));
        container.add(new JLabel("näytteen koko bitteinä"));
        ButtonGroup bg2 = new ButtonGroup();
        for (int sampleSize : sampleSizeInBits) {
            nappi = new JRadioButton(sampleSize + " bittiä");
            bg2.add(nappi);
            container.add(nappi);
        }

// --------------------------------------------------------
        container.add(new JLabel(" "));
        container.add(new JLabel("etumerkillisyys"));
        ButtonGroup bg3 = new ButtonGroup();
        nappi = new JRadioButton("etumerkillinen");
        bg3.add(nappi);
        container.add(nappi);
        nappi = new JRadioButton("etumerkitön");
        bg3.add(nappi);
        container.add(nappi);
// --------------------------------------------------------
        container.add(new JLabel(" "));
        container.add(new JLabel("tavujärjestys"));
        ButtonGroup bg4 = new ButtonGroup();
        nappi = new JRadioButton("big-endian");
        bg4.add(nappi);
        container.add(nappi);
        nappi = new JRadioButton("little-endian");
        bg4.add(nappi);
        container.add(nappi);
// --------------------------------------------------------
        container.add(new JLabel(" "));
        JButton aloita = new JButton("Aloita");
        container.add(aloita);
        aloita.addActionListener(new kaynnistysnapinKuuntelija(this.peli));
    }

    /**
     * tämä kuuntelija mahdollisesti omaksi luokakseen pakkaukseen
     * .kayttoliittyma
     */
    private class kaynnistysnapinKuuntelija implements ActionListener {

        private Vokaalipeli peli;

        public kaynnistysnapinKuuntelija(Vokaalipeli peli) {
            this.peli = peli;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
//            this.peli.pysayta();
            // miten saan tiedot siitä, mitkä napit on valittuna ?           

            // ????

            // ---> no asetetaan sitten vain jotkut arvot 
            AudioFormat formaatti = new AudioFormat(44100, 16, 1, true, false);
            this.peli.setAanilahde(new Mikrofoni(formaatti));
            
        }
    }
}
