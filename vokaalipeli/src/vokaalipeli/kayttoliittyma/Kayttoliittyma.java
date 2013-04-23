package vokaalipeli.kayttoliittyma;

/**
 * Rajapinta käyttöliittymälle. Tällä hetkellä on olemassa vain yksi rajapinnan 
 * toteuttava luokka, mutta mahdollista myöhempää kehitystä varten on hyvä olla
 * olemassa rajapinta.
 *
 * @author A J Salmi
 */
public interface Kayttoliittyma extends Runnable{

    public void asetaArvotKayralle(double[] arvot);

    @Override
    public void run();
}
