/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package animated_sprite.pose.events;

import animated_sprite_viewer.AnimatedSpriteViewer;
import anime.files.SpriteFileManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import poseur.Poseur;
import poseur.files.PoseurFileManager;
import poseur.gui.PoseurGUI;

/**
 * This handler responds to when the user wants to duplicate an existing Pose
 * file. It will have to make sure any file being edited is not
 * accidentally lost.
 * 
 * @author  Burak Firik
 *          Bugging Enterprises
 * @version 1.0
 */


public class DuplicatePoseHandler  implements ActionListener{
 /**
     * Called when the user requests to duplicate a pose via the
     * duplicate pose button, this will make sure no work is lost
     * and then close the application.
     * 
     * @param ae The Event Object.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        
        
        Poseur singleton = Poseur.getPoseur();
        PoseurGUI gui=singleton.getGUI();
        SpriteFileManager spriteFileManager = singleton.getSpriteFileManager();
        AnimatedSpriteViewer view=gui.getAnimametedViewerPanel();
        String  combo=view.getCombo().getSelectedItem().toString();
           
        PoseurFileManager poseurFileManager = singleton.getFileManager();
        poseurFileManager.requestExportPose();
    }
    
}