package vokaalipeli.kayttoliittyma;

/**
 * Rajapinta käyttöliittymälle. Tällä hetkellä on olemassa vain yksi rajapinnan 
 * toteuttava luokka, mutta mahdollista myöhempää kehitystä varten on hyvä olla
 * olemassa rajapinta.
 *
 * @author A J Salmi
 */
public interface Kayttoliittyma extends Runnable{

    public void asetaArvot(double[] arvot);
    
    public boolean arvojenAsettaminenValmis();

    @Override
    public void run();
}
