package vokaalipeli.domain;

import java.util.Objects;

/**
 * Luokka kuvaa yhtä vokaalia sen kolmen ensimmäisen formanttitaajuuden avulla.
 *
 * @author A J Salmi
 */
public class Vokaali {

    private String nimi;  // IPA unicode (??) / latinalaisilla aakkosilla(?)/ niinkuin kielessä kirjoitetaan ? / esimerkkisana ?
    private int ekaFormantti;
    private int tokaFormantti;
    private int kolmasFormantti;

    /**
     * Konstruktori.
     *
     * @param nimi vokaalin nimi merkkijonona
     * @param ekaFormantti vokaalin ensimmäinen formanttitaajuus
     * @param tokaFormantti vokaalin toinen formanttitaajuus
     * @param kolmasFormantti vokaalin kolmas formanttitaajuus
     */
    public Vokaali(String nimi, int ekaFormantti, int tokaFormantti, int kolmasFormantti) {
        this.nimi = nimi;
        this.ekaFormantti = ekaFormantti;
        this.tokaFormantti = tokaFormantti;
        this.kolmasFormantti = kolmasFormantti;
    }


    /**
     * Object-luokan ylikirjoitettu equals-metodi. Vokaalit ovat samat, jos niiden
     * nimi ja kaikki kolme formanttia ovat samat.
     * 
     * @param o verrattava olio
     * @return totuusarvo vokaalien samuudesta
     */
    @Override
    public boolean equals(Object o){
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;
        
        Vokaali verratava = (Vokaali) o;   
        if (this.ekaFormantti != verratava.ekaFormantti) return false;
        if (this.tokaFormantti != verratava.tokaFormantti) return false;
        if (this.kolmasFormantti != verratava.kolmasFormantti) return false;
        if (!this.nimi.equals(verratava.nimi)) return false;
        
        return true;
    }

    /**
     * Object-luokan ylikirjoitettu hashCode-metodi.
     * @return hajautusarvo
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.nimi);
        hash = 97 * hash + this.ekaFormantti;
        hash = 97 * hash + this.tokaFormantti;
        hash = 97 * hash + this.kolmasFormantti;
        return hash;
    }

    public String getNimi() {
        return this.nimi;
    }

    public int getEkaFormantti() {
        return this.ekaFormantti;
    }

    public int getTokaFormantti() {
        return this.tokaFormantti;
    }

    public int getKolmasFormantti() {
        return this.kolmasFormantti;
    }
}
