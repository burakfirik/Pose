package poseur.state;

import java.awt.Color;
import poseur.gui.ColorPallet;

/**
 * This class stores the state information needed for the
 * application's color pallet, including what colors are
 * currently in the pallet and where new custom colors
 * will be placed.
 * 
 * @author  Burak
 *          Debugging Enterprises
 * @version 1.0
 */
public class ColorPalletState
{
    // THIS IS THE VIEW OF THIS DATA
    private ColorPallet view;
    
    // WE'LL STORE THE COLOR PALLET INFORMATION IN
    // THIS SET OF VARIABLES
    private Color[] colorPalletColors;
    private int colorPalletRows;
    private int customColorIndex;
    private int numberOfFixedColors;
    private Color unassignedCustomColor;
    
    /**
     * This constructor provides does not initialize the data 
     * for use for the color pallet. To initialize
     * the data, the loadColorPallet method must be called
     * following construction.
     * 
     * @param initView The GUI component that will represent
     * this color pallet.
     */
    public ColorPalletState()
    {
        // NOTE WE'LL NEED TO GET THE VIEW LATER
        view = null;
        
        // AND MARK EVERYTHING ELSE AS UNINITIALIZED
        colorPalletColors = null;
        colorPalletRows = -1;
        customColorIndex = -1;
        numberOfFixedColors = -1;
        unassignedCustomColor = null;        
    }
    
    // ACCESSOR METHODS
    
    /**
     * Gets a color at a given index in the color pallet.
     * 
     * @return The Color at the index location in the color pallet.
     */
    public Color getColorPalletColor(int index) { return colorPalletColors[index]; }
    
    /**
     * Gets the size of the color pallet to be used by the Poseur app.
     * 
     * @return Number of colors in the color pallet to be used by the GUI. 
     */
    public int getColorPalletSize() { return colorPalletColors.length; }
    
    /**
     * Gets the number of rows in the color pallet.
     * 
     * @return Number representing the number of rows of colors in the
     * color pallet.
     */
    public int getColorPalletRows() { return colorPalletRows; }

    /**
     * Gets the index of where a custom color is to be added next.
     * 
     * @return The custom color index, which is where a custom
     * color would be added next to the pallet.
     */
    public int getCustomColorIndex() { return customColorIndex; }

    // MUTATOR METHODS

    /**
     * This mutator method sets the view of the color pallet.
     * 
     * @param initView The component that will be used to
     * rendering this color pallet.
     */
    public void setView(ColorPallet initView)
    {
        view = initView;
    }
    
    /**
     * Sets a color at a given index in the color pallet.
     * 
     * @param color Color to put into the color pallet.
     * 
     * @param index Index location in the color pallet to put color.
     */
    public void setColorPalletColor(Color color, int index)
    {
        // PUT THE COLOR IN THE PALLET
        colorPalletColors[index] = color;
        
        // AND UPDATE THE GUI SO IT SHOWS THE NEW COLOR
        view.refreshView();
    }
    
    /**
     * This method sets up the color pallet for use. It loads
     * all the custom values at once.
     * 
     * @param initColorPalletColors This array should be preloaded
     * with all the Colors necessary for the pallet.
     * 
     * @param initColorPalletRows This represents the number of
     * rows the color pallet will be split into.
     * 
     * @param initNumberOfFixedColors This number represents the
     * number of colors in the pallet that are set, and cannot be
     * changed. The remaining colors can be replaced by a custom
     * color.
     * 
     * @param initUnassignedCustomColor This is the Color to use
     * in the pallet for custom slots before the user has chosen
     * a custom color to put there.
     */
    public void loadColorPalletState(   Color[] initColorPalletColors,
                                        int initColorPalletRows,
                                        int initNumberOfFixedColors,
                                        Color initUnassignedCustomColor)
    {
        // LOAD EVERYTHING AT ONCE
        colorPalletColors = initColorPalletColors;
        colorPalletRows = initColorPalletRows;
        numberOfFixedColors = initNumberOfFixedColors;
        customColorIndex = numberOfFixedColors;
        unassignedCustomColor = initUnassignedCustomColor;
        
        // NOW FILL THE CUSTOM COLORS WITH THE
        // UNASSIGNED, i.e. DEFAULT COLOR
        for (int i = numberOfFixedColors; i < colorPalletColors.length; i++)
        {
            colorPalletColors[i] = unassignedCustomColor;
        }
    }
    
    /**
     * This method will put the customColor argument into this
     * application's color pallet, and then force an update of
     * the view. Note that we will use a round robin algorithm
     * for adding custom colors. We'll never touch the colors 
     * loaded from the XML file, but if the pallet is full, the
     * one provided by this method will replace the oldest 
     * custom color in the pallet.
     * 
     * @param customColor Color to add to the pallet.
     */
    public void putCustomColorInPallet(Color customColor)
    {
        // REPLACE THE OLDEST CUSTOM COLOR IN THE
        // PALLET WITH THE customColor
        if (customColorIndex == colorPalletColors.length)
        {
            customColorIndex = numberOfFixedColors;
        }
        colorPalletColors[customColorIndex] = customColor;
        customColorIndex++;
        
        // UPDATE THE VIEW
        view.refreshView();
    }
}