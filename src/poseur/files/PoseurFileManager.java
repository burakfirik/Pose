package poseur.files;

import animated_sprite_viewer.AnimatedSpriteViewer;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import poseur.Poseur;
import static poseur.PoseurSettings.*;
import poseur.gui.PoseurGUI;
import poseur.state.PoseurState;
import poseur.state.PoseurStateManager;
import sprite_renderer.Pose;
import sprite_renderer.PoseList;
import sprite_renderer.SpriteType;

/**
 * This class provides all the file servicing for the Poseur application. This
 * means it directs all operations regarding loading, exporting, creating, and
 * saving files, Note that it employs use of PoseIO for the actual file work, 
 * this class manages when to actually read and write from/to files, prompting
 * the user when necessary for file names and validation on actions.
 * 
 * @author  Burak
 *          Debugging Enterprises
 * @version 1.0
 */
public class PoseurFileManager 
{
    // WE'LL STORE THE FILE CURRENTLY BEING WORKED ON
    // AND THE NAME OF THE FILE
    private File currentFile;
    private String currentPoseName;
    private String currentFileName;
    
    // WE WANT TO KEEP TRACK OF WHEN SOMETHING HAS NOT BEEN SAVED
    private boolean saved;
    
    // THIS GUY KNOWS HOW TO READ, WRITE, AND EXPORT POESS
    private PoseIO poseIO;
    
    /**
     * This default constructor starts the program without a
     * pose file being edited.
     */
    public PoseurFileManager()
    {
        // NOTHING YET
        currentFile = null;
        currentFileName = null;
        saved = true;
        poseIO = new PoseIO();
    }
    
    /**
     * This method starts the process of editing a new pose. If
     * a pose is already being edited, it will prompt the user
     * to save it first.
     */
    public void requestNewPose()
    {
        // WE MAY HAVE TO SAVE CURRENT WORK
        boolean continueToMakeNew = true;
        if (!saved)
        {
            // THE USER CAN OPT OUT HERE WITH A CANCEL
            continueToMakeNew = promptToSave();
        }
        
        // IF THE USER REALLY WANTS TO MAKE A NEW POSE
        if (continueToMakeNew)
        {
            // GO AHEAD AND PROCEED MAKING A NEW POSE
            continueToMakeNew = promptForNew();

            if (continueToMakeNew)
            {
                // NOW THAT WE'VE SAVED, LET'S MAKE SURE WE'RE IN THE RIGHT MODE
                PoseurStateManager poseurStateManager = Poseur.getPoseur().getStateManager();
                poseurStateManager.resetState();
                poseurStateManager.setState(PoseurState.SELECT_SHAPE_STATE);
            }
        }
    }
    
    /**
     * This method lets the user open a pose saved
     * to a file. It will also make sure data for the
     * current pose is not lost.
     */
    public void requestOpenPose()
    {
        // WE MAY HAVE TO SAVE CURRENT WORK
        boolean continueToOpen = true;
        if (!saved)
        {
            // THE USER CAN OPT OUT HERE WITH A CANCEL
            continueToOpen = promptToSave();
        }
        
        // IF THE USER REALLY WANTS TO OPEN A POSE
        if (continueToOpen)
        {
            // GO AHEAD AND PROCEED MAKING A NEW POSE
            promptToOpen();
        }
    }
    
    /**
     * This method will save the current pose to a file. Note that 
     * we already know the name of the file, so we won't need to
     * prompt the user.
     */
    public void requestSavePose()
    {
        
        // DON'T ASK, JUST SAVE
       // boolean savedSuccessfully = poseIO.savePose(currentFile);
        boolean savedSuccessfully = poseIO.savePose(currentFile);
        if (savedSuccessfully)
        {
            // MARK IT AS SAVED
            saved = true;
            
            // AND REFRESH THE GUI
            Poseur.getPoseur().getGUI().updateMode();
            
        }
    }
    
