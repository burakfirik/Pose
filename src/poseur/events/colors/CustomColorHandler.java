package poseur.events.colors;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JColorChooser;
import poseur.Poseur;
import poseur.PoseurSettings;
import poseur.gui.ColorPallet;
import poseur.gui.PoseurGUI;
import poseur.state.ColorPalletState;

/**
 * This handler responds to when the user wants to create a custom color
 * for rendering, which should then be added to the color pallet.
 * 
 * @author  Burak
 *          Debugging Enterprises
 * @version 1.0
 */
public class CustomColorHandler implements ActionListener
{
    /**
     * Called when the user wants to create a custom color, this
     * method will use a JColorChooser to ask the user for a color, 
     * and if the user selects one, will add it to the pallet.
     * 
     * @param ae The Event Object.
     */
    @Override
    public void actionPerformed(ActionEvent ae)
    {
        // GET THE GUI
        Poseur singleton = Poseur.getPoseur();
        PoseurGUI gui = singleton.getGUI();
        
        // PROMPT THE USER FOR A NEW COLOR
        Color customColor = JColorChooser.showDialog( 
                gui, PoseurSettings.SELECT_CUSTOM_COLOR_TEXT, Color.yellow);
        
        // DON'T PROCEED IF THE USER CANCELLED
        if (customColor != null)
        {
            // PUT THIS COLOR IN THE PALLET'S STATE, WHICH WILL
            // FORCE AN UPDATE ON THE PALLET
            ColorPallet colorPallet = gui.getColorPallet();
            ColorPalletState colorPalletState = colorPallet.getState();
            colorPalletState.putCustomColorInPallet(customColor);                
        }
    }
}