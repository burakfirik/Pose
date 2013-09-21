/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package animated_sprite.sprite;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import poseur.Poseur;
import poseur.files.PoseurFileManager;

/**
 * This handler responds to when the user wants to open an existing Pose
 * file. It will have to make sure any file being edited is not
 * accidentally lost.
 * 
 * @author  Burak Firik
 *          Bugging Enterprises
 * @version 1.0
 */
public class OpenPoseFromPoseList implements ActionListener {
    
     /**
     * Called when the user requests to exit via the
     * exit button, this will make sure no work is lost
     * and then close the application.
     * 
     * @param ae The Event Object.
     */
    @Override
    public void actionPerformed(ActionEvent ae)
    {
        // FORWARD THE REQUEST TO THE FILE MANAGER
        Poseur singleton = Poseur.getPoseur();
        PoseurFileManager poseurFileManager = singleton.getFileManager();
        poseurFileManager.requestOpenPose();
    }
    
}
