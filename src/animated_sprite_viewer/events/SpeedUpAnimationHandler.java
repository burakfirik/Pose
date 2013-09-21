package animated_sprite_viewer.events;

import animated_sprite_viewer.AnimatedSpriteXMLLoader;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import sprite_renderer.SceneRenderer;

/**
 * The SlowDownAnimationHandler responds to when the
 * user requests to speed up animation. It proceeds
 * to do just that.
 * 
 * @author  Burak
 *          Debugging Enterprises
 * @version 1.0
 */
public class SpeedUpAnimationHandler implements ActionListener
{
    // THE SCENE RENDERER KNOWS AND USES THE RENDERING SPEED
    private SceneRenderer renderer;
    
    /**
     * We'll need the renderer for when the event happens.
     * 
     * @param initRenderer The renderer from the SceneRenderer library.
     */
    public SpeedUpAnimationHandler(SceneRenderer initRenderer)
    {
        renderer = initRenderer;
    }    
    
     /**
     * Called when someone presses the speed up the scene button,
     * it tells the renderer to do so.
     * 
     * @param ae Has info about the event.
     */
    @Override
    public void actionPerformed(ActionEvent ae)
    {
        // GET THE SCALER AND SCALE IT
        float scaler = renderer.getTimeScaler();
        renderer.setTimeScaler(scaler/1.5f);
    }
}