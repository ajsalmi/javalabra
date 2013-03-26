package vokaalipeli.domain;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class Mikrofoni {

    private AudioInputStream stream;

    public Mikrofoni(AudioFormat formaatti) {
       this.stream= luoInputStream(formaatti);
    }

    public AudioInputStream getInputStream (){
        return this.stream;
    }
    
    private AudioInputStream luoInputStream(AudioFormat formaatti) {
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, formaatti);
        System.out.println(info);
        TargetDataLine linja;
        AudioInputStream striimi = null;
        try {
            linja = (TargetDataLine) AudioSystem.getLine(info);
            linja.open(formaatti);
            linja.start();
            striimi = new AudioInputStream(linja);
        } catch (LineUnavailableException ex) {

            // Line unavailable ... ???
            // käsittele jotenkin
        }
        return striimi;
    }
}

