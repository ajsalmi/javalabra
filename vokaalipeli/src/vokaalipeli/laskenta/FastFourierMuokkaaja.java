package vokaalipeli.domain;

public class FastFourierMuokkaaja {

    static final double PII = Math.PI;
    int[] jarjestysTaulukko;

    public FastFourierMuokkaaja(int ikkunanPituus) {
        this.jarjestysTaulukko = luoOikeaJarjestys(ikkunanPituus);
    }

    public boolean asetaUusiPituus(int ikkunanPituus) {
        int[] taulukko = luoOikeaJarjestys(ikkunanPituus);
        if (taulukko != null){
            this.jarjestysTaulukko = taulukko;
            return true;
        }
        return false;
    }

    public int[] getJarjestys() {
        return this.jarjestysTaulukko;
    }

    public double[][] muokkaaFFT(double[][] arvot, boolean pelkkiaReaalilukuja) {
        double[] reaalinen = arvot[0];
        double[] imaginaarinen = arvot[1];
        return muokkaaFFT(reaalinen, imaginaarinen, pelkkiaReaalilukuja);
    }

    public double[][] muokkaaFFT(double[] reaalinen, double[] imaginaarinen, boolean pelkkiaReaalilukuja) {
        int pituus = reaalinen.length;

        if (pituus != imaginaarinen.length) return null;
        if (pituus != jarjestysTaulukko.length)return null;

        double pituudenLog = Math.log(pituus) / Math.log(2); // <-- logaritmien laskukaavasta
        
        reaalinen = jarjesta(reaalinen);
        if (!pelkkiaReaalilukuja) {
            imaginaarinen = jarjesta(imaginaarinen);  // <-- complex-to-complex -muunnoksessa tarpeen
        }

        /* ------ alkutoimet loppu & itse laskenta alkaa ----------------------*/

        int hyppy; // kertoo kuinka monen luvun yli hypataan ettei samaa lukua kasitella kahdesti
        double ensimmaisenJuurenEksponentti; // ykkosen 1. kompleksijuuren eksp. 
        double ykkosenKompleksijuurenRe, ykkosenKompleksijuurenIm; // siita johdettu koordinaattiesitys
        double ekanReaali, ekanImag; // 1. tarkasteltava kompleksiluku
        double tokanReaali, tokanImag; // 2. tarkasteltava kompleksiluku
        double apuReaali, apuImag; // apumuuttujat joihin tallennetaan ns. "twiddle factor"

        for (int kierros = 1; kierros <= (int) pituudenLog; kierros++) {
            hyppy = (int) Math.pow(2, kierros - 1);
            ensimmaisenJuurenEksponentti = PII / hyppy; // oikeasti juuri on muotoa: e^(2*PII/(2*hyppy))
            for (int k = 0; k < hyppy; k++) {
                ykkosenKompleksijuurenRe = Math.cos(k * ensimmaisenJuurenEksponentti); //  e^(i*x) = cos x + i*sinx
                ykkosenKompleksijuurenIm = Math.sin(k * ensimmaisenJuurenEksponentti); //  e^(i*x) = cos x + i*sinx

                for (int i = 0; i < reaalinen.length; i += 2 * hyppy) {
                    ekanReaali = reaalinen[k + i];
                    ekanImag = imaginaarinen[k + i];
                    tokanReaali = reaalinen[k + hyppy + i];
                    tokanImag = imaginaarinen[k + hyppy + i];
                    apuReaali = tokanReaali * ykkosenKompleksijuurenRe - tokanImag * ykkosenKompleksijuurenIm;
                    apuImag = tokanImag * ykkosenKompleksijuurenRe + tokanReaali * ykkosenKompleksijuurenIm;
                    reaalinen[k + i] = ekanReaali + apuReaali;
                    imaginaarinen[k + i] = ekanImag + apuImag;
                    reaalinen[k + hyppy + i] = ekanReaali - apuReaali;
                    imaginaarinen[k + hyppy + i] = ekanImag - apuImag;
                }
            }
        }
        double[][] palautetaan = new double[2][pituus];
        palautetaan[0] = reaalinen;
        palautetaan[1] = imaginaarinen;
        return palautetaan;
    }

    private int[] luoOikeaJarjestys(int ikkunanPituus) {
        if (!onKakkosenPotenssi(ikkunanPituus)) return null;
        
        int[] luvut = new int[ikkunanPituus];
        for (int i = 0; i < ikkunanPituus; i++) {
            luvut[i] = i;
        }
        for (int patkanPituus = ikkunanPituus; patkanPituus > 2; patkanPituus /= 2) {
            for (int missaMennaan = 0; missaMennaan < ikkunanPituus; missaMennaan += patkanPituus) {
                int[] parittomatMuistiin = new int[patkanPituus / 2];
                for (int j = 0; j < patkanPituus; j += 2) {
                    parittomatMuistiin[j / 2] = luvut[missaMennaan + j + 1];
                    luvut[missaMennaan + j / 2] = luvut[missaMennaan + j];  // <--- siirretään parilliset vasempaan laitaan
                }
                System.arraycopy(parittomatMuistiin, 0, luvut, missaMennaan + patkanPituus / 2, patkanPituus / 2 - 1);
            }
        }
        return luvut;
    }

    private double[] jarjesta(double[] jarjestettava) {
        double[] jarjestetty = new double[jarjestettava.length];
        for (int i = 0; i < jarjestettava.length; i++) {
            jarjestetty[jarjestysTaulukko[i]] = jarjestettava[i];
        }
        return jarjestetty;
    }

    private boolean onKakkosenPotenssi(int luku) {
        if (luku <= 0) return false;
        
        double logaritmi = Math.log(luku) / Math.log(2); // <-- logaritmien laskukaavasta
        if (((int) logaritmi) - logaritmi == 0) {
            return true;
        }
        return false;
    }
}
