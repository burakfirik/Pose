package poseur.events.canvas;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import poseur.gui.PoseCanvas;
import poseur.state.PoseCanvasState;

/**
 * This event handler responds to when the canvas is rendered for 
 * the first time. When that happens, we know the size of the 
 * canvas, and so can update the necessary rendering information.
 * 
 * @author  Burak
 *          Debugging Enterprises
 * @version 1.0
 */
public class PoseCanvasComponentHandler implements ComponentListener
{
    /**
     * This method is called when the size of the canvas is 
     * determined and set. We can respond by getting that
     * value and using it to size the pose area. Note that
     * the canvas state knows how to update itself, so we'll
     * just ask it to do so.
     * 
     * @param ce The Event Object.
     */
    @Override
    public void componentResized(ComponentEvent ce)
    {
        // GET THE CANVAS' STATE AND UPDATE IT
        PoseCanvas canvas = (PoseCanvas)ce.getSource();       
        PoseCanvasState canvasState = canvas.getState();
        canvasState.updatePoseArea();
    }

    // WE WON'T USE THE REST OF THESE METHODS
    // BUT WE HAVE TO PROVIDE EMPTY DEFINITIONS
    // AT LEAST, BECAUSE THIS CLASS
    // implements ComponentListener
    
    @Override
    public void componentMoved(ComponentEvent e)    {}

    @Override
    public void componentShown(ComponentEvent e)    {}

    @Override
    public void componentHidden(ComponentEvent e)   {}
}
