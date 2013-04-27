package vokaalipeli.peli;

import java.util.ArrayList;
import vokaalipeli.domain.Vokaali;
import vokaalipeli.domain.VokaaliInventorio;

/**
 * Tämä luokka vastaa VokaaliInventorion luonnista. Tällä hetkellä se osaa luoda
 * vain suomen vokaalit, mutta tarkoituksena on että sillä olisi muitakin kieliä
 * valikoimassaan. Lisäksi se voisi lukea tekstitiedostosta, jolloin käyttäjällä
 * on mahdollisuus paremmin valita harjoittelemansa kielen vokaaleja.
 *
 * Vokaalien taajuudet otettu sivulta:
 * http://www.helsinki.fi/puhetieteet/projektit/Finnish_Phonetics/vokaaliakustiikka.htm
 *
 * jossa lähteeksi mainitaan: Wiik, Kalevi (1965) Finnish and English Vowels.
 * Turun yliopiston julkaisuja B: 94. Turun yliopisto.
 *
 *
 *
 * @author A J Salmi
 */
public class VokaaliInventorionLuoja {

    /**
     * Parametriton konstruktori luo suomen vokaalit.
     *
     * @return
     */
    public VokaaliInventorio luoSuomenVokaalit() {
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
