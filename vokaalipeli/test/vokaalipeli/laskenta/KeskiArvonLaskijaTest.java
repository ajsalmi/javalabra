/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vokaalipeli.laskenta;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author A J Salmi
 */
public class KeskiArvonLaskijaTest {

    private KeskiarvonLaskija laskija1;
    private KeskiarvonLaskija laskija2;

    public KeskiArvonLaskijaTest() {
        this.laskija1 = new KeskiarvonLaskija(3); // ei tarvitse olla kakkosen potensseja
        this.laskija2 = new KeskiarvonLaskija(1024);
    }

    @Test
    public void laskeminenToimiiKeskiarvollaYksi() {
        double[] arvot = {1.0, 2.0, 3.0};
        double[] ka = laskija1.laske(arvot);
        boolean oikein = tarkistaOnkoSamat(arvot, ka);
        assertTrue(oikein);
        
        arvot = new double[1024];
        arvot[35] = 4;
        arvot[36] = 2;
        arvot[39] = 100;
        ka = laskija2.laske(arvot);
        oikein = tarkistaOnkoSamat(arvot, ka);
        assertTrue(oikein);
    }

    @Test
    public void laskeminenToimiiKeskiarvollaKaksi() {
        laskija1.setLaskettavienKeskiarvojenLkm(2);
        laskija2.setLaskettavienKeskiarvojenLkm(2);
        
        double[] arvot = {1.0, 2.0, 3.0};
        double[] arvot2 = {-1, 4, 4};
        laskija1.laske(arvot);
        double[] ka = laskija1.laske(arvot2);
        double[] oikeaKeskiarvo = {0, 3, 3.5}; 
        
        boolean oikein = tarkistaOnkoSamat(oikeaKeskiarvo, ka);
        assertTrue(oikein);
        
        arvot = new double[1024];
        arvot[35] = 4;
        arvot[36] = 2;
        arvot[39] = 100;
        
        arvot2 = new double[1024];
        arvot2[35] = 7;
        arvot2[36] = -3;
        arvot2[40] = 60;

        oikeaKeskiarvo = new double[1024];
        oikeaKeskiarvo[35] = 5.5;
        oikeaKeskiarvo[36] = -0.5;
        oikeaKeskiarvo[39] = 50;
        oikeaKeskiarvo[40] = 30;
        
        laskija2.laske(arvot);
        ka = laskija2.laske(arvot2);
        
        oikein = tarkistaOnkoSamat(oikeaKeskiarvo, ka);
        assertTrue(oikein);
    }
    
    private boolean tarkistaOnkoSamat(double[] eka, double[] toka) {
        for (int i = 0; i < eka.length; i++) {
            if (eka[i] != toka[i]) {
                return false;
            }
        }
        return true;
    }
}
