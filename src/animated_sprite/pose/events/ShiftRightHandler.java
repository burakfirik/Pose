/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package animated_sprite.pose.events;

import anime.files.SpriteFileManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import poseur.Poseur;

/**
 * This handler responds to when the user wants to reorder a Pose 
 * . It will have to make sure any file being edited is not
 * accidentally lost. this will shift the selected pose towards right
 * 
 * @author  Burak Firik
 *          Bugging Enterprises
 * @version 1.0
 */
 

public class ShiftRightHandler implements ActionListener{
/**
     * Called when the user requests to reorder new Pose  via the
     * new pose button, this will make sure no work is lost
     * and then close the application.
     * 
     * @param ae The Event Object.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Poseur singleton = Poseur.getPoseur();
        SpriteFileManager spriteFileManager = singleton.getSpriteFileManager();
        spriteFileManager.requestShiftRight();
    }
    
}