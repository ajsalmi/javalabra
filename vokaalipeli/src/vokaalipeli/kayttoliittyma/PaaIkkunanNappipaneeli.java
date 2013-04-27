package vokaalipeli.kayttoliittyma;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Paneeli napeille.
 *
 * @author A J Salmi
 */
public class PaaIkkunanNappipaneeli extends JPanel {
    private JButton ohjeNappi;
    private JButton kayranNostamisNappi;
    private JButton kayranLaskemisNappi;
    private JButton vokaalinVaihtoNappi;
    
    public JButton getNostamisNappi (){
        return this.kayranNostamisNappi;
    }
    
    public JButton getLaskemisNappi (){
        return this.kayranLaskemisNappi;
    }
    
    public PaaIkkunanNappipaneeli(){
        super(new GridLayout(1, 8));
        luoKomponentit();
    }
    
    public JButton getVokaalinVaihtoNappi(){
        return this.vokaalinVaihtoNappi;
    }

    private void luoKomponentit() {
        this.kayranNostamisNappi = new JButton("nosta käyrää");
        add(kayranNostamisNappi);
        // ---
        this.kayranLaskemisNappi = new JButton("laske käyrää");
        add(kayranLaskemisNappi);
        // ---
        this.vokaalinVaihtoNappi = new JButton("uusi vokaali");
        add(vokaalinVaihtoNappi);
        add(new JLabel(" "));
//        add(new JTextField(" "));
        // vaihda kieli 
        // 
//        add(new JLabel(" "));
//        add(new JLabel(" "));
        add(new JLabel(" "));
        add(new JLabel(" "));
        
        this.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void keyPressed(KeyEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void keyReleased(KeyEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        
        
        ohjeNappi = new JButton(" ? ");
        add(ohjeNappi);
                
        ohjeNappi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Infoikkuna(Info.PAAIKKUNAN_KAYTTO, ohjeNappi);
            }
        });
    }
        
//    }

}
