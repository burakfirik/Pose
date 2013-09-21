package animated_sprite.pose.events;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import poseur.Poseur;
import poseur.files.PoseurFileManager;

/**
 * This handler responds to when the user wants to export the 
 * current pose to an image file.
 * 
 * @author  Burak Firik
 *          Debugging Enterprises
 * @version 1.0
 */
public class ExportPoseHandler implements ActionListener
{
    /**
     * Called when the user requests to export the pose
     * currently being edited to an image file.
     * 
     * @param ae The Event Object.
     */
    @Override
    public void actionPerformed(ActionEvent ae)
    {
        // FORWARD THE REQUEST TO THE FILE MANAGER
        Poseur singleton = Poseur.getPoseur();
        PoseurFileManager poseurFileManager = singleton.getFileManager();
        poseurFileManager.requestExportPose();
        //poseurFileManager.requestSavePose(singleton.getGUI().getID());
        //poseurFileManager.requestExportPose(singleton.getGUI().getID());
    }
}