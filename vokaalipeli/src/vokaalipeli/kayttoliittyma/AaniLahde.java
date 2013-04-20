package vokaalipeli.kayttoliittyma;

import javax.sound.sampled.AudioInputStream;

/**
 * Rajapinta äänilähteille. Tässä projektissa äänilähteenä toimii mikrofoni,
 * mutta rajapinnan avulla on helppoa myöhemmin laajentaa muihin lähteisiin 
 * (esim. nauhoitettuihin ääninäytteisiin).
 *
 * @author A J Salmi
 *
 * @see Mikrofoni
 */
public interface AaniLahde {

    /**
     * Rajapinnan toteuttavien luokilla täytyy olla metodi joka palauttaa
     * AudioInputStream-luokan olion.
     *
     * @return striimi, johon äänisyöte ohjataan
     */
    public AudioInputStream getStriimi();
}
