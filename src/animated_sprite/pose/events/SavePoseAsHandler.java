/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package animated_sprite.pose.events;

/**
 *
 * @author burak
 */


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import poseur.Poseur;
import poseur.files.PoseurFileManager;

/**
 * This handler responds to when the user wants to save the current
 * pose to a new file name.
 * 
 * @author  Burak Firik
 *          Debugging Enterprises
 * @version 1.0
 */
public class SavePoseAsHandler implements ActionListener
{
    /**
     * Called when the user requests to save the current
     * pose as another file.
     * 
     * @param ae The Event Object.
     */
    @Override
    public void actionPerformed(ActionEvent ae)
    {
        // FORWARD THE REQUEST TO THE FILE MANAGER
        Poseur singleton = Poseur.getPoseur();
        PoseurFileManager poseurFileManager = singleton.getFileManager();
        poseurFileManager.requestSaveAsPose();
    }
}
