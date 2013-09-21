package poseur.events.edit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import poseur.Poseur;
import poseur.state.PoseurStateManager;

/**
 * This event handler responds to when the user has selected an item
 * on the rendering surface and wants to move it to the background,
 * behind all other items.
 */
public class MoveToBackHandler implements ActionListener
{
    /**
     * This method relays this event to the state manager, which
     * will update the order of shapes accordingly.
     * 
     * @param ae The event object for this button press.
     */
    @Override
    public void actionPerformed(ActionEvent ae) 
    {
        Poseur singleton = Poseur.getPoseur();
        PoseurStateManager poseurStateManager = singleton.getStateManager();
        poseurStateManager.moveSelectedItemToBack();
    }   
}