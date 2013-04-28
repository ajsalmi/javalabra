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
 * Ikkuna parametrien kyselyä varten.
 *
 * @author A J Salmi
 */
public class ParametrienKyselyIkkuna extends JFrame {

    private JButton kaynnistysNappi;
    private JButton ohjeNappi;
    private JComboBox<String> taajuusValinta;
    private ButtonGroup tavuaPerNayteValinta;
    private JComboBox<Ikkunafunktio> ikkunafunktioValinta;
    private ButtonGroup etumerkillisyysValinta;
    private ButtonGroup tavujarjestysValinta;
    private JTextField aikaikkunanPituusValinta;

    public ParametrienKyselyIkkuna() {
        setPreferredSize(new Dimension(280, 480));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        luoKomponentit(getContentPane());
        pack();
        setVisible(true);
    }

    public JButton getKaynnistysNappi() {
        return kaynnistysNappi;
    }

    public int getNaytteenottoTaajuus() {
        String valittuTaajuusMerkkijonona = (String) this.taajuusValinta.getSelectedItem();
        return Integer.parseInt(valittuTaajuusMerkkijonona.split("\\D")[0]);
    }

    public boolean getEtumerkillisyys() {
        AbstractButton valittuNappi = etsiValittuNappi(etumerkillisyysValinta);
        if (valittuNappi.getText().contains("lli")) {  // etumerki_lli_nen
            return true;
        }
        return false;
    }

    public int getIkkunanKoko() {
        return Integer.parseInt(aikaikkunanPituusValinta.getText().trim());
    }

    public boolean getTavujarjestys() {
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
        AbstractButton valittuNappi = etsiValittuNappi(tavuaPerNayteValinta);
        String arvoMerkkinona = valittuNappi.getText().split("\\D")[0];
        return Integer.parseInt(arvoMerkkinona);
    }

    /**
     * Täytetään Container-luokan olio halutuilla komponenteilla: napit ja 
     * valikot audioformaatin valintaa varten (näytteenottotaajuus 'sample rate', 
     * näytteen tavumäärä 'bits per sample', etumerkillisyys, tavujärjestys
     * 'endianness') ja ikkunafunktiolle sekä aikaikkunan pituudelle. Lisäksi
     * asetetaan käynnistys- ja ohjenapit.
     * 
     * @param container
     */
    private void luoKomponentit(Container container) {
        container.setLayout(new GridLayout(25, 1));        
        luoTaajuusValinta(container);
        luoTavuaPerNayteValinta(container);
        luoEtumerkillisyysValinta(container);
        luoTavujarjestysValinta(container);
        luoIkkunaFunktionValinta(container);
        luoAikaikkunanPituusValinta(container);
        asetaLoputNapit(container);
    }

    /**
     * Etsii valitun napin annetusta nappiryhmästä (ButtonGroup).
     * 
     * @param nappiryhma nappiryhmä
     * @return valittu nappi tai null jos ei mitään valittuna
     */
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

    private void luoTaajuusValinta(Container container) {
        container.add(new JLabel(" näytteenottotaajuus"));
        String[] taajuusvaihtoehdot = {"8000 Hz", "11025 Hz", "16000 Hz", "22050 Hz", "44100 Hz"};
        taajuusValinta = new JComboBox<>(taajuusvaihtoehdot);
        taajuusValinta.setSelectedIndex(3);
        container.add(taajuusValinta);
        container.add(new JLabel(" "));
    }

    private void luoTavuaPerNayteValinta(Container container) {
        container.add(new JLabel(" näytteen koko bitteinä"));
        tavuaPerNayteValinta = new ButtonGroup();
        JRadioButton nappi = new JRadioButton("8 bittiä");
        tavuaPerNayteValinta.add(nappi);
        container.add(nappi);
        nappi = new JRadioButton("16 bittiä");
        nappi.setSelected(true);
        tavuaPerNayteValinta.add(nappi);
        container.add(nappi);
        container.add(new JLabel(" "));
    }

