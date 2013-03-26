package vokaalipeli;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import vokaalipeli.domain.Mikrofoni;

public class Main {

    public static void main(String[] args) {
        AudioFormat f = new AudioFormat(8000, 8, 1, true, true); // <--- valitse formaatti 
        Mikrofoni mikki = new Mikrofoni(f);

        AudioInputStream s = mikki.getInputStream();

        int laskuri = 0;

        while (true) {
            try {
                if (s.available()>1) {
                    byte[] pari = new byte[2];
                    s.read(pari);

                    System.out.print(pari[0] + " " + pari[1] + " "); // <--- 2 bytes per frame
                    laskuri++;

                    if (laskuri == 40) {
                        laskuri = 0;
                         System.out.print("\nav: "+s.available()+" ");
                   
                        System.out.println();
                    }
                }
            } catch (Exception ex) {
                
                // käsittele jotenkin
            
            }
        }
    }
}



