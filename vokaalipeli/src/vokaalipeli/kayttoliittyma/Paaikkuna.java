package vokaalipeli.kayttoliittyma;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
    private Taajuuskayra taajuuskayra;
    private PaaIkkunanNappipaneeli nappipaneeli;

    public Paaikkuna(Taajuuskayra kayra, int leveys, int korkeus) {
        super("Vokaalipeli");
        setPreferredSize(new Dimension(leveys, korkeus));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.taajuuskayra = kayra;
        this.nappipaneeli = new PaaIkkunanNappipaneeli();
        asetaNappienKuuntelijat();
        luoKomponentit(this.getContentPane());
        
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_LEFT){
                    taajuuskayra.pienennaKerrointa();
                    return true;
                }                
                if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_RIGHT){
                    taajuuskayra.kasvataKerrointa();
                    return true;
                }                
                return false;
            }
        });
        
        pack();
        setVisible(true);
    }

    public void asetaVokaali(Vokaali vokaali){
        this.taajuuskayra.setVokaali(vokaali);
    }
    
    public void asetaArvotTaajuuskayralle(double[] uudetArvot){
        this.taajuuskayra.setArvot(uudetArvot);
    }
    
    public Taajuuskayra getTaajuuskayra(){
        return this.taajuuskayra;
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
        container.add((Component) this.taajuuskayra);
        container.add(nappipaneeli, BorderLayout.SOUTH);        

    }

    private void asetaNappienKuuntelijat() {
        nappipaneeli.getLaskemisNappi().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                taajuuskayra.kasvataMaxArvoa();
            }
        });
        
        nappipaneeli.getNostamisNappi().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                taajuuskayra.pienennaMaxArvoa();
            }
        });        
    }
}
