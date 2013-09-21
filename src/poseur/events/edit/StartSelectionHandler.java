package poseur.events.edit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import poseur.Poseur;
import poseur.state.PoseurStateManager;

/**
 * This event handler responds to when the user has clicked on
 * the arrow button, denoting they want to select a shape to
 * edit in some way.
 */
public class StartSelectionHandler implements ActionListener
{
    /**
     * This method relays this event to the data manager, which
     * will update its state and the gui.
     * 
     * @param ae The event object for this button press.
     */
    @Override
    public void actionPerformed(ActionEvent ae) 
    {
        Poseur singleton = Poseur.getPoseur();
        PoseurStateManager poseurStateManager = singleton.getStateManager();
        poseurStateManager.startShapeSelection();
    }   
}