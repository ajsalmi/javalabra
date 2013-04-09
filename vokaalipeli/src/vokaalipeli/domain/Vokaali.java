package vokaalipeli.domain;

public class Vokaali {

    private String nimi;  // IPA unicode (??) / latinalaisilla aakkosilla(?) 
    private int ekaFormantti;
    private int tokaFormantti;
    private int kolmasFormantti;

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
