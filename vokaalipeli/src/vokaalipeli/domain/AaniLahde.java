package vokaalipeli.domain;

import javax.sound.sampled.AudioInputStream;

/**
 * Rajapinta, joka mahdollistaa Vokaalipelille äänisyötteen vastaanottamisen
 * myös muista lähteistä, esim. nauhoitetuista vokaalinäytteistä.
 *
 * @author A J Salmi
 *
 * @see AaniLahde
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
