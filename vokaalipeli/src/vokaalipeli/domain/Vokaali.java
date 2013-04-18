package vokaalipeli.domain;

/**
 * Luokka kuvaa yhtä vokaalia sen kolmen ensimmäisen formanttitaajuuden avulla.
 *
 * @author A J Salmi
 */
public class Vokaali {

    private String nimi;  // IPA unicode (??) / latinalaisilla aakkosilla(?) 
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
