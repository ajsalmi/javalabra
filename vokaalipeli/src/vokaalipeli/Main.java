package vokaalipeli;

import vokaalipeli.kayttoliittyma.Kayttoliittyma;
import vokaalipeli.peli.Vokaalipeli;

/**
 * Pääluokka joka vain luo Vokaalipeli-luokan olion luomisen lisäksi luo 
 * Kayttoliittyma-luokan olion ja käynnistää sen.
 * 
 * @author A J Salmi
 */
public class Main {
    
    /**
     * @see Kayttoliittyma
     * @see Vokaalipeli
     */
    public static void main(String[] args) {
        
        Vokaalipeli peli = new Vokaalipeli();
        Kayttoliittyma testikayttis = new Kayttoliittyma(peli, 1000, 400);
        testikayttis.run();
        while (peli.getStriimi() == null) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
            }
        }
        peli.kaynnista();        
    }
}