    private void luoEtumerkillisyysValinta(Container container) {
        container.add(new JLabel(" etumerkillisyys"));
        etumerkillisyysValinta = new ButtonGroup();
        JRadioButton nappi = new JRadioButton("etumerkillinen");
        nappi.setSelected(true);
        etumerkillisyysValinta.add(nappi);
        container.add(nappi);
        nappi = new JRadioButton("etumerkitön");
        etumerkillisyysValinta.add(nappi);
        container.add(nappi);
        container.add(new JLabel(" "));
    }

    private void luoTavujarjestysValinta(Container container) {
        container.add(new JLabel(" tavujärjestys"));
        tavujarjestysValinta = new ButtonGroup();
        JRadioButton nappi = new JRadioButton("big-endian");
        nappi.setSelected(true);
        tavujarjestysValinta.add(nappi);
        container.add(nappi);
        nappi = new JRadioButton("little-endian");
        tavujarjestysValinta.add(nappi);
        container.add(nappi);
        container.add(new JLabel(" "));
    }

    private void luoIkkunaFunktionValinta(Container container) {
        container.add(new JLabel(" ikkunafunktio"));
        Ikkunafunktio[] ikkunafunktiot = Ikkunafunktio.values();
        ikkunafunktioValinta = new JComboBox<>(ikkunafunktiot);
        container.add(ikkunafunktioValinta);
        container.add(new JLabel(" "));
    }

    private void luoAikaikkunanPituusValinta(Container container) {
        container.add(new JLabel(" aikaikkunan koko"));
        aikaikkunanPituusValinta = new JTextField("8192");
        aikaikkunanPituusValinta.setEditable(false);
        container.add(aikaikkunanPituusValinta);
        // ---
        JPanel paneeliSaatoNapeille = new JPanel(); // lue: "SäätöNapeille"
        paneeliSaatoNapeille.setLayout(new BoxLayout(paneeliSaatoNapeille, BoxLayout.X_AXIS));
        JButton kasvatusNappi = new JButton("     *2     ");
        paneeliSaatoNapeille.add(kasvatusNappi);
        kasvatusNappi.addActionListener(luoAikaikkunanValinnanNappienKuuntelija());
        // ---
        JButton pienennysNappi = new JButton("     /2     ");
        pienennysNappi.addActionListener(kasvatusNappi.getActionListeners()[0]);
        paneeliSaatoNapeille.add(pienennysNappi);
        // ---
        container.add(paneeliSaatoNapeille);
        container.add(new JLabel(" "));
    }

    private ActionListener luoAikaikkunanValinnanNappienKuuntelija() {
        ActionListener a = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int ikkunanPituus = Integer.parseInt(aikaikkunanPituusValinta.getText());
                if (e.getActionCommand().trim().equals("*2")) {
                    ikkunanPituus *= 2;
                    if (ikkunanPituus > 32768) {  // minne asti kannattaa laittaa ehdotuksia?
                        ikkunanPituus = 32768;
                    }
                    aikaikkunanPituusValinta.setText(ikkunanPituus + "");
                } else {
                    ikkunanPituus /= 2;
                    if (ikkunanPituus < 64) { // minne asti ehdotuksia ?
                        ikkunanPituus = 64;
                    }
                    aikaikkunanPituusValinta.setText(ikkunanPituus + "");
                }
            }
        };
        return a;
    }
    
    /**
     * Metodi asettaa käynnistys- ja ohjenapit. Ohjenappiin laitetaan lisäksi
     * kuuntelija infoikkunan luomista varten.
     * 
     * @param container 
     */    
    private void asetaLoputNapit(Container container) {
        kaynnistysNappi = new JButton("Käynnistä");
        container.add(kaynnistysNappi);
        
        ohjeNappi = new JButton(" ohje ");
        container.add(ohjeNappi);
        ohjeNappi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Infoikkuna(Info.PARAMETRIEN_VALINTA, ohjeNappi);
            }
        });
    }
}
