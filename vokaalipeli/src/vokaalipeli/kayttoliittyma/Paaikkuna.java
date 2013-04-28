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
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * Tässä JFramen perivässä ikkunaluokassa on Taajuuskayra-luokan olio,
 * jolle lähetetään arvot piirrettäviksi, ja nappipaneeli.
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
        asetaKuuntelijatJaKasittelijat();
        luoKomponentit(this.getContentPane());                
        pack();
        setVisible(true);
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
     * TODO: ... ja vokaalin pyytämistä varten nappi 
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

    private void asetaKuuntelijatJaKasittelijat() {
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
        
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_LEFT){
                    taajuuskayra.pienennaKorjausKerrointa();
                    return true;
                }                
                if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_RIGHT){
                    taajuuskayra.kasvataKorjausKerrointa();
                    return true;
                }                
                return false;
            }
        });    
    }
}
