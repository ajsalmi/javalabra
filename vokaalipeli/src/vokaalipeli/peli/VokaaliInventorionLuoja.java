
package vokaalipeli.peli;

import java.util.ArrayList;
import vokaalipeli.domain.Vokaali;
import vokaalipeli.domain.VokaaliInventorio;

/**
 *
 * @author A J Salmi
 */
public class VokaaliInventorionLuoja {

    public VokaaliInventorio luoSuomenVokaalit(){
        ArrayList<Vokaali> vokaalilista = new ArrayList<>();
        vokaalilista.add(new Vokaali("a", 720, 1240, 2455));
        vokaalilista.add(new Vokaali("e", 450, 2240, 2810));
        vokaalilista.add(new Vokaali("i", 275, 2495, 3200));
        vokaalilista.add(new Vokaali("o", 515, 905, 2430));
        vokaalilista.add(new Vokaali("u", 340, 605, 2615));
        vokaalilista.add(new Vokaali("y", 300, 1995, 2430));
        vokaalilista.add(new Vokaali("ä", 690, 1840, 2650));
        vokaalilista.add(new Vokaali("ö", 455, 1805, 2465));
        return new VokaaliInventorio(vokaalilista, "suomi");
    }
}
