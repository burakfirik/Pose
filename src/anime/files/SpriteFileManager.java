/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package anime.files;

import animated_sprite_viewer.AnimatedSpriteViewer;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import poseur.Poseur;
import poseur.files.PoseIO;
import poseur.gui.PoseurGUI;
import poseur.state.PoseurState;
import poseur.state.PoseurStateManager;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import poseur.Poseur;
import static poseur.PoseurSettings.*;
import poseur.gui.PoseurGUI;
import poseur.state.PoseurState;
import poseur.state.PoseurStateManager;


/**
 * This class provides all the file servicing for the Poseur application. This
 * means it directs all operations regarding loading, exporting, creating, and
 * saving files, Note that it employs use of PoseIO for the actual file work, 
 * this class manages when to actually read and write from/to files, prompting
 * the user when necessary for file names and validation on actions.
 * 
 * @author  Burak Firik
 *          Debugging Enterprises
 * @version 1.0
 */
public class SpriteFileManager {
    




    // WE'LL STORE THE FILE CURRENTLY BEING WORKED ON
    // AND THE NAME OF THE FILE
    private File currentFile;
    private String currentSpriteName;
    private String currentFileName;
    
    // WE WANT TO KEEP TRACK OF WHEN SOMETHING HAS NOT BEEN SAVED
    private boolean saved;
    
    // THIS GUY KNOWS HOW TO READ, WRITE, AND EXPORT POESS
    private SpriteIO spriteIO;
    
    /**
     * This default constructor starts the program without a
     * pose file being edited.
     */
    public SpriteFileManager()
    {
        // NOTHING YET
        currentFile = null;
        currentFileName = null;
        saved = true;
        spriteIO = new SpriteIO();
    }
    
