package vokaalipeli.laskenta;

/**
 * Luokka koordinoi pääosaa ohjelman laskennasta. 
 *
 * @author A J Salmi
 */
public class LaskentaKeskus {

    private int aikaikkunanPituus;
    private FastFourierMuokkaaja fftMuokkaaja;
    private KeskiarvonLaskija keskiarvonLaskija;
    private IkkunafunktionLaskija ikkunaFunktionLaskija;

    /**
     * Konstruktorissa luodaan FastFourierMuokkaaja ja KeskiarvonLaskija.
     * 
     * @param ikkunanKoko aikaikkunan pituus
     * @see KeskiarvonLaskija
     * @see FastFourierMuokkaaja
     */    
    public LaskentaKeskus(int ikkunanKoko) {
        this.fftMuokkaaja = new FastFourierMuokkaaja(ikkunanKoko);
        this.keskiarvonLaskija = new KeskiarvonLaskija(ikkunanKoko / 2);
        this.aikaikkunanPituus = ikkunanKoko;
    }
    
    public int getAikaikkunanPituus() {
        return this.aikaikkunanPituus;
    }

    public double annaIkkunakerroin(int indeksi) {
        return this.ikkunaFunktionLaskija.annaKerroin(indeksi);
    }

    public void setIkkunaFunktio(Ikkunafunktio funktio) {
        this.ikkunaFunktionLaskija = new IkkunafunktionLaskija(aikaikkunanPituus, funktio);
    }

    /**
     * Metodi käsittelee yhden aikaikkunan, jossa syötteestä luetut arvot ovat 
     * taulukon ensimmäisellä rivillä. Toista riviä on käytetty vain tallettamaan
     * tieto siitä mihin asti taulukkoa on täytetty (ks. Vokaalipeli).
     * 
     * @param ikkuna käsiteltävä aikaikkuna
     * @return käsitellyt amplitudit
     */
    public double[] kasittele(double[][] ikkuna) {
        ikkuna[1] = new double[aikaikkunanPituus];
        ikkuna = fftMuokkaaja.muokkaaFFT(ikkuna, true);
        double[] arvot = laskeAmplitudit(ikkuna);
        arvot = this.keskiarvonLaskija.laske(arvot);
        return arvot;
    }

    /**
     * Metodi laskee amplitudit kaikille taulukon arvoille reaaliosan ja
     * imaginaariosan euklidisena normina: (re^2 + im^2)^(0.5)
     *
     * @param analysoitava analysoitava (reaali- ja imag-) taulukko
     * @return eri taajuuksien amplitudit
     */
    private double[] laskeAmplitudit(double[][] analysoitava) {
        double[] amplitudit = new double[analysoitava[0].length / 2];
        double a;
        for (int i = 0; i < amplitudit.length; i++) {
            a = analysoitava[0][i + 1] * analysoitava[0][i + 1]
                    + analysoitava[1][i + 1] * analysoitava[1][i + 1];
            amplitudit[i] = Math.sqrt(a);
        }
        return amplitudit;
    }
}
