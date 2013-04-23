package vokaalipeli.kayttoliittyma;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.sound.sampled.AudioFormat;
import vokaalipeli.laskenta.Ikkunafunktio;
import vokaalipeli.peli.Vokaalipeli;

/**
 * Luokka koordinoi Taajuuskäyrä-olion luomista, parametrien kysymistä
 * käyttäjältä ja niiden antamista Vokaalipeli-luokan oliolle.
 *
 * @author A J Salmi
 */
public class PerusGUI implements Kayttoliittyma {

    private ParametrienKyselyIkkuna ikkuna;
    private Paaikkuna paaikkuna;
    private int korkeus;
    private int leveys;
    private Vokaalipeli peli;

    /**
     * Konstruktori luo Taajuuskäyrä-luokan olion saamiensa mittojen mukaisesti,
     * kysyy kaikkia tarvittavia parametreja käyttäjältä ja
     *
     * @param peli vokaalipeli
     * @param leveys luotavan pääikkunan leveys
     * @param korkeus luotavan pääikkunan korkeus
     * @see Vokaalipeli
     * @see Taajuuskayra
     */
    public PerusGUI(Vokaalipeli peli, int leveys, int korkeus) {
        this.korkeus = korkeus;
        this.leveys = leveys;
        this.peli = peli;
    }

    public void asetaArvotKayralle(double[]arvot){
        this.paaikkuna.asetaArvotTaajuuskayralle(arvot);
    }

     /**
     * Runnable-rajapinnan toteuttavan luokan run()-metodi, jossa luodaan ensin 
     * kyselyikkunan ja liitetään sen käynnistysnappiin tapahtumakuuntelijan, 
     * joka puolestaan asettaa Vokaalipelille tarvittavat parametrit.  
     *
     * @see Runnable;
     */
    @Override
    public void run() {
        this.ikkuna = new ParametrienKyselyIkkuna();
        
        ikkuna.getKaynnistysnappi().addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) { 
  
            int ikkunanKoko = ikkuna.getIkkunanKoko();
            peli.setAikaikkunanKoko(ikkunanKoko);
  
            Ikkunafunktio funktio =  ikkuna.getIkkunafunktio();
            peli.setIkkunafunktio(funktio);                   
  
            int naytteenottoTaajuus = ikkuna.getNaytteenottoTaajuus();
            int tavuaPerNayte = ikkuna.getTavuaPerNayte();
            boolean signed = ikkuna.getEtumerkillisyys();
            boolean bigEndian = ikkuna.getTavujarjestys();                        
            AudioFormat formaatti = new AudioFormat(naytteenottoTaajuus, tavuaPerNayte, 1, signed, bigEndian);
            peli.setAanilahde(new Mikrofoni(formaatti));
            Taajuuskayra taajuuskayra = new Taajuuskayra(korkeus - 45, leveys - 20, 60000, naytteenottoTaajuus/2);    
            paaikkuna = new Paaikkuna(taajuuskayra, leveys, korkeus);
            ikkuna.dispose();
            ikkuna = null;
            }
        });
    }
}
