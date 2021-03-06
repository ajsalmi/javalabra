package vokaalipeli.laskenta;


import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author A J Salmi
 */
public class FastFourierMuokkaajaTest {
    private FastFourierMuokkaaja muokkaaja;

    @Before
    public void setUp() {
        this.muokkaaja = new FastFourierMuokkaaja(4);
    }

    @Test
    public void taulukkoEiOleNullLuotaessa() {
        assertNotNull(this.muokkaaja.getJarjestys());
    }

    @Test
    public void taulukkoOnNullJosKonstruktorissaAnnetaanSopimatonPituus() {
        FastFourierMuokkaaja muokkaaja2 = new FastFourierMuokkaaja(7);
        assertNull(muokkaaja2.getJarjestys());
    }

    @Test
    public void konstruktoriTaulukonPituusOnOikea4() {
        int taulukonPituus = this.muokkaaja.getJarjestys().length;
        assertEquals(4, taulukonPituus);
    }

    @Test
    public void konstruktoriTaulukonPituusOnOikea8() {
        this.muokkaaja = new FastFourierMuokkaaja(8);//asetaUusiPituus(8);
        int taulukonPituus = this.muokkaaja.getJarjestys().length;
        assertEquals(8, taulukonPituus);
    }

    @Test
    public void taulukkoOikeassaJarjestyksessa4() {
        int[] taulukko = this.muokkaaja.getJarjestys();
        int[] oikeaJarj = {0, 2, 1, 3};
        assertArrayEquals(oikeaJarj, taulukko);
    }

    @Test
    public void taulukkoOikeassaJarjestyksessa16() {
        this.muokkaaja = new FastFourierMuokkaaja(16); //.asetaUusiPituus(16);
        int[] oikeaJarj = {0, 8, 4, 12, 2, 10, 6, 14, 1, 9, 5, 13, 3, 11, 7, 15};
        assertArrayEquals(oikeaJarj, this.muokkaaja.getJarjestys());
    }

    @Test
    public void fourierTulosOikeinNollasyotteella() {
        double[][] syote = {{0, 0, 0, 0}, {0, 0, 0, 0}};
        assertArrayEquals(syote, this.muokkaaja.muokkaaFFT(syote, true));

        this.muokkaaja = new FastFourierMuokkaaja(8); //.asetaUusiPituus(8);
        double[][] syote2 = {{0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0}};
        assertArrayEquals(syote2, this.muokkaaja.muokkaaFFT(syote2, true));
    }

    @Test
    public void fourierTulosOikeinLyhyetSyotteet() {
        this.muokkaaja= new FastFourierMuokkaaja(1);//.asetaUusiPituus(1);
        double[][] syote = {{1}, {-1}};
        assertArrayEquals(syote, this.muokkaaja.muokkaaFFT(syote, false));

        this.muokkaaja = new FastFourierMuokkaaja(2);//.asetaUusiPituus(2);
        double[][] syote2 = {{1, 2}, {0, 0}};
        double[][] oikea2 = {{3, -1}, {0, 0}};
        assertArrayEquals(oikea2, this.muokkaaja.muokkaaFFT(syote2, true));

        this.muokkaaja = new FastFourierMuokkaaja(4); //.asetaUusiPituus(4);
        double[][] syote3 = {{1, 2, 3, 4}, {0, 0, 0, 0}};
        double[][] oikea3 = {{10, -2, -2, -2}, {0, -2, 0, 2}};
        boolean tarpeeksiLahella = onkoTarpeeksiLahella(this.muokkaaja.muokkaaFFT(syote3, true), oikea3, 0.00_000_000_000_1);
        assertTrue(tarpeeksiLahella);
    }

