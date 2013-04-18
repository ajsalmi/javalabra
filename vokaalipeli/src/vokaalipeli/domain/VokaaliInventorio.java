package vokaalipeli.domain;

import java.util.ArrayList;

/**
 * VokaaliInventorio sisältää jonkin tietyn kielen (esim. suomen) vokaalit.
 *
 *
 * @author A J Salmi
 */
public class VokaaliInventorio {

    private final String kieli;
    private final ArrayList<Vokaali> vokaalit;  // missä muodossa, tyypillisesti vokaaleita on kielessä vähän...

    /**
     * Konstruktori.
     *
     * @param vokaalit  lista kielen vokaaleista
     * @param kieli     kielen nimi
     */
    public VokaaliInventorio(ArrayList<Vokaali> vokaalit, String kieli) {
        this.vokaalit = vokaalit;
        this.kieli = kieli;
    }

    public ArrayList<Vokaali> getVokaalit() {  // onko tämä tarpeen ???
        return this.vokaalit;
    }

    public String getKieli() {
        return this.kieli;
    }

    /**
     * laskee vokaalien määrän
     * 
     * @return          vokaalien lukumäärä tai -1, jos vokaalit on null 
     */
    public int getVokaalienMaara() {
        if (this.vokaalit == null) {
            return -1;
        }
        return this.vokaalit.size();
    }

    /**
     * antaa vokaalin
     *
     * @param indeksi   halutun vokaalin indeksi
     * @return          vokaali kyseisestä indeksistä tai null, jos indeksi virheellinen
     *
     * @see Vokaali
     */
    public Vokaali annaVokaali(int indeksi) {   // HUOM: voi palauttaa null
        if (this.vokaalit == null || indeksi < 0 || indeksi >= this.vokaalit.size()) {
            return null;
        }
        return this.vokaalit.get(indeksi);
    }
    // public Vokaali satunnainenVokaali() {}    
    // public Vokaali satunnainenVokaaliPoislukien(Vokaali vokaali) {}   
}
