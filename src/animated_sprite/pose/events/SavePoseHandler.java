package animated_sprite.pose.events;

import animated_sprite_viewer.AnimatedSpriteViewer;
import anime.files.SpriteFileManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import poseur.Poseur;
import poseur.files.PoseurFileManager;
import poseur.gui.PoseurGUI;

/**
 * This handler responds to when the user wants to save the
 * Pose currently being edited.
 * 
 * @author  BURAK FIRIK
 *          Debugging Enterprises
 * @version 1.0
 */
public class SavePoseHandler implements ActionListener
{
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
        
       Poseur singleton = Poseur.getPoseur();
       PoseurGUI gui =singleton.getGUI(); 
       
       AnimatedSpriteViewer view=gui.getAnimametedViewerPanel();
       String jlistname=view.getJlist().getSelectedValue().toString();
       String comboname=view.getCombo().getSelectedItem().toString();
      
       PoseurFileManager poseurFileManager = singleton.getFileManager();
       poseurFileManager.requestOpenPose(gui.getID());
       poseurFileManager.requestSavePose(gui.getID());
       poseurFileManager.requestExportPose(gui.getID());
       
       
            
           
           view.loadSpritesFromState(jlistname);
           
           
            
           view.getJlist().setSelectedValue(jlistname, true);
           view.getCombo().setSelectedItem(comboname);
           //JOptionPane.showMessageDialog(gui, "Selected Pose is deleted");
        
    }
}