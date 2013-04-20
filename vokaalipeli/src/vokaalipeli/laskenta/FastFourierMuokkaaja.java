package vokaalipeli.laskenta;

/**
 * Luokka laskee nopean Fourier-muunnoksen (FFT) sille annetusta 
 * liukulukutaulukosta.
 * 
 * @author A J Salmi
 */
public class FastFourierMuokkaaja {

    static final double PII = Math.PI;
    int[] jarjestysTaulukko;

    /**
     * Konstruktori, joka laskee taulukon pituuden avulla järjestyksen, 
     * joka helpottaa FFT:n laskemista.  
     * 
     * @param taulukonPituus laskettavien taulukoiden pituus. Täytyy 
     * olla kakkosen potenssi
     */
    public FastFourierMuokkaaja(int taulukonPituus) {
        this.jarjestysTaulukko = luoOikeaJarjestys(taulukonPituus);
    }

    public int[] getJarjestys() { // onko tarpeellinen ? 
        return this.jarjestysTaulukko;
    }

    /**
    * Metodi laskee FFT:n käyttäen apuna toista samannimistä metodia.
    * 
    * @param arvot annetut arvot, reaalinen ja imaginaarinen
    * @param pelkkiaReaalilukuja kertoo onko parametrina annetussa taulukossa vain reaalilukuja
    * @return kaksiuloitteinen taulukko jossa on FFT:n reaaliset ja 
    * imaginaariset osat omissa taulukoissaan
    */
    public double[][] muokkaaFFT(double[][] arvot, boolean pelkkiaReaalilukuja) {
        double[] reaalinen = arvot[0];
        double[] imaginaarinen = arvot[1];
        return muokkaaFFT(reaalinen, imaginaarinen, pelkkiaReaalilukuja);
    }

    /**
    * Metodi laskee FFT:n.
    * 
    * @param reaaliOsat annettujen lukuarvojen reaaliosat
    * @param imaginaariOsat annettujen lukuarvojen imaginaariosat
    * @param pelkkiaReaalilukuja kertoo onko parametrina annetussa taulukossa vain reaalilukuja
    * @return kaksiuloitteinen taulukko jossa on FFT:n reaaliset ja 
    * imaginaariset osat omissa taulukoissaan
    */
    public double[][] muokkaaFFT(double[] reaaliOsat, double[] imaginaariOsat, boolean pelkkiaReaalilukuja) {

        if (imaginaariOsat == null) {
            throw new IllegalArgumentException("annettu imaginaaritaulukko on null");
        }
        if (reaaliOsat == null){
            throw new IllegalArgumentException("annettu reaalitaulukko on null");
        }
        
        int pituus = reaaliOsat.length;
        if (pituus != imaginaariOsat.length) {
            throw new IllegalArgumentException("reaali- ja imaginaaritalukot eripituiset");        
        }
        if (pituus != jarjestysTaulukko.length) {
            throw new IllegalArgumentException("annettu taulukko ei ole samanmittainen kuin muokkaajan");
        }

        double pituudenLog = Math.log(pituus) / Math.log(2); // <-- logaritmien laskukaavasta
        
        reaaliOsat = jarjesta(reaaliOsat);
        if (!pelkkiaReaalilukuja) {
            imaginaariOsat = jarjesta(imaginaariOsat);  // <-- complex-to-complex -muunnoksessa tarpeen
        }

        /* ------ alkutoimet loppu & itse laskenta alkaa ----------------------*/

        for (int kierros = 1; kierros <= (int) pituudenLog; kierros++) {
            
            /**
             * monenko luvun yli hypätään, ettei yhdellä kierroksella käsiteltäisi samaa lukua kahdesti.
             */            
            int hyppy = (int) Math.pow(2, kierros - 1);

            /**
             * Ykkösen ensimmäisen kompleksijuuren eksponentti. 
             */
            double ensimmaisenJuurenEksponentti = PII / hyppy; // oikeasti juuri on muotoa: e^(2*PII/(2*hyppy))
            
            for (int k = 0; k < hyppy; k++) {
                double ykkosenJuurenReaali = Math.cos(k * ensimmaisenJuurenEksponentti); //  e^(i*x) = cos x + i*sinx
                double ykkosenJuurenImag = Math.sin(k * ensimmaisenJuurenEksponentti); //  e^(i*x) = cos x + i*sinx

                for (int i = 0; i < reaaliOsat.length; i += 2 * hyppy) {
                    double ekanLuvunReaali = reaaliOsat[k + i];
                    double ekanLuvunImag = imaginaariOsat[k + i];
                    double tokanLuvunReaali = reaaliOsat[k + hyppy + i];
                    double tokanLuvunImag = imaginaariOsat[k + hyppy + i];
                    double apuReaali = tokanLuvunReaali * ykkosenJuurenReaali - tokanLuvunImag * ykkosenJuurenImag;
                    double apuImag = tokanLuvunImag * ykkosenJuurenReaali + tokanLuvunReaali * ykkosenJuurenImag;
                    reaaliOsat[k + i] = ekanLuvunReaali + apuReaali;
                    imaginaariOsat[k + i] = ekanLuvunImag + apuImag;
                    reaaliOsat[k + hyppy + i] = ekanLuvunReaali - apuReaali;
                    imaginaariOsat[k + hyppy + i] = ekanLuvunImag - apuImag;
                }
            }
        }
        double[][] palautetaan = {reaaliOsat, imaginaariOsat};
        return palautetaan;
    }

    /**
     * Metodi luo tietyn mittaiselle taulukolle järjestyksen, joka helpottaa
     * FFT:n laskemista.
     * 
     * @param taulukonPituus laskettavien taulukkojen pituus, jonka täytyy olla kakkosen potenssi
     * @return järjestyksen kertova taulukko tai null, jos pituus on virheellinen
     */
    private int[] luoOikeaJarjestys(int taulukonPituus) {
        if (!onKakkosenPotenssi(taulukonPituus)) return null;
        
        int[] jarjestys = new int[taulukonPituus];
        for (int i = 0; i < taulukonPituus; i++) {
            jarjestys[i] = i;
        }
        for (int patkanPituus = taulukonPituus; patkanPituus > 2; patkanPituus /= 2) {
            for (int missaMennaan = 0; missaMennaan < taulukonPituus; missaMennaan += patkanPituus) {
                int[] parittomatMuistiin = new int[patkanPituus / 2];
                for (int j = 0; j < patkanPituus; j += 2) {
                    parittomatMuistiin[j / 2] = jarjestys[missaMennaan + j + 1]; // pariton muistiin
                    jarjestys[missaMennaan + j / 2] = jarjestys[missaMennaan + j];  // parillinen pätkän vasempaan laitaan
                }
                System.arraycopy(parittomatMuistiin, 0, jarjestys, missaMennaan + patkanPituus / 2, patkanPituus / 2 - 1);
            }
        }
        return jarjestys;
    }

    /**
     * Metodi järjestää annetun taulukon sellaiseen järjestykseen, joka helpottaa merkittävästi
     * FFT:n laskemista.
     * 
     * @param jarjestettava taulukko joka halutaan FFT:ta helpottavaan järjestykseen
     * @return järjestetyt arvot
     */ 
    private double[] jarjesta(double[] jarjestettava) {
        double[] jarjestetty = new double[jarjestettava.length];
        for (int i = 0; i < jarjestettava.length; i++) {
            jarjestetty[jarjestysTaulukko[i]] = jarjestettava[i];
        }
        return jarjestetty;
    }

    /**
     * Metodi tarkistaa onko annettu luku kakkosen potenssi.
     * 
     * @param luku tarkistettava luku
     * @return tieto siitä onko annettu luku kakkosen potenssi
     */
    private boolean onKakkosenPotenssi(int luku) {
        if (luku <= 0) return false;
        
        double logaritmi = Math.log(luku) / Math.log(2); // logaritmien laskukaavasta
        if (((int) logaritmi) - logaritmi == 0) {
            return true;
        }
        return false;
    }
}