    /**
     * This method will save the current pose as a named file provided
     * by the user.
     */
    public void requestSaveAsPose()
    {
        // ASK THE USER FOR A FILE NAME
        promptForNew();
    }
    
    /**
     * This method will export the current pose to an image file.
     */
    public void requestExportPose()
    {
        
        Poseur singleton=Poseur.getPoseur();
        PoseurGUI gui=singleton.getGUI();
        AnimatedSpriteViewer viewer=gui.getAnimametedViewerPanel();
        String spriteTypeName=viewer.getJlist().getSelectedValue().toString();
        String stateName=viewer.getCombo().getSelectedItem().toString();
        // ASK THE USER TO MAKE SURE THEY WANT TO GO AHEAD WITH IT
        
       
        // IF THE USER CLICKED OK, THEN EXPORT THE POSE
        
        {
            poseIO.exportPose(currentPoseName, spriteTypeName);
        }
    }
    
    /**
     * This method will exit the application, making sure the user
     * doesn't lose any data first.
     */
    public void requestExit()
    {
        // WE MAY HAVE TO SAVE CURRENT WORK
        boolean continueToExit = true;
        if (!saved)
        {
            // THE USER CAN OPT OUT HERE
            continueToExit = promptToSave();
        }
        
        // IF THE USER REALLY WANTS TO EXIT THE APP
        if (continueToExit)
        {
            // EXIT THE APPLICATION
            System.exit(0);
        }
    }

    /**
     * This helper method asks the user for a name for the pose about
     * to be created. Note that when the pose is created, a corresponding
     * .pose file is also created.
     * 
     * @return true if the user goes ahead and provides a good name
     * false if they cancel.
     */
    private boolean promptForNew()
    {
        // SO NOW ASK THE USER FOR A POSE NAME
        PoseurGUI gui = Poseur.getPoseur().getGUI();
        AnimatedSpriteViewer viewer=gui.getAnimametedViewerPanel();
        String nameOfSprite;
        String nameOfState;
        String path="./data/sprite_types/";
        try{
           
           
            String spriteTypeName=viewer.getJlist().getSelectedValue().toString();
            String spriteState=viewer.getCombo().getSelectedItem().toString();
            HashMap<String, SpriteType> spriteTypes=viewer.getSpriteTypes();
            SpriteType spriteType=spriteTypes.get(spriteTypeName);
            String xsdFile=SPRITES_DATA_PATH+SPRITE_TYPE_SCHEMA_FILE;
            int size=0;
            
            Iterator<String>states=spriteType.getAnimationStates();
            
            while(states.hasNext()){
                spriteState=states.next();
                PoseList poseList=spriteType.getPoseList(spriteState);
                Iterator <Pose>poseIterator=poseList.getPoseIterator();
                
            for(int i=0;i<poseList.getNumOfPoses();i++){
                Pose p1=poseIterator.next();
                int id=p1.getImageID();
                if(id==1000){
                    size=0;
                    break;
                }else{
                    //size=spriteType.getSpriteImages().size();
                }
                if(id>size){
                    size=id;
                }
            }
            }
            spriteState=viewer.getCombo().getSelectedItem().toString();
            size++;
            path=path+spriteTypeName+"/"+spriteTypeName+"_"+(spriteState+"_"+size).toUpperCase() +".pose";
            gui.setID(size);
            size=0;
            SpriteType sprite=viewer.getSpriteTypes().get(spriteTypeName);
            
        }catch(NullPointerException e){
        
        }
        
        
        // IF THE USER CANCELLED, THEN WE'LL GET A fileName
        // OF NULL, SO LET'S MAKE SURE THE USER REALLY
        // WANTS TO DO THIS ACTION BEFORE MOVING ON
       
            // UPDATE THE FILE NAMES AND FILE
          
            currentFile = new File(path);

            // SAVE OUR NEW FILE
            poseIO.savePose(currentFile);
            saved = true;

            // AND PUT THE FILE NAME IN THE TITLE BAR
            String appName = gui.getAppName();
            gui.setTitle(appName + APP_NAME_FILE_NAME_SEPARATOR + currentFile); 
            
            JOptionPane.showConfirmDialog(viewer, "New Pose Editor Canvas Created");
            
       return true;
    }

    /**
     * This helper method verifies that the user really wants to save their
     * unsaved work, which they might not want to do. Note that it could be
     * used in multiple contexts before doing other actions, like creating a
     * new pose, or opening another pose, or exiting. Note that the user will 
     * be presented with 3 options: YES, NO, and CANCEL. YES means the user 
     * wants to save their work and continue the other action (we return true
     * to denote this), NO means don't save the work but continue with the
     * other action (true is returned), CANCEL means don't save the work and
     * don't continue with the other action (false is retuned).
     * 
     * @return true if the user presses the YES option to save, true if the user
     * presses the NO option to not save, false if the user presses the CANCEL
     * option to not continue.
     */
    private boolean promptToSave()
    {
       // PROMPT THE USER TO SAVE UNSAVED WORK
        PoseurGUI gui = Poseur.getPoseur().getGUI();
        int selection =JOptionPane.showOptionDialog(gui, 
                    PROMPT_TO_SAVE_TEXT, PROMPT_TO_SAVE_TITLE_TEXT, 
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, 
                    null, null, null);
        
        // IF THE USER SAID YES, THEN SAVE BEFORE MOVING ON
        if (selection == JOptionPane.YES_OPTION)
        {
            poseIO.savePose(currentFile);
            saved = true;
        }
        
        // IF THE USER SAID CANCEL, THEN WE'LL TELL WHOEVER
        // CALLED THIS THAT THE USER IS NOT INTERESTED ANYMORE
        else if (selection == JOptionPane.CANCEL_OPTION)
        {
            return false;
        }

        // IF THE USER SAID NO, WE JUST GO ON WITHOUT SAVING
        // BUT FOR BOTH YES AND NO WE DO WHATEVER THE USER
        // HAD IN MIND IN THE FIRST PLACE
        return true;
    }
    
    /**
     * This helper method asks the user for a file to open. The
     * user-selected file is then loaded and the GUI updated. Note
     * that if the user cancels the open process, nothing is done.
     * If an error occurs loading the file, a message is displayed,
     * but nothing changes.
     */
    private void promptToOpen()
    {
        // WE'LL NEED THE GUI
        Poseur singleton = Poseur.getPoseur();
        PoseurGUI gui = singleton.getGUI();
        
        // AND NOW ASK THE USER FOR THE POSE TO OPEN
        JFileChooser poseFileChooser = new JFileChooser(POSES_PATH);
        int buttonPressed = poseFileChooser.showOpenDialog(gui);
        
        // ONLY OPEN A NEW FILE IF THE USER SAYS OK
        if (buttonPressed == JFileChooser.APPROVE_OPTION)
        {
            // GET THE FILE THE USER ENTERED
            currentFile = poseFileChooser.getSelectedFile();
            currentFileName = currentFile.getName();
            currentPoseName = currentFileName.substring(0, currentFileName.indexOf("."));
            saved = true;
 
            // AND PUT THE FILE NAME IN THE TITLE BAR
            String appName = gui.getAppName();
            gui.setTitle(appName + APP_NAME_FILE_NAME_SEPARATOR + currentFile);             
            
            // AND LOAD THE .pose (XML FORMAT) FILE
            poseIO.loadPose(currentFile.getAbsolutePath());
        }
    }
    
    /**
     * This helper method asks the user for a file to open. The
     * user-selected file is then loaded and the GUI updated. Note
     * that if the user cancels the open process, nothing is done.
     * If an error occurs loading the file, a message is displayed,
     * but nothing changes.
     */
    private void promptToOpen(int i)
    {
        // WE'LL NEED THE GUI
        Poseur singleton = Poseur.getPoseur();
        PoseurGUI gui = singleton.getGUI();
        AnimatedSpriteViewer view=gui.getAnimametedViewerPanel();
        
        String state=view.getCombo().getSelectedItem().toString();
        String spriteType=view.getJlist().getSelectedValue().toString();
        
       
        
       
            // GET THE FILE THE USER ENTERED
            currentFile = new File("./data/sprite_types/"+spriteType+"/"+spriteType+"_"+state.toUpperCase()+"_"+i+".pose");
            currentFileName = currentFile.getName();
            currentPoseName = currentFileName.substring(0, currentFileName.indexOf("."));
            saved = true;
 
            // AND PUT THE FILE NAME IN THE TITLE BAR
            String appName = gui.getAppName();
            gui.setTitle(appName + APP_NAME_FILE_NAME_SEPARATOR + currentFile);             
            
            // AND LOAD THE .pose (XML FORMAT) FILE
            poseIO.loadPose(currentFile.getAbsolutePath());
        
    }
    
    
    
    /**
     * This mutator method marks the file as not saved, which means that
     * when the user wants to do a file-type operation, we should prompt
     * the user to save current work first. Note that this method should
     * be called any time the pose is changed in some way.
     */
    public void markFileAsNotSaved()
    {
        saved = false;
    }

    /**
     * Accessor method for checking to see if the current pose has been
     * saved since it was last editing. If the current file matches
     * the pose data, we'll return true, otherwise false.
     * 
     * @return true if the current pose is saved to the file, false otherwise.
     */
    public boolean isSaved()
    {
        return saved;
    }

    public void requestOpenPose(int id) {
     
        // WE MAY HAVE TO SAVE CURRENT WORK
        boolean continueToOpen = true;
        if (!saved)
        {
            // THE USER CAN OPT OUT HERE WITH A CANCEL
            continueToOpen = promptToSave();
        }
        
        // IF THE USER REALLY WANTS TO OPEN A POSE
        if (continueToOpen)
        {
            // GO AHEAD AND PROCEED MAKING A NEW POSE
            promptToOpen(id);
        }
    
    }

    public void requestSavePose(int id) {
        
        // DON'T ASK, JUST SAVE
       // boolean savedSuccessfully = poseIO.savePose(currentFile);
        boolean savedSuccessfully = poseIO.savePose(id);
        if (savedSuccessfully)
        {
            // MARK IT AS SAVED
            saved = true;
            
            // AND REFRESH THE GUI
            Poseur.getPoseur().getGUI().updateMode();
            
        }
    }

    public void requestExportPose(int id)
    {
        
        Poseur singleton=Poseur.getPoseur();
        PoseurGUI gui=singleton.getGUI();
        AnimatedSpriteViewer viewer=gui.getAnimametedViewerPanel();
        String spriteTypeName=viewer.getJlist().getSelectedValue().toString();
        String stateName=viewer.getCombo().getSelectedItem().toString();
        // ASK THE USER TO MAKE SURE THEY WANT TO GO AHEAD WITH IT
        
        /*
        int selection = JOptionPane.showOptionDialog(   gui, 
                spriteTypeName+"_"+(stateName+"_"+1).toUpperCase()+ POSE_FILE_EXTENSION,
                EXPORT_POSE_TITLE_TEXT, 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, null, null);
       
        */
       
        // IF THE USER CLICKED OK, THEN EXPORT THE POSE
        //if (selection == JOptionPane.OK_OPTION)
        {
            poseIO.exportPose(currentPoseName, spriteTypeName, id);
        }
    }

    public void requestDeletePose() {
        Poseur singleton=Poseur.getPoseur();
        PoseurGUI gui=singleton.getGUI();
        AnimatedSpriteViewer viewer=gui.getAnimametedViewerPanel();
        String spriteTypeName=viewer.getJlist().getSelectedValue().toString();
        String stateName=viewer.getCombo().getSelectedItem().toString();
        // ASK THE USER TO MAKE SURE THEY WANT TO GO AHEAD WITH IT
        
        
   
        poseIO.deletePose(currentPoseName, gui.getID());
        
    }
}