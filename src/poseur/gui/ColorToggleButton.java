package poseur.gui;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;

/**
 * This is a custom component that we'll use for representing a toggle
 * button where the background color of the button will change depending
 * on color selection. The reason we don't just just JToggleButton is that
 * we want the color to show through even when the button is in the
 * selected state.
 * 
 * @author  Burak
 *          Debugging Enterprises
 * @version 1.0
 */
public class ColorToggleButton extends JButton
{
    // THIS KEEPS TRACK OF WHETHER THIS TOGGLE BUTTON
    // IS SELECTED OR NOT. NOTE THAT WHEN IT IS SELECTED,
    // WE RENDERING THE BORDER, WHICH HIGHLIGHTS IT
    private boolean selected;

    /**
     * Default constructor, by default it is not selected. The constructor
     * also sets up the border to use
     */
    public ColorToggleButton() 
    {
        selected = false;
        Border outline = BorderFactory.createLineBorder(Color.GRAY, 4);
        setBorder(outline);
    }
    
    /**
     * Accessor method to check and see if this button is currently selected.
     * 
     * @return true if this button is currently selected, false otherwise.
     */
    @Override
    public boolean isSelected() { return selected; }

    /**
     * Toggles this button as selected.
     */
    public void select()
    {
        selected = true;
        this.setBorderPainted(true);
    }
    
    /**
     * Toggles this button as unselected.
     */
    public void deselect()
    {
        selected = false;
        this.setBorderPainted(false);
    }
}