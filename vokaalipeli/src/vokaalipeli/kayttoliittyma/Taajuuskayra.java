package vokaalipeli.kayttoliittyma;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayDeque;
import java.util.Queue;
import javax.swing.JPanel;

/**
 * Luokka vastaa taajuuskäyrän piirtämisestä/päivittämisestä. Se myös laskee 
 * visualisointia varten viimeisimpien käyrien keskiarvon (aikasarjan suuntaisesti).
 *
 * @author A J Salmi
 */
public class Taajuuskayra extends JPanel {
//   TODO: private Vokaali verrattava;

    private int korkeus;
    private int leveys;
    private double[] edellistenKeskiarvo;
    private int monenkoKeskiarvoLasketaan = 1; // TODO: kysy tätä käyttäjältä
    private Queue<double[]> edelliset = new ArrayDeque<>();
    private double[] suhteellisetTaajuudet;   // eka on 0 ja viimeinen on 1 
    private double maxArvo;
    private boolean arvotValmiina;
    private boolean piirtoValmiina;

    /**
     * Konstruktori. 
     * 
     * @param korkeus piirtoalustan korkeus 
     * @param leveys piirtoalustan leveys
     */
    public Taajuuskayra(int korkeus, int leveys) {
        super.setBackground(Color.DARK_GRAY);
        this.korkeus = korkeus;
        this.leveys = leveys;
        this.maxArvo = 60000;     // TODO: kysy tätä käyttäjältä ???
        this.piirtoValmiina = true;
        this.arvotValmiina = true;
    }

    public boolean odottaaArvoja() {
        return this.piirtoValmiina;
    }

    /**
     * Metodi ottaa vastaan uudet arvot, laskee edellisten käyrien keskiarvon
     * ja kutsuu piirtämismetodia.
     * 
     * @param uudetArvot taajuuskäyrälle syötetyt uudet arvot 
     */
    public void setArvot(double[] uudetArvot) {

        while (!piirtoValmiina) {
            try {
                Thread.sleep(0, 1);
            } catch (InterruptedException ex) {
            }
        }

        if (this.edellistenKeskiarvo == null || edellistenKeskiarvo.length != uudetArvot.length) {
            this.edellistenKeskiarvo = new double[uudetArvot.length];
        }

        if (this.suhteellisetTaajuudet == null || suhteellisetTaajuudet.length != uudetArvot.length) {
            luoTasavalisetTaajuudet(uudetArvot.length);
        }

        edelliset.add(uudetArvot);
        for (int i = 0; i < uudetArvot.length; i++) {
            edellistenKeskiarvo[i] += uudetArvot[i] / monenkoKeskiarvoLasketaan;
        }

        if (edelliset.size() >= monenkoKeskiarvoLasketaan + 1) {
            double[] poisOtettu = edelliset.poll();
            for (int i = 0; i < uudetArvot.length; i++) {
                edellistenKeskiarvo[i] -= poisOtettu[i] / monenkoKeskiarvoLasketaan;
            }
        }

        arvotValmiina = true;
        piirtoValmiina = false;
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

        if (edellistenKeskiarvo == null || suhteellisetTaajuudet == null) {
            return;
        }

        //
        // TODO: piirrä vokaalin formantit esim. keltaisella
        //

        g.setColor(Color.BLACK);
        for (int i = 0; i < edellistenKeskiarvo.length - 1; i++) {
            piirraViiva(g, i, 1);
        }

        g.setColor(Color.GRAY);
        g.draw3DRect(2, 2, leveys, korkeus, true);

        // piirto valmis
        piirtoValmiina = true;
        arvotValmiina = false;
    }

    /**
     * Metodi kutsuu yläluokan (JPanel) repaint()-metodia.
     */
    public void paivita() {
        
        while (!arvotValmiina){
            try {
                Thread.sleep(0,1);
            } catch (InterruptedException ex) {
//                Logger.getLogger(Taajuuskayra.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
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
        if (edellistenKeskiarvo == null || suhteellisetTaajuudet == null) { // onko tarpeen?
            return;
        }
        if (edellistenKeskiarvo.length != suhteellisetTaajuudet.length) { // onko tarpeen?
            return;
        }

        int alkuX, alkuY, loppuX, loppuY;
        for (int j = 0; j < viivanPaksuus; j++) {
            alkuX = (int) (leveys * suhteellisetTaajuudet[i]) + j;
            alkuY = (int) (korkeus * (1 - edellistenKeskiarvo[i] / maxArvo));
            loppuX = (int) (leveys * suhteellisetTaajuudet[i + 1]) + j;
            loppuY = (int) (korkeus * (1 - edellistenKeskiarvo[i + 1] / maxArvo));
            grafiikka.drawLine(alkuX, alkuY, loppuX, loppuY);
        }
    }

    /**
     * Metodi luo tasaväliset taajuudet jakamalla käyrän leveyden annettuun
     * määrään pisteitä.
     * 
     * @param taajuuksia montako eri tasavälistä taajuutta käyrässä esitetään 
     */
    private void luoTasavalisetTaajuudet(int taajuuksia) {
        double[] tasavalisetTaajuudet = new double[taajuuksia];
        for (int i = 0; i < taajuuksia; i++) {
            tasavalisetTaajuudet[i] = (i + 1.0) / (taajuuksia + 1.0);
        }
        suhteellisetTaajuudet = tasavalisetTaajuudet;
    }
}
