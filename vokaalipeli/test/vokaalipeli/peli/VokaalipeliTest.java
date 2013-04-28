package vokaalipeli.peli;

import javax.sound.sampled.AudioFormat;
import static org.junit.Assert.*;
import org.junit.Test;
import vokaalipeli.domain.Vokaali;
import vokaalipeli.kayttoliittyma.Mikrofoni;
import vokaalipeli.laskenta.Ikkunafunktio;

/**
 *
 * @author A J Salmi
 */
public class VokaalipeliTest {

    private Vokaalipeli peli;

    public VokaalipeliTest() {
        this.peli = new Vokaalipeli();
    }

    @Test
    public void vokaalipeliEiOleNull() {
        assertNotNull(peli);
    }

    @Test
    public void vokaalipeliAsettaaAaniLahteen() {
        peli.setAanilahde(new Mikrofoni(new AudioFormat(8000, 16, 1, true, true)));
        assertTrue(peli.aaniLahdeAsetettu());
    }

    @Test
    public void ikkunanPituudenAsetusPalauttaaTrueKakkosenPotenssilla() {
        assertTrue(peli.asetaAikaikkunanKoko(1024));
    }

    @Test
    public void ikkunanPituudenAsetusPalauttaaFalseMuutoin() {
        assertFalse(peli.asetaAikaikkunanKoko(1023));
    }

    @Test
    public void ikkunaFunktionAsetusToimii() {
        ikkunanPituudenAsetusPalauttaaTrueKakkosenPotenssilla();
        peli.asetaIkkunafunktio(Ikkunafunktio.HANN);
    }

//    @Test
//    public void peliLahettaaArvojaKayttikselle() {
//         yritin testata tätäkin, mutta ei onnistunut 
//    }
    @Test
    public void annettuVokaaliEiOleNull() {
        Vokaali v = peli.annaUusiVokaali();
        assertNotNull(v);
    }

    @Test
    public void annettuVokaaliEiOleSamaKuinEdellinen() {
        Vokaali edellinen = peli.annaUusiVokaali();
        Vokaali uusi;
        boolean vokaaliToistui = false;
        for (int i = 0; i < 1000; i++) {
            uusi = peli.annaUusiVokaali();
            if (uusi.equals(edellinen)) {
                vokaaliToistui = true;
            }
            edellinen = uusi;
        }
        assertFalse(vokaaliToistui);
    }

    @Test
    public void korjausKertoimenMuuttaminenVaikuttaaFormantteihin() {
        boolean kerroinToimii = kerroinMuuttuuOikein(2);
        assertTrue(kerroinToimii);

        kerroinToimii = kerroinMuuttuuOikein(0.5);
        assertTrue(kerroinToimii);
    }

    private boolean kerroinMuuttuuOikein(double kerroin) {
        Vokaali v1 = peli.annaUusiVokaali();
        peli.muutaKorjausKerrointa(kerroin);

        Vokaali v2 = peli.annaUusiVokaali();
        while (!v2.getNimi().equals(v1.getNimi())) {
            v2 = peli.annaUusiVokaali();
        }

        int[] ekanFormantit = v1.getFormantit();
        int[] tokanFormantit = v2.getFormantit();

        boolean kerroinToimii = true;

        for (int i = 0; i < 3; i++) {
            if (kerroin * ekanFormantit[i] != tokanFormantit[i]) {
                kerroinToimii = false;
            }
        }
        return kerroinToimii;
    }
}
