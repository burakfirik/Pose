/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package animated_sprite.state.events;

import animated_sprite_viewer.AnimatedSpriteViewer;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import poseur.Poseur;
import poseur.gui.PoseurGUI;

/**
 *
 * @author burak
 */
public class LoadStateImagesHandler implements ItemListener {

      // THIS GUY WILL DO ALL THE WORK
    private AnimatedSpriteViewer viewer;

    /**
     * We'll need the viewer for later.
     * 
     * @param initViewer We'll ask the view to update itself when the event happens.
     */
    public LoadStateImagesHandler(AnimatedSpriteViewer initViewer)
    {
        viewer = initViewer;
    }
    
    @Override
    public void itemStateChanged(ItemEvent e) {
       
      
       viewer.loadImages();
     
    }
    
}
