package vokaalipeli;

import vokaalipeli.kayttoliittyma.Kayttoliittyma;
import vokaalipeli.peli.Vokaalipeli;

public class Main {

    public static void main(String[] args) {

        Vokaalipeli peli = new Vokaalipeli();
        Kayttoliittyma testikayttis = new Kayttoliittyma(peli, 1000, 400);
        testikayttis.run();
        while(peli.getMikrofoni()==null){;}
        peli.kaynnista();     
    }
}
