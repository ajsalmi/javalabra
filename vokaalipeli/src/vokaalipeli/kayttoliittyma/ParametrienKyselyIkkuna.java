
package vokaalipeli.kayttoliittyma;

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
 * Tänne eriytetään ikkuna parametrien kyselyä varten. 
 * 
 * TODO : sama pätkä pois Kayttoliittymasta
 * TODO : parametrien kysely toimimaan
 * 
 * 
 * @author A J Salmi
 */
public class ParametrienKyselyIkkuna extends JFrame {
    
    public ParametrienKyselyIkkuna(){
        setPreferredSize(new Dimension(200, 480));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        luoKomponentitParametrienKysely(getContentPane());
        pack();
        setVisible(true);
    
    }
    
    /**
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
//        aloita.addActionListener(new kaynnistysnapinKuuntelija(this.peli));
    }

    /**
     */
    private class kaynnistysnapinKuuntelija implements ActionListener {

        private Vokaalipeli peli;

        public kaynnistysnapinKuuntelija(Vokaalipeli peli) {
            this.peli = peli;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO: kysy käyttäjältä aikaikkunan pituus (=> taajuusresoluutio)

            // vastaus saatu ---> 
            Ikkunafunktio funktio = Ikkunafunktio.KOLMIO;// tämä ei tule tähän lopullisessa versiossa
            this.peli.setIkkunafunktio(funktio);
            // -----
            int ikkunanKoko = 1024 * 2 * 2 * 2 * 2;// <-- tämä tieto käyttäjältä
            this.peli.setAikaikkunanKoko(ikkunanKoko);
//            this.peli.setTaajuuskayra(taajuuskayra);
            this.peli.setFastFourierMuokkaaja(new FastFourierMuokkaaja(ikkunanKoko));
            
            
            // miten saan tiedot siitä, mitkä napit on valittuna ?           

            // ????

            // ---> no asetetaan sitten vain jotkut arvot 
            AudioFormat formaatti = new AudioFormat(16000, 16, 1, true, true);
            this.peli.setAanilahde(new Mikrofoni(formaatti));
        }
    }
    
    
    
    
}
