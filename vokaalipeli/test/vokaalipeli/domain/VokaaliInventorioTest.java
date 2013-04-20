package vokaalipeli.domain;

import java.util.ArrayList;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author A J Salmi
 */
public class VokaaliInventorioTest {

    private VokaaliInventorio vokaalit;

    public VokaaliInventorioTest() {
        ArrayList<Vokaali> vokaalilista = luoSuomenVokaalit();
        this.vokaalit = new VokaaliInventorio(vokaalilista, "suomi");
    }

    @Test
    public void vokaaliInventorioEiOleNull() {
        assertNotNull(vokaalit);
    }

    @Test
    public void getKieliToimii() {
        assertEquals("suomi", vokaalit.getKieli());

        this.vokaalit = new VokaaliInventorio(luoYksivokaalisenKielenVokaalit(), "yksivokaalinen");
        assertEquals("yksivokaalinen", vokaalit.getKieli());

        this.vokaalit = new VokaaliInventorio(null, "null");
        assertEquals("null", vokaalit.getKieli());
    }

    @Test
    public void getVokaalienMaaraToimii() {
        assertEquals(8, vokaalit.getVokaalienMaara());

        this.vokaalit = new VokaaliInventorio(luoYksivokaalisenKielenVokaalit(), "yksivokaalinen");
        assertEquals(1, vokaalit.getVokaalienMaara());

        this.vokaalit = new VokaaliInventorio(new ArrayList<Vokaali>(), "nollavokaalinen");
        assertEquals(0, vokaalit.getVokaalienMaara());

        this.vokaalit = new VokaaliInventorio(null, "null");
        assertEquals(-1, vokaalit.getVokaalienMaara());
    }

    private ArrayList<Vokaali> luoYksivokaalisenKielenVokaalit() {
        ArrayList<Vokaali> vokaalilista = new ArrayList<>();
        vokaalilista.add(new Vokaali("a", 700, 1200, 2600));
        return vokaalilista;
    }

    private ArrayList<Vokaali> luoSuomenVokaalit() {
        ArrayList<Vokaali> vokaalilista = new ArrayList<>();
        vokaalilista.add(new Vokaali("a", 720, 1240, 2455));
        vokaalilista.add(new Vokaali("e", 450, 2240, 2810));
        vokaalilista.add(new Vokaali("i", 275, 2495, 3200));
        vokaalilista.add(new Vokaali("o", 515, 905, 2430));
        vokaalilista.add(new Vokaali("u", 340, 605, 2615));
        vokaalilista.add(new Vokaali("y", 300, 1995, 2430));
        vokaalilista.add(new Vokaali("ä", 690, 1840, 2650));
        vokaalilista.add(new Vokaali("ö", 455, 1805, 2465));
        return vokaalilista;
    }
}
