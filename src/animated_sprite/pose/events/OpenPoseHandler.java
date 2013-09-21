package animated_sprite.pose.events;

import animated_sprite_viewer.AnimatedSpriteViewer;
import anime.files.SpriteFileManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import poseur.Poseur;
import poseur.files.PoseurFileManager;
import poseur.gui.PoseurGUI;

/**
 * This handler responds to when the user wants to open an existing Pose
 * file. It will have to make sure any file being edited is not
 * accidentally lost.
 * 
 * @author  Burak Firik
 *          Debugging Enterprises
 * @version 1.0
 */
public class OpenPoseHandler implements ActionListener
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
        //CREATE AND RETRIEVE THE SINGLETON OBJECT
        Poseur singleton = Poseur.getPoseur();
        //GET GUI OBJECT 
        PoseurGUI gui=singleton.getGUI();
        //GET FILE MANAGER
        PoseurFileManager poseurFileManager = singleton.getFileManager();
        
        SpriteFileManager spriteFileManager = singleton.getSpriteFileManager();
        AnimatedSpriteViewer view=gui.getAnimametedViewerPanel();
        String  combo=view.getCombo().getSelectedItem().toString();
        String  spriteName=view.getJlist().getSelectedValue().toString();
        view.updateEveryhting();
        //CREATE RANDOM NUMBER AND ASSIGN IT TO NEW STATE NAME AND ADD IT TO THE COMBO BOX
        int rand=(int)(Math.random()*1000);
        //NAME OF THE NEW STATE
        String test=combo+rand;
        
        //SPRITE FILE MANAGER OBJECT TO MANIPULATE THE LEFTPANEL
        spriteFileManager.requestDuplicateState(test);
        String  combo2=view.getCombo().getSelectedItem().toString();  
        //SET THE VALUE OF SELECTED OBJECT IN JLIST
        view.getJlist().setSelectedValue(spriteName, true);
        //SET THE VALUE OF SELECTED OBJECT IN COMBOBOX
        view.getCombo().setSelectedItem(combo);
        //SET THE VALUE TO 5 FOR 5 MILISECONDS
        spriteFileManager.requestDeleteState(5);
        view.getJlist().setSelectedValue(spriteName, true);
        view.getCombo().setSelectedItem(test);
        spriteFileManager.requestRenameStateFor(combo);
       
        
    }
}