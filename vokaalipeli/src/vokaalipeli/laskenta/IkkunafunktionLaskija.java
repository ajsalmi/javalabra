package vokaalipeli.laskenta;

/**
 * Laskee jonkin tietyn ikkunafunktion mukaiset kertoimet (välillä 0 ja 1). 
 * Ikkunafunktion tarkoitus on heikentää signaalia aikaikkunan reunoilta, 
 * koska silloin FFT-muunnoksella laskettu taajuuskäyrä on selkeämpi eikä 
 * taajuuspiikki valuu ympärillään oleviin taajuuksiin (ns. 'spectral leakage').
 *
 * @author A J Salmi
 * 
 * @see Ikkunafunktio
 */
public class IkkunafunktionLaskija {

    private double[] korjauskertoimet;

    /**
     * Konstruktori, joka vastaa ikkunafunktion laskemisesta. 
     * 
     * @param pituus aikaikkunan pituus
     * @param funktio jokin ikkunafunktioista
     *
     */
    public IkkunafunktionLaskija(int pituus, Ikkunafunktio funktio) {
        double[] kertoimet = new double[pituus];

        for (int i = 0; i < pituus; i++) {
            if (funktio == Ikkunafunktio.HANN) {
                kertoimet[i] = 0.5 * (1 - Math.cos(i * 2 * Math.PI / (pituus - 1.0)));
            } else if (funktio == Ikkunafunktio.KOLMIO) {
                double a = (pituus - 1)/2.0;
                kertoimet[i] = 1 - Math.abs(i - a) / a;
            } else if (funktio == Ikkunafunktio.SUORAKULMAINEN) {
                kertoimet[i] = 1;
            } else {
                throw new IllegalArgumentException("virheellinen ikkunafunktio");
            }
        }
        this.korjauskertoimet = kertoimet;
    }

    /**
     * Metodi palauttaa annettua indeksiä vastaavan ikkunafunktion kertoimen arvon.
     * 
     * @param indeksi indeksi, jonka arvo halutaan
     * @return annettua indeksiä vastaava arvo
     */
    
    public double annaKerroin(int indeksi) {
        return this.korjauskertoimet[indeksi];
    }
}
