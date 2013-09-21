/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package poseur.gui;

import animated_sprite.pose.events.SavePoseAsHandler;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import poseur.Poseur;
import poseur.files.PoseurFileManager;

/**
 *
 * @author burak
 */
public class SpriteTypeGUIPanel extends JPanel {
    
    private JLabel label;
    private int id;
    private String nameOfImage;
 
   

    public SpriteTypeGUIPanel(JLabel lbl, int i){ 
        super();
        label=lbl;
        id=i;
        this.add(label);
        this.updateUI();
        
       
        this.addMouseListener(new MouseAdapter()
                      {
                       public void mouseClicked(MouseEvent me)
                         {
                            
                             
                             
                             System.out.println("TESSSSSSSSSSSSSSS");
                         }
                       public void mousePressed(MouseEvent me){
                           Poseur singleton = Poseur.getPoseur();
                             PoseurFileManager poseurFileManager = singleton.getFileManager();
                             poseurFileManager.requestOpenPose(id);
                             poseurFileManager.requestSavePose(id);
                             poseurFileManager.requestExportPose(id);
                             
                             PoseurGUI gui=singleton.getGUI();
                             gui.setID(id);
                             repaint();
                       }
                       
                      }
          );
        
 }
    
    
    public int getID(){
        return id;
    }
    public void setID(int i){
       id=i;
    }
    
    
   
    
}
