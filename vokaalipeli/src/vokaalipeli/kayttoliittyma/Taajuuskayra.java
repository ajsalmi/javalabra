package vokaalipeli.kayttoliittyma;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayDeque;
import java.util.Queue;
import javax.swing.JPanel;
import vokaalipeli.domain.Vokaali;

/**
 * Luokka vastaa taajuuskäyrän piirtämisestä/päivittämisestä.
 *
 * @author A J Salmi
 */
public class Taajuuskayra extends JPanel {
    
    private Vokaali verrattavaVokaali;
    private int korkeus;
    private int leveys;
    private double[] arvot;
    private double[] suhteellisetTaajuudet;   // eka on 0 ja viimeinen on 1 
    private double maxArvo;

    /**
     * Konstruktori. 
     * 
     * @param korkeus piirtoalustan korkeus 
     * @param leveys piirtoalustan leveys
     * @param maxArvo määrittää sen, kuinka suuret arvot mahtuvat kuvaajaan
     */
    public Taajuuskayra(int korkeus, int leveys, int maxArvo) {
        super.setBackground(Color.DARK_GRAY);
        this.korkeus = korkeus;
        this.leveys = leveys;
        this.maxArvo = maxArvo;
    }

    public void setVokaali (Vokaali v){
        this.verrattavaVokaali = v;
    }
    
    /**
     * Metodi ottaa vastaan uudet arvot ja kutsuu piirtämismetodia.
     * 
     * @param uudetArvot taajuuskäyrälle syötetyt uudet arvot 
     */
    public void setArvot(double[] uudetArvot) {

        if (this.suhteellisetTaajuudet == null ){//|| suhteellisetTaajuudet.length != uudetArvot.length) {
            luoTasavalisetTaajuudet(uudetArvot.length);
        }

        this.arvot = uudetArvot;
        paivita();
    }

    /**
     * Kasvattaa piirrettävien arvojen maksimia. Hyödyllinen jos käyrän arvot eivät 
     * mahdu visualisointiin varattuun tilaan. 
     */
    public void kasvataMaxArvoa() { // TODO: näille napit GUI:hin
        this.maxArvo = 1.5 * maxArvo;
    }

     /**
     * Pienentää piirrettävien arvojen maksimia. Hyödyllinen jos käyrä näkyy liian
     * pienenä.
     */
    public void pienennaMaxArvoa() { // TODO: näille napit GUI:hin
        this.maxArvo = 0.8 * maxArvo;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fill3DRect(2, 2, leveys, korkeus, true);

        if (arvot == null || suhteellisetTaajuudet == null) {
            return;
        }

        //
        // TODO: piirrä vokaalin formantit esim. keltaisella
        //
        if (verrattavaVokaali!=null){
            piirraVokaalinFormantit();
        }

        g.setColor(Color.BLACK);
        for (int i = 0; i < arvot.length - 1; i++) {
            piirraViiva(g, i, 1);
        }

        g.setColor(Color.GRAY);
        g.draw3DRect(2, 2, leveys, korkeus, true);
    }

    /**
     * Metodi kutsuu yläluokan (JPanel) repaint()-metodia.
     */
    public void paivita() {
        repaint();
    }

    /**
     * Metodi piirtää viivan.
     * 
     * @param grafiikka grafiikkaolio
     * @param i piirrettävän viivan alkupisteen indeksi
     * @param viivanPaksuus viivan paksuus leveyssuunnassa
     */
    private void piirraViiva(Graphics grafiikka, int i, int viivanPaksuus) {
        if (arvot == null || suhteellisetTaajuudet == null) { // onko tarpeen?
            return;
        }
        if (arvot.length != suhteellisetTaajuudet.length) { // onko tarpeen?
            return;
        }

        int alkuX, alkuY, loppuX, loppuY;
        for (int j = 0; j < viivanPaksuus; j++) {
            alkuX = (int) (leveys * suhteellisetTaajuudet[i]) + j;
            alkuY = (int) (korkeus * (1 - arvot[i] / maxArvo));
            loppuX = (int) (leveys * suhteellisetTaajuudet[i + 1]) + j;
            loppuY = (int) (korkeus * (1 - arvot[i + 1] / maxArvo));
            grafiikka.drawLine(alkuX, alkuY, loppuX, loppuY);
        }
    }

    /**
     * Metodi luo tasaväliset taajuudet jakamalla käyrän leveyden annettuun
     * määrään pisteitä.
     * 
     * @param taajuuksia moneenko eri tasaväliseen pisteeseen taajuudet jakautuvat.  
     */
    private void luoTasavalisetTaajuudet(int taajuuksia) {
        double[] tasavalisetTaajuudet = new double[taajuuksia];
        for (int i = 0; i < taajuuksia; i++) {
            tasavalisetTaajuudet[i] = (i + 1.0) / (taajuuksia + 1.0);
        }
        suhteellisetTaajuudet = tasavalisetTaajuudet;
    }

    /**
     * Metodi piirtää verrattavan vokaalin formanttitaajuudet käyrälle.
     * 
     */
    private void piirraVokaalinFormantit() {
        
        // TODO
        
    }
}
