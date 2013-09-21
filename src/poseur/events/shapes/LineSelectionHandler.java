package poseur.events.shapes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import poseur.Poseur;
import poseur.shapes.PoseurShapeType;
import poseur.state.PoseurStateManager;

/**
 * This handler responds to when the user requests to
 * start drawing a line.
 * 
 * @author  Burak
 * 
 *          Debugging Enterprises
 * @version 1.0
 */
public class LineSelectionHandler implements ActionListener
{
    /**
     * When the user requests to draw a line, we'll need
     * to notify the data manager, since it managers the 
     * shape in progress. It will update the gui as needed
     * as well.
     * 
     * @param ae The Event Object.
     */
    @Override
    public void actionPerformed(ActionEvent ae)
    {
        // RELAY THE REQUEST TO THE DATA MANAGER
        Poseur singleton = Poseur.getPoseur();
        PoseurStateManager poseurStateManager = singleton.getStateManager();
        poseurStateManager.selectShapeToDraw(PoseurShapeType.LINE);
    }
}
