package animated_sprite_viewer.events;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import sprite_renderer.SceneRenderer;

/**
 * The StopAnimationHandler class responds to when the user
 * requests to stop animation.
 * 
 * @author  Burak
 *          Debugging Enterprises
 * @version 1.0
 */
public class StopAnimationHandler implements ActionListener
{
    // THIS IS REALLY THE ONLY ONE WHO CAN PAUSE OR UNPAUSE ANIMATION
    private SceneRenderer renderer;
    
     /**
     * Constructor will need the renderer for when the event happens.
     * 
     * @param initRenderer Renderers can pause and unpause the rendering.
     */
    public StopAnimationHandler(SceneRenderer initRenderer)
    {
        // KEEP THIS FOR LATER
        renderer = initRenderer;
    }    
    
    /**
     * Here's the actual method called when the user clicks the 
     * stop animation method, which results in pausing of the
     * renderer, and thus the animator as well.
     * 
     * @param ae Contains information about the event.
     */
    @Override
    public void actionPerformed(ActionEvent ae)
    {
        renderer.pauseScene();
    }
}
