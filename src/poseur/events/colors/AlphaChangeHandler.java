package poseur.events.colors;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import poseur.Poseur;
import poseur.state.PoseurStateManager;

/**
 * This class responds to when the user drags the slider
 * that changes the transparency of the selected object.
 * 
 * @author  Burak
 *          Debugging Enterprises
 * @version 1.0
 */
public class AlphaChangeHandler implements ChangeListener
{
    /**
     * This event handler method is called when the user changes the
     * alpha (transparency) slider. Note that 0 means fully 
     * transparent and 255 means fully opaque. When the slider value
     * is changed we get that value and send it off to the state
     * manager to update the current state.
     * 
     * @param ce The Event Object.
     */
    @Override
    public void stateChanged(ChangeEvent ce)
    {
        // GET THE ALPHA VALUE THE USER JUST CHOSE
        Poseur singleton = Poseur.getPoseur();
        PoseurStateManager stateManager = singleton.getStateManager();
        JSlider alphaSlider = (JSlider)ce.getSource();
        int alpha = alphaSlider.getValue();
        
        // AND SEND IT OFF TO UPDATE THE SELECTED SHAPE
        stateManager.changeSelectedShapeAlpha(alpha);
    }
}