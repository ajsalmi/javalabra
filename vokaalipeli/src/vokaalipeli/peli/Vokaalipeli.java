package vokaalipeli.peli;

import java.io.IOException;
import java.util.ArrayList;
import javax.sound.sampled.AudioInputStream;
import vokaalipeli.domain.FastFourierMuokkaaja;
import vokaalipeli.domain.Mikrofoni;
import vokaalipeli.kayttoliittyma.Taajuuskayra;

public class Vokaalipeli {

    private FastFourierMuokkaaja muokkaaja;
    private Taajuuskayra kayra;
    private Mikrofoni mikki;
    private int ikkunanPituus;        // täytyy olla kakkosen potenssi
    private double siirtyma;           // paljonko aikaikkunaa siirretään kerralla
    private boolean jatkuu;

    public void setMikrofoni(Mikrofoni mikki) {
        this.mikki = mikki;
    }

    public Mikrofoni getMikrofoni() {
        return this.mikki;
    }

    public void pysayta() {
        this.jatkuu = false;
    }

    public void kaynnista() {
        AudioInputStream striimi = mikki.getInputStream();
        int bytesPerFrame = striimi.getFormat().getFrameSize();
        byte[] lukupari = new byte[2];
        int aikaIkkunoitaAnalysoitu = 0;
        int apulaskuri = 0;
        double luettuArvo = 0;
        double keskipoikkeamaNollasta = 0;
        this.jatkuu = true;
        ArrayList<double[][]> kasiteltavatAikaikkunat = new ArrayList<>();

        double striimissaLuettavissaKeskimaarin = 3000; // tavoitearvo voisi riippua näytteenottotaajuudesta

        while (this.jatkuu) {
            try {
                if (bytesPerFrame == 2) {
                    striimi.read(lukupari);
                    luettuArvo = Math.pow(2, 8) * lukupari[0] + lukupari[1]; // toisinpäin jos little-endian ?
                } else {    // if (bytesPerFrame==1)
                    luettuArvo = striimi.read();
                }
            } catch (IOException exc) {
            }


            if (apulaskuri >= siirtyma) {
                kasiteltavatAikaikkunat.add(new double[2][ikkunanPituus]);
                apulaskuri = 0;
            }

            for (int i = 0; i < kasiteltavatAikaikkunat.size(); i++) {
                double[][] ikkuna = kasiteltavatAikaikkunat.get(i);
                int indeksi = (int) ikkuna[1][0];

                if (indeksi == ikkunanPituus - 1) {
                    if (aikaIkkunoitaAnalysoitu == 4000 / (int) siirtyma) {
                        aikaIkkunoitaAnalysoitu = 0;
                        arvioiSiirtymanPituutta(striimi, striimissaLuettavissaKeskimaarin);
                    }

                    ikkuna[1][0] = 0;
                    ikkuna = this.muokkaaja.muokkaaFFT(ikkuna, true);
                    double[] amplitudit = laskeAmplitudit(ikkuna);
                    kayra.setArvot(amplitudit);
                    kasiteltavatAikaikkunat.remove(i);
                    kayra.paivita();

                    aikaIkkunoitaAnalysoitu++;
                }

                double k = 0.0000005; // ratkaistaan syötteen etumerkittömyys
                keskipoikkeamaNollasta = k * luettuArvo + (1 - k) * keskipoikkeamaNollasta;
                
                ikkuna[0][indeksi] = laskeHannWindow(luettuArvo - keskipoikkeamaNollasta, indeksi) - 0.1;   // Hann Window
                ikkuna[1][0] = indeksi + 1; // talletetaan indeksi käyttämättömään imaginaaritaulukkoon
            }
            apulaskuri++;
        }
    }

    public void setIkkunanKoko(int ikkunanKoko) {
        if (onKakkosenPotenssi(ikkunanKoko)) {
            this.ikkunanPituus = ikkunanKoko;
            this.siirtyma = ikkunanKoko / 32;   // <----- aloituskoko 
        }
    }

    public void setFastFourierMuokkaaja(FastFourierMuokkaaja muokkaaja) {
        this.muokkaaja = muokkaaja;
    }

    public void setSiirtyma(int siirtyma) {
        if (siirtyma < 1) {
            this.siirtyma = 1;
        } else {
            this.siirtyma = siirtyma;
        }
    }

    public void setTaajuuskayra(Taajuuskayra kayra) {
        this.kayra = kayra;
    }

    private boolean onKakkosenPotenssi(int luku) {
        if (luku <= 0) {
            return false;
        }
        double logaritmi = Math.log(luku) / Math.log(2); // <-- logaritmien laskukaavasta
        if (((int) logaritmi) - logaritmi == 0) {
            return true;
        }
        return false;
    }

    private double[] laskeAmplitudit(double[][] analysoitava) {
        double[] amp = new double[analysoitava[0].length / 2];
        double amplitudi;
        for (int i = 0; i < amp.length; i++) {
            amplitudi = analysoitava[0][i + 1] * analysoitava[0][i + 1]
                    + analysoitava[1][i + 1] * analysoitava[1][i + 1];
            amp[i] = Math.sqrt(amplitudi);
        }
        return amp;
    }

    private double laskeHannWindow(double arvo, int indeksi) {
        return 0.5 * arvo * (1 - Math.cos(2 * indeksi * Math.PI / (ikkunanPituus - 1)));
    }

    private void arvioiSiirtymanPituutta(AudioInputStream striimi, double valmiinaKeskimaarin) {
        System.out.println((int) siirtyma);
        try {
            int striimissaValmiina = striimi.available();
            valmiinaKeskimaarin =
                    0.2 * striimissaValmiina
                    + 0.8 * valmiinaKeskimaarin;

            if (valmiinaKeskimaarin < 2950
                    && siirtyma > ikkunanPituus / 32) {
                siirtyma *= 0.995;

            } else if (valmiinaKeskimaarin > 3150) {
                siirtyma *= 1.005;
            }
        } catch (IOException io) {
        }
    }
}
