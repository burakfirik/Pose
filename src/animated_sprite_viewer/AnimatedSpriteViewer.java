package animated_sprite_viewer;

import animated_sprite.state.events.DeleteStateHandler;
import animated_sprite.state.events.DuplicateStateHandler;
import animated_sprite.state.events.LoadStateImagesHandler;
import animated_sprite.state.events.NewStateHandler;
import animated_sprite.state.events.OpenStateHandler;
import animated_sprite.state.events.RenameStateHandler;
import animated_sprite.state.events.SaveStateHandler;

import animated_sprite_viewer.events.*;
import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import poseur.Poseur;
import poseur.gui.PoseurGUI;
import sprite_renderer.AnimationState;
import sprite_renderer.SceneRenderer;
import sprite_renderer.Sprite;
import sprite_renderer.SpriteType;
import static poseur.PoseurSettings.*;
import poseur.gui.SpriteTypeGUIPanel;
import sprite_renderer.Pose;
import sprite_renderer.PoseList;

/**
 * The AnimatedSpriteViewer application lets one load and view
 * sprite states and then view their animation. Note that all
 * data concerning the sprites should be done via .xml files.
 * 
 * The sprite types should be listed in the following file:
 * ./data/sprite_types/sprite_types_list.xml, 
 * 
 * which can be validated by:
 * ./data/sprite_types/sprite_types_list.xsd
 * 
 * Each sprite type then has its own description found inside
 * its own directory. They can be validated by:
 * ./data/sprite_types/sprite_type.xsd
 * 
 * @author  Burak
 *          Debugging Enterprises
 * @version 1.0
 */
public class AnimatedSpriteViewer extends JPanel
{
   
    
    public int flagLoadSprites=0;
 
    // WE'LL ONLY ACTUALLY HAVE ONE SPRITE AT A TIME IN HERE,
    // THE ONE THAT WE ARE CURRENTLY VIEWING
    private ArrayList<Sprite> spriteList;
    
    // WE'LL LOAD ALL THE SPRITE TYPES INTO LIST
    // FROM AN XML FILE
    private ArrayList<String> spriteTypeNames;
    private HashMap<String, SpriteType> spriteTypes;

    // THIS WILL DO OUR XML FILE LOADING FOR US
    private AnimatedSpriteXMLLoader xmlLoader;

    // THE WEST WILL PROVIDE SPRITE TYPE AND ANIM STATE SELECTION CONTROLS
    private JPanel westOfSouthPanel;

    // THIS WILL STORE A SELECTABLE LIST OF THE LOADED SPRITES
    private JScrollPane spriteTypesListJSP;
    private JList spriteTypesList;
    private DefaultListModel spriteTypesListModel;
    
    // THIS WELL LET THE USER CHOOSE DIFFERENT ANIMATION STATES TO VIEW
    private JComboBox spriteStateCombobox;
    private DefaultComboBoxModel spriteStateComboBoxModel;

    // THIS PANEL WILL ORGANIZE THE CENTER
    private JPanel southPanel;
    
    private JPanel northOfRenderingPanel;
    
    // THIS PANEL WILL RENDER OUR SPRITE
    private SceneRenderer sceneRenderingPanel;
    
    //JEditToolbars
    private JToolBar jtbRenderEdit;
    
    //JButtons EditRendering
    private JButton btnNew;
    private JButton btnOpen;
    private JButton btnRename;
    private JButton btnDuplicate;
    private JButton btnDelete;
    private JButton btnSave;
    private JButton btnInfoState;
    
    //To display left upper label
    private JLabel picLabel;

    // THIS TOOLBAR WILL ALLOW THE USER TO CONTROL ANIMATION
    private JPanel animationToolbar;
    private JButton startButton;
    private JButton stopButton;
    private JButton slowDownButton;
    private JButton speedUpButton;
    private BufferedImage image;
    
    public String SPRITE_TYPE_NAME_XML = "yellow_box_man.xml";
    private Iterator<Pose> poseIterator;
    
