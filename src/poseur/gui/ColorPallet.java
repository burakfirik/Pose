package poseur.gui;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;
import poseur.events.colors.ColorPalletHandler;
import poseur.state.ColorPalletState;

/**
 * This component serves as a color pallet for selecting
 * preset and custom colors in an editing application. Note
 * the corresponding ColorPalletState class contains data
 * and methods for initializing the colors in the pallet and
 * for updating them. When updated through that class, this
 * view is refreshed.
 * 
 * Note that this class has a symbiotic relationship with
 * the ColorPalletState class, they each have references
 * to the other.
 * 
 * @author  Burak
 *          Debugging Enterprises
 * @version 1.0
 */
public class ColorPallet extends JPanel
{
    // THESE ARE INSIDE THIS PANEL AND WILL HAVE BACKGROUND
    // COLORS THAT CORRESOPND TO THE
    private JButton[] colorPalletButtons;
    
    // HERE'S THE STATE, WHICH THIS COMPONENT REFLECTS
    private ColorPalletState state;

    /**
     * This constructor sets up the pallet for use. Note that
     * the ColorPalletState argument must be fully loaded with
     * the colors for the color pallet before this is called.
     * 
     * @param initColorPalletState The state information for
     * this color pallet, it must be pre-loaded so that this
     * constructor can correctly make the color pallet buttons.
     */
    public ColorPallet(ColorPalletState initColorPalletState)
    {
        // WE'LL KEEP THE STATE, SINCE WE'LL USE
        // IT EVERY TIME WE REFRESH
        state = initColorPalletState;
        
        // FIRST LET'S CONSTRUCT ALL THE BUTTONS
        int numColorsInPallet = state.getColorPalletSize();
        colorPalletButtons = new JButton[numColorsInPallet];
        for (int i = 0; i < numColorsInPallet; i++)
        {
            // CONSTRUCT AND ADD EACH BUTTON ONE AT A TIME
            colorPalletButtons[i] = new JButton();
        }
        
        // AND NOW ARRANGE THEM INSIDE THIS PALLET
        int colorPalletRows = state.getColorPalletRows();
        int colorPalletSize = colorPalletButtons.length;
        GridLayout gL = new GridLayout(colorPalletRows, colorPalletSize/colorPalletRows);
        this.setLayout(gL);
        Border etchedBorder = BorderFactory.createEtchedBorder();
        this.setBorder(etchedBorder);
        
        // NOW PUT THE BUTTONS INTO THE COLOR PALLET PANEL
        // WITH THEIR PROPER INITIAL COLORS
        int row = 0;
        int col = 0;
        for (int i = 0; i < colorPalletSize; i++)
        {
            if (col == (colorPalletRows/colorPalletRows))
            {
                col = 0;
                row++;
            }
            this.add(colorPalletButtons[i]);
            colorPalletButtons[i].setBackground(state.getColorPalletColor(i));
        }
    }

    /**
     * Accessor method for getting the state manager for
     * this color pallet.
     * 
     * @return The state manager for this pallet.
     */
    public ColorPalletState getState()
    {
        return state;
    }
    
    /**
     * Called whenever the color pallet state changes, this method
     * reflects the state data by updating the background colors
     * of all the colors in the state.
     */
    public void refreshView()
    {
        // UPDATE THE BACKGROUND COLORS OF ALL THE
        // BUTTONS, EVEN THOUGH SOME HAVE NOT CHANGED
        for (int i = 0; i < colorPalletButtons.length; i++)
        {
            Color color = state.getColorPalletColor(i);
            colorPalletButtons[i].setBackground(color);
        }
    }
    
    /**
     * Called when the GUI is setup, this method registers the
     * handler for the color pallet buttons. Note that all the
     * color pallet buttons share this one event handler.
     * 
     * @param cph The event handler that will respond to when
     * a user clicks on a color pallet button.
     */
    public void registerColorPalletHandler(ColorPalletHandler cph)
    {
        // REGISTER THSI HANDLER WITH ALL THE BUTTONS
        for (int i = 0; i < colorPalletButtons.length; i++)
        {
            colorPalletButtons[i].addActionListener(cph);
        }
    }

    /**
     * This method is used to enable and disable this color
     * pallet, which fill cascade this setting through all 
     * the buttons in the pallet.
     * 
     * @param isEnabled If true is provided all the buttons
     * in the pallet will be enabled. If false is provided,
     * all the buttons will be disabled. Disabled buttons
     * will not trigger events when one clicks on them and
     * they are rendered as gray.
     */
    @Override
    public void setEnabled(boolean isEnabled)
    {
        // DISABLE/ENABLE THE PANEL ITSELF FOR ITS DEFAULT
        // DISABLED/ENABLED RENDERING LOOK
        super.setEnabled(isEnabled);
        
        // AND THEN CASCADE IT THROUGH ALL THE BUTTONS
        for (int i = 0; i < colorPalletButtons.length; i++)
        {
            colorPalletButtons[i].setEnabled(isEnabled);
        }
    }
}