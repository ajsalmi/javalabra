package vokaalipeli.laskenta;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * [Tarkoitus olisi saada käyttöliittymään napit, joiden painaminen muutaman 
 * mutkan kautta päätyisi tänne. Ei ehdi tähän toteutukseen.]
 * 
 * Luokka laskee edellisten taulukoiden keskiarvon (taulukon jokaisessa indeksissä 
 * olevan arvon ja edellisten taulukoiden vastaavaan indeksin arvojen keskiarvo)
 *
 * TODO: ja tasoittaa myös taulukon arvoja (keskiarvo taulukon viereisissä 
 * indekseissä olevista arvoista).
 *
 * @author A J Salmi
 */
public class KeskiarvonLaskija {

    private double[] edellistenArvojenKeskiarvo;
    int monenkoKeskiarvoLasketaan = 1; // luodaan keskiarvolla 1 eli ei laske mitään
    private Queue<double[]> edelliset;

    public KeskiarvonLaskija(int taulukonPituus) {
        this.edellistenArvojenKeskiarvo = new double[taulukonPituus];
        this.edelliset = new ArrayDeque<>();
    }
    
    public void setLaskettavienKeskiarvojenLkm (int monenkoKeskiarvo){
        this.monenkoKeskiarvoLasketaan = monenkoKeskiarvo;
    }

    public double[] laske(double[] uudetArvot) {
        
        edelliset.add(uudetArvot);
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
