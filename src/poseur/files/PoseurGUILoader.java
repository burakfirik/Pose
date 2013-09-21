package poseur.files;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.w3c.dom.Document;
import poseur.Poseur;
import static poseur.PoseurSettings.*;
import poseur.gui.PoseurGUI;

/**
 * This class provides services for loading settings from a 
 * properly formatted XML file into a PoseurGUI. At the moment,
 * it only loads window settings, but it could be extended to
 * provide additional functionality. 
 * 
 * @author  Burak
 *          Debugging Enterprises
 * @version 1.0
 */
public class PoseurGUILoader 
{
    // THE DEFAULT CONSTRUCT DOESN'T INITIALIZE ANTHING. THE initWindow
    // METHOD DOES ALL THE WORK. NOTE THAT THIS SIMPLE LITTLE CLASS IS
    // JUST A DEMONSTRATION OF HOW GUI SETTINGS CAN BE LOADED DYNAMICALLY.
    public PoseurGUILoader() {}
    
    /**
     * This method loads the settings found in the setupFileXML file, which
     * must be an XML file that validates against the gui_app_settings schema.
     * The loaded settings are then used to initialize the window argument.
     * 
     * @param window The GUI window to be initialized.
     * 
     * @param setupFileXML The full file name and path of the XML file that
     * contains values for initializing the window.
     */
    public void initWindow(PoseurGUI window, String setupFileXML)
    {
        try
        {
            // THIS WILL DO OUR VALIDATION FOR US
            XMLUtilities xmlUtil = new XMLUtilities();
        
            // LOAD THE XML FILE INTO A DOC.
            Document setupDoc = xmlUtil.loadXMLDocument(setupFileXML, WINDOW_SETTINGS_SCHEMA);

            // GET AND SET THE WINDOW TITLE
            String title = xmlUtil.getTextData(setupDoc, APP_TITLE_NODE);
            window.setAppName(title);
            window.setTitle(title);

            // GET AND SET THE WINDOW DIMENSIONS
            int windowWidth = xmlUtil.getIntData(setupDoc, WINDOW_WIDTH_NODE);
            int windowHeight = xmlUtil.getIntData(setupDoc, WINDOW_HEIGHT_NODE);
            windowHeight=680;
            window.setSize(windowWidth, windowHeight);

            // GET AND SET WHETHER WE NEED TO MAKE THE WINDOW RESIZABLE
            boolean resizable = xmlUtil.getBooleanData(setupDoc, WINDOW_RESIZABLE_NODE);
            window.setResizable(resizable);
        
            // GET AND SET THE LOCATION OF THE WINDOW, EITHER CENTERED
            // ON THE SCREEN, OR THE DEFAULT, IN THE TOP-LEFT CORNER
            boolean centered = xmlUtil.getBooleanData(setupDoc, WINDOW_CENTERED_NODE);
        
            // IF IT'S TO BE CENTERED, WE'LL NEED TO KNOW THE SCREEN SIZE
            if (centered)
            {
                // GET THE TOOLKIT SINGLETON, IT KNOWS THE SCREEN SIZE
                Toolkit tk = Toolkit.getDefaultToolkit();
                Dimension screenSize = tk.getScreenSize();
                int screenWidth = screenSize.width;
                int screenHeight = screenSize.height;
            
                // NOW CALCULATE WHERE TO PUT IT
                int windowX = (screenWidth/2) - (windowWidth/2);
                int windowY = (screenHeight/2) - (windowHeight/2);
            
                // AND PUT IT THERE
                window.setLocation(windowX, windowY);
            }
            // IT'S NOT CENTERED
            else
            {
                // THE DEFAULT IS 0,0 ANYWAY, BUT WE'LL JUST MAKE SURE
                window.setLocation(0,0);
            }
        
            // WE COULD LOAD MORE STUFF TO IF WE WANTED TO. WE'D HAVE TO CHANGE
            // THE SCHEMA TO ACCOMODATE MORE DATA, AND THE PROVIDE THAT DATA IN
            // THE XML FILE. WE'D PROBABLY NEED MORE SERVICE FUNCTIONS IN
            // XMLUtilities AS WELL.
        }
        catch(InvalidXMLFileFormatException ixffe)
        {
            // IF THIS HAPPENS THERE WERE PROBLEMS WITH THE XML FILE
            // LOADING AND THEREFORE THE PROGRAM SHOULD NOT CONTINUE. 
            // SO WE'LL JUST POST A FEEDBACK MESSAGE TO THE USER
            // AND KILL THE APP
            JOptionPane.showMessageDialog(  Poseur.getPoseur().getGUI(), 
                                            ixffe.getMessage(), 
                                            LOADING_XML_ERROR_TEXT, 
                                            JOptionPane.ERROR_MESSAGE);
            
            // LOG THE ERROR
            Logger.getLogger(Poseur.class.getName()).log(Level.SEVERE, null, ixffe);
            
            // KILL THE APP
            System.exit(0);
        }
    }
}