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
import vokaalipeli.laskenta.Ikkunafunktio;
import vokaalipeli.peli.Vokaalipeli;

/**
 * Luokka koordinoi Taajuuskäyrä-olion luomista, parametrien kysymistä
 * käyttäjältä ja niiden antamista Vokaalipeli-luokan oliolle.
 *
 * @author A J Salmi
 */
public class Kayttoliittyma implements Runnable {

    private JFrame paaikkuna;
    private ParametrienKyselyIkkuna ikkuna;
    private Taajuuskayra taajuuskayra;
    private int korkeus;
    private int leveys;
    private Vokaalipeli peli;

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
        kysyKayttajaltaParametrit();
    }

    /**
     * Runnable-rajapinnan toteuttavan luokan run()-metodi, jossa luodaan
     * Taajuuskayra-luokan oliota varten tyhjä ikkuna ja asetetaan se näkyviin.
     *
     * @see Runnable;
     */
    @Override
    public void run() {
        while (this.taajuuskayra == null){
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
            }
        }
        paaikkuna = new JFrame("Vokaalipeli!");
        paaikkuna.setPreferredSize(new Dimension(this.leveys, this.korkeus));
        paaikkuna.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        luoKomponentit(paaikkuna.getContentPane());
        paaikkuna.pack();
        paaikkuna.setVisible(true);
    }

    public void asetaArvotKayralle(double[]arvot){
        this.taajuuskayra.setArvot(arvot);
    }
    
    /**
     * Metodi täyttää ikkunan halutuilla komponenteilla: taajuuskäyrä 
     * 
     * TODO: ... ja liittyvät säätönapit.
     *
     * @param container
     */
    private void luoKomponentit(Container container) {
        container.add((Component) this.taajuuskayra);
        // TODO: esim. alapaneeliin napit maxArvon muokkaamiseen 
        // ja käyrän keskiarvojen määrän muokkaamiseen 
    }

    /**
     * Kysyy käyttäjältä parametrit. Se luoma ensin kyselyikkunan ja liittää sen
     * käynnistysnappiin tapahtumakuuntelijan, joka asettaa Vokaalipelille  
     * 
     */
    private void kysyKayttajaltaParametrit() {
        ikkuna = new ParametrienKyselyIkkuna();
        
        ikkuna.getKaynnistysnappi().addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) { 
                
            //-------------TODO: ANALYSOI VALINNAT !!!! --------
                
                
            int ikkunanKoko = 1024 * 2 * 2 * 2 * 2; // <-- tämä tieto käyttäjältä
            peli.setAikaikkunanKoko(ikkunanKoko);
            // ----
            Ikkunafunktio funktio = Ikkunafunktio.HANN; // <-- tämä tieto käyttäjältä
            peli.setIkkunafunktio(funktio);                   
            // ----
            int sampleRate = 22050; // <-- tämä tieto käyttäjältä
            int bytesPerSample = 16; // <-- tämä tieto käyttäjältä
            boolean signed = true; // <-- tämä tieto käyttäjältä
            boolean bigEndian = true; // <-- tämä tieto käyttäjältä
            
            AudioFormat formaatti = new AudioFormat(sampleRate, bytesPerSample, 1, signed, bigEndian);
            peli.setAanilahde(new Mikrofoni(formaatti));
            taajuuskayra = new Taajuuskayra(korkeus - 45, leveys - 20, 60000, sampleRate/2);    
            ikkuna.dispose();
            }
        });
    }
}
