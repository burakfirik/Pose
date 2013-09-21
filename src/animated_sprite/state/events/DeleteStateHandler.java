/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package animated_sprite.state.events;

import anime.files.SpriteFileManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import poseur.Poseur;

/**
 * This handler responds to when the user wants to delete a state 
 * . It will have to make sure any file being edited is not
 * accidentally lost.
 * 
 * @author  Burak Firik
 *          Bugging Enterprises
 * @version 1.0
 */


public class DeleteStateHandler  implements ActionListener{
/**
     * Called when the user requests to delete a state  via the
     * save pose button, this will make sure no work is lost
     * and then close the application.
     * 
     * @param ae The Event Object.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Poseur singleton = Poseur.getPoseur();
        SpriteFileManager spriteFileManager = singleton.getSpriteFileManager();
        spriteFileManager.requestDeleteState();
    }
    
}