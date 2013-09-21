package poseur.events.edit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import poseur.Poseur;
import poseur.state.PoseurStateManager;

/**
 * This event handler responds to when the user wants to paste the
 * item on the clipboard onto the rendering surface.
 */
public class PasteHandler implements ActionListener
{
    /**
     * This method relays this event to the data manager, which
     * will put the item on the clipboard onto the rendering 
     * surface and will make that item the selected item.
     * 
     * @param ae The event object for this button press.
     */
    @Override
    public void actionPerformed(ActionEvent ae) 
    {
        Poseur singleton = Poseur.getPoseur();
        PoseurStateManager poseurStateManager = singleton.getStateManager();
        poseurStateManager.pasteSelectedItem();
    }   
}
