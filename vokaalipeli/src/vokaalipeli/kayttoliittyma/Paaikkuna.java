package vokaalipeli.kayttoliittyma;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 *
 * @author A J Salmi
 */
public class Paaikkuna extends JFrame {

    public Paaikkuna(Taajuuskayra kayra, int leveys, int korkeus) {
        super("Vokaalipeli");
        setPreferredSize(new Dimension(leveys, korkeus));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        luoKomponentit(this.getContentPane(), kayra);
        pack();
        setVisible(true);
    }

    /**
     * Metodi täyttää ikkunan halutuilla komponenteilla: taajuuskäyrä
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
    private void luoKomponentit(Container container, Taajuuskayra kayra) {
        container.add((Component) kayra);
        
        // TODO: esim. alapaneeliin napit maxArvon muokkaamiseen 
        // ja käyrän keskiarvojen määrän muokkaamiseen 
    }
}
