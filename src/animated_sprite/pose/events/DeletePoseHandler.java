/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package animated_sprite.pose.events;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import poseur.Poseur;
import poseur.files.PoseurFileManager;

/**
 * This handler responds to when the user wants to delete an existing Pose
 * file. It will have to make sure any file being edited is not
 * accidentally lost.
 * 
 * @author  Burak Firik
 *          Bugging Enterprises
 * @version 1.0
 */

 
public class DeletePoseHandler implements ActionListener{

     /**
     * Called when the user requests to delete a pose via the
     * delete pose button, this will make sure no work is lost
     * and then close the application.
     * 
     * @param ae The Event Object.
     */
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Poseur singleton = Poseur.getPoseur();
        PoseurFileManager poseurFileManager = singleton.getFileManager();
        poseurFileManager.requestDeletePose();
    }
    
}