    private LinkedList <JLabel> labelPoseList;
    private LinkedList <SpriteTypeGUIPanel> labelPoses;
    
    
    /**
     * The entire application will be initialized from here, including
     * the loading of all the sprite states from the xml file.
     */
   
    
    public AnimatedSpriteViewer()
    {
        flagLoadSprites=0;
        initWindow();
        initData();
        initGUI();
        initLayout();
        initHandlers();
    }
    
    
    /**
     * Sets up the window for use.
     */
    private void initWindow()
    {
        //setTitle(APP_TITLE);
        setSize(300, 200);
        setLocation(0, 0);
        
        //setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    /**
     * Loads all the data needed for the sprite types. Note that we'll
     * need all the sprite states, including their art and animations,
     * loaded before we initialize the GUI.
     */
    private void initData()
    {
         // WE'LL ONLY PUT ONE SPRITE IN THIS
        spriteList = new ArrayList<Sprite>();
        
        // WE'LL PUT ALL THE SPRITE TYPES HERE
        spriteTypeNames = new ArrayList<String>();
        spriteTypes = new HashMap<String, SpriteType>();
        
        // LOAD THE SPRITE TYPES FROM THE XML FILE
        try
        {
            // THIS WILL LOAD AND VALIDATE
            // OUR XML FILES
           xmlLoader = new AnimatedSpriteXMLLoader(this);
            
            // FIRST UP IS THE SPRITE TYPES LIST
            
            xmlLoader.loadSpriteTypeNames(SPRITE_TYPES_XML_PATH,
                            SPRITE_TYPE_NAME_XML, spriteTypeNames);
            
            // THEN THE SPRITE TYPES THEMSELVES
                
                sceneRenderingPanel = new SceneRenderer(spriteList);
                sceneRenderingPanel.setBackground(Color.WHITE);
                sceneRenderingPanel.startScene();
         
            
        }
        catch(InvalidXMLFileFormatException ixffe)
        {
            // IF WE DON'T HAVE A VALID SPRITE TYPE 
            // LIST WE HAVE NOTHING TO DO, WE'LL POP
            // OPEN A DIALOG BOX SO THE USER KNOWS
            // WHAT HAPPENED
            //JOptionPane.showMessageDialog(this, ixffe.toString());
            //System.exit(0);
            return;
            
        }
        
    }
    
    
    
    
    /**
     * This initializes all the GUI components and places
     * them into the frame in their appropriate locations.
     */
    private void initGUI()
    {
        // NOTE THAT WE'VE ALREADY LOADED THE XML FILE
        // WITH ALL THE SPRITE TYPES, SO WE CAN USE
        // THEM HERE TO POPULATE THE JList
        this.setLayout(new BorderLayout());
        spriteTypesListModel = new DefaultListModel();
        Iterator<String> spriteTypeNamesIt;
        try{
                spriteTypeNamesIt = spriteTypes.keySet().iterator();
        
        while (spriteTypeNamesIt.hasNext())
        {
            String spriteTypeName = spriteTypeNamesIt.next();
          //  spriteTypesListModel.addElement(spriteTypeName);
        }
        }catch(NullPointerException e){
            return;
        }
        spriteTypesListModel.removeAllElements();
        
        spriteTypeNames.clear();
        spriteTypesList = new JList();
        spriteTypesList.setModel(spriteTypesListModel);
        spriteTypesListJSP = new JScrollPane(spriteTypesList);
        
        labelPoseList=new LinkedList<>();
        labelPoses=new LinkedList<>();
              
        // OUR COMBO BOX STARTS OUT EMPTY
        spriteStateComboBoxModel = new DefaultComboBoxModel();        
        spriteStateCombobox = new JComboBox();
        spriteStateCombobox.setModel(spriteStateComboBoxModel);
        clearAnimationStatesComboBox();

        // NOW LET'S ARRANGE ALL OUR CONTROLS IN THE WEST
        westOfSouthPanel = new JPanel();
        westOfSouthPanel.setLayout(new BorderLayout());
        westOfSouthPanel.add(spriteTypesList, BorderLayout.NORTH);
        westOfSouthPanel.add(spriteStateCombobox, BorderLayout.SOUTH);
        
        //North of Scene rendering panel
        northOfRenderingPanel=new JPanel();
        northOfRenderingPanel.setLayout(new BorderLayout());
        
        // AND LET'S PUT A TITLED BORDER AROUND THE WEST OF THE SOUTH
        Border etchedBorder = BorderFactory.createEtchedBorder();
        Border titledBorder = BorderFactory.createTitledBorder(etchedBorder, "Sprite Type Selection");
        westOfSouthPanel.setBorder(titledBorder);       

        // NOW THE STUFF FOR THE SOUTH
        animationToolbar = new JPanel();         
        MediaTracker mt = new MediaTracker(this);
        startButton = initButton(   "StartAnimationButton.png",     "Start Animation",      mt, 0, animationToolbar);
        stopButton = initButton(    "StopAnimationButton.png",      "Stop Animation",       mt, 2, animationToolbar);
        slowDownButton = initButton("SlowDownAnimationButton.png",  "Slow Down Animation",  mt, 3, animationToolbar);
        speedUpButton = initButton( "SpeedUpAnimationButton.png",   "Speed Up Animation",   mt, 4, animationToolbar);
        try { mt.waitForAll(); }
        catch(InterruptedException ie)
        { ie.printStackTrace(); }

        // LET'S PUT OUR STUFF IN THE SOUTH
        southPanel = new JPanel();
        southPanel.add(westOfSouthPanel);
        southPanel.add(animationToolbar);
        
        // AND OF COURSE OUR RENDERING PANEL
        sceneRenderingPanel = new SceneRenderer(spriteList);
        
        sceneRenderingPanel.setBackground(Color.WHITE);
        sceneRenderingPanel.startScene();
        
        //ALl The editing Buttons will be added
        jtbRenderEdit=new JToolBar();
        
        
        
    
       try {                
          image = ImageIO.read(new File(EDITSTATE_IMAGE_FILE));
          System.out.println("Image has read");
           picLabel = new JLabel(new ImageIcon( image ));

       } catch (IOException ex) {
           System.out.println("Did not read Image");
       }
        
        MediaTracker tracker = new MediaTracker(this);
        int idCounter = 0;
        btnNew  = (JButton)initButton(NEWSTATE_IMAGE_FILE,      jtbRenderEdit,  tracker, idCounter++, JButton.class, null, NEWSTATE_TOOLTIP);
       // btnOpen = (JButton)initButton(OPENSTATE_IMAGE_FILE,      jtbRenderEdit,  tracker, idCounter++, JButton.class, null, OPENSTATE_TOOLTIP);
        btnRename = (JButton)initButton(RENAMESTATE_IMAGE_FILE,      jtbRenderEdit,  tracker, idCounter++, JButton.class, null, RENAMESTATE_TOOLTIP);
        btnDuplicate = (JButton)initButton(DUPLICATESTATE_IMAGE_FILE,      jtbRenderEdit,  tracker, idCounter++, JButton.class, null, DUPLICATESTATE_TOOLTIP);
        btnDelete = (JButton)initButton(DELETESTATE_IMAGE_FILE,      jtbRenderEdit,  tracker, idCounter++, JButton.class, null, DELETESTATE_TOOLTIP);
       // btnSave = (JButton)initButton(SAVESTATE_IMAGE_FILE,      jtbRenderEdit,  tracker, idCounter++, JButton.class, null, SAVESTATE_TOOLTIP);
       // btnInfoState = (JButton)initButton(EDITSTATE_IMAGE_FILE,      jtbRenderEdit,  tracker, idCounter++, JButton.class, null, DELETESTATE_TOOLTIP);
        
            
            try{
                //xmlLoader.loadSpriteTypeNames(SPRITES_DATA_PATH,
                //            "sprite_types_xml/blue_round_man.xml", spriteTypeNames);
                xmlLoader.loadSpriteTypes(SPRITES_DATA_PATH, spriteTypeNames, spriteTypes);
             }catch(InvalidXMLFileFormatException e){
                 e.printStackTrace();
             }   
                
                sceneRenderingPanel = new SceneRenderer(spriteList);
                sceneRenderingPanel.setBackground(Color.WHITE);
                sceneRenderingPanel.startScene();
              
        
    }

    /**
     * This helper method empties the combo box with animations
     * and disables the component.
     */
    private void clearAnimationStatesComboBox()
    {
        if(spriteStateCombobox.getItemCount()!=1){
        spriteStateComboBoxModel.removeAllElements();
        spriteStateComboBoxModel.addElement(SELECT_ANIMATION_TEXT);        
        spriteStateCombobox.setEnabled(false);
        }
    }
    
    private void clearAnimationStatesComboBox2()
    {
        spriteStateComboBoxModel.removeAllElements();
        spriteStateComboBoxModel.addElement("ADEDDDDDDDDDED");        
        spriteStateCombobox.setEnabled(false);      
    }
  
    /**
     * Called when the user selects a sprite type from the list, it
     * will get all the animation states for that sprite type and 
     * load them into the combo box.
     */
    public void selectSpriteType()
    {            
        // FIRST THERE IS NO LONGER A SPRITE TO RENDER
        spriteList.clear();
            
        // THEN THERE ARE NO ANIMATION STATES
        clearAnimationStatesComboBox();
        spriteStateCombobox.setEnabled(true);

        // NOW LOAD THE ANIMATIONS FOR THE NEWLY SELECTED SPRITE TYPE
        String selectedSpriteTypeName = (String)spriteTypesList.getSelectedValue();
        if(spriteTypes.get(selectedSpriteTypeName)!=null){
            SpriteType selectedSpriteType = spriteTypes.get(selectedSpriteTypeName);
            Iterator<String> it = selectedSpriteType.getAnimationStates();
            while(it.hasNext())
            {
                String animState = it.next();
            
                spriteStateComboBoxModel.addElement(animState);
                this.updateUI();
                spriteStateCombobox.updateUI();
                repaint();
            }        
        }
    }

    /**
     * Called when the user selects an animation state from
     * the combo box, it will ensure this one is rendered.
     */
    public void selectAnimationState()
    {
        // GET RID OF THE OLD SPRITE, IT MAY HAVE HAD A DIFFERENT STATE
        spriteList.clear();
        
        // WHICH ONE IS NOW SELECTED?
        Object selectedItem = spriteStateCombobox.getSelectedItem();
        
        // MAKE SURE THIS IS ACTUALLY THE USER
        if (selectedItem != null)
        {
            // THIS ISN'T AN ANIMATION STATE
            if (!selectedItem.equals(SELECT_ANIMATION_TEXT))
            {
                // FIRST STOP RENDERING FOR A MOMENT
                sceneRenderingPanel.pauseScene();

                // THEN GET THE STATE AND MAKE A SPRITE FOR IT
                String selectedState = spriteStateCombobox.getSelectedItem().toString();
                String selectedSpriteTypeName = (String)spriteTypesList.getSelectedValue();
                SpriteType selectedSpriteType = spriteTypes.get(selectedSpriteTypeName);
                Sprite spriteToAnimate = new Sprite(selectedSpriteType, selectedState);
                PoseList list= selectedSpriteType.getPoseList(selectedState);
                
                
                // PUT THE SPRITE IN THE MIDDLE OF THE PANEL
                int rendererWidth = sceneRenderingPanel.getWidth();
                int rendererHeight = sceneRenderingPanel.getHeight();
                int x = (rendererWidth/2) - (selectedSpriteType.getWidth()/2);
                int y = (rendererHeight/2) - (selectedSpriteType.getHeight()/2);
                spriteToAnimate.setPositionX(x);
                spriteToAnimate.setPositionY(y);
                
                // PUT THE SPRITE WHERE THE RENDERER WILL FIND IT
                spriteList.add(spriteToAnimate);
                
                // AND START IT UP AGAIN
                sceneRenderingPanel.unpauseScene();
            }
        }
    }

    /**
     * This is a helper method for making a button. It loads the image and sets
     * it as the button image. It then puts it in the panel.
     * 
     * @param iconFilename Image for button
     * @param tooltip Tooltip for button
     * @param mt Used for batch loading of images
     * @param id Numeric id of image to help with batch loading
     * @param panel The container to place the button into
     * 
     * @return The fully constructed button, ready for use.
     */
    private JButton initButton(String iconFilename, String tooltip, MediaTracker mt, int id, JPanel panel)
    {
        // LOAD THE IMAGE
        Toolkit tk = Toolkit.getDefaultToolkit();
        Image img = tk.getImage(GUI_IMAGES_PATH + iconFilename);
        mt.addImage(img, id);
        
        // AND USE IT TO BUILD THE BUTTON
        ImageIcon ii = new ImageIcon(img);
        JButton button = new JButton(ii);
        button.setToolTipText(tooltip);
        
        // LET'S PUT A LITTLE BUFFER AROUND THE IMAGE AND THE EDGE OF THE BUTTON
        Insets insets = new Insets(2,2,2,2);
        button.setMargin(insets);
        
        // PUT THE BUTTON IN THE CONTAINER
        panel.add(button);
        
        // AND SEND THE CONSTRUCTED BUTTON BACK
        return button;
    }
    
    private void initLayout() {
        
       try{ 
       
           northOfRenderingPanel.add(picLabel, BorderLayout.NORTH);
        
       }catch(NullPointerException e){
           return;
       } 
        northOfRenderingPanel.add(jtbRenderEdit, BorderLayout.CENTER);
        
        add(northOfRenderingPanel, BorderLayout.NORTH);
        add(sceneRenderingPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
        
        northOfRenderingPanel.repaint();
        this.repaint();
    }
    
    
    /**
     * GUI setup method can be quite lengthy and repetitive so
     * it helps to create helper methods that can do a bunch of
     * things at once. This method creates a button with a bunch
     * of premade values. Note that we are using Java reflection
     * here, to make an object based on what class type it has.
     * 
     * @param imageFile The image to use for the button.
     * 
     * @param parent The container inside which to put the button.
     * 
     * @param tracker This makes sure our button fully loads.
     * 
     * @param id A unique id for the button so the tracker knows it's there.
     * 
     * @param buttonType The type of button, we'll use reflection for making it.
     * 
     * @param bg Some buttons will go into groups where only one may be selected
     * at a time.
     * 
     * @param tooltip The mouse-over text for the button.
     * 
     * @return A fully constructed and initialized button with all the data
     * provided to it as arguments.
     */
    
    private AbstractButton initButton(   String imageFile, 
                                        Container parent, 
                                        MediaTracker tracker, 
                                        int id, 
                                        Class buttonType,
                                        ButtonGroup bg,
                                        String tooltip)
    {
        try 
        {
            // LOAD THE IMAGE AND MAKE AN ICON
            Image img = batchLoadImage(imageFile, tracker, id);
            ImageIcon ii = new ImageIcon(img);
            
            // HERE'S REFLECTION MAKING OUR OBJECT USING IT'S CLASS
            // NOTE THAT DOING IT LIKE THIS CALLS THE buttonType
            // CLASS' DEFAULT CONSTRUCTOR, SO WE MUST MAKE SURE IT HAS ONE
            AbstractButton createdButton;
            createdButton = (AbstractButton)buttonType.newInstance();
            
            // NOW SETUP OUR BUTTON FOR USE
            createdButton.setIcon(ii);
            createdButton.setToolTipText(tooltip);
            parent.add(createdButton);
            
            // INSETS ARE SPACING INSIDE THE BUTTON,
            // TOP LEFT RIGHT BOTTOM
            Insets buttonMargin = new Insets(   
                    BUTTON_INSET, BUTTON_INSET, BUTTON_INSET, BUTTON_INSET);
            createdButton.setMargin(buttonMargin);
            
            // ADD IT TO ITS BUTTON GROUP IF IT'S IN ONE
            if (bg != null)
            {
                bg.add(createdButton);
            }
            
            // AND RETURN THE SETUP BUTTON
            return createdButton;
        } 
        catch (InstantiationException | IllegalAccessException ex) 
        {
            // WE SHOULD NEVER GET THIS ERROR, BUT WE HAVE TO PUT
            // A TRY CATCH BECAUSE WE'RE USING REFLECTION TO DYNAMICALLY
            // CONSTRUCT OUR BUTTONS BY CLASS NAME
            Logger.getLogger(PoseurGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        // THIS WOULD MEAN A FAILURE OF SOME SORT OCCURED
        return null;
    }

    /**
     * This method helps us load a bunch of images and ensure they are 
     * fully loaded when we want to use them.
     * 
     * @param imageFile The path and name of the image file to load.
     * 
     * @param tracker This will help ensure all the images are loaded.
     * 
     * @param id A unique identifier for each image in the tracker. It
     * will only wait for ids it knows about.
     * 
     * @return A constructed image that has been registered with the tracker.
     * Note that the image's data has not necessarily been fully loaded when 
     * this method ends.
     */
    private Image batchLoadImage(String imageFile, MediaTracker tracker, int id)
    {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Image img = tk.getImage(imageFile);
        tracker.addImage(img, id);        
        return img;
    }
    int i=0;
    /**
     * This helper links up all the components with their event
     * handlers, ensuring the proper responses.
     */
    SpriteTypeSelectionHandler stsh;
    private void initHandlers()
    {
        // CONSTRUCT AND REGISTER ALL THE HANDLERS
        StartAnimationHandler sah = new StartAnimationHandler(sceneRenderingPanel);
        startButton.addActionListener(sah);
        
        StopAnimationHandler stah = new StopAnimationHandler(sceneRenderingPanel);
        stopButton.addActionListener(stah);
        SlowDownAnimationHandler sdah = new SlowDownAnimationHandler(sceneRenderingPanel);
        slowDownButton.addActionListener(sdah);
        SpeedUpAnimationHandler suah = new SpeedUpAnimationHandler(sceneRenderingPanel);
        speedUpButton.addActionListener(suah);
         stsh = new SpriteTypeSelectionHandler(this);
        //spriteTypesList.addListSelectionListener(stsh);
        
        AnimationStateSelectionHandler assh = new AnimationStateSelectionHandler(this);
        spriteStateCombobox.addItemListener(assh);
        
        
        
        //Pose State Control HANDLERs
        DeleteStateHandler dsh=new DeleteStateHandler();
        btnDelete.addActionListener(dsh);
        DuplicateStateHandler dupsh=new DuplicateStateHandler();
        btnDuplicate.addActionListener(dupsh);
        LoadStateImagesHandler lsih=new LoadStateImagesHandler(this);
        spriteStateCombobox.addItemListener(lsih);
        NewStateHandler newsh=new NewStateHandler();
        btnNew.addActionListener(newsh);
        RenameStateHandler renamesh=new RenameStateHandler();
        btnRename.addActionListener(renamesh);
        SaveStateHandler savesh=new SaveStateHandler();
        //btnSave.addActionListener(savesh);
        OpenStateHandler opensh=new OpenStateHandler();
       // btnOpen.addActionListener(opensh);
        
    }

    public void removeElementsInArrayList(ArrayList list){
        for(int i=0;i<list.size();i++){
            list.remove(i);
        }
    }
    
    /**
     * This method reads in the xmlFile, validates it against the
     * schemaFile, and if valid, loads the name of the sprites in to the 
     * SpriteTypeNames array list and then loads the spriteTypes in a HashMap 
     * using the SpriteTypeNames.pose
     * 
     * @param String name of the XML file where the sprite name is located.
     * 
     * 
     */
    
    public void loadSprites(String name) {
        
        //SPRITE_TYPE_NAME_XML, file name will be return .xml, I cut the the .xml strictly get the file name
        if(name.length()>4)
        name=name.substring(0, name.length()-4);
        //Assign the file name for load purposes
        SPRITE_TYPE_NAME_XML=name+".xml";
         
        try{//SPRITES_DATA_PATH
            spriteTypeNames.clear();
            //Load the sprite names from xml, after checking with schema
            xmlLoader.loadSpriteTypeNames(SPRITE_TYPES_XML_PATH,
                            SPRITE_TYPE_NAME_XML, spriteTypeNames);
            //After having the names in spriteTypeNames array list, I load all the data to HaspMap spriteTypes
            xmlLoader.loadSpriteTypes(SPRITE_TYPES_XML_PATH, spriteTypeNames, spriteTypes);
            
            //If schema mathc fails
        }catch(InvalidXMLFileFormatException e){
            System.out.println("Body not Good look at AnimatedSpriteViewer line 646");
            //e.printStackTrace();
           // return;
        }
        
        
       
        //To iterate through the spriteTypes hashMap
        Iterator<String> spriteTypeNamesIt;
        
        try{
            /*
            //GET THE NAME OF EACH SPRITE FROM THE HASH MAP
            spriteTypeNamesIt = spriteTypes.keySet().iterator();
                                    
            //ALL I DO NOT WANT JLIST TO BE LISTENED REMOVE THE LISTENER
            spriteTypesList.removeListSelectionListener(stsh);
            //CLEAR THE JLIST BEFORE WE ADD MORE ELEMENTS
            spriteTypesListModel.clear();
            //ITERATE THROUGH EACH SPRITE NAME TO ADD INTO JLIST        
            while (spriteTypeNamesIt.hasNext())
            {
                String spriteTypeName = spriteTypeNamesIt.next();
                spriteTypesListModel.addElement(spriteTypeName);
           
            }
            */
            
            int i=0;
            while ( i<spriteTypeNames.size())
            {
                spriteTypesList.removeListSelectionListener(stsh);
                //String spriteTypeName = spriteTypeNamesIt.next();
                spriteTypesListModel.addElement(spriteTypeNames.get(i));
                spriteTypesList.addListSelectionListener(stsh);
                i++;
            }
            
        }catch(NullPointerException e){
            System.out.println("spriteTypeNamesIt is empty son");
           // return;
        }
        //spriteTypesListModel.addElement("TEST");
        //ADD THE MODEL TO JLIST
        spriteTypesList.setModel(spriteTypesListModel);
        //ADD BACK THE LISTNER TO THE JLIST
        
    }
    
     public void loadSpritesFromState(String name) {
        Object state=getCombo().getSelectedItem();
        //Assign the file name for load purposes
        SPRITE_TYPE_NAME_XML=name+".xml";
        //spriteTypes.remove(name);
        
        //labelPoses.clear();
        
        
        spriteTypesListModel.removeElement(name);
        try{//SPRITES_DATA_PATH
            spriteTypeNames.clear();
            //Load the sprite names from xml, after checking with schema
            xmlLoader.loadSpriteTypeNames(SPRITE_TYPES_XML_PATH,
                            SPRITE_TYPE_NAME_XML, spriteTypeNames);
            //After having the names in spriteTypeNames array list, I load all the data to HaspMap spriteTypes
            xmlLoader.loadSpriteTypes(SPRITE_TYPES_XML_PATH, spriteTypeNames, spriteTypes);
            getJlist().setSelectedValue(name, DEFAULT_DEBUG_TEXT_ENABLED);
            getCombo().setSelectedItem(state);
            loadImages();
            //labelPoses.get(0).removeAll();
            //If schema mathc fails
        }catch(InvalidXMLFileFormatException e){
            System.out.println("Body not Good look at AnimatedSpriteViewer line 646");
            //e.printStackTrace();
           // return;
        }
        
        
       
        //To iterate through the spriteTypes hashMap
        Iterator<String> spriteTypeNamesIt;
        
        try{
            /*
            //GET THE NAME OF EACH SPRITE FROM THE HASH MAP
            spriteTypeNamesIt = spriteTypes.keySet().iterator();
                                    
            //ALL I DO NOT WANT JLIST TO BE LISTENED REMOVE THE LISTENER
            spriteTypesList.removeListSelectionListener(stsh);
            //CLEAR THE JLIST BEFORE WE ADD MORE ELEMENTS
            spriteTypesListModel.clear();
            //ITERATE THROUGH EACH SPRITE NAME TO ADD INTO JLIST        
            while (spriteTypeNamesIt.hasNext())
            {
                String spriteTypeName = spriteTypeNamesIt.next();
                spriteTypesListModel.addElement(spriteTypeName);
           
            }
            */
            
            int i=0;
            while ( i<spriteTypeNames.size())
            {
                spriteTypesList.removeListSelectionListener(stsh);
                //String spriteTypeName = spriteTypeNamesIt.next();
                spriteTypesListModel.addElement(spriteTypeNames.get(i));
                spriteTypesList.addListSelectionListener(stsh);
                i++;
            }
            
        }catch(NullPointerException e){
            System.out.println("spriteTypeNamesIt is empty son");
           // return;
        }
        //spriteTypesListModel.addElement("TEST");
        //ADD THE MODEL TO JLIST
        spriteTypesList.setModel(spriteTypesListModel);
        //ADD BACK THE LISTNER TO THE JLIST
        
    }
    

    /*
     * This method returns all the Spritetypes in HashMap.
     * 
     * @return HashMap<String, SpriteType> has all the sprite related data
     * 
     */
    
    public HashMap<String, SpriteType> getSpriteTypes(){
        
        return spriteTypes;
    }
   
    /*
     * 
     * This method returns the JList component from rendering Panel.
     * 
     * @return JList which is a list with all the Sprite names
     */
    public JList getJlist(){
        return spriteTypesList;
    }
    /*
     * This JComboBox returns the combobox from rendering panel on the left.
     * 
     * @return JComboBox where all the state related information is stored.
     */
    public JComboBox getCombo(){
        
        return spriteStateCombobox;
    }
    
    /*
     * 
     * This method retrivies all the sprite list. 
     * 
     * @return ArrayList<Sprite> where all the sprites are stored in ArrayList
     */
    public ArrayList<Sprite> getSprites(){
    
        return spriteList;
    }

    /**
     * This method reads from SpriteType hashmap, gets all the pose image related
     * paths and data and loads these images in the PoseurEdit panel which is a
     * right Panel. All the images is loaded in to JPanel at the south of the 
     * Canvas Edit Panel.
     * 
     */
    
    public void loadImages() {
         
        // GET RID OF THE OLD SPRITE, IT MAY HAVE HAD A DIFFERENT STATE
        //spriteList.clear();
        Poseur singleton=Poseur.getPoseur();
        Object selectedItem; 
       try{
        selectedItem=spriteStateCombobox.getSelectedItem().toString();
       }catch(NullPointerException e){
           return;
       }
         if (selectedItem != null)
        {
            // THIS ISN'T AN ANIMATION STATE
            if (!selectedItem.equals(SELECT_ANIMATION_TEXT))
            {
                
                JLabel temp;
                
                String selectedSpriteTypeName = spriteTypesList.getSelectedValue().toString();
                SpriteType selectedSpriteType = spriteTypes.get(selectedSpriteTypeName);
                PoseList list= selectedSpriteType.getPoseList(selectedItem.toString());
                Pose tempPose;
                int i=0;
                poseIterator=list.getPoseIterator();
                labelPoseList.clear();
                labelPoses.clear();
                while(i<list.getNumOfPoses())
                {
                    tempPose = poseIterator.next();
                    //selectedSpriteType.getImage(tempPose.getImageID());
                    try{
                    Image img=selectedSpriteType.getImage(tempPose.getImageID());
                    img=createResizedCopy(img, false);
                    //BufferedImage bufferedImg=(BufferedImage)img;
                   // reSizeImage(bufferedImg);
                    temp=new JLabel(new ImageIcon(img));
                    labelPoseList.add(temp);
                    labelPoses.add(new SpriteTypeGUIPanel(temp, tempPose.getImageID()));
                    i++;
                    }catch(NullPointerException e){
                        System.out.println("LOOK AT THE LINE 851 adding the labels in AnimatedViewer ");
                        labelPoseList.clear();
                        labelPoses.clear();
                        Poseur poseur=Poseur.getPoseur();
                        PoseurGUI gui=poseur.getGUI();
                        gui.removeLabels();
                        return;
                    }
                    
                }  
                
               Poseur poseur=Poseur.getPoseur();
               PoseurGUI gui=poseur.getGUI();
              // gui.setLabels(labelPoseList);
               gui.setLabelsPanel(labelPoses);
               this.updateUI();
               
                
            }
        
            repaint();
        }
    }

    public void updateEveryhting(){
        Poseur singleton=Poseur.getPoseur();
        PoseurGUI gui=singleton.getGUI();
        
        for(int i=0;i<labelPoses.size();i++){
            labelPoses.get(i).updateUI();
            labelPoses.get(i).repaint();
            gui.getSouthRightPanel().updateUI();
            gui.getSouthRightPanel().repaint();
            System.out.println("ASDSDSDSD");
        }
    }
    
    public void addNewSpriteToJlist(String spriteName) {
        spriteTypesList.removeListSelectionListener(stsh);
       
        //spriteTypesListModel.addElement("TEST");
        spriteTypesList.setModel(spriteTypesListModel);
        //ADD BACK THE LISTNER TO THE JLIST
        spriteTypesList.addListSelectionListener(stsh);
        
        
        repaint();
        westOfSouthPanel.updateUI();
        southPanel.updateUI();
        
    }

     public void reSizeImage(BufferedImage img){
        int nWidth=img.getWidth();
        int nHeight=img.getHeight();
        nWidth-=10;
        nHeight-=10;
        
       BufferedImage scaledImage = new BufferedImage(nWidth, nHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = scaledImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,

        RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        //graphics2D.drawImage(sharedGeometry, 0, 0, nWidth, nHeight, null);

        graphics2D.dispose();
        img=scaledImage;
                
    
    }
    
     
     BufferedImage createResizedCopy(Image originalImage, 
    		boolean preserveAlpha)
    {
         
    	System.out.println("resizing...");
    	int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
    	BufferedImage scaledBI = new BufferedImage(100, 100, imageType);
    	Graphics2D g = scaledBI.createGraphics();
    	if (preserveAlpha) {
    		g.setComposite(AlphaComposite.Src);
    	}
    	g.drawImage(originalImage, 0, 0, 100, 100, null); 
    	g.dispose();
    	return scaledBI;
    }
     
     public LinkedList<SpriteTypeGUIPanel> getLabelPoses(){
     
         return labelPoses;
     }
     
}