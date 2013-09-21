package poseur;

import animated_sprite_viewer.AnimatedSpriteViewer;
import anime.files.SpriteFileManager;
import java.util.ArrayList;
import java.util.Iterator;
import static poseur.PoseurSettings.*;
import poseur.files.PoseurFileManager;
import poseur.files.PoseurGUILoader;
import poseur.gui.PoseurGUI;
import poseur.state.PoseurStateManager;
/**
 * The Poseur application is a pose editor, allowing the user to construct
 * images to be used for making sprites. The application will save its internal
 * representation of a pose in an xml file and can export that representation
 * to a .png format as an image. This class employs the Singleton Design Pattern,
 * which makes it the focal point, providing a means of data exchange between
 * the other classes.
 * 
 * @author  BURAK Firik
 *          Debugging Enterprises
 * @version 1.0
 */
public class Poseur
{
    // THIS SINGLETON WILL ONLY BE CONSTRUCTED ONCE, BUT WILL
    // BE AVAILABLE TO ALL CLASSES
    private static Poseur singleton = null;

    // THIS WILL STORE ALL THE DATA IN USE FOR THE
    // POSE THE USER IS CURRENTLY EDITING
    private PoseurStateManager poseurStateManager;
    
    private SpriteFileManager spriteFileMamager;
    
    // THIS WILL MANAGE ALL THE FILE I/O FOR LOADING
    // AND SAVING POSES THAT THE USER HAS CREATED
    private PoseurFileManager fileManager;
    
    // THIS WILL STORE THE ENTIRE GUI, INCLUDING ALL COMPONENTS,
    // AND REFERENCES TO THE RENDERER AND EVENT HANDLERS
    private PoseurGUI gui;
    
    // WE CAN USE THIS FOR DEBUGGING PURPOSES BY RENDERING 
    // TEXT ON THE SCREEN WHILE TESTING OUR APP
    private ArrayList<String> debugText;
    private boolean debugTextEnabled;
    
    private AnimatedSpriteViewer viewer;

    // THIS IS A SINGLETON OBJECT, SO WE HAVE A CONSTRUCTOR THAT
    // IS PRIVATE THAT DOES NOTHING
    private Poseur() {}
    
    /**
     * When called, this init method will fully initialize
     * the application, including loading the application settings from an
     * xml file and using that to setup the window. Notice that the constructor
     * is a singleton object, so we make the constructor private to prevent
     * misuse. This method has the obligation of initialization of the app.
     */
    public void init()
    {
        // INITIALIZE THE DATA MANAGER
        poseurStateManager = new PoseurStateManager();
            
        // INITIALIZE THE FILE MANAGER
        fileManager = new PoseurFileManager();

        // INITALIZE THE GUI
        gui = new PoseurGUI();
        
        // WE'LL USE THIS FOR DEBUGGING
        debugText = new ArrayList();
        debugTextEnabled = DEFAULT_DEBUG_TEXT_ENABLED;

        // AND LOAD THE WINDOW SETTINGS FROM AN XML FILE
        PoseurGUILoader pgl = new PoseurGUILoader();
        
        //Animatated sprite viewer
        viewer=new AnimatedSpriteViewer();
        
        spriteFileMamager=new SpriteFileManager();
        
        pgl.initWindow(gui, WINDOW_SETINGS_XML);
    }

    /**
     * This is an important method to understand. This method initialize
     * the singleton object, making sure it is only done the first time
     * this method is called. From there on out, anyone can access this
     * Poseur by calling this method.
     * 
     * @return The singleton Poseur used for this application.
     */
    public static Poseur getPoseur()
    {
        // ONLY CONSTRUCT THE SINGLETON THE FIRST TIME
        if (singleton == null)
        {
            singleton = new Poseur();
        }
        
        // GET THE SINGLETON NO MATTER WHAT
        return singleton;
    }

    // ACCESSOR METHODS
    
    /**
     * Accessor method for getting the application's GUI. Note that since
     * this is a singleton, anyone can get access to the GUI.
     * 
     * @return The PoseurFrame for this application, which is the window
     * for this application and contains references to all components and
     * event handlers in use.
     */
    public PoseurGUI getGUI() { return gui; }
    
    /**
     * Accessor method for getting this application's state manager. Note
     * that since this is a singleton, anyone can get access to the state.
     * 
     * @return The PoseurDataManager for this application, which contains
     * all the data for the pose being edited.
     */
    public PoseurStateManager getStateManager() { return poseurStateManager; }
    
    /**
     * Accessor method for getting this application's file manager. Note
     * that since this is a singleton, anyone can get access to this object.
     */
    public PoseurFileManager getFileManager() { return fileManager; }
    
    
    /**
     * Accessor method for getting this application's rendering panel 
     * which is the left panel of the editor.
     */
    public AnimatedSpriteViewer getSpriteViewer(){return viewer;}
    
    /**
     * Accessor method for getting this application's SpriteFile Manager  
     * which is the left panel of the editor.
     */
    public SpriteFileManager getSpriteFileManager(){return spriteFileMamager;}
    /**
     * This accessor method gets an iterator for going through
     * all the current debug text sequentially.
     * 
     * @return An iterator for the debug text list.
     */
    public Iterator<String> getDebugTextIterator() { return debugText.iterator(); }
    
    /**
     * Accessor method for testing if the debug text is enabled. If true,
     * the text will be rendered to the canvas, if false, it will not.
     * 
     * @return true if the debug text rendering is enabled, false otherwise.
     */
    public boolean isDebugTextEnabled() { return debugTextEnabled; }

    /**
     * Adds the text argument to the debugText list, which means
     * that it will be rendered to the screen. This is good for
     * getting feedback in a graphical application.
     * 
     * @param text The text to be rendered.
     */
    public void addDebugText(String text)
    {
        debugText.add(text);
    }

    /**
     * This method removes all debug text, and will typically be
     * done after each time we clear the canvases.
     */
    public void clearDebugText()
    {
        debugText.clear();
    }

    /**
     * Entry point for the Poseur application, code starts its execution
     * here. This method simply inits the Poseur singleton and starts up
     * the GUI.
     * 
     * @param args Command line arguments are not used by this application.
     */
    public static void main(String[] args)
    {
        // HERE IS THE CONSTRUCTION OF THE SINGLETON Poseur OBJECT,
        // SINCE THIS IS THE FIRST REFERENCE TO getPoseur. THE CONSTRUCTOR
        // WILL FULLY SETUP THE Poseur DATA AND GUI FOR USE.
        Poseur app = Poseur.getPoseur();
        app.init();
        
        // GET THE GUI AND START IT UP
        PoseurGUI window = app.getGUI();
        window.setVisible(true);
    }
}