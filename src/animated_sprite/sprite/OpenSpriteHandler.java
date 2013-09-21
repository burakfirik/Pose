/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package animated_sprite.sprite;

import animated_sprite_viewer.AnimatedSpriteViewer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import poseur.Poseur;
import poseur.files.InvalidXMLFileFormatException;
import poseur.gui.PoseurGUI;

/**
 * This handler responds to when the user wants to open an existing sprite
 *  It will have to make sure any file being edited is not
 * accidentally lost.
 * 
 * @author  Burak Firik
 *          Bugging Enterprises
 * @version 1.0
 */
public class OpenSpriteHandler implements ActionListener{

    @Override
    public void actionPerformed(ActionEvent e) {
        PoseurGUI gui = Poseur.getPoseur().getGUI();
         //CALL THE LOAD SPRITE METHOD TO MAKE SURE NEW DATA IS LOADED
        gui.loadSprite();
       
    }
    
}
