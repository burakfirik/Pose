package animated_sprite_viewer.events;

import animated_sprite_viewer.AnimatedSpriteViewer;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import poseur.Poseur;
import poseur.gui.PoseurGUI;

/**
 * The AnimationStateSelectionHandler responds to when the
 * user selects an animation state from the GUI. Note that
 * it simply forwards ownership of the request to the viewer.
 * 
 * @author  BURAK firik
 *          Debugging Enterprises
 * @version 1.0
 */
public class AnimationStateSelectionHandler implements ItemListener
{
    // THIS GUY WILL DO ALL THE WORK
    private AnimatedSpriteViewer viewer;

    /**
     * We'll need the viewer for later.
     * 
     * @param initViewer We'll ask the view to update itself when the event happens.
     */
    public AnimationStateSelectionHandler(AnimatedSpriteViewer initViewer)
    {
        viewer = initViewer;
    }
    
    /**
     * Called by Swing when something in the combobox changes, either
     * because the user selected something or because items were added
     * or removed from it.
     * 
     * @param ie Contains information about the event.
     */
    @Override
    public void itemStateChanged(ItemEvent ie) 
    {
        // THE viewer WILL HANDLE IT
        viewer.selectAnimationState();
        Poseur singleton =Poseur.getPoseur();
        PoseurGUI gui=singleton.getGUI();
        
        try{
            if(!viewer.getCombo().getSelectedItem().toString().equals("Select Animation State")){
            
                gui.setDisablePoseButton(true);
            }else{
                gui.setDisablePoseButton(false);
            }
        }catch(NullPointerException e){
                gui.setDisablePoseButton(false);
        }
        
    }    
}
