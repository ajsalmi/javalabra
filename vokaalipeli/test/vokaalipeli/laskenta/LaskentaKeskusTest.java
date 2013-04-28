/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vokaalipeli.laskenta;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author A J Salmi
 */
public class LaskentaKeskusTest {

    private LaskentaKeskus keskus1 = new LaskentaKeskus(64);
    private LaskentaKeskus keskus2 = new LaskentaKeskus(1024);

    @Test
    public void ikkunanPituusLuodaanOikein() {
        assertEquals(64, keskus1.getAikaikkunanPituus());
        assertEquals(1024, keskus2.getAikaikkunanPituus());
    }
    
    //
    // täältä jäi puuttumaan pari testiä
    // 
}
