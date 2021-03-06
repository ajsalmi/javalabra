package vokaalipeli;

import vokaalipeli.kayttoliittyma.Kayttoliittyma;
import vokaalipeli.kayttoliittyma.PerusGUI;
import vokaalipeli.peli.Vokaalipeli;

/**
 * Pääluokka joka Vokaalipeli-luokan ja Kayttoliittyma-luokan 
 * olioiden luomisen lisäksi luo käynnistää ne.
 * 
 * @author A J Salmi
 */
public class Main {
    
    /**
     * @param args the command line arguments
     * @see Kayttoliittyma
     * @see Vokaalipeli
     */
    public static void main(String[] args) {
        
        Vokaalipeli peli = new Vokaalipeli();
        Kayttoliittyma kayttis = new PerusGUI(peli, 1000, 400);
        peli.setKayttoliittyma(kayttis);
        kayttis.kaynnista();
        while (!peli.aaniLahdeAsetettu()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
            }
        } 
        peli.run();
    }
}
