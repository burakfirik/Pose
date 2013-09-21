package poseur.events.zoom;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import poseur.gui.PoseDimensionsDialog;

/**
 * This event handler responds to when the user wants to change the
 * pose dimensions.
 * 
 * @author  Burak
 *          Debugging Enterprises
 * @version 1.0
 */
public class ChangePoseDimensionsHandler implements ActionListener
{
    /**
     * When called, this method starts the dialog that allows
     * the user to enter a pose width and height to change the
     * pose dimensions.
     * 
     * @param ae The Event Object.
     */
    @Override
    public void actionPerformed(ActionEvent ae)
    {
        PoseDimensionsDialog dialog = new PoseDimensionsDialog();
        dialog.initLocation();
        dialog.setVisible(true);
    }
}