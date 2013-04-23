package vokaalipeli.kayttoliittyma;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import javax.swing.AbstractButton;
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
 * @author A J Salmi
 */
public class ParametrienKyselyIkkuna extends JFrame {

    private JButton kaynnistysnappi;
    private JComboBox<String> taajuusvalinta;
    private ButtonGroup bitsPerSampleValinta;
    private JComboBox<Ikkunafunktio> ikkunafunktioValinta;
    private ButtonGroup etumerkillisyysValinta;
    private ButtonGroup tavujarjestysValinta;
    private JTextField aikaikkunanValinta;

    public ParametrienKyselyIkkuna() {
        setPreferredSize(new Dimension(280, 480));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        luoKomponentit(getContentPane());
        pack();
        setVisible(true);
    }

    public JButton getKaynnistysnappi() {
        return kaynnistysnappi;
    }

    public int getNaytteenottoTaajuus() {
        String valittuTaajuusMerkkijonona = (String) this.taajuusvalinta.getSelectedItem();
        return Integer.parseInt(valittuTaajuusMerkkijonona.split("\\D")[0]);
    }

    public boolean getEtumerkillisyys() {
        AbstractButton valittuNappi = etsiValittuNappi(etumerkillisyysValinta);
        if (valittuNappi.getText().contains("lli")) {  // etumerki_lli_nen
            return true;
        }
        return false;
    }
    
    public int getIkkunanKoko(){
        return Integer.parseInt(aikaikkunanValinta.getText().trim());
    }
    
    public boolean getTavujarjestys(){
        AbstractButton valittuNappi = etsiValittuNappi(tavujarjestysValinta);
        if (valittuNappi.getText().contains("big")) {  
            return true;
        }
        return false;
    }

    public Ikkunafunktio getIkkunafunktio() {
        return (Ikkunafunktio) ikkunafunktioValinta.getSelectedItem();
    }

    public int getTavuaPerNayte() {
        AbstractButton valittuNappi = etsiValittuNappi(bitsPerSampleValinta);
        String arvoMerkkinona = valittuNappi.getText().split("\\D")[0];
        return Integer.parseInt(arvoMerkkinona);
    }

    /**
     * @param container
     */
    private void luoKomponentit(Container container) {
        GridLayout layout = new GridLayout(24, 1);
        container.setLayout(layout);
//----------------------------------------------------------
        container.add(new JLabel(" näytteenottotaajuus"));
        String[] taajuusvaihtoehdot = {"8000 Hz", "11025 Hz", "16000 Hz", "22050 Hz", "44100 Hz"};
        taajuusvalinta = new JComboBox<>(taajuusvaihtoehdot);
        taajuusvalinta.setSelectedIndex(3);
        container.add(taajuusvalinta);
        container.add(new JLabel(" "));
//// --------------------------------------------------------        
        container.add(new JLabel(" näytteen koko bitteinä"));
        bitsPerSampleValinta = new ButtonGroup();
        JRadioButton nappi = new JRadioButton("8 bittiä");
        bitsPerSampleValinta.add(nappi);
        container.add(nappi);
        nappi = new JRadioButton("16 bittiä");
        nappi.setSelected(true);
        bitsPerSampleValinta.add(nappi);
        container.add(nappi);
        container.add(new JLabel(" "));
//// --------------------------------------------------------
        container.add(new JLabel(" etumerkillisyys"));
        etumerkillisyysValinta = new ButtonGroup();
        nappi = new JRadioButton("etumerkillinen");
        nappi.setSelected(true);
        etumerkillisyysValinta.add(nappi);
        container.add(nappi);
        nappi = new JRadioButton("etumerkitön");
        etumerkillisyysValinta.add(nappi);
        container.add(nappi);
        container.add(new JLabel(" "));
//// --------------------------------------------------------
        container.add(new JLabel(" tavujärjestys"));
        tavujarjestysValinta = new ButtonGroup();
        nappi = new JRadioButton("big-endian");
        nappi.setSelected(true);
        tavujarjestysValinta.add(nappi);
        container.add(nappi);
        nappi = new JRadioButton("little-endian");
        tavujarjestysValinta.add(nappi);
        container.add(nappi);
        container.add(new JLabel(" "));
//// --------------------------------------------------------
        container.add(new JLabel(" ikkunafunktio"));
        Ikkunafunktio[] ikkunafunktiot = Ikkunafunktio.values();
        ikkunafunktioValinta = new JComboBox<>(ikkunafunktiot);
        container.add(ikkunafunktioValinta);
        container.add(new JLabel(" "));
//------------------------------------------------
        container.add(new JLabel(" aikaikkunan koko"));
        aikaikkunanValinta = new JTextField("8192");
        aikaikkunanValinta.setEditable(false);
        container.add(aikaikkunanValinta);
        JPanel paneeliPlussalleJaMiinukselle = new JPanel();
        paneeliPlussalleJaMiinukselle.setLayout(new BoxLayout(paneeliPlussalleJaMiinukselle, BoxLayout.X_AXIS));
        JButton plussa = new JButton("     *2     ");
        paneeliPlussalleJaMiinukselle.add(plussa);
        plussa.addActionListener(new PlusJaMiinusnapinKuuntelija(aikaikkunanValinta));
        JButton miinus = new JButton("     /2     ");
        miinus.addActionListener(plussa.getActionListeners()[0]);
        paneeliPlussalleJaMiinukselle.add(miinus);
        container.add(paneeliPlussalleJaMiinukselle);
        container.add(new JLabel(" "));
        //------------------------------------------ 
        kaynnistysnappi = new JButton("Käynnistä");
        container.add(new JLabel(" "));
        container.add(kaynnistysnappi);
    }

    private AbstractButton etsiValittuNappi(ButtonGroup nappiryhma) {
        Enumeration<AbstractButton> napit = nappiryhma.getElements();
        while (napit.hasMoreElements()) {
            AbstractButton nappi = napit.nextElement();
            if (nappi.isSelected()) {
                return nappi;
            }
        }
        return null;
    }

    private static class PlusJaMiinusnapinKuuntelija implements ActionListener {

        JTextField aikaikkunanValintaKentta;

        private PlusJaMiinusnapinKuuntelija(JTextField aikaikkunanValintaKentta) {
            this.aikaikkunanValintaKentta = aikaikkunanValintaKentta;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int ikkunanPituus = Integer.parseInt(aikaikkunanValintaKentta.getText());
            if (e.getActionCommand().trim().equals("*2")) {
                ikkunanPituus *= 2;
                if (ikkunanPituus > 32768) {  // minne asti kannattaa laittaa ehdotuksia?
                    ikkunanPituus = 32768;
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