    /**
     * This method starts the process of editing a new pose. If
     * a pose is already being edited, it will prompt the user
     * to save it first.
     */
    public void requestNewSprite()
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
                /*
                // NOW THAT WE'VE SAVED, LET'S MAKE SURE WE'RE IN THE RIGHT MODE
                PoseurStateManager poseurStateManager = Poseur.getPoseur().getStateManager();
                poseurStateManager.resetState();
                poseurStateManager.setState(PoseurState.SELECT_SHAPE_STATE);
                */
            }
        }
    }
    
    /**
     * This method lets the user open a sprite saved
     * to a file. It will also make sure data for the
     * current pose is not lost.
     */
    public void requestOpenSprite()
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
     * This method will save the current sprite to a file. Note that 
     * we already know the name of the file, so we won't need to
     * prompt the user.
     */
    public void requestSaveSprite()
    {
        String path= "./data/sprite_types/"+"/"+currentFile.getName();
        File currentFileFolder=new File(path+"/"+currentFile.getName());
        // DON'T ASK, JUST SAVE
        boolean savedSuccessfully = spriteIO.saveSprite(currentFile,currentFileFolder);
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
    public void requestSaveAsSprite()
    {
        // ASK THE USER FOR A FILE NAME
        promptForNew();
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
     * This helper method asks the user for a name for the sprite about
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
        String fileName = JOptionPane.showInputDialog(
                gui,
                SPRITE_NAME_REQUEST_TEXT,
                SPRITE_NAME_REQUEST_TITLE_TEXT,
                JOptionPane.QUESTION_MESSAGE);
        
        // IF THE USER CANCELLED, THEN WE'LL GET A fileName
        // OF NULL, SO LET'S MAKE SURE THE USER REALLY
        // WANTS TO DO THIS ACTION BEFORE MOVING ON
        if ((fileName != null)
                &&
            (fileName.length() > 0))
        {
            // UPDATE THE FILE NAMES AND FILE
            currentSpriteName = fileName;
            currentFileName = fileName + SPRITE_FILE_EXTENSION; 
            currentFile = new File(SPRITES_PATH + currentFileName);

            // SAVE OUR NEW FILE
            //spriteIO.saveSprite(currentFile);
            String path= "./data/sprite_types/"+"/"+fileName;
            File currentFile1 = new File(path);
            currentFile1.mkdirs();
            File currentFile3=new File(path+"/"+currentFileName);
            spriteIO.saveSprite(currentFile, currentFile3);
            //spriteIO.saveSprite(currentFile3);
            saved = true;
            
            // AND PUT THE FILE NAME IN THE TITLE BAR
            String appName = gui.getAppName();
            gui.setTitle(appName + APP_NAME_FILE_NAME_SEPARATOR + currentFile); 
            Poseur singleton=Poseur.getPoseur();
            singleton.getGUI().loadSprite(currentFileName);
            
            // WE DID IT!
            return true;
        }
        // USER DECIDED AGAINST IT
        return false;
    }
    
    
    /**
     * This helper method asks the user for a name for the sprite about
     * to be created. Note that when the pose is created, a corresponding
     * .pose file is also created.
     * 
     * @return true if the user goes ahead and provides a good name
     * false if they cancel.
     */
    private boolean promptForNewState()
    {
        // SO NOW ASK THE USER FOR A POSE NAME
        PoseurGUI gui = Poseur.getPoseur().getGUI();
        String fileName = JOptionPane.showInputDialog(
                gui,
                SPRITE_STATE_REQUEST_TEXT,
                SPRITE_NAME_REQUEST_TITLE_TEXT,
                JOptionPane.QUESTION_MESSAGE);
        
        // IF THE USER CANCELLED, THEN WE'LL GET A fileName
        // OF NULL, SO LET'S MAKE SURE THE USER REALLY
        // WANTS TO DO THIS ACTION BEFORE MOVING ON
        if ((fileName != null)
                &&
            (fileName.length() > 0))
        {
            // UPDATE THE FILE NAMES AND FILE
            currentSpriteName = fileName;
            currentFileName = fileName + SPRITE_FILE_EXTENSION; 
            currentFile = new File(SPRITES_PATH + currentFileName);

            // SAVE OUR NEW FILE
            //spriteIO.saveSprite(currentFile);
            String path= "./data/sprite_types/"+"/"+fileName;
            
            File currentFile3=new File(path+"/"+currentFileName);
            spriteIO.saveState(fileName);
            //spriteIO.saveSprite(currentFile3);
            saved = true;
            
            // AND PUT THE FILE NAME IN THE TITLE BAR
            String appName = gui.getAppName();
            gui.setTitle(appName + APP_NAME_FILE_NAME_SEPARATOR + currentFile); 
            Poseur singleton=Poseur.getPoseur();
            //singleton.getGUI().loadSprite(currentFileName);
            
            // WE DID IT!
            return true;
        }
        // USER DECIDED AGAINST IT
        return false;
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
            String path= "./data/sprite_types/"+"/"+currentFile.getName();
            File currentFileFolder=new File(path+"/"+currentFile.getName());
            spriteIO.saveSprite(currentFile,currentFileFolder);
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
    private boolean promptToSaveState()
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
            String path= "./data/sprite_types/"+"/"+currentFile.getName();
            File currentFileFolder=new File(path+"/"+currentFile.getName());
            spriteIO.saveSprite(currentFile,currentFileFolder);
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
            currentSpriteName = currentFileName.substring(0, currentFileName.indexOf("."));
            saved = true;
 
            // AND PUT THE FILE NAME IN THE TITLE BAR
            String appName = gui.getAppName();
            gui.setTitle(appName + APP_NAME_FILE_NAME_SEPARATOR + currentFile);             
            
            // AND LOAD THE .pose (XML FORMAT) FILE
            spriteIO.loadSprite(currentFile.getAbsolutePath());
        }
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

    
    public void requestNewState() {
         // WE MAY HAVE TO SAVE CURRENT WORK
        boolean continueToMakeNew = true;
        if (!saved)
        {
            // THE USER CAN OPT OUT HERE WITH A CANCEL
            continueToMakeNew = promptToSaveState();
        }
        
        // IF THE USER REALLY WANTS TO MAKE A NEW POSE
        if (continueToMakeNew)
        {
            // GO AHEAD AND PROCEED MAKING A NEW POSE
            continueToMakeNew = promptForNewState();

            if (continueToMakeNew)
            {
                /*
                // NOW THAT WE'VE SAVED, LET'S MAKE SURE WE'RE IN THE RIGHT MODE
                PoseurStateManager poseurStateManager = Poseur.getPoseur().getStateManager();
                poseurStateManager.resetState();
                poseurStateManager.setState(PoseurState.SELECT_SHAPE_STATE);
                */
            }
        }
    }
    
    
    
    public void requestDeleteState() {
         // WE MAY HAVE TO SAVE CURRENT WORK
        boolean continueToMakeNew = true;
        if (!saved)
        {
            // THE USER CAN OPT OUT HERE WITH A CANCEL
            continueToMakeNew = promptToSaveState();
        }
        
        // IF THE USER REALLY WANTS TO MAKE A NEW POSE
        if (continueToMakeNew)
        {
            // GO AHEAD AND PROCEED MAKING A NEW POSE
            continueToMakeNew = promptForDeleteState();

            if (continueToMakeNew)
            {
                /*
                // NOW THAT WE'VE SAVED, LET'S MAKE SURE WE'RE IN THE RIGHT MODE
                PoseurStateManager poseurStateManager = Poseur.getPoseur().getStateManager();
                poseurStateManager.resetState();
                poseurStateManager.setState(PoseurState.SELECT_SHAPE_STATE);
                */
            }
        }
    }
    
    
    public void requestDeleteState(int i) {
         // WE MAY HAVE TO SAVE CURRENT WORK
        boolean continueToMakeNew = true;
        if (!saved)
        {
            // THE USER CAN OPT OUT HERE WITH A CANCEL
            continueToMakeNew = promptToSaveState();
        }
        
        // IF THE USER REALLY WANTS TO MAKE A NEW POSE
        if (continueToMakeNew)
        {
            // GO AHEAD AND PROCEED MAKING A NEW POSE
            continueToMakeNew = promptForDeleteState(i);

            if (continueToMakeNew)
            {
                /*
                // NOW THAT WE'VE SAVED, LET'S MAKE SURE WE'RE IN THE RIGHT MODE
                PoseurStateManager poseurStateManager = Poseur.getPoseur().getStateManager();
                poseurStateManager.resetState();
                poseurStateManager.setState(PoseurState.SELECT_SHAPE_STATE);
                */
            }
        }
    }
    

    private boolean promptForDeleteState() {
         // SO NOW ASK THE USER FOR A POSE NAME
        String spriteTypeName=null;
        String selectedState=null;
        PoseurGUI gui = Poseur.getPoseur().getGUI();
        AnimatedSpriteViewer view=gui.getAnimametedViewerPanel();
        
        try{
            //if the selected state either one of the those empty one, throw null pointer exception
            spriteTypeName=view.getJlist().getSelectedValue().toString();
            selectedState=view.getCombo().getSelectedItem().toString();
            if(view.getCombo().getSelectedItem().toString().equals("NULL")||view.getCombo().getSelectedItem().toString().equals("Select Animation State")){
                throw new NullPointerException();
            }
            int input = JOptionPane.showConfirmDialog(gui, "Are you sure you want to delete currently selected state ?");
        }catch(NullPointerException e){
            JOptionPane.showMessageDialog(view,"You did not select either state or sprite type");
            return false;
        }
            // UPDATE THE FILE NAMES AND FILE
            currentSpriteName = spriteTypeName;
            currentFileName = spriteTypeName + SPRITE_FILE_EXTENSION; 
            currentFile = new File(SPRITES_PATH + currentFileName);

            // SAVE OUR NEW FILE
            //spriteIO.saveSprite(currentFile);
            String path= "./data/sprite_types/"+"/"+spriteTypeName;
            
            File currentFile3=new File(path+"/"+currentFileName);
            spriteIO.deleteState(path+"/"+currentFileName);
            
            saved = true;
            
            // AND PUT THE FILE NAME IN THE TITLE BAR
            String appName = gui.getAppName();
            gui.setTitle(appName + APP_NAME_FILE_NAME_SEPARATOR + currentFile); 
            Poseur singleton=Poseur.getPoseur();
            //singleton.getGUI().loadSprite(currentFileName);
            
            // WE DID IT!
           
        // USER DECIDED AGAINST IT
        return false;
    }

    private boolean promptForDeleteState(int i) {
         // SO NOW ASK THE USER FOR A POSE NAME
        String spriteTypeName=null;
        String selectedState=null;
        PoseurGUI gui = Poseur.getPoseur().getGUI();
        AnimatedSpriteViewer view=gui.getAnimametedViewerPanel();
        
        try{
            //if the selected state either one of the those empty one, throw null pointer exception
            spriteTypeName=view.getJlist().getSelectedValue().toString();
            selectedState=view.getCombo().getSelectedItem().toString();
            if(view.getCombo().getSelectedItem().toString().equals("NULL")||view.getCombo().getSelectedItem().toString().equals("Select Animation State")){
                throw new NullPointerException();
            }
           // int input = JOptionPane.showConfirmDialog(gui, "Are you sure you want to delete currently selected state ?");
        }catch(NullPointerException e){
            JOptionPane.showMessageDialog(view,"You did not select either state or sprite type");
            return false;
        }
            // UPDATE THE FILE NAMES AND FILE
            currentSpriteName = spriteTypeName;
            currentFileName = spriteTypeName + SPRITE_FILE_EXTENSION; 
            currentFile = new File(SPRITES_PATH + currentFileName);

            // SAVE OUR NEW FILE
            //spriteIO.saveSprite(currentFile);
            String path= "./data/sprite_types/"+"/"+spriteTypeName;
            
            File currentFile3=new File(path+"/"+currentFileName);
            spriteIO.deleteState(path+"/"+currentFileName, i);
            
            saved = true;
            
            // AND PUT THE FILE NAME IN THE TITLE BAR
            String appName = gui.getAppName();
            gui.setTitle(appName + APP_NAME_FILE_NAME_SEPARATOR + currentFile); 
            Poseur singleton=Poseur.getPoseur();
            //singleton.getGUI().loadSprite(currentFileName);
            
            // WE DID IT!
           
        // USER DECIDED AGAINST IT
        return false;
    }

    
    public void requestRenameState() {
          // WE MAY HAVE TO SAVE CURRENT WORK
        boolean continueToMakeNew = true;
        if (!saved)
        {
            // THE USER CAN OPT OUT HERE WITH A CANCEL
            continueToMakeNew = promptToSaveState();
        }
        
        // IF THE USER REALLY WANTS TO MAKE A NEW POSE
        if (continueToMakeNew)
        {
            // GO AHEAD AND PROCEED MAKING A NEW POSE
            continueToMakeNew = promptForRenameState();

            if (continueToMakeNew)
            {
                /*
                // NOW THAT WE'VE SAVED, LET'S MAKE SURE WE'RE IN THE RIGHT MODE
                PoseurStateManager poseurStateManager = Poseur.getPoseur().getStateManager();
                poseurStateManager.resetState();
                poseurStateManager.setState(PoseurState.SELECT_SHAPE_STATE);
                */
            }
        }
    }
    
    
    

    public void requestRenameStateFor(String name) {
          // WE MAY HAVE TO SAVE CURRENT WORK
        boolean continueToMakeNew = true;
        if (!saved)
        {
            // THE USER CAN OPT OUT HERE WITH A CANCEL
            continueToMakeNew = promptToSaveState();
        }
        
        // IF THE USER REALLY WANTS TO MAKE A NEW POSE
        if (continueToMakeNew)
        {
            // GO AHEAD AND PROCEED MAKING A NEW POSE
            continueToMakeNew = promptForRenameStateFor(name);

            if (continueToMakeNew)
            {
                /*
                // NOW THAT WE'VE SAVED, LET'S MAKE SURE WE'RE IN THE RIGHT MODE
                PoseurStateManager poseurStateManager = Poseur.getPoseur().getStateManager();
                poseurStateManager.resetState();
                poseurStateManager.setState(PoseurState.SELECT_SHAPE_STATE);
                */
            }
        }
    }
    
    private boolean promptForRenameState() {
        // SO NOW ASK THE USER FOR A POSE NAME
        String spriteTypeName=null;
        String selectedState=null;
        PoseurGUI gui = Poseur.getPoseur().getGUI();
        AnimatedSpriteViewer view=gui.getAnimametedViewerPanel();
        
        try{
            //if the selected state either one of the those empty one, throw null pointer exception
            spriteTypeName=view.getJlist().getSelectedValue().toString();
            selectedState=view.getCombo().getSelectedItem().toString();
            if(view.getCombo().getSelectedItem().toString().equals("NULL")||view.getCombo().getSelectedItem().toString().equals("Select Animation State")){
                throw new NullPointerException();
            }
            int input = JOptionPane.showConfirmDialog(gui, "Are you sure you want to rename currently selected state ?");
        }catch(NullPointerException e){
            JOptionPane.showMessageDialog(view,"You did not select either state or sprite type");
            return false;
        }
            // UPDATE THE FILE NAMES AND FILE
            currentSpriteName = spriteTypeName;
            currentFileName = spriteTypeName + SPRITE_FILE_EXTENSION; 
            currentFile = new File(SPRITES_PATH + currentFileName);

            // SAVE OUR NEW FILE
            //spriteIO.saveSprite(currentFile);
            String path= "./data/sprite_types/"+spriteTypeName;
            
            File currentFile3=new File(path+"/"+currentFileName);
            spriteIO.renameState(path+"/"+currentFileName);
            
            saved = true;
            
            // AND PUT THE FILE NAME IN THE TITLE BAR
            String appName = gui.getAppName();
            gui.setTitle(appName + APP_NAME_FILE_NAME_SEPARATOR + currentFile); 
            Poseur singleton=Poseur.getPoseur();
            //singleton.getGUI().loadSprite(currentFileName);
            
            // WE DID IT!
           
        // USER DECIDED AGAINST IT
        return false;
    }
    
    private boolean promptForRenameStateFor(String nameState) {
        // SO NOW ASK THE USER FOR A POSE NAME
        String spriteTypeName=null;
        String selectedState=null;
        PoseurGUI gui = Poseur.getPoseur().getGUI();
        AnimatedSpriteViewer view=gui.getAnimametedViewerPanel();
        
        try{
            //if the selected state either one of the those empty one, throw null pointer exception
            spriteTypeName=view.getJlist().getSelectedValue().toString();
            selectedState=view.getCombo().getSelectedItem().toString();
            if(view.getCombo().getSelectedItem().toString().equals("NULL")||view.getCombo().getSelectedItem().toString().equals("Select Animation State")){
                throw new NullPointerException();
            }
           // int input = JOptionPane.showConfirmDialog(gui, "Are you sure you want to rename currently selected state ?");
        }catch(NullPointerException e){
            JOptionPane.showMessageDialog(view,"You did not select either state or sprite type");
            return false;
        }
            // UPDATE THE FILE NAMES AND FILE
            currentSpriteName = spriteTypeName;
            currentFileName = spriteTypeName + SPRITE_FILE_EXTENSION; 
            currentFile = new File(SPRITES_PATH + currentFileName);

            // SAVE OUR NEW FILE
            //spriteIO.saveSprite(currentFile);
            String path= "./data/sprite_types/"+spriteTypeName;
            
            File currentFile3=new File(path+"/"+currentFileName);
            spriteIO.renameStateFor(path+"/"+currentFileName,nameState);
            
            saved = true;
            
            // AND PUT THE FILE NAME IN THE TITLE BAR
            String appName = gui.getAppName();
            gui.setTitle(appName + APP_NAME_FILE_NAME_SEPARATOR + currentFile); 
            Poseur singleton=Poseur.getPoseur();
            //singleton.getGUI().loadSprite(currentFileName);
            
            // WE DID IT!
           
        // USER DECIDED AGAINST IT
        return false;
    }

    public void requestDuplicateState() {
            // WE MAY HAVE TO SAVE CURRENT WORK
        boolean continueToMakeNew = true;
        if (!saved)
        {
            // THE USER CAN OPT OUT HERE WITH A CANCEL
            continueToMakeNew = promptToSaveState();
        }
        
        // IF THE USER REALLY WANTS TO MAKE A NEW POSE
        if (continueToMakeNew)
        {
            // GO AHEAD AND PROCEED MAKING A NEW POSE
            continueToMakeNew = promptForDuplicateState();

            if (continueToMakeNew)
            {
                /*
                // NOW THAT WE'VE SAVED, LET'S MAKE SURE WE'RE IN THE RIGHT MODE
                PoseurStateManager poseurStateManager = Poseur.getPoseur().getStateManager();
                poseurStateManager.resetState();
                poseurStateManager.setState(PoseurState.SELECT_SHAPE_STATE);
                */
            }
        }
    }
    
    public void requestDuplicateState(String i) {
            // WE MAY HAVE TO SAVE CURRENT WORK
        boolean continueToMakeNew = true;
        if (!saved)
        {
            // THE USER CAN OPT OUT HERE WITH A CANCEL
            continueToMakeNew = promptToSaveState();
        }
        
        // IF THE USER REALLY WANTS TO MAKE A NEW POSE
        if (continueToMakeNew)
        {
            // GO AHEAD AND PROCEED MAKING A NEW POSE
            continueToMakeNew = promptForDuplicateState(i);

            if (continueToMakeNew)
            {
                /*
                // NOW THAT WE'VE SAVED, LET'S MAKE SURE WE'RE IN THE RIGHT MODE
                PoseurStateManager poseurStateManager = Poseur.getPoseur().getStateManager();
                poseurStateManager.resetState();
                poseurStateManager.setState(PoseurState.SELECT_SHAPE_STATE);
                */
            }
        }
    }
    

    private boolean promptForDuplicateState() {
         // SO NOW ASK THE USER FOR A POSE NAME
        String spriteTypeName=null;
        String selectedState=null;
        PoseurGUI gui = Poseur.getPoseur().getGUI();
        AnimatedSpriteViewer view=gui.getAnimametedViewerPanel();
        
        try{
            //if the selected state either one of the those empty one, throw null pointer exception
            spriteTypeName=view.getJlist().getSelectedValue().toString();
            selectedState=view.getCombo().getSelectedItem().toString();
            if(view.getCombo().getSelectedItem().toString().equals("NULL")||view.getCombo().getSelectedItem().toString().equals("Select Animation State")){
                throw new NullPointerException();
            }
            int input = JOptionPane.showConfirmDialog(gui, "Are you sure you want to duplicate currently selected state ?");
        }catch(NullPointerException e){
            JOptionPane.showMessageDialog(view,"You did not select either state or sprite type");
            return false;
        }
            // UPDATE THE FILE NAMES AND FILE
            currentSpriteName = spriteTypeName;
            currentFileName = spriteTypeName + SPRITE_FILE_EXTENSION; 
            currentFile = new File(SPRITES_PATH + currentFileName);

            // SAVE OUR NEW FILE
            //spriteIO.saveSprite(currentFile);
            String path= "./data/sprite_types/"+spriteTypeName;
            
            File currentFile3=new File(path+"/"+currentFileName);
            spriteIO.duplicateState(path+"/"+currentFileName);
            
            saved = true;
            
            // AND PUT THE FILE NAME IN THE TITLE BAR
            String appName = gui.getAppName();
            gui.setTitle(appName + APP_NAME_FILE_NAME_SEPARATOR + currentFile); 
            Poseur singleton=Poseur.getPoseur();
            //singleton.getGUI().loadSprite(currentFileName);
            
            // WE DID IT!
           
        // USER DECIDED AGAINST IT
        return false;
    }
    
    
    private boolean promptForDuplicateState(String i) {
         // SO NOW ASK THE USER FOR A POSE NAME
        String spriteTypeName=null;
        String selectedState=null;
        PoseurGUI gui = Poseur.getPoseur().getGUI();
        AnimatedSpriteViewer view=gui.getAnimametedViewerPanel();
        
        try{
            //if the selected state either one of the those empty one, throw null pointer exception
            spriteTypeName=view.getJlist().getSelectedValue().toString();
            selectedState=view.getCombo().getSelectedItem().toString();
            if(view.getCombo().getSelectedItem().toString().equals("NULL")||view.getCombo().getSelectedItem().toString().equals("Select Animation State")){
                throw new NullPointerException();
            }
           
        }catch(NullPointerException e){
            JOptionPane.showMessageDialog(view,"You did not select either state or sprite type");
            return false;
        }
            // UPDATE THE FILE NAMES AND FILE
            currentSpriteName = spriteTypeName;
            currentFileName = spriteTypeName + SPRITE_FILE_EXTENSION; 
            currentFile = new File(SPRITES_PATH + currentFileName);

            // SAVE OUR NEW FILE
            //spriteIO.saveSprite(currentFile);
            String path= "./data/sprite_types/"+spriteTypeName;
            
            File currentFile3=new File(path+"/"+currentFileName);
            spriteIO.duplicateState(path+"/"+currentFileName,i);
            
            saved = true;
            
            // AND PUT THE FILE NAME IN THE TITLE BAR
            String appName = gui.getAppName();
            gui.setTitle(appName + APP_NAME_FILE_NAME_SEPARATOR + currentFile); 
            Poseur singleton=Poseur.getPoseur();
            //singleton.getGUI().loadSprite(currentFileName);
            
            // WE DID IT!
           
        // USER DECIDED AGAINST IT
        return false;
    }
    

    public void requestSetDuration() {
             // WE MAY HAVE TO SAVE CURRENT WORK
        boolean continueToMakeNew = true;
        if (!saved)
        {
            // THE USER CAN OPT OUT HERE WITH A CANCEL
            continueToMakeNew = promptToSaveState();
        }
        
        // IF THE USER REALLY WANTS TO MAKE A NEW POSE
        if (continueToMakeNew)
        {
            // GO AHEAD AND PROCEED MAKING A NEW POSE
            continueToMakeNew = promptForSetDuration();

            if (continueToMakeNew)
            {
                /*
                // NOW THAT WE'VE SAVED, LET'S MAKE SURE WE'RE IN THE RIGHT MODE
                PoseurStateManager poseurStateManager = Poseur.getPoseur().getStateManager();
                poseurStateManager.resetState();
                poseurStateManager.setState(PoseurState.SELECT_SHAPE_STATE);
                */
            }
        }
    }

    private boolean promptForSetDuration() {
          // SO NOW ASK THE USER FOR A POSE NAME
        String spriteTypeName=null;
        String selectedState=null;
        PoseurGUI gui = Poseur.getPoseur().getGUI();
        AnimatedSpriteViewer view=gui.getAnimametedViewerPanel();
        
        try{
            //if the selected state either one of the those empty one, throw null pointer exception
            spriteTypeName=view.getJlist().getSelectedValue().toString();
            selectedState=view.getCombo().getSelectedItem().toString();
            if(view.getCombo().getSelectedItem().toString().equals("NULL")||view.getCombo().getSelectedItem().toString().equals("Select Animation State")){
                throw new NullPointerException();
            }
            int input = JOptionPane.showConfirmDialog(gui, "Are you sure you want to rename currently selected state ?");
        }catch(NullPointerException e){
            JOptionPane.showMessageDialog(view,"You did not select either state or sprite type");
            return false;
        }
            // UPDATE THE FILE NAMES AND FILE
            currentSpriteName = spriteTypeName;
            currentFileName = spriteTypeName + SPRITE_FILE_EXTENSION; 
            currentFile = new File(SPRITES_PATH + currentFileName);

            // SAVE OUR NEW FILE
            //spriteIO.saveSprite(currentFile);
            String path= "./data/sprite_types/"+spriteTypeName;
            
            File currentFile3=new File(path+"/"+currentFileName);
            spriteIO.setDuration(gui.getID());
            
            saved = true;
            
            // AND PUT THE FILE NAME IN THE TITLE BAR
            String appName = gui.getAppName();
            gui.setTitle(appName + APP_NAME_FILE_NAME_SEPARATOR + currentFile); 
            Poseur singleton=Poseur.getPoseur();
            //singleton.getGUI().loadSprite(currentFileName);
            
            // WE DID IT!
           
        // USER DECIDED AGAINST IT
        return false;
    }

    public void requestShiftRight() {
              // WE MAY HAVE TO SAVE CURRENT WORK
        boolean continueToMakeNew = true;
        if (!saved)
        {
            // THE USER CAN OPT OUT HERE WITH A CANCEL
            continueToMakeNew = promptToSaveState();
        }
        
        // IF THE USER REALLY WANTS TO MAKE A NEW POSE
        if (continueToMakeNew)
        {
            // GO AHEAD AND PROCEED MAKING A NEW POSE
            continueToMakeNew = promptShiftRight();

            if (continueToMakeNew)
            {
                /*
                // NOW THAT WE'VE SAVED, LET'S MAKE SURE WE'RE IN THE RIGHT MODE
                PoseurStateManager poseurStateManager = Poseur.getPoseur().getStateManager();
                poseurStateManager.resetState();
                poseurStateManager.setState(PoseurState.SELECT_SHAPE_STATE);
                */
            }
        }
    }

    private boolean promptShiftRight() {
       
          // SO NOW ASK THE USER FOR A POSE NAME
        String spriteTypeName=null;
        String selectedState=null;
        PoseurGUI gui = Poseur.getPoseur().getGUI();
        AnimatedSpriteViewer view=gui.getAnimametedViewerPanel();
        
        try{
            //if the selected state either one of the those empty one, throw null pointer exception
            spriteTypeName=view.getJlist().getSelectedValue().toString();
            selectedState=view.getCombo().getSelectedItem().toString();
            if(view.getCombo().getSelectedItem().toString().equals("NULL")||view.getCombo().getSelectedItem().toString().equals("Select Animation State")){
                throw new NullPointerException();
            }
           
        }catch(NullPointerException e){
            JOptionPane.showMessageDialog(view,"You did not select either state or sprite type");
            return false;
        }
            // UPDATE THE FILE NAMES AND FILE
            currentSpriteName = spriteTypeName;
            currentFileName = spriteTypeName + SPRITE_FILE_EXTENSION; 
            currentFile = new File(SPRITES_PATH + currentFileName);

            // SAVE OUR NEW FILE
            //spriteIO.saveSprite(currentFile);
            String path= "./data/sprite_types/"+spriteTypeName;
            
            File currentFile3=new File(path+"/"+currentFileName);
            spriteIO.poseShiftRight(gui.getID());
            
            saved = true;
            
            // AND PUT THE FILE NAME IN THE TITLE BAR
            String appName = gui.getAppName();
            gui.setTitle(appName + APP_NAME_FILE_NAME_SEPARATOR + currentFile); 
            Poseur singleton=Poseur.getPoseur();
            //singleton.getGUI().loadSprite(currentFileName);
            
            // WE DID IT!
           
        // USER DECIDED AGAINST IT
        return false;
    }

    public void requestLeftRight() {
              // WE MAY HAVE TO SAVE CURRENT WORK
        boolean continueToMakeNew = true;
        if (!saved)
        {
            // THE USER CAN OPT OUT HERE WITH A CANCEL
            continueToMakeNew = promptToSaveState();
        }
        
        // IF THE USER REALLY WANTS TO MAKE A NEW POSE
        if (continueToMakeNew)
        {
            // GO AHEAD AND PROCEED MAKING A NEW POSE
            continueToMakeNew = promptShiftLeft();

            if (continueToMakeNew)
            {
                /*
                // NOW THAT WE'VE SAVED, LET'S MAKE SURE WE'RE IN THE RIGHT MODE
                PoseurStateManager poseurStateManager = Poseur.getPoseur().getStateManager();
                poseurStateManager.resetState();
                poseurStateManager.setState(PoseurState.SELECT_SHAPE_STATE);
                */
            }
        }
    }

    private boolean promptShiftLeft() {
       
          // SO NOW ASK THE USER FOR A POSE NAME
        String spriteTypeName=null;
        String selectedState=null;
        PoseurGUI gui = Poseur.getPoseur().getGUI();
        AnimatedSpriteViewer view=gui.getAnimametedViewerPanel();
        
        try{
            //if the selected state either one of the those empty one, throw null pointer exception
            spriteTypeName=view.getJlist().getSelectedValue().toString();
            selectedState=view.getCombo().getSelectedItem().toString();
            if(view.getCombo().getSelectedItem().toString().equals("NULL")||view.getCombo().getSelectedItem().toString().equals("Select Animation State")){
                throw new NullPointerException();
            }
           
        }catch(NullPointerException e){
            JOptionPane.showMessageDialog(view,"You did not select either state or sprite type");
            return false;
        }
            // UPDATE THE FILE NAMES AND FILE
            currentSpriteName = spriteTypeName;
            currentFileName = spriteTypeName + SPRITE_FILE_EXTENSION; 
            currentFile = new File(SPRITES_PATH + currentFileName);

            // SAVE OUR NEW FILE
            //spriteIO.saveSprite(currentFile);
            String path= "./data/sprite_types/"+spriteTypeName;
            
            File currentFile3=new File(path+"/"+currentFileName);
            spriteIO.poseShiftLeft(gui.getID());
            
            saved = true;
            
            // AND PUT THE FILE NAME IN THE TITLE BAR
            String appName = gui.getAppName();
            gui.setTitle(appName + APP_NAME_FILE_NAME_SEPARATOR + currentFile); 
            Poseur singleton=Poseur.getPoseur();
            //singleton.getGUI().loadSprite(currentFileName);
            
            // WE DID IT!
           
        // USER DECIDED AGAINST IT
        return false;
    }
    
    
    
}
