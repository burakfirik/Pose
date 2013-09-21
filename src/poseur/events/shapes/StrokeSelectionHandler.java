package poseur.events.shapes;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JComboBox;
import poseur.Poseur;
import poseur.state.PoseurStateManager;

/**
 * This handler responds to when the user wishes to change
 * the stroke used for rendering shapes. Note that this may
 * also affect the currently selected shape, if there is one.
 * 
 * @author  Burak
 *          Debugging Enterprises
 * @version 1.0
 */
public class StrokeSelectionHandler implements ItemListener
{
    /**
     * When an item is selected, we'll need to change the
     * rendering settings and may also have to change the
     * stroke for a selected shape.
     * 
     * @param ie The Event Object.
     */
    @Override
    public void itemStateChanged(ItemEvent ie)
    {
        // WE ONLY WANT TO RESPOND IF THE USER
        // TRIGGERED THIS EVENT, NOT THE BUILDING
        // OF THE COMBO BOX IN THE FIRST PLACE
        if (ie.getStateChange() == ItemEvent.SELECTED)
        {
            // FIGURE OUT WHICH INDEX WAS SELECTED, SINCE
            // THAT WILL TELL US WHAT STROKE WIDTH THE
           // USER WANTS. THE MINUMUM WIDTH WE WISH TO
           // RENDER WITH IS 1 PIXEL WIDE, SO WE'LL ADD
            // ONE TO THE SELECTED INDEX FOR A STROKE
            JComboBox comboBox = (JComboBox)ie.getSource();
            int chosenStroke = comboBox.getSelectedIndex() + 1;
        
            // RELAY THE REQUEST TO THE DATA MANAGER
            Poseur singleton = Poseur.getPoseur();
            PoseurStateManager dataManager = singleton.getStateManager();
            dataManager.selectStroke(chosenStroke);
        }
    }
}