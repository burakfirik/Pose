package animated_sprite_viewer.events;

import animated_sprite_viewer.AnimatedSpriteViewer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import poseur.Poseur;
import poseur.gui.PoseurGUI;

/**
 * The SpriteTypeSelectionHandler responds to when the
 * user selects a sprite type from the GUI. Note that
 * it simply forwards ownership of the request to the viewer.
 * 
 * @author  Burak
 *          Debugging Enterprises
 * @version 1.0
 */
public class SpriteTypeSelectionHandler implements ListSelectionListener
{
    // THIS GUY WILL DO ALL THE WORK
    private AnimatedSpriteViewer viewer;
    
    /**
     * We'll need the viewer for later.
     * 
     * @param initViewer We'll ask the view to update itself when the event happens.
     */
    public SpriteTypeSelectionHandler(AnimatedSpriteViewer initViewer)
    {
        
        viewer = initViewer;
    }

    /**
     * Called by Swing when someone selects a sprite type from the list.
     * It will forward the request to the view so that it can load the
     * appropriate sprite.
     * 
     * @param ie Contains information about the event.
     */
    @Override
    public void valueChanged(ListSelectionEvent lse) 
    {
        Poseur singleton=Poseur.getPoseur();
        PoseurGUI gui=singleton.getGUI();
        AnimatedSpriteViewer view=gui.getAnimametedViewerPanel();
        view.selectSpriteType();
        
    }
}