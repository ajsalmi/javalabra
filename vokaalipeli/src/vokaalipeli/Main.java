package vokaalipeli;

import java.util.Random;
import vokaalipeli.domain.FastFourierMuokkaaja;

public class Main {

    public static void main(String[] args) {

        double[][] testi = {{0, 1, -1, -1, 1, 1, -2, 1}, {0, 0, 0, 0, 0, 0, 0, 0}};
        double[][] testi2 = {{1, 0, 0, -1, 0, 0, 1, 0, 0, 0, -1, 0, 0, 1, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
        double[][] testi3 = new double[2][256];

        for (int i = 0; i < 256; i++) {
            double hairio = new Random().nextDouble() + new Random().nextDouble() - 1;
            double arvo = 5 * Math.sin(i * Math.PI / 1.0002)
                    + Math.sin((i * Math.PI / 2) + 1)
                    + 0.7 * Math.sin((-i * Math.PI / 4) + 4)
                    + 1.0 * hairio;
            double ikkunaKerroin = Math.sin(i * Math.PI / 255); 
            //Math.atan(i/3.0-16)+Math.atan(67-i/3.0);
            //0.54 + 0.46*Math.cos(2*i*Math.PI/255); // Hamming 

            System.out.println(arvo*ikkunaKerroin);
            testi3[0][i] = arvo * ikkunaKerroin;
        }

        double[][] testattava = testi3;

        FastFourierMuokkaaja f = new FastFourierMuokkaaja(testattava[0].length);

        testattava = f.muokkaaFFT(testattava, true);

        System.out.println("+-+-+-+-+");
        for (int i = 0; i < testattava[0].length; i++) {
            double amplitudi = testattava[0][i]*testattava[0][i]+testattava[1][i]*testattava[1][i];
            System.out.println(Math.sqrt(amplitudi));
//            System.out.println(testattava[0][i] + ", " + testattava[1][i]);
        }



//        AudioFormat f = new AudioFormat(8000, 8, 1, true, true); // <--- valitse formaatti 
//        Mikrofoni mikki = new Mikrofoni(f);
//
//        AudioInputStream s = mikki.getInputStream();
//
//        int laskuri = 0;
//
//        while (true) {
//            try {
//                if (s.available()>1) {
//                    byte[] pari = new byte[2];
//                    s.read(pari);
//
//                    System.out.print(pari[0] + " " + pari[1] + " "); // <--- 2 bytes per frame
//                    laskuri++;
//
//                    if (laskuri == 40) {
//                        laskuri = 0;
//                         System.out.print("\nav: "+s.available()+" ");
//                   
//                        System.out.println();
//                    }
//                }
//            } catch (Exception ex) {
//                
//                // käsittele jotenkin
//            
//            }
//        }
    }
}
