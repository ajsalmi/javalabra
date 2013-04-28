package vokaalipeli.domain;

import java.util.Objects;

/**
 * Luokka kuvaa yhtä vokaalia sen kolmen ensimmäisen formanttitaajuuden avulla.
 *
 * @author A J Salmi
 */
public class Vokaali {

    private String vokaalinNimi;  // IPA / latinalaisilla aakkosilla / esimerkkisana ???
    private int[] formantit = new int[3];

    /**
     * Konstruktori.
     *
     * @param nimi vokaalin nimi merkkijonona
     * @param ekaFormantti vokaalin ensimmäinen formanttitaajuus
     * @param tokaFormantti vokaalin toinen formanttitaajuus
     * @param kolmasFormantti vokaalin kolmas formanttitaajuus
     */
    public Vokaali(String nimi, int ekaFormantti, int tokaFormantti, int kolmasFormantti) {
        this.vokaalinNimi = nimi;
        this.formantit[0] = ekaFormantti;
        this.formantit[1] = tokaFormantti;
        this.formantit[2] = kolmasFormantti;
    }

    public String getNimi() {
        return this.vokaalinNimi;
    }

    public int[] getFormantit(){
        return this.formantit;
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
        if (this.formantit[0] != verratava.formantit[0]) return false;
        if (this.formantit[1] != verratava.formantit[1]) return false;
        if (this.formantit[2] != verratava.formantit[2]) return false;
        if (!this.vokaalinNimi.equals(verratava.vokaalinNimi)) return false;
        
        return true;
    }

    /**
     * Object-luokan ylikirjoitettu hashCode-metodi.
     * @return hajautusarvo
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.vokaalinNimi);
        hash = 97 * hash + formantit[0];//this.ekaFormantti;
        hash = 97 * hash + formantit[1];//this.tokaFormantti;
        hash = 97 * hash + formantit[2];//this.kolmasFormantti;
        return hash;
    }
}
