package vokaalipeli.kayttoliittyma;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayDeque;
import java.util.Queue;
import javax.swing.JPanel;
//import vokaalipeli.domain.Vokaali;

public class Taajuuskayra extends JPanel {
//    private Vokaali verrattava;

    private int korkeus;
    private int leveys;
    private double[] edellistenKeskiarvo;
    private Queue<double[]> useampiEdellinen = new ArrayDeque<>();
    private double[] taajuudet;   // suhteellinen: eka on 0 ja viimeinen on 1 
    private double maxArvo;

    public Taajuuskayra(int korkeus, int leveys) {
        super.setBackground(Color.DARK_GRAY);
        this.korkeus = korkeus;
        this.leveys = leveys;
        this.maxArvo = 30000;     // tätä tarvitsee muuttaa eri ikkunanpituuksilla
    }

    public void setArvot(double[] uudetArvot) {
        if (this.edellistenKeskiarvo == null|| edellistenKeskiarvo.length != uudetArvot.length) {
            this.edellistenKeskiarvo = new double[uudetArvot.length];
        }

        if (this.taajuudet == null || taajuudet.length != uudetArvot.length) {
            luoTasaisetTaajuusvalit(uudetArvot.length);
        }
        int monenkoKeskiarvoLasketaan = 10; // tatakin voisi kysya kayttajalta

        useampiEdellinen.add(uudetArvot);
        for (int i = 0; i < uudetArvot.length; i++) {
            edellistenKeskiarvo[i] += uudetArvot[i] / monenkoKeskiarvoLasketaan;
        }

        if (useampiEdellinen.size() >= monenkoKeskiarvoLasketaan) {
            double[] poisotettu = useampiEdellinen.poll();
            for (int i = 0; i < uudetArvot.length; i++) {
                edellistenKeskiarvo[i] -= poisotettu[i] / monenkoKeskiarvoLasketaan;
            }
        }
        this.paivita();
    }

    public void setArvot(double[] arvot, double[] taajuudet) {
        this.taajuudet = taajuudet;
        setArvot(arvot);
    }

    // TODO: 
    // public void vaidaVerrattavaVokaali (){}
    // gettereitä
    public void kasvataMaxArvoa() {
        this.maxArvo = 1.1 * maxArvo;
    }

    public void pienennaMaxArvoa() {
        this.maxArvo = 0.9 * maxArvo;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.WHITE);
        g.fill3DRect(2, 2, leveys, korkeus, true);

        if (this.edellistenKeskiarvo == null || this.taajuudet == null) {
            return;
        }

        //
        // piirrä vokaalin formantit esim. keltaisella
        //

        g.setColor(Color.BLACK);
        for (int i = 0; i < edellistenKeskiarvo.length-1; i++) {
            piirraViiva(g, i, 1);
        }

        g.setColor(Color.GRAY);
        g.draw3DRect(2, 2, leveys, korkeus, true);

    }

    public void paivita() {
        repaint();
    }

    private void piirraViiva(Graphics g, int i, int viivanPaksuus) {
        if (edellistenKeskiarvo == null || taajuudet == null) {
            return;
        }
        if (edellistenKeskiarvo.length != taajuudet.length) {
            return;
        }

        for (int j = 0; j < viivanPaksuus; j++) {
            g.drawLine((int) (leveys * taajuudet[i]) + j, (int) (korkeus * (1 - edellistenKeskiarvo[i] / maxArvo)),
                    (int) (leveys * taajuudet[i + 1]) + j, (int) (korkeus * (1 - edellistenKeskiarvo[i + 1] / maxArvo)));
        }
    }

    private void luoTasaisetTaajuusvalit(int pisteita) {
        double[] tasavalisetTaajuudet = new double[pisteita];
        for (int i = 0; i < pisteita; i++) {
            tasavalisetTaajuudet[i] = (i + 1.0) / (pisteita + 1.0);
        }
        this.taajuudet = tasavalisetTaajuudet;
    }
}