    @Test
    public void fourierTulosOikeinPitemmatSyotteet() {
        this.muokkaaja = new FastFourierMuokkaaja(8); //.asetaUusiPituus(8);
        double[][] syote = {{0, 1, -1, -1, 1, 1, -2, 1}, {0, 0, 0, 0, 0, 0, 0, 0}};
        double a = Math.sqrt(2);
        double[][] vastaus = {{0, a-1, 4, -1-a, -4, -1-a,  4, -1+a},
                              {0, 1-a, 2, -1-a,  0,  1+a, -2, -1+a}};
        boolean lahella = onkoTarpeeksiLahella(this.muokkaaja.muokkaaFFT(syote, true), vastaus, 0.00_000_000_000_1);
        assertTrue(lahella);

        double b = Math.sqrt(2+a);
        double c = Math.sqrt(2-a);

        this.muokkaaja = new FastFourierMuokkaaja(16); //.asetaUusiPituus(16);

        double[][] syote2 = {{1, 0, 0, -1, 0, 0, 1, 0, 0, 0, -1, 0, 0, 1, 0, 0},
                             {0, 0, 0,  0, 0, 0, 0, 0, 0, 0,  0, 0, 0, 0, 0, 0}};

        double[][] vastaus2 = {{1,   1,   1,   1,   1,   1,   1,   1,   1,   1,   1,   1,  1,   1,   1,   1},
                               {0, a-b, -2-a, a+c,  2, c-a, 2-a, -a-b,  0, a+b, a-2, a-c, -2, -a-c, 2+a, b-a}};
       
        lahella = onkoTarpeeksiLahella(this.muokkaaja.muokkaaFFT(syote2, true), vastaus2, 0.00_000_000_000_1);
        assertTrue(lahella);
    }

    @Test
    public void fourierPalauttaaVirheIlmoituksenJosReaaliJaImagEripituiset() {
        boolean palauttaaVirheilmoituksen = false;
        
        try{
            double[][] syote = {{0, 1, 2, 3}, {0, 1, 2}};
            this.muokkaaja.muokkaaFFT(syote, false);
        } catch (IllegalArgumentException e){
            palauttaaVirheilmoituksen = true;
        } 
        assertTrue(palauttaaVirheilmoituksen);
        
        palauttaaVirheilmoituksen = false;
        this.muokkaaja = new FastFourierMuokkaaja(8);//.asetaUusiPituus(8);
        try{
            double[][] syote2 = {{0, 1, 2, 3}, {0, 1, 2, 3, 4, 5, 6, 7}};
            this.muokkaaja.muokkaaFFT(syote2, false);
        } catch (IllegalArgumentException e){
            palauttaaVirheilmoituksen = true;
        }
        assertTrue(palauttaaVirheilmoituksen);
    }

    @Test
    public void fourierPalauttaaVirheIlmoituksenJosTaulukotOnNull() {
        boolean palauttaaVirheilmoituksen = false;
        
        try{
            double[][] syote = {{0, 1, 2, 3}, null};
            this.muokkaaja.muokkaaFFT(syote, false);
        } catch (IllegalArgumentException e){
            palauttaaVirheilmoituksen = true;
        } 
        assertTrue(palauttaaVirheilmoituksen);
        
        palauttaaVirheilmoituksen = false;
        this.muokkaaja = new FastFourierMuokkaaja(8);//.asetaUusiPituus(8);
        try{
            double[][] syote2 = {null, {0, 1, 2, 3, 4, 5, 6, 7}};
            this.muokkaaja.muokkaaFFT(syote2, false);
        } catch (IllegalArgumentException e){
            palauttaaVirheilmoituksen = true;
        }

        assertTrue(palauttaaVirheilmoituksen);
    }
    
    @Test
    public void fourierPalauttaaVirheIlmoituksenJosTaulukotVaaranmittaiset() {
        boolean palauttaaVirheilmoituksen = false;
        
        try{
            double[][] syote = {{0, 1, 2, 3, 4, 5, 6, 7}, {9, 1, 9, 1, 9, 1, 2, 6}};
            this.muokkaaja.muokkaaFFT(syote, false);
        } catch (IllegalArgumentException e){
            palauttaaVirheilmoituksen = true;
        } 
        assertTrue(palauttaaVirheilmoituksen);
        
        palauttaaVirheilmoituksen = false;
        this.muokkaaja = new FastFourierMuokkaaja(8);//.asetaUusiPituus(8);
        try{
            double[][] syote2 = {{0, 1, 2, 3}, {4, 5, 6, 7}};
            this.muokkaaja.muokkaaFFT(syote2, false);
        } catch (IllegalArgumentException e){
            palauttaaVirheilmoituksen = true;
        }

        assertTrue(palauttaaVirheilmoituksen);
    }

    
    private boolean onkoTarpeeksiLahella(double[][] eka, double[][] toka, double raja) {
        for (int i = 0; i < eka[0].length; i++) {
            if (Math.abs(eka[0][i]-toka[0][i]) > raja || Math.abs(eka[1][i]-toka[1][i]) > raja) {
                return false;
            }
        }
        return true;
    }
}
