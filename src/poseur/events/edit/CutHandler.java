package poseur.events.edit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import poseur.Poseur;
import poseur.state.PoseurStateManager;

/**
 * This event handler responds to when the user has selected an item
 * on the canvas and has asked to cut it, which should remove
 * it from the rendering surface and place it on the clipboard.
 */
public class CutHandler implements ActionListener
{
    /**
     * This method relays this event to the data manager, which
     * will update the selected item and the clipboard accordingly.
     * 
     * @param ae The event object for this button press.
     */
    @Override
    public void actionPerformed(ActionEvent ae) 
    {
        Poseur singleton = Poseur.getPoseur();
        PoseurStateManager poseurStateManager = singleton.getStateManager();
        poseurStateManager.cutSelectedItem();
    }   
}