/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vokaalipeli.laskenta;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author A J Salmi
 */
public class IkkunaFunktionLaskijaTest {

    private IkkunafunktionLaskija laskHann;
    private IkkunafunktionLaskija laskKolmio;
    private IkkunafunktionLaskija laskSuor;
    
    public IkkunaFunktionLaskijaTest() {
        this.laskHann = new IkkunafunktionLaskija(1024, Ikkunafunktio.HANN);
        this.laskKolmio = new IkkunafunktionLaskija(1024, Ikkunafunktio.KOLMIO);
        this.laskSuor = new IkkunafunktionLaskija(1024, Ikkunafunktio.SUORAKULMAINEN);        
    }

    @Test
    public void oikeatArvotTaulukonPaadyissa() {
        double a = 0.00_000_000_1;
        assertEquals(0, laskHann.annaKerroin(0), a);
        assertEquals(0, laskHann.annaKerroin(1023), a);
        
        assertEquals(0, laskKolmio.annaKerroin(0), a);
        assertEquals(0, laskKolmio.annaKerroin(1023), a);
        
        assertEquals(1, laskSuor.annaKerroin(0), a);
        assertEquals(1, laskSuor.annaKerroin(1023), a);                
    }
    
    @Test
    public void joitakinMuitaArvoja () {
        
        assertTrue(laskHann.annaKerroin(512) > Math.PI || laskHann.annaKerroin(511) < Math.PI);
        assertTrue(laskHann.annaKerroin(256) > 0.5 || laskHann.annaKerroin(255) < 0.5);
        
        double a = 0.00_000_000_1;
        assertEquals(130.0/511.5, laskKolmio.annaKerroin(130), a);
        assertEquals(323.0/511.5, laskKolmio.annaKerroin(700), a);
        
        assertEquals(1, laskSuor.annaKerroin(21), a);
        assertEquals(1, laskSuor.annaKerroin(123), a);                
    }
}
