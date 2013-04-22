package vokaalipeli.kayttoliittyma;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import vokaalipeli.laskenta.Ikkunafunktio;

/**
 * Tänne eriytetään ikkuna parametrien kyselyä varten.
 *
 * TODO : parametrien kysely toimimaan
 *
 *
 * @author A J Salmi
 */
public class ParametrienKyselyIkkuna extends JFrame {

    private JButton kaynnistysnappi;

    public ParametrienKyselyIkkuna() {
        setPreferredSize(new Dimension(280, 480));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        luoKomponentitParametrienKysely(getContentPane());
        pack();
        setVisible(true);
    }

    public JButton getKaynnistysnappi() {
        return kaynnistysnappi;
    }

    /**
     * @param container
     */
    private void luoKomponentitParametrienKysely(Container container) {
        GridLayout layout = new GridLayout(24, 1);
        container.setLayout(layout);
//----------------------------------------------------------
        container.add(new JLabel(" näytteenottotaajuus"));
        String[] taajuusvaihtoehdot = {"8000 Hz", "11025 Hz", "16000 Hz", "22050 Hz", "44100 Hz"};
        JComboBox<String> taajuudet = new JComboBox<>(taajuusvaihtoehdot);
        taajuudet.setSelectedIndex(3);
        container.add(taajuudet);
        container.add(new JLabel(" "));
//// --------------------------------------------------------        
        container.add(new JLabel(" näytteen koko bitteinä"));
        ButtonGroup bg2 = new ButtonGroup();
        JRadioButton nappi = new JRadioButton("8 bittiä");
        bg2.add(nappi);
        container.add(nappi);
        nappi = new JRadioButton("16 bittiä");
        nappi.setSelected(true);
        bg2.add(nappi);
        container.add(nappi);
        container.add(new JLabel(" "));
//// --------------------------------------------------------
        container.add(new JLabel(" etumerkillisyys"));
        ButtonGroup bg3 = new ButtonGroup();
        nappi = new JRadioButton("etumerkillinen");
        nappi.setSelected(true);
        bg3.add(nappi);
        container.add(nappi);
        nappi = new JRadioButton("etumerkitön");
        bg3.add(nappi);
        container.add(nappi);
        container.add(new JLabel(" "));
//// --------------------------------------------------------
        container.add(new JLabel(" tavujärjestys"));
        ButtonGroup bg4 = new ButtonGroup();
        nappi = new JRadioButton("big-endian");
        nappi.setSelected(true);
        bg4.add(nappi);
        container.add(nappi);
        nappi = new JRadioButton("little-endian");
        bg4.add(nappi);
        container.add(nappi);
        container.add(new JLabel(" "));
//// --------------------------------------------------------
        container.add(new JLabel(" ikkunafunktio"));
        Ikkunafunktio[] ikkunafunktiot = Ikkunafunktio.values();
        JComboBox<Ikkunafunktio> ikkunat = new JComboBox<>(ikkunafunktiot);
        container.add(ikkunat);
        container.add(new JLabel(" "));
//------------------------------------------------
        container.add(new JLabel(" aikaikkunan koko"));
        JTextField aikaikkunanValintaKentta = new JTextField("8192");
        aikaikkunanValintaKentta.setEditable(false);
        container.add(aikaikkunanValintaKentta);
        JPanel paneeliPlussalleJaMiinukselle = new JPanel();
        paneeliPlussalleJaMiinukselle.setLayout(new BoxLayout(paneeliPlussalleJaMiinukselle,  BoxLayout.X_AXIS));
        JButton plussa = new JButton("     *2     ");
        paneeliPlussalleJaMiinukselle.add(plussa);
        plussa.addActionListener(new PlusJaMiinusnapinKuuntelija(aikaikkunanValintaKentta));
        JButton miinus = new JButton("     /2     ");
        miinus.addActionListener(plussa.getActionListeners()[0]);
        paneeliPlussalleJaMiinukselle.add(miinus);
        container.add(paneeliPlussalleJaMiinukselle);
        container.add(new JLabel(" "));
        //------------------------------------------
        JButton kaynnistys = new JButton("Käynnistä");
        container.add(new JLabel(" "));
        kaynnistysnappi = kaynnistys;
        container.add(kaynnistysnappi);
    }

    private static class PlusJaMiinusnapinKuuntelija implements ActionListener {

        int ikkunanPituus;
        JTextField aikaikkunanValintaKentta;

        private PlusJaMiinusnapinKuuntelija(JTextField aikaikkunanValintaKentta) {
            this.ikkunanPituus = Integer.parseInt(aikaikkunanValintaKentta.getText());
            this.aikaikkunanValintaKentta = aikaikkunanValintaKentta;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().trim().equals("*2")) {
                ikkunanPituus *= 2;
                if (ikkunanPituus > 536870912) {  // minne asti kannattaa laittaa ehdotuksia?
                    ikkunanPituus = 536870912;
                }
                aikaikkunanValintaKentta.setText(ikkunanPituus + "");
            } else {
                ikkunanPituus /= 2;
                if (ikkunanPituus < 64) { // minne asti ehdotuksia ?
                    ikkunanPituus = 64;
                }
                aikaikkunanValintaKentta.setText(ikkunanPituus + "");
            }
        }
    }
}
