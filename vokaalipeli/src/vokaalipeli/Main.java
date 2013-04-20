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
     * @param args the command line arguments
     * @see Kayttoliittyma
     * @see Vokaalipeli
     */
    public static void main(String[] args) {
        
        Vokaalipeli peli = new Vokaalipeli();
        Kayttoliittyma kayttis = new Kayttoliittyma(peli, 1000, 400);
        kayttis.run();
        while (peli.getStriimi() == null) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
            }
        }
        peli.kaynnista();        
    }
}
