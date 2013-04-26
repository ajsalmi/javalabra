package vokaalipeli.kayttoliittyma;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

/**
 * Tämä luokka on nimensämukaisesti ikkina joka antaa informaatiota käyttäjälle.
 *
 * @author A J Salmi
 */
public class Infoikkuna extends JFrame {

    private Info info;
    private JButton poisKykettavaNappi;

    /**
     * Konstruktori luo uuden infoikkunan parametrina saamansa Infon (lueteltu
     * tyyppi) avulla.
     *
     * @param info tieto siitä millainen infoikkuna pitää luoda
     * @param nappi viite nappiin jota klikkaamalla tämä Infoikkuna syntyy.
     *
     * @see Info
     */
    public Infoikkuna(Info info, JButton nappi) {
        super("   Ohje");
        this.info = info;
        this.poisKykettavaNappi = nappi;
        setPreferredSize(new Dimension(500, 400));
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE); // haha, yrität kuitenkin!
        luoKomponentit(this.getContentPane());
        pack();
        setVisible(true);
    }

    /**
     * Täyttää ikkunan halutuilla elementeillä: tekstialue (kutsuttuaan metodia
     * luoOhjeet()) ja napin ikkunan sulkemista varten.
     *
     * @param container
     */
    private void luoKomponentit(Container container) {
        JButton okNappi = new JButton("OK");
        container.add(luoOhjeet());
        container.add(okNappi, BorderLayout.SOUTH);
        okNappi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                poisKykettavaNappi.setEnabled(true);
            }
        });
    }

    /**
     * Metodi luo tekstialueen (JTextArea) johon asettaa oikean tekstin. Kytkee
     * napin pois päältä ettei infoikkunoita voisi luoda aina lisää.
     *
     * @return ohjeet
     */
    private JTextArea luoOhjeet() {
        poisKykettavaNappi.setEnabled(false);

        JTextArea ohjeTekstialue = new JTextArea();
        ohjeTekstialue.setEditable(false);

        if (this.info == Info.PAAIKKUNAN_KAYTTO) {
            ohjeTekstialue.append(
                    "\n  Kieli on oletusarvoisesti suomi. \n\n"
                    + "  Tutustu ensin käyrään, tarkastele miten se esittää äänisignaalin ja \n"
                    + "  miten eri vokaalit piirtyvät käyrälle. Ääntöväylän pituus vaihtelee \n"
                    + "  eri puhujien välillä anatomisista syistä johtuen ja siksi täsmälleen \n"
                    + "  samojen äänteiden akustiset ominaisuudet vaihtelevat eri puhujilla. \n"
                    + "  Selkeimmin tämä näkyy äänteen taajuuspiikkien (ns. formanttitaajuuksien)\n"
                    + "  siirtymisenä joko oikealle tai vasemmalle, kuitenkin niiden keskinäisen \n"
                    + "  suhteen pysyessä samana. Voit käyttää nuolinäppäimiä (oikea ja vasen) \n"
                    + "  säätämään vokaalien formantteja varten piirrettyjä keltaisia kaistaleita.\n\n\n "
                    + "  Alapaneelin napeilla voit vaikuttaa käyrän korkeuteen ja pyytää uuden vokaalin. \n"
                    + "  ... \n"
                    + "  ... kielen, kun olet valmis siirtymään suomen kielestä vieraan kielen vokaleihin. \n");
        } else if (this.info == Info.PARAMETRIEN_VALINTA) {
            ohjeTekstialue.append(
                    "\n  Kokeile käynnistää vokaalipeliä eri parametreilla. Aluksi on ääniformaatin \n"
                    + "  ominaisuudet. Näytteenottotaajuus ('sample rate') kertoo kuinka monta kertaa \n"
                    + "  sekunnissa syötteestä otetaan näyte. Näytteen koko bitteinä ('bits per sample') \n"
                    + "  kertoo digitaalisen signaalin resoluution. Etumerkillisyys kertoo sen onko \n"
                    + "  näytteissä vain positiivisia arvoja vai myös negatiivisia. Tavujärjestyksellä \n"
                    + "  puolestaan tarkoitetaan sitä missä järjestyksessä tavut esitetään formaatissa. \n"
                    + "  Näillä kahdella viimeisellä arvolla ei ole käytännössä merkitystä itse pelin \n"
                    + "  tai taajuuskäyrän esittämisen kannalta.\n\n\n"
                    + "  Seuraavaksi on ikkunafunktion valinta, joka määrittää miten äänisyötettä \n"
                    + "  käsitellään. Oletusarvona on Hann-funktio, mutta voit myös kokeilla miten \n"
                    + "  muut ikkunafunktiot vaikuttavat käyrän esitykseen. Aikaikkuna määrittää sen \n"
                    + "  kuinka pitkä pätkä äänisyötettä analysoidaan kerralla. Pidemmällä aikaikkunalla\n"
                    + "  taajuudet erottuvat tarkemmin mutta käyrän viive on suurempi, koska pidemmän \n"
                    + "  aikaikkunan täyttäminen yksittäisillä syötteillä kestää pidempään.");
        } else if (this.info == Info.EI_TUETTU_FORMAATTI) {
            ohjeTekstialue.append(
                    "\n  Antamaasi formaattia ei tueta. Kokeile jotain muuta. \n");
        }
        return ohjeTekstialue;
    }
}
