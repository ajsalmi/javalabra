package vokaalipeli.laskenta;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Luokka laskee edellisten taulukoiden keskiarvon (aikasarjan suuntaisesti)
 *
 * TODO: ja tasoittaa myös taulukon arvoja (keskiarvo viereisistä indekseistä) ???.
 *
 * @author A J Salmi
 */
public class KeskiarvonLaskija {

    private double[] edellistenArvojenKeskiarvo;
    int monenkoKeskiarvoLasketaan = 1;
    private Queue<double[]> edelliset;

    public KeskiarvonLaskija(int taulukonPituus) {
        this.edellistenArvojenKeskiarvo = new double[taulukonPituus];
        this.edelliset = new ArrayDeque<>();
    }

    public KeskiarvonLaskija(int taulukonPituus, int monenkoKeskiarvo) {
        this(taulukonPituus);
        if (monenkoKeskiarvo > 1) {
            this.monenkoKeskiarvoLasketaan = monenkoKeskiarvo;
        }
    }
    
    public void setKeskiarvojenLkm (int monenkoKeskiarvo){
        this.monenkoKeskiarvoLasketaan = monenkoKeskiarvo;
    }

    public double[] laske(double[] uudetArvot) {

        for (int i = 0; i < uudetArvot.length; i++) {
            edellistenArvojenKeskiarvo[i] += uudetArvot[i] / monenkoKeskiarvoLasketaan;
        }

        while (edelliset.size() >= monenkoKeskiarvoLasketaan + 1) {
            double[] poisOtettu = edelliset.poll();
            for (int i = 0; i < uudetArvot.length; i++) {
                edellistenArvojenKeskiarvo[i] -= poisOtettu[i] / monenkoKeskiarvoLasketaan;
            }
        }
        return edellistenArvojenKeskiarvo;
    }
}
