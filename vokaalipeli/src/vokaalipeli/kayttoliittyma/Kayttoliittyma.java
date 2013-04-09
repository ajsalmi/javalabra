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
import vokaalipeli.domain.FastFourierMuokkaaja;
import vokaalipeli.domain.Mikrofoni;
import vokaalipeli.peli.Vokaalipeli;

public class Kayttoliittyma implements Runnable {

    private JFrame frame;
    private Taajuuskayra taajuuskayra;
    // private ??? formanttikartta;    <-----   myöhemmin
    private int korkeus;
    private int leveys;
    private Vokaalipeli peli;
    // TODO: getterit käyrille ja mitoille ?

    public Kayttoliittyma(Vokaalipeli peli, int leveys, int korkeus) {
        this.korkeus = korkeus;
        this.leveys = leveys;
        this.peli = peli;
        this.taajuuskayra = new Taajuuskayra(korkeus-45, leveys-20);
        kysyMikkia();
        kysyAikaikkunanPituus();
//        this.run();
    }

    private void kysyAikaikkunanPituus() {
        // TODO: kysy käyttäjältä aikaikkunan pituus (= taajuusresoluutio)
        
        // vastaus saatu ---> 
        int ikkunanKoko = 4096;// <-- tämä tieto käyttäjältä
        this.peli.setIkkunanKoko(ikkunanKoko);
        this.peli.setTaajuuskayra(taajuuskayra);
        this.peli.setFastFourierMuokkaaja(new FastFourierMuokkaaja(ikkunanKoko)); 
    }

    @Override
    public void run() {
        frame = new JFrame("Taajuuskäyrä");
        frame.setPreferredSize(new Dimension(this.leveys, this.korkeus));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        luoKomponentit(frame.getContentPane());
        frame.pack();
        frame.setVisible(true);
    }

    public JFrame getFrame() {
        return this.frame;
    }

    private void luoKomponentit(Container container) {
        container.add((Component) this.taajuuskayra);
        // muuta tavaraa tähän: napit maxArvon muokkaamiseen?
    }

    private void kysyMikkia() {
        frame = new JFrame();
        frame.setPreferredSize(new Dimension(200, 600));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        luoKomponentitMikinKysely(frame.getContentPane());
        frame.pack();
        frame.setVisible(true);
    }

    private void luoKomponentitMikinKysely(Container container) {
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        final ButtonGroup bg1 = new ButtonGroup();
        int[] eriTaajuudet = {8000, 11025, 16000, 22050, 44100};   // kaksi suurinta pois
        container.add(new JLabel("sampling rate"));
        JRadioButton nappi;
        for (int taajuus : eriTaajuudet) {
            nappi = new JRadioButton(taajuus + " Hz");
            bg1.add(nappi);
            container.add(nappi);
        }
// --------------------------------------------------------        
        int[] sampleSizeInBits = {8, 16};
        container.add(new JLabel(" "));
        container.add(new JLabel("sample size in bits"));
        ButtonGroup bg2 = new ButtonGroup();
        for (int sampleSize : sampleSizeInBits) {
            nappi = new JRadioButton(sampleSize + " bits / sample");
            bg2.add(nappi);
            container.add(nappi);
        }
// --------------------------------------------------------
        int[] numberOfChannels = {1, 2};
        container.add(new JLabel(" "));
        container.add(new JLabel("channels"));
        ButtonGroup bg3 = new ButtonGroup();
        for (int channels : numberOfChannels) {
            nappi = new JRadioButton(channels + "");
            bg3.add(nappi);
            container.add(nappi);
        }

// --------------------------------------------------------
        container.add(new JLabel(" "));
        container.add(new JLabel("signedness"));
        ButtonGroup bg4 = new ButtonGroup();
        nappi = new JRadioButton("signed");
        bg4.add(nappi);
        container.add(nappi);
        nappi = new JRadioButton("unsigned");
        bg4.add(nappi);
        container.add(nappi);
// --------------------------------------------------------
        container.add(new JLabel(" "));
        container.add(new JLabel("endianness"));
        ButtonGroup bg5 = new ButtonGroup();
        nappi = new JRadioButton("big-endian");
        bg5.add(nappi);
        container.add(nappi);
        nappi = new JRadioButton("little-endian");
        bg5.add(nappi);
        container.add(nappi);
// --------------------------------------------------------
        container.add(new JLabel(" "));
        JButton valmis = new JButton("Ready");
        container.add(valmis);
        valmis.addActionListener(new OmaKuuntelija(this.peli)); // ???
    }

    private class OmaKuuntelija implements ActionListener { // nimea jotenkin paremmin

        private Vokaalipeli peli;
        public OmaKuuntelija(Vokaalipeli peli) {
            this.peli = peli;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            this.peli.pysayta();
            // miten saan tiedot siitä, mitkä napit on valittuna ?           

            // ????

            // ---> no asetetaan sitten vain jotkut arvot 
            AudioFormat formaatti = new AudioFormat(16000, 16, 1, true, true);
            this.peli.setMikrofoni(new Mikrofoni(formaatti));
        }
    }
}
