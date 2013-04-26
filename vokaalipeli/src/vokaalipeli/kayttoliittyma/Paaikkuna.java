package vokaalipeli.kayttoliittyma;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import vokaalipeli.domain.Vokaali;

/**
 * Tässä JFramen perivässä ikkunaluokassa on Taajuuskayra-luokan olio,
 * jolle lähetetään arvot piirrettäviksi, ja käyttäjälle näkyvä 
 * nappipaneeli, jonka kautta käyttäjä voi 
 * [bla bla bla ] <-------------------------- !!
 * 
 * @author A J Salmi
 */
public class Paaikkuna extends JFrame {
    private Taajuuskayra kayra;
    private PaaIkkunanNappipaneeli nappipaneeli;

    public Paaikkuna(Taajuuskayra kayra, int leveys, int korkeus) {
        super("Vokaalipeli");
        setPreferredSize(new Dimension(leveys, korkeus));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.kayra = kayra;
        this.nappipaneeli = new PaaIkkunanNappipaneeli();
        asetaNappienKuuntelijat();
        luoKomponentit(this.getContentPane());
        pack();
        setVisible(true);
    }

    public void asetaVokaali(Vokaali vokaali){
        this.kayra.setVokaali(vokaali);
    }
    
    public void asetaArvotTaajuuskayralle(double[] uudetArvot){
        this.kayra.setArvot(uudetArvot);
    }
    
    public Taajuuskayra getTaajuuskayra(){
        return this.kayra;
    }
    
    public PaaIkkunanNappipaneeli getNappipaneeli(){
        return this.nappipaneeli;
    }
    
    /**
     * Metodi täyttää ikkunan halutuilla komponenteilla: taajuuskäyrä,
     *
     * TODO: ... ja siihen liittyvät säätönapit:
     *          * MaxArvo +/-    (--> Taajuuskäyrälle)  
     *          * keskiarvot     (--> GUI --> Vokaalipeli --> KeskiArvonLaskija)
     *          * ääntöväylän pituudesta johtuvan formanttien siirtymän korjauskerroin:
     *          muutetaanko Vokaaleja vai visualisointia vai onko Vokaalipelissä jokin
     *          double-muuttuja tälle?
     * 
     * TODO: ... ja vokaalin pyytämistä varten myös nappi 
     *          --> pyyntö GUI:lle --> Vokaalipelille --> VokaaliInventorio
     *             Taajuuskayra <--  GUI <-- Vokaalipelille uusi Vokaali <--
     * 
     * TODO: ... ja kielen vaihtamista varten myös valikko(--> pyyntö GUI:lle --> Vokaalipelille)
     *
     * @param container
     */
    private void luoKomponentit(Container container) {
        container.add((Component) this.kayra);
        container.add(nappipaneeli, BorderLayout.SOUTH); 
    }

    private void asetaNappienKuuntelijat() {
        nappipaneeli.getLaskemisNappi().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                kayra.kasvataMaxArvoa();
            }
        });
        
        nappipaneeli.getNostamisNappi().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                kayra.pienennaMaxArvoa();
            }
        });        
    }
}
