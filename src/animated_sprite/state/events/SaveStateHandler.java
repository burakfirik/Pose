/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package animated_sprite.state.events;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This handler responds to when the user wants to save a new state 
 * . It will have to make sure any file being edited is not
 * accidentally lost.
 * 
 * @author  Burak Firik
 *          Bugging Enterprises
 * @version 1.0
 */

public class SaveStateHandler  implements ActionListener{
/**
     * Called when the user wants to save an existing state  via the
     * save state button, this will make sure no work is lost
     * and then close the application.
     * 
     * @param ae The Event Object.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("COMPONENT CONNECTED BUTTON IS PRESSED");
    }
    
}