package vokaalipeli.kayttoliittyma;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
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
    private double maxArvoAmplitudi;
    private double[] suhteellisetTaajuudet;   // eka on 0 ja viimeinen on 1 
    private int maxTaajuus;

    /**
     * Konstruktori.
     *
     * @param korkeus piirtoalustan korkeus
     * @param leveys piirtoalustan leveys
     * @param maxAmplitudi määrittää sen, kuinka suuret arvot mahtuvat kuvaajaan
     */
    public Taajuuskayra(int korkeus, int leveys, int maxAmplitudi, int suurinEsitettavaTaajuus) {
        super.setBackground(Color.DARK_GRAY);
        this.korkeus = korkeus;
        this.leveys = leveys;
        this.maxArvoAmplitudi = maxAmplitudi;
        this.maxTaajuus = suurinEsitettavaTaajuus;
    }

    public void setVokaali(Vokaali verrattava) {
        this.verrattavaVokaali = verrattava;
    }

    /**
     * Metodi ottaa vastaan uudet arvot ja kutsuu piirtämismetodia.
     *
     * @param uudetArvot taajuuskäyrälle syötetyt uudet arvot
     */
    public void setArvot(double[] uudetArvot) {

        if (this.suhteellisetTaajuudet == null) {
            luoTasavalisetTaajuudet(uudetArvot.length);
        }

        this.arvot = uudetArvot;
        paivita();
    }

    /**
     * Kasvattaa piirrettävien arvojen maksimia. Hyödyllinen jos käyrän arvot
     * eivät mahdu visualisointiin varattuun tilaan.
     */
    public void kasvataMaxArvoa() {
        this.maxArvoAmplitudi = 1.8 * maxArvoAmplitudi;
    }

    /**
     * Pienentää piirrettävien arvojen maksimia. Hyödyllinen jos käyrä näkyy
     * liian pienenä.
     */
    public void pienennaMaxArvoa() {
        this.maxArvoAmplitudi = 0.6 * maxArvoAmplitudi;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fill3DRect(2, 2, leveys, korkeus, true);

        if (arvot == null || suhteellisetTaajuudet == null) {
            return;
        }

        if (verrattavaVokaali != null) {
            piirraVokaalinFormantitJaNimi(g);
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

        int alkuX, alkuY, loppuX, loppuY;

        for (int j = 0; j < viivanPaksuus; j++) {
            alkuX = (int) (leveys * suhteellisetTaajuudet[i]) + j;
            alkuY = (int) (korkeus * (1 - arvot[i] / maxArvoAmplitudi));
            loppuX = (int) (leveys * suhteellisetTaajuudet[i + 1]) + j;
            loppuY = (int) (korkeus * (1 - arvot[i + 1] / maxArvoAmplitudi));
            grafiikka.drawLine(alkuX, alkuY, loppuX, loppuY);
        }
    }

    /**
     * Metodi luo tasaväliset taajuudet jakamalla käyrän leveyden annettuun
     * määrään pisteitä.
     *
     * @param taajuuksia moneenko eri tasaväliseen pisteeseen taajuudet
     * jakautuvat.
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
    private void piirraVokaalinFormantitJaNimi(Graphics g) {

        g.setColor(Color.BLACK);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 100));
        g.drawString(verrattavaVokaali.getNimi(), (int) (leveys * 0.9), (int) (korkeus * 0.3));

        g.setColor(Color.YELLOW);
        int kaistaleenLeveys = 700_000 / maxTaajuus;
        for (int taajuus : verrattavaVokaali.getFormantit()) {
            g.fillRect((int) (leveys * taajuus / maxTaajuus) - kaistaleenLeveys / 2, 2, kaistaleenLeveys, korkeus - 2);
        }

        g.setColor(Color.ORANGE);
        kaistaleenLeveys = 200_000 / maxTaajuus;
        for (int taajuus : verrattavaVokaali.getFormantit()) {
            g.fillRect((int) (leveys * taajuus / maxTaajuus) - kaistaleenLeveys / 2, 2, kaistaleenLeveys, korkeus - 2);
        }
    }
}
