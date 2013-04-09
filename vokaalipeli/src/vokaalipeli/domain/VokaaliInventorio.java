package vokaalipeli.domain;

import java.util.ArrayList;

public class VokaaliInventorio {

    private final String kieli;
    private final ArrayList<Vokaali> vokaalit;  // missä muodossa, tyypillisesti vokaaleita on kielessä vähän...

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

    public int getVokaalienMaara() {            // jos null ----> palautetaan pituudeksi -1
        if (this.vokaalit == null) {
            return -1;
        }
        return this.vokaalit.size();
    }

    public Vokaali annaVokaali(int indeksi) {   // HUOM: voi palauttaa null
        if (this.vokaalit == null || indeksi < 0 || indeksi >= this.vokaalit.size()) {
            return null;
        }
        return this.vokaalit.get(indeksi);
    }
    // public Vokaali satunnainenVokaali() {}    
    // public Vokaali satunnainenVokaaliPoislukien(Vokaali vokaali) {}   
}
