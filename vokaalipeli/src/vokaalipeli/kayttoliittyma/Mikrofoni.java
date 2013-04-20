package vokaalipeli.kayttoliittyma;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

/**
 * Mikrofoni toteuttaa rajapinnan AaniLahde, ja sen muuttujana on 
 * AudioInputStream johon äänisyöte ohjautuu.
 *
 * @see AaniLahde
 *
 * @author A J Salmi
 */
public class Mikrofoni implements AaniLahde {

    private AudioInputStream striimi;

    /**
     * Konstruktori, kutsuu metodia luoInputStream parametrina saamallaan
     * formaatilla.
     * 
     * @param formaatti ääniformaatti 
     */
    public Mikrofoni(AudioFormat formaatti) {
        luoInputStream(formaatti);
    }

    @Override
    public AudioInputStream getStriimi() {
        return this.striimi;
    }

    /**
     * Metodi luo parametrina saamansa ääniformaatin avulla AudioInputStreamin.
     *
     * @param formaatti ääniformaatti
     */
    private void luoInputStream(AudioFormat formaatti) {
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, formaatti);
        System.out.println(info);
        TargetDataLine linja;
        AudioInputStream stream = null;
        try {
            linja = (TargetDataLine) AudioSystem.getLine(info);
            linja.open(formaatti);
            linja.start();
            stream = new AudioInputStream(linja);
        } catch (LineUnavailableException ex) {
            System.out.println(ex);
        }
        this.striimi = stream;
    }
}
