package vokaalipeli.kayttoliittyma;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JPanel;
import vokaalipeli.domain.Vokaali;

/**
 * Luokka vastaa taajuuskäyrän ja piirrettävän vokaalin päivittämisestä. 
 *
 * @author A J Salmi
 */
public class Taajuuskayra extends JPanel {

    private Vokaali piirrettavaVokaali;
    private double korjausKertoimenMuutos = 1;
    private int korkeus;
    private int leveys;
    private double[] amplitudit;
    private double maxAmplitudi;
    private double[] suhteellisetTaajuudet;   // eka on 0 ja viimeinen on 1 
    private int maxTaajuus;
    private boolean piirtoValmiina = true;

    /**
     * Konstruktori.
     *
     * @param korkeus piirtoalustan korkeus
     * @param leveys piirtoalustan leveys
     * @param maxAmplitudi määrittää sen, kuinka suuret arvot mahtuvat kuvaajaan
     */
    public Taajuuskayra(int korkeus, int leveys, int maxAmplitudi, int maxTaajuus) {
        this.korkeus = korkeus;
        this.leveys = leveys;
        this.maxAmplitudi = maxAmplitudi;
        this.maxTaajuus = maxTaajuus;
        setBackground(Color.DARK_GRAY.darker());
    }

    public void kasvataKorjausKerrointa() {
        this.korjausKertoimenMuutos *= 1.001;
    }

    public void pienennaKorjausKerrointa() {
        this.korjausKertoimenMuutos *= 0.999;
    }

    /**
     * 
     * 
     * @param uusiVokaali uusi vokaali
     */
    public void setVokaali(Vokaali uusiVokaali) {
        this.korjausKertoimenMuutos = 1;
        this.piirrettavaVokaali = uusiVokaali;
    }

    public double getKorjausKerroin(){
        return this.korjausKertoimenMuutos;
    }
    
    public boolean getPiirtoValmiina() {
        return this.piirtoValmiina;
    }

    /**
     * Metodi ottaa vastaan uudet arvot ja kutsuu päivitysmetodia.
     *
     * @param uudetArvot taajuuskäyrälle syötetyt uudet arvot
     */
    public void setArvot(double[] uudetArvot) {
        this.piirtoValmiina = false;
        if (this.suhteellisetTaajuudet == null) {
            luoTasavalisetTaajuudet(uudetArvot.length);
        }
        this.amplitudit = uudetArvot;
        paivita();
    }

    /**
     * Kasvattaa piirrettävien arvojen maksimia. Hyödyllinen jos käyrä ei mahdu
     * ikkunaan.
     */
    public void kasvataMaxArvoa() {
        this.maxAmplitudi = 1.8 * maxAmplitudi;
    }

    /**
     * Pienentää piirrettävien arvojen maksimia. Hyödyllinen jos käyrä näkyy
     * liian pienenä.
     */
    public void pienennaMaxArvoa() {
        this.maxAmplitudi = 0.6 * maxAmplitudi;
    }

    /**
     * Yläluokan korvattu metodi.
     * 
     * @param g grafiikkaolio
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fill3DRect(2, 2, leveys, korkeus, true);

        if (amplitudit == null || suhteellisetTaajuudet == null) return;
        
        if (piirrettavaVokaali != null) {
            piirraVokaalinFormantitJaNimi(g);
        }
        
        g.setColor(Color.BLACK);
        for (int i = 0; i < amplitudit.length - 1; i++) {
            piirraViiva(g, i, 1);
        }

        g.setColor(Color.GRAY);
        g.draw3DRect(2, 2, leveys, korkeus, true);
        this.piirtoValmiina = true;
    }

    /**
     * Metodi kutsuu yläluokan (JPanel) repaint-metodia.
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
            alkuY = (int) (korkeus * (1 - amplitudit[i] / maxAmplitudi));
            loppuX = (int) (leveys * suhteellisetTaajuudet[i + 1]) + j;
            loppuY = (int) (korkeus * (1 - amplitudit[i + 1] / maxAmplitudi));
            grafiikka.drawLine(alkuX, alkuY, loppuX, loppuY);
        }
    }

    /**
     * Metodi luo tasaväliset taajuudet jakamalla käyrän leveyden annettuun
     * määrään pisteitä.
     *
     * @param eriTaajuuksienLkm moneenko eri tasaväliseen pisteeseen käyrän leveys
     * pitää jakaa.
     */
    private void luoTasavalisetTaajuudet(int eriTaajuuksienLkm) {
        double[] tasavalisetTaajuudet = new double[eriTaajuuksienLkm];
        for (int i = 0; i < eriTaajuuksienLkm; i++) {
            tasavalisetTaajuudet[i] = (i + 1.0) / (eriTaajuuksienLkm + 1.0);
        }
        suhteellisetTaajuudet = tasavalisetTaajuudet;
    }

    /**
     * Metodi piirtää vokaalin formanttitaajuudet ja nimen.
     *
     */
    private void piirraVokaalinFormantitJaNimi(Graphics g) {
        g.setColor(Color.YELLOW);
        int kaistaleenLeveys = 700_000 / maxTaajuus;
        for (int taajuus : piirrettavaVokaali.getFormantit()) {
            g.fillRect((int) (korjausKertoimenMuutos * leveys * taajuus / maxTaajuus) - kaistaleenLeveys / 2, 2, kaistaleenLeveys, korkeus - 2);
        }

        g.setColor(Color.ORANGE);
        kaistaleenLeveys = 200_000 / maxTaajuus;
        for (int taajuus : piirrettavaVokaali.getFormantit()) {
            g.fillRect((int) (korjausKertoimenMuutos * leveys * taajuus / maxTaajuus) - kaistaleenLeveys / 2, 2, kaistaleenLeveys, korkeus - 2);
        }
        
        g.setColor(Color.BLACK);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 100));
        g.drawString(piirrettavaVokaali.getNimi(), (int) (leveys * 0.9), (int) (korkeus * 0.3));
    }
}
