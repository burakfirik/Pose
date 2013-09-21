package poseur.events.files;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import poseur.Poseur;
import poseur.files.PoseurFileManager;

/**
 * This handler responds to when the user wants to exit the
 * application. We'll have to be careful not to accidentally
 * lose any of the user's work.
 * 
 * @author  Burak
 *          Debugging Enterprises
 * @version 1.0
 */
public class ExitHandler implements ActionListener
{
    /**
     * Called when the user requests to exit via the
     * exit button, this will make sure no work is lost
     * and then close the application.
     * 
     * @param ae The Event Object.
     */
    @Override
    public void actionPerformed(ActionEvent ae)
    {
        // FORWARD THE REQUEST TO THE FILE MANAGER
        Poseur singleton = Poseur.getPoseur();
        PoseurFileManager poseurFileManager = singleton.getFileManager();
        poseurFileManager.requestExit();
    }
}