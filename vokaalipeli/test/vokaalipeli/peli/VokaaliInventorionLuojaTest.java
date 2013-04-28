/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vokaalipeli.peli;

import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import vokaalipeli.domain.Vokaali;
import vokaalipeli.domain.VokaaliInventorio;

/**
 *
 * @author A J Salmi
 */
public class VokaaliInventorionLuojaTest {

    private VokaaliInventorionLuoja luoja;

    public VokaaliInventorionLuojaTest() {
        this.luoja = new VokaaliInventorionLuoja();
    }

    @Test
    public void kielivaihtoehdotEiOleLuotaessaNull() {
        assertNotNull(luoja.getKielivaihtoehdot());
    }

    @Test
    public void kielivaihtoehtojaVahintaanYksi() {
        int kielia = luoja.getKielivaihtoehdot().size();
        assertTrue(kielia > 0);
    }

    @Test
    public void kielivaihtoehtojenJoukossaSuomi() {
        boolean b = luoja.getKielivaihtoehdot().contains("suomi");
        assertTrue(b);
    }

    @Test
    public void suomenVokaalitLuodaan() {
        VokaaliInventorio inv = luoja.luoVokaalit("suomi");
        assertEquals("suomi", inv.getKieli());

        int vokaalienMaara = inv.getVokaalienMaara();
        assertEquals(8, vokaalienMaara);  

        for (int i = 0; i < 8; i++) {
            Vokaali v = inv.annaVokaali(i);
            if (!v.getNimi().matches("a|e|i|o|u|y|ä|ö")) {
                Assert.fail();
            }
        }
        
        // formanttien testaus? 
    }
}
