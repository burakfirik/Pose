/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package poseur.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author burak
 */
class ColorPanel extends JPanel{
	BufferedImage theCat;
	public ColorPanel(BufferedImage image){
	theCat = image;
	}
 
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(theCat, 0, 0, 200, 20, this);
                repaint();
	}
        
}
