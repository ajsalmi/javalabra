package vokaalipeli.kayttoliittyma;

/**
 * Rajapinta käyttöliittymälle. Tällä hetkellä on olemassa vain yksi rajapinnan 
 * toteuttava luokka, mutta mahdollista myöhempää kehitystä varten on hyvä olla
 * olemassa rajapinta.
 *
 * @author A J Salmi
 */
public interface Kayttoliittyma {

    public void asetaArvot(double[] arvot);
    
    public boolean arvojenAsettaminenValmis();

    public void kaynnista();
}
