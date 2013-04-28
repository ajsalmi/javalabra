package vokaalipeli.domain;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author A J Salmi
 */
public class VokaaliTest {

    private Vokaali testattavaVokaali;

    public VokaaliTest() {
        this.testattavaVokaali = new Vokaali("a", 720, 1240, 2455);
    }

    private void asetaE() {
        this.testattavaVokaali = new Vokaali("e", 450, 2240, 2810);
    }

    @Test
    public void vokaaliEiOleNull() {
        assertNotNull(this.testattavaVokaali);
    }

    @Test
    public void vokaalinNimiOikein() {
        assertEquals("a", testattavaVokaali.getNimi());
        asetaE();
        assertEquals("e", testattavaVokaali.getNimi());
    }

    @Test
    public void vokaalinEkaFormanttiOikein() {
        assertEquals(720, testattavaVokaali.getFormantit()[0]);
        asetaE();
        assertEquals(450, testattavaVokaali.getFormantit()[0]);

    }

    @Test
    public void vokaalinTokaFormanttiOikein() {
        assertEquals(1240, testattavaVokaali.getFormantit()[1]);
        asetaE();
        assertEquals(2240, testattavaVokaali.getFormantit()[1]);
    }

    @Test
    public void vokaalinKolmasFormanttiOikein() {
        assertEquals(2455, testattavaVokaali.getFormantit()[2]);
        asetaE();
        assertEquals(2810, testattavaVokaali.getFormantit()[2]);
    }

    @Test
    public void vokaalinEqualsMetodiToimii() {
        boolean samat = testattavaVokaali.equals(new Vokaali("a", 720, 1240, 2455));
        assertTrue(samat);
        
        assertFalse(testattavaVokaali.equals(new Vokaali("y", 300, 1995, 2430)));
        samat = testattavaVokaali.equals(new Vokaali("a*", 720, 1240, 2455));
        assertFalse(samat);
        
        asetaE();
        samat = testattavaVokaali.equals(new Vokaali("e", 450, 2240, 2810));
        assertTrue(samat);         
    }
}
