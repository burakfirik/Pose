/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package animated_sprite.sprite;

import anime.files.SpriteFileManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import poseur.Poseur;
import poseur.files.PoseurFileManager;

/**
 * This handler responds to when the user wants to create new sprite
 * file. It will have to make sure any file being edited is not
 * accidentally lost.
 * 
 * @author  Burak Firik
 *          Bugging Enterprises
 * @version 1.0
 */
public class NewSpriteHandler implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        // FORWARD THE REQUEST TO THE FILE MANAGER
        Poseur singleton = Poseur.getPoseur();
        SpriteFileManager spriteFileManager = singleton.getSpriteFileManager();
        spriteFileManager.requestNewSprite();
    }
    
}
