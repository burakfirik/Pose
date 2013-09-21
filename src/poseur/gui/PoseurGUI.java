package poseur.gui;

import animated_sprite.pose.events.DeletePoseHandler;
import animated_sprite.pose.events.DuplicatePoseHandler;
import animated_sprite.pose.events.DurationPoseHandler;
import animated_sprite_viewer.AnimatedSpriteViewer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import poseur.Poseur;
import static poseur.PoseurSettings.*;
import poseur.events.canvas.PoseCanvasComponentHandler;
import poseur.events.canvas.PoseCanvasMouseHandler;
import poseur.events.colors.AlphaChangeHandler;
import poseur.events.colors.ColorPalletHandler;
import poseur.events.colors.CustomColorHandler;
import poseur.events.colors.FillColorHandler;
import poseur.events.colors.OutlineColorHandler;
import poseur.events.edit.CopyHandler;
import poseur.events.edit.CutHandler;
import poseur.events.edit.MoveToBackHandler;
import poseur.events.edit.MoveToFrontHandler;
import poseur.events.edit.PasteHandler;
import poseur.events.edit.StartSelectionHandler;
import poseur.events.files.ExitHandler;
import animated_sprite.pose.events.ExportPoseHandler;
import animated_sprite.pose.events.NewPoseHandler;
import animated_sprite.pose.events.OpenPoseHandler;
import animated_sprite.pose.events.ShiftRightHandler;
import animated_sprite.pose.events.SavePoseAsHandler;
import animated_sprite.pose.events.SavePoseHandler;
import animated_sprite.pose.events.ShiftLeftHandler;
import animated_sprite.sprite.NewSpriteHandler;
import animated_sprite_viewer.WhitespaceFreeXMLNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import animated_sprite.sprite.OpenSpriteHandler;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import poseur.events.shapes.EllipseSelectionHandler;
import poseur.events.shapes.LineSelectionHandler;
import poseur.events.shapes.RectangleSelectionHandler;
import poseur.events.shapes.StrokeSelectionHandler;
import poseur.events.window.PoseurWindowHandler;
import poseur.events.zoom.ChangePoseDimensionsHandler;
import poseur.events.zoom.ZoomInHandler;
import poseur.events.zoom.ZoomOutHandler;
import poseur.files.ColorPalletLoader;
import poseur.files.InvalidXMLFileFormatException;
import poseur.files.PoseurFileManager;
import poseur.shapes.PoseurShape;
import poseur.state.ColorPalletState;
import poseur.state.PoseCanvasState;
import poseur.state.PoseurState;
import poseur.state.PoseurStateManager;
import sprite_renderer.AnimationState;
import sprite_renderer.Pose;
import sprite_renderer.PoseList;
import sprite_renderer.Sprite;
import sprite_renderer.SpriteType;
import static javax.swing.ScrollPaneConstants.*;
/**
 * This class provides the full Graphical User Interface for the
 * Poseur application. It contains references to all GUI components,
 * including the rendering surfaces, and has service methods for
 * updating them.
 * 
 * @author  Burak
 *          Debugging Enterprises
 * @version 1.0
 */
public class PoseurGUI extends JFrame
{
    // THE NAME OF THE APPLICATION, WE'LL PUT THIS IN THE 
    // WINDOW'S TITLE BAR, BUT MIGHT ALSO INCLUDE THE 
    // FILE NAME THERE
    protected String appName;
        
    // WE'LL HAVE TWO CANVASES IN THE CENTER, THE
    // ONE ON THE LEFT IS THE TRUE ONE, AND WILL NEVER
    // ZOOM THE VIEW. THE ONE ON THE RIGHT WILL ALLOW 
    // FOR ZOOMED IN AND OUT VIEWS. NOTE THAT WE'LL PUT
    // THEM INTO A SPLIT PANE
    private JSplitPane canvasSplitPane;
    
    AnimatedSpriteViewer appWindow ;
    
    private int flag=0;
    private PoseCanvas zoomableCanvas;
    
    // NORTH PANEL - EVERYTHING ELSE GOES IN HERE
    private JPanel northPanel;
    private JPanel northOfNorthPanel;
    private JPanel southOfNorthPanel;
    private JPanel southOfRightCanvas;
    
    // FILE CONTROLS
    private JToolBar fileToolbar;
    private JButton newButton;
    private JButton openButton;
    private JButton saveButton;
    private JButton saveAsButton;
    private JButton exportButton;
    private JButton exitButton;
    
    // EDIT CONTROLS
    private JToolBar editToolbar;
    private JButton selectionButton;
    private JButton cutButton;
    private JButton copyButton;
    private JButton pasteButton;
    private JButton moveToBackButton;
    private JButton moveToFrontButton;
    
    // SHAPE SELECTION CONTROLS
    private JToolBar shapeToolbar;
    private JToggleButton lineToggleButton;
    private JToggleButton rectToggleButton;
    private JToggleButton ellipseToggleButton;
    private ButtonGroup shapeButtonGroup;
    private JComboBox lineStrokeSelectionComboBox;
    
    // ZOOM CONTROLS
    private JToolBar zoomToolbar;
    private JButton zoomInButton;
    private JButton zoomOutButton;
    private JButton dimensionsButton;
    private JLabel zoomLabel;
        
    // COLOR SELECTION CONTROLS
    private JToolBar colorSelectionToolbar;
    private ColorToggleButton outlineColorSelectionButton;
    private ColorToggleButton fillColorSelectionButton;
    private ButtonGroup colorButtonGroup;
    private ColorPallet colorPallet;    
    private JButton customColorSelectorButton;
    private JLabel alphaLabel;
    private JSlider transparencySlider;
    
    //Editing toolbar
    private JToolBar jtbEdit;
    
    //Poseur Controls
    private JButton btnNewPose;
    private JButton btnSavePose;
    private JButton btnOpenPose;
    private JButton btnDeletePose;
    private JButton btnDuplicatePose;
    private JButton btnReorderPose;
    private JButton btnShifRighttPose;
    private JButton btnShiftLeftPose;
    private JButton btnSetDurationPose;
    private JButton btnInfoStatePose;
    private BufferedImage imagePose;
    private JLabel picLabelPose;
    
    //private LinkedList<JLabel> labelPoseList;
    private LinkedList<SpriteTypeGUIPanel> labelPoses;
    
    //To place poseur controls
    private JPanel northOfZoomableCanvasPanel;
    
    //To iterate through the poses
    private Iterator<Pose> poseIterator;
    //To place the 
    private JScrollPane vertical;
    
    private int imageID=1;

    /**
     * Default constructor for initializing the GUI. Note that the Poseur
     * application's state manager must have already been constructed and
     * setup when this method is called because we'll use some of the values
     * found there to initializer the GUI. The color pallet, for example, must
     * have already been loaded into the state manager before this constructor
     * is called.
     */
    public PoseurGUI()
    {
        // IN CASE THE PARENT DOES ANYTHING, I USUALLY LIKE TO EXPLICITY INCLUDE THIS
        super();
        
        // CONSTRUCT AND LAYOUT THE COMPONENTS
        initGUI();

        // AND SETUP THE HANDLERS
        initHandlers();
        
        // ENABLE AND DISABLE ALL CONTROLS AS NEEDED
        updateMode();
    }
    
    // ACCESSOR METHODS
    
    /**
     * Accessor method for getting the application's name.
     * 
     * @return The name of the application this window is 
     * being used for.
     */
    public String getAppName() { return appName; }
    
    /**
     * Accessor method for getting the color pallet.
     * 
     * @return The color pallet component.
     */
    public ColorPallet getColorPallet() { return colorPallet; }
    
    /**
     * Accessor method for getting the color currently set
     * for filling shapes.
     * 
     * @return The fill color currently in use for making shapes.
     */
    public Color getFillColor() { return fillColorSelectionButton.getBackground(); }

    /**
     * Accessor method for getting the color currently set
     * four outlining shapes.
     * 
     * @return The outline color currently in use for making shapes.
     */
    public Color getOutlineColor() { return outlineColorSelectionButton.getBackground(); }

    /**
     * Accessor method for getting the line thickness currently
     * set for drawing shape outlines and lines.
     * 
     * @return The line thickness currently in use for making shapes.
     */
    public int getLineThickness() { return lineStrokeSelectionComboBox.getSelectedIndex() + 1; }
    
    /**
     * Accessor method for getting the current transparency value of the slider.
     * 
     * @return The alpha value, between 0 (fully transparent) and 255 (fully opaque)
     * as currently set by the transparency slider.
     */
    public int getAlphaTransparency() { return transparencySlider.getValue(); }

   

    /**
     * Accessor method for getting the canvas that will
     * zoom in and out, rendering the pose accordingly.
     * 
     * @return The zoomable canvas, which is on the right.
     */
    public PoseCanvas getZoomablePoseCanvas() { return zoomableCanvas; }
    
    /**
     * Accessor method to test if the outline color toggle button
     * is selected. Note that either the outline or fill button
     * must be selected at all times.
     * 
     * @return true if the outline toggle button is selected, false if
     * the fill button is selected.
     */
    public boolean isOutlineColorSelectionButtonToggled() { return outlineColorSelectionButton.isSelected(); }
    
    // MUTATOR METHODS
    
    /**
     * Mutator method for setting the app name.
     * 
     * @param initAppName The name of the application,
     * which will be put in the window title bar.
     */
    public void setAppName(String initAppName)
    {
        appName = initAppName;
    }    
    
    /**
     * This mutator method sets the background color for the
     * outline toggle button, which can then be used for 
     * the outline of new shapes.
     * 
     * @param initColor The color to use for shape outlines.
     */
    public void setOutlineToggleButtonColor(Color initColor)
    {
        outlineColorSelectionButton.setBackground(initColor);
    }

    /**
     * This mutator method sets the background color for the fill
     * toggle button, which can then be used for the fill
     * color of new shapes.
     * 
     * @param initColor The color to use for shape filling.
     */
    public void setFillToggleButtonColor(Color initColor)
    {
        fillColorSelectionButton.setBackground(initColor);
    }

    // PUBLIC METHODS OTHER CLASSES NEED TO CALL
    
    /**
     * This method updates the zoom label display with the current
     * zoom level.
     */
    public void updateZoomLabel()
    {
        // GET THE RIGHT CANVAS STATE, SINCE IT ZOOMS
        Poseur singleton = Poseur.getPoseur();
        PoseurStateManager poseurStateManager = singleton.getStateManager();
        PoseCanvasState zoomableCanvasState = poseurStateManager.getZoomableCanvasState();

        // GET THE ZOOM LEVEL
        float zoomLevel = zoomableCanvasState.getZoomLevel();
        
        // MAKE IT LOOK NICE
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(1);
        nf.setMaximumFractionDigits(1);
        String zoomText = ZOOM_LABEL_TEXT_PREFIX
                + nf.format(zoomLevel)
                + ZOOM_LABEL_TEXT_POSTFIX;
        
        // AND PUT IT IN THE LABEL
        zoomLabel.setText(zoomText);
    }    
    
    /**
     * The fill and outline toggle buttons are connected,
     * only one can be toggled on a any time. This method
     * turns the fill toggle button on.
     */
    public void toggleFillColorButton()
    {
        fillColorSelectionButton.select();
        outlineColorSelectionButton.deselect();
    }
    
    /**
     * The fill and outline toggle buttons are connected,
     * only one can be toggled on a any time. This method
     * turns the outline toggle button on.
     */
    public void toggleOutlineColorButton()
    {
        fillColorSelectionButton.deselect();
        outlineColorSelectionButton.select();
    }
        
    /**
     * This method updates the appropriate toggle button,
     * either outline or fill, with the color argument, 
     * setting it to its background.
     * 
     * @param color Color to use to set the background
     * for the appropriate toggle button.
     */
    public void updateDrawingColor(Color color)
    {
        // IF THE OUTLINE TOGGLE IS THE ONE THAT'S
        // CURRENTLY SELECTED, THEN THAT'S THE ONE
        // THE USER WANTED TO CHANGE
        if (outlineColorSelectionButton.isSelected())
        {
            outlineColorSelectionButton.setBackground(color);
        }
        // OTHERWISE IT'S THE FILL TOGGLE BUTTON
        else if (fillColorSelectionButton.isSelected())
        {
            fillColorSelectionButton.setBackground(color);
        }
    }
    
    /**
     * Called each time the application's state changes, this method
     * is responsible for enabling, disabling, and generally updating 
     * all the GUI control based on what the current application
     * state (i.e. the PoseurMode) is in.
     */
    public final void updateMode()
    {
        // WE'LL NEED THESE GUYS
        Poseur singleton = Poseur.getPoseur();
        PoseurStateManager state = singleton.getStateManager();
        PoseurState mode = state.getMode();
        PoseurFileManager fileManager = singleton.getFileManager();

        // IN THIS MODE THE USER IS DRAGGING THE MOUSE TO
        // COMPLETE THE DRAWING OF A SINGLE SHAPE
        if (mode == PoseurState.COMPLETE_SHAPE_STATE)
        {
            // THIS USES THE CROSSHAIR
            selectCursor(Cursor.CROSSHAIR_CURSOR);
        }
        // IN THIS MODE THE USER IS ABOUT TO START DRAGGING
        // THE MOUSE TO CREATE A SHAPE
        else if (mode == PoseurState.CREATE_SHAPE_STATE)
        {
            // THIS USES THE CROSSHAIR
            selectCursor(Cursor.CROSSHAIR_CURSOR);
            
            // TURN THE APPROPRIATE CONTROLS ON/OFF
            setEnabledEditControls(false);
            
            selectionButton.setEnabled(true);            
        }
        // IN THIS STATE THE USER HAS SELECTED A SHAPE
        // ON THE CANVAS AND IS DRAGGING IT
        else if (mode == PoseurState.DRAG_SHAPE_STATE)
        {
            // THIS USES THE MOVE 
            selectCursor(Cursor.MOVE_CURSOR);
        }
        // IN THIS STATE THE USER IS ABLE TO CLICK ON
        // A SHAPE TO SELECT IT. THIS IS THE MOST COMMON
        // STATE AND IS THE DEFAULT AT THE START OF THE APP
        else if (mode == PoseurState.SELECT_SHAPE_STATE)
        {
            // THIS USES THE ARROW CURSOR
            selectCursor(Cursor.DEFAULT_CURSOR);
            
            // THERE IS NO SHAPE SELECTED, SO WE CAN'T
            // USE THE EDIT CONTROLS
            enableSaveAsAndExport();
            setEnabledEditControls(false);
            selectionButton.setEnabled(false);
            setEnabledColorControls(true);
            setEnabledShapeControls(true);
            setEnabledZoomControls(true);
        }
        // IN THIS STATE A SHAPE HAS BEEN SELECTED AND SO WE
        // MAY EDIT IT, LIKE CHANGE IT'S COLORS OR TRANSPARENCY
        else if (mode == PoseurState.SHAPE_SELECTED_STATE)
        {
            // THIS USES THE ARROW CURSOR
            selectCursor(Cursor.DEFAULT_CURSOR);
            
            // THE EDIT CONTROLS CAN NOW BE USED
            setEnabledEditControls(true);
        }
        // THIS IS THE STATE WHEN THE Poseur APP FIRST
        // STARTS. THERE IS NO Pose YET, SO MOST CONTROLS
        // ARE DISABLED
        else if (mode == PoseurState.STARTUP_STATE)
        {
            // THIS USES THE ARROW CURSOR
            selectCursor(Cursor.DEFAULT_CURSOR);
            
            // NOTHING IS SELECTED SO WE CAN'T EDIT YET
            enableStartupFileControls();
            setEnabledEditControls(false);
            selectionButton.setEnabled(false);
            setEnabledColorControls(false);
            toggleOutlineColorButton();
            setEnabledZoomControls(false);
            setEnabledShapeControls(false);
        }
        saveButton.setEnabled(!fileManager.isSaved());
        
        // AND UPDATE THE SLIDER
        PoseurShape selectedShape = state.getSelectedShape();
        if (selectedShape != null)
        {
            // UPDATE THE SLIDER ACCORDING TO THE SELECTED
            // SHAPE'S ALPHA (TRANSPARENCY) VALUE, IF THERE
            // EVEN IS A SELECTED SHAPE
            transparencySlider.setValue(selectedShape.getAlpha());
        }    

        // REDRAW EVERYTHING
       
        zoomableCanvas.repaint();    
        this.repaint();
    }

    
    // PRIVATE HELPER METHODS

    /**
     * This helper method constructs and lays out all GUI components, initializing
     * them to their default startup state.
     */
    private void initGUI()
    {
        // MAKE THE COMPONENTS
        constructGUIControls();
        
        // AND ARRANGE THEM
        layoutGUIControls();
    }
    
    /**
     * Helper method that constructs all the GUI controls and
     * loads them with their necessary art and data.
     */    
    private void constructGUIControls()
    {
        // SOME COMPONENTS MAY NEED THE STATE MANAGER
        // FOR INITIALIZATION, SO LET'S GET IT
        Poseur singleton = Poseur.getPoseur();
        PoseurStateManager poseurStateManager = singleton.getStateManager();

        // LET'S START BY INITIALIZING THE CENTER AREA,
        // WHERE WE'LL RENDER EVERYTHING. WE'LL HAVE TWO
        // CANVASES AND PUT THEM INTO DIFFERENT SIDES
        // OF A JSplitPane
        canvasSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
       // if(flag==0)
        appWindow=new AnimatedSpriteViewer();
        // LET'S MAKE THE CANVAS ON THE LEFT SIDE, WHICH
        // WILL NEVER ZOOM
        
        
        // AND NOW THE CANVAS ON THE RIGHT SIDE, WHICH
        // WILL BE ZOOMABLE
        PoseCanvasState zoomableCanvasState = poseurStateManager.getZoomableCanvasState();
        zoomableCanvas = new PoseCanvas(zoomableCanvasState);
        zoomableCanvasState.setPoseCanvas(zoomableCanvas);
        zoomableCanvas.setBackground(ZOOMABLE_CANVAS_COLOR);
        
        // ULTIMATELY EVERYTHING IN THE NORTH GOES IN HERE, INCLUDING
        // TWO PANELS FULL OF JToolBars
        northPanel = new JPanel();
        northOfNorthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        southOfNorthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        southOfRightCanvas= new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        vertical = new JScrollPane(southOfRightCanvas);
        //vertical.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        
        // WE'LL BATCH LOAD THE IMAGES
        MediaTracker tracker = new MediaTracker(this);
        int idCounter = 0;

        // FILE CONTROLS
        fileToolbar  = new JToolBar();
        newButton    = (JButton)initButton(NEW_IMAGE_FILE,      fileToolbar,  tracker, idCounter++, JButton.class, null, NEW_TOOLTIP);
        openButton   = (JButton)initButton(OPEN_IMAGE_FILE,     fileToolbar,  tracker, idCounter++, JButton.class, null, OPEN_TOOLTIP);
        saveButton   = (JButton)initButton(SAVE_IMAGE_FILE,     fileToolbar,  tracker, idCounter++, JButton.class, null, SAVE_TOOLTIP);
        saveAsButton = (JButton)initButton(SAVE_AS_IMAGE_FILE,  fileToolbar,  tracker, idCounter++, JButton.class, null, SAVE_AS_TOOLTIP);
        exportButton = (JButton)initButton(EXPORT_IMAGE_FILE,   fileToolbar,  tracker, idCounter++, JButton.class, null, EXPORT_TOOLTIP);
        exitButton   = (JButton)initButton(EXIT_IMAGE_FILE,     fileToolbar,  tracker, idCounter++, JButton.class, null, EXIT_TOOLTIP);
        
        // EDITING CONTROLS
        editToolbar = new JToolBar();
        selectionButton = (JButton)initButton(SELECTION_IMAGE_FILE, editToolbar, tracker, idCounter++, JButton.class, null, SELECT_TOOLTIP);
        cutButton   = (JButton)initButton(CUT_IMAGE_FILE,    editToolbar, tracker, idCounter++, JButton.class, null, CUT_TOOLTIP);   
        copyButton  = (JButton)initButton(COPY_IMAGE_FILE,   editToolbar, tracker, idCounter++, JButton.class, null, COPY_TOOLTIP);
        pasteButton = (JButton)initButton(PASTE_IMAGE_FILE,  editToolbar, tracker, idCounter++, JButton.class, null, PASTE_TOOLTIP);
        moveToBackButton = (JButton)initButton(MOVE_TO_BACK_IMAGE_FILE, editToolbar, tracker, idCounter++, JButton.class, null, MOVE_TO_BACK_TOOLTIP);
        moveToFrontButton = (JButton)initButton(MOVE_TO_FRONT_IMAGE_FILE, editToolbar, tracker, idCounter++, JButton.class, null, MOVE_TO_FRONT_TOOLTIP);
        
        // HERE ARE OUR SHAPE SELECTION CONTROLS
        shapeToolbar = new JToolBar();
        shapeButtonGroup = new ButtonGroup();
        lineToggleButton   = (JToggleButton)initButton( LINE_SELECTION_IMAGE_FILE,      shapeToolbar, tracker, idCounter++, JToggleButton.class, shapeButtonGroup, LINE_TOOLTIP);
        rectToggleButton   = (JToggleButton)initButton( RECT_SELECTION_IMAGE_FILE,      shapeToolbar, tracker, idCounter++, JToggleButton.class, shapeButtonGroup, RECT_TOOLTIP);
        ellipseToggleButton = (JToggleButton)initButton( ELLIPSE_SELECTION_IMAGE_FILE,    shapeToolbar, tracker, idCounter++, JToggleButton.class, shapeButtonGroup, ELLIPSE_TOOLTIP);            

        // THE LINE THICKNESS SELECTION COMBO BOX WILL GO WITH THE SHAPE CONTROLS
        DefaultComboBoxModel lineThicknessModel = new DefaultComboBoxModel();
        for (int i = 0; i < NUM_STROKES_TO_CHOOSE_FROM; i++)
        {
            String imageFileName =  STROKE_SELECTION_FILE_PREFIX
                                  + (i+1)
                                  + PNG_FILE_EXTENSION;
            Image img = batchLoadImage(imageFileName, tracker, idCounter++);
            ImageIcon ii = new ImageIcon(img);
            lineThicknessModel.addElement(ii);
        }
        lineStrokeSelectionComboBox = new JComboBox(lineThicknessModel);

        // NOW THE ZOOM TOOLBAR
        zoomToolbar = new JToolBar();
        zoomOutButton = (JButton)initButton(ZOOM_OUT_IMAGE_FILE, zoomToolbar, tracker, idCounter++, JButton.class, null, ZOOM_OUT_TOOLTIP);
        zoomInButton = (JButton)initButton(ZOOM_IN_IMAGE_FILE, zoomToolbar, tracker, idCounter++, JButton.class, null, ZOOM_IN_TOOLTIP);
        zoomLabel = new JLabel();
        zoomLabel.setFont(ZOOM_LABEL_FONT);
        updateZoomLabel();
        dimensionsButton = (JButton)initButton(POSE_DIMENSIONS_IMAGE_FILE, zoomToolbar, tracker, idCounter++, JButton.class, null, CHANGE_POSE_DIMENSIONS_TOOLTIP);
        
        // COLOR SELECTION CONTROLS
        colorSelectionToolbar = new JToolBar();
        colorButtonGroup = new ButtonGroup();
        outlineColorSelectionButton = (ColorToggleButton)initButton(OUTLINE_COLOR_IMAGE_FILE, colorSelectionToolbar, tracker, idCounter++, ColorToggleButton.class, colorButtonGroup, OUTLINE_TOOLTIP);
        outlineColorSelectionButton.setBackground(Color.BLACK);
        fillColorSelectionButton = (ColorToggleButton)initButton(FILL_COLOR_IMAGE_FILE, colorSelectionToolbar, tracker, idCounter++, ColorToggleButton.class, colorButtonGroup, FILL_TOOLTIP);
        fillColorSelectionButton.setBackground(Color.WHITE);
        outlineColorSelectionButton.setSelected(true);
        
        // AND LET'S LOAD THE COLOR PALLET FROM AN XML FILE
        ColorPalletLoader cpl = new ColorPalletLoader();
        ColorPalletState cps = new ColorPalletState();
        cpl.initColorPallet(COLOR_PALLET_SETTINGS_XML, cps);

        // NOW LET'S SETUP THE COLOR PALLET. USING THE
        // STATE WE JUST LOADED. NOW MAKE OUR COLOR PALLET
        // AND MAKE SURE THEY KNOW ABOUT ONE ANOTHER
        colorPallet = new ColorPallet(cps);
        cps.setView(colorPallet);

        // THIS CONTROL WILL LET US CHANGE THE COLORS IN THE COLOR PALLET
        customColorSelectorButton = (JButton)initButton(CUSTOM_COLOR_SELECTOR_IMAGE_FILE, colorSelectionToolbar, tracker, idCounter++, JButton.class, null, CUSTOM_COLOR_TOOLTIP);
        
        // AND THE TRANSPARENCY SLIDER AND LABEL
        alphaLabel = new JLabel(ALPHA_LABEL_TEXT);
        alphaLabel.setFont(ALPHA_LABEL_FONT);
        alphaLabel.setBackground(ALPHA_BACKGROUND_COLOR);
        transparencySlider = new JSlider(JSlider.HORIZONTAL, TRANSPARENT, OPAQUE, OPAQUE);
        transparencySlider.setBackground(ALPHA_BACKGROUND_COLOR);
        transparencySlider.setMajorTickSpacing(ALPHA_MAJOR_TICK_SPACING);
        transparencySlider.setMinorTickSpacing(ALPHA_MINOR_TICK_SPACING);
        transparencySlider.setPaintLabels(true);
        transparencySlider.setPaintTicks(true);
        transparencySlider.setPaintTrack(true);
        transparencySlider.setToolTipText(ALPHA_TOOLTIP);
        transparencySlider.setSnapToTicks(false);
        
        //labelPoseList=new LinkedList<>();
        labelPoses=appWindow.getLabelPoses();
        
        try {                
          imagePose = ImageIO.read(new File(EDITPOSE_IMAGE_FILE));
          System.out.println("Image has read");
           picLabelPose = new JLabel(new ImageIcon( imagePose ));
          
           

       } catch (IOException ex) {
           System.out.println("Did not read Image");
       }
        
        jtbEdit=new JToolBar();
        idCounter = 0;
        btnNewPose  = (JButton)initButton(NEWPOSE_IMAGE_FILE,      jtbEdit,  tracker, idCounter++, JButton.class, null, NEWPOSE_TOOLTIP);
        btnSavePose  = (JButton)initButton(SAVEPOSE_IMAGE_FILE,      jtbEdit,  tracker, idCounter++, JButton.class, null, SAVEPOSE_TOOLTIP);
        btnOpenPose = (JButton)initButton(OPENPOSE_IMAGE_FILE,      jtbEdit,  tracker, idCounter++, JButton.class, null, OPENPOSE_TOOLTIP);
        btnDeletePose = (JButton)initButton(DELETEPOSE_IMAGE_FILE,      jtbEdit,  tracker, idCounter++, JButton.class, null, DELETEPOSE_TOOLTIP);
        btnDuplicatePose = (JButton)initButton(DUPLICATEPOSE_IMAGE_FILE,      jtbEdit,  tracker, idCounter++, JButton.class, null, DUPLICATEPOSE_TOOLTIP);
       // btnReorderPose = (JButton)initButton(REORDERPOSE_IMAGE_FILE,      jtbEdit,  tracker, idCounter++, JButton.class, null, DELETEPOSE_TOOLTIP);
        btnShiftLeftPose = (JButton)initButton(SHIFTLEFT_IMAGE_FILE,      jtbEdit,  tracker, idCounter++, JButton.class, null, LEFTPOSE_TOOLTIP);
        btnShifRighttPose = (JButton)initButton(SHIFTRIGHT_IMAGE_FILE,      jtbEdit,  tracker, idCounter++, JButton.class, null, RIGHTPOSE_TOOLTIP);
        btnSetDurationPose = (JButton)initButton(DURATIONPOSE_IMAGE_FILE,      jtbEdit,  tracker, idCounter++, JButton.class, null, DURATIONPOSE_TOOLTIP);
        northOfZoomableCanvasPanel=new JPanel();
        northOfZoomableCanvasPanel.setLayout(new BorderLayout());
        
        
        try{
            if(!appWindow.getCombo().getSelectedItem().toString().equals("Select Animation State")){
            
                this.setDisablePoseButton(true);
            }else{
                setDisablePoseButton(false);
                
            }
        }catch(NullPointerException e){
                setDisablePoseButton(false);
        }
        
        // NOW WE NEED TO WAIT FOR ALL THE IMAGES THE
        // MEDIA TRACKER HAS BEEN GIVEN TO FULLY LOAD
        try
        {
            tracker.waitForAll();
        }
        catch(InterruptedException ie)
        {
            // LOG THE ERROR
            Logger.getLogger(PoseurGUI.class.getName()).log(Level.SEVERE, null, ie);           
        }
    }
        
    /**
     * This helper method locates all the components inside the frame. Note
     * that it does not put most buttons into their proper toolbars because 
     * that was already done for most when they were initialized by initButton.
     */
    private void layoutGUIControls()
    {
        // LET'S PUT THE TWO CANVASES INSIDE 
        // THE SPLIT PANE. WE'LL PUT THE DIVIDER
        // RIGHT IN THE MIDDLE AND WON'T LET
        // THE USER MOVE IT - FOOLPROOF DESIGN!
        
       // if(flag==0)
        canvasSplitPane.setLeftComponent(appWindow);
        canvasSplitPane.setDividerLocation(450);
        canvasSplitPane.setRightComponent(zoomableCanvas);
        //canvasSplitPane.setResizeWeight(0.5);
        canvasSplitPane.setEnabled(false);
        
        // PUT THE COMBO BOX IN THE SHAPE TOOLBAR
        shapeToolbar.add(lineStrokeSelectionComboBox);
        
        // ARRANGE THE COLOR SELECTION TOOLBAR
        colorSelectionToolbar.add(colorPallet);        
        colorSelectionToolbar.add(customColorSelectorButton);
        colorSelectionToolbar.add(alphaLabel);
        colorSelectionToolbar.add(transparencySlider);
  
        zoomToolbar.add(zoomLabel);
        // NOW ARRANGE THE TOOLBARS
        northOfNorthPanel.add(fileToolbar);
        northOfNorthPanel.add(editToolbar);
        northOfNorthPanel.add(shapeToolbar);
        southOfNorthPanel.add(zoomToolbar);
        southOfNorthPanel.add(colorSelectionToolbar);
        
        // NOW PUT ALL THE CONTROLS IN THE NORTH
        northPanel.setLayout(new BorderLayout());
        northPanel.add(northOfNorthPanel, BorderLayout.NORTH);
        northPanel.add(southOfNorthPanel, BorderLayout.SOUTH);        
        
        zoomableCanvas.setLayout(new BorderLayout());
        northOfZoomableCanvasPanel.add(picLabelPose, BorderLayout.NORTH);
        northOfZoomableCanvasPanel.add(jtbEdit, BorderLayout.CENTER);
        
        Image image;
        JLabel labelBlank = null;
        try {                
          image = ImageIO.read(new File(BLANKLABEL_IMAGE_FILE));
          System.out.println("Image has read");
           labelBlank = new JLabel(new ImageIcon( image ));

       } catch (IOException ex) {
           System.out.println("Did not read Image");
       }
        
        JPanel test=new JPanel();
       //sequenceLabels[0]=new JLabel(new ImageIcon(imagePose));
        southOfRightCanvas.add(labelBlank, BorderLayout.SOUTH);
        //test.add(vertical, BorderLayout.SOUTH);
        southOfRightCanvas.updateUI();
        zoomableCanvas.add(vertical, BorderLayout.SOUTH);
        
        //zoomableCanvas.add(southOfRightCanvas, BorderLayout.SOUTH);
       
       
        
        zoomableCanvas.add(northOfZoomableCanvasPanel, BorderLayout.NORTH);
        
        // AND NOW PUT EVERYTHING INSIDE THE FRAME
        add(northPanel, BorderLayout.NORTH);
        add(canvasSplitPane, BorderLayout.CENTER);
        zoomableCanvas.repaint();
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

    /**
     * This method constructs and registers all the event handlers
     * for all the GUI controls.
     */
    private void initHandlers()
    {
        // THIS WILL HANDLE THE SCENARIO WHEN THE USER CLICKS ON
        // THE X BUTTON, WE'LL WANT A CUSTOM RESPONSE
        PoseurWindowHandler pwh = new PoseurWindowHandler();
        this.addWindowListener(pwh);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        // FILE TOOLBAR HANDLER
        NewSpriteHandler newsph=new NewSpriteHandler();
        newButton.addActionListener(newsph);
        OpenSpriteHandler osh = new OpenSpriteHandler();
        openButton.addActionListener(osh);
       
        ExportPoseHandler eph = new ExportPoseHandler();
        //exportButton.addActionListener(eph);
        SavePoseHandler sph=new SavePoseHandler();
        btnSavePose.addActionListener(sph);
        ExitHandler eh = new ExitHandler();
        exitButton.addActionListener(eh);
        
        // EDIT TOOLBAR HANDLER
        StartSelectionHandler startSH = new StartSelectionHandler();
        selectionButton.addActionListener(startSH);
        CutHandler cutEh = new CutHandler();
        cutButton.addActionListener(cutEh);
        CopyHandler copyEh = new CopyHandler();
        copyButton.addActionListener(copyEh);
        PasteHandler pasteEh = new PasteHandler();
        pasteButton.addActionListener(pasteEh);
        MoveToBackHandler mtbh = new MoveToBackHandler();
        moveToBackButton.addActionListener(mtbh);
        MoveToFrontHandler mtfh = new MoveToFrontHandler();
        moveToFrontButton.addActionListener(mtfh);
        
        // SHAPE SELECTION HANDLERS
        LineSelectionHandler lsh = new LineSelectionHandler();
        lineToggleButton.addActionListener(lsh);
        RectangleSelectionHandler rsh = new RectangleSelectionHandler();
        rectToggleButton.addActionListener(rsh);
        EllipseSelectionHandler esh = new EllipseSelectionHandler();
        ellipseToggleButton.addActionListener(esh);
        StrokeSelectionHandler ssh = new StrokeSelectionHandler();
        lineStrokeSelectionComboBox.addItemListener(ssh);
                
        // ZOOM HANDLERS
        ZoomOutHandler zoh = new ZoomOutHandler();
        zoomOutButton.addActionListener(zoh);
        ZoomInHandler zih = new ZoomInHandler();
        zoomInButton.addActionListener(zih);
        ChangePoseDimensionsHandler cpdh = new ChangePoseDimensionsHandler();
        dimensionsButton.addActionListener(cpdh);
        
        // COLOR CONTROL HANDLERS
        OutlineColorHandler acal = new OutlineColorHandler();
        outlineColorSelectionButton.addActionListener(acal);
        FillColorHandler fcal = new FillColorHandler();
        fillColorSelectionButton.addActionListener(fcal);
        ColorPalletHandler cph = new ColorPalletHandler();
        colorPallet.registerColorPalletHandler(cph);
        CustomColorHandler cch = new CustomColorHandler();
        customColorSelectorButton.addActionListener(cch);
        AlphaChangeHandler ach = new AlphaChangeHandler();
        transparencySlider.addChangeListener(ach);
        
       
        
        //ADD POSE CONTROL HANDLERS
        DeletePoseHandler dph=new DeletePoseHandler();
        btnDeletePose.addActionListener(dph);
        DuplicatePoseHandler dpph=new DuplicatePoseHandler();
        btnDuplicatePose.addActionListener(dpph);
        DurationPoseHandler durph=new DurationPoseHandler();
        btnSetDurationPose.addActionListener(durph);
        NewPoseHandler newph=new NewPoseHandler();
        btnNewPose.addActionListener(newph);
        OpenPoseHandler openph=new OpenPoseHandler();
        btnOpenPose.addActionListener(openph);
        ShiftRightHandler srh=new ShiftRightHandler();
        //btnReorderPose.addActionListener(reorderph);
        btnShifRighttPose.addActionListener(srh);
        ShiftLeftHandler slh=new ShiftLeftHandler();
        //btnReorderPose.addActionListener(reorderph);
        btnShiftLeftPose.addActionListener(slh);
        
        
        
        
        // CANVAS MOUSE HANDLERS
        PoseCanvasMouseHandler rsmh = new PoseCanvasMouseHandler();
        zoomableCanvas.addMouseListener(rsmh);
        zoomableCanvas.addMouseMotionListener(rsmh);
        
        // THIS HANDLER IS CALLED WHEN THE COMPONENT IS 
        // FIRST SIZED TO BE DISPLAYED. WE WISH TO CALCULATE
        // THE POSE AREA AT THAT TIME, SO WE'LL DO IT FOR
        // BOTH CANVASES
        PoseCanvasComponentHandler pcch = new PoseCanvasComponentHandler();

        zoomableCanvas.addComponentListener(pcch);
    }
       
    // METHODS FOR ENABLING AND DISABLING GROUPS OF CONTROLS.
    // THESE METHODS ALL SUPPOR THE updateMode METHOD. I'LL
    // SPARE YOU DESCRIPTIONS OF EACH ONE
    
    private void enableStartupFileControls()
    {
        // THESE BUTTONS ARE ALWAYS ENABLED
        newButton.setEnabled(true);
        openButton.setEnabled(true);
        exitButton.setEnabled(true);
        
        // THESE BUTTONS START OFF AS DISABLED
        saveButton.setEnabled(false);
        saveAsButton.setEnabled(false);
        exportButton.setEnabled(false);
    }
    
    private void enableSaveAsAndExport()
    {
        // THESE ARE ENABLED AS SOON AS WE START EDITING
        saveAsButton.setEnabled(true);
        exportButton.setEnabled(true);
    }    
        
    private void setEnabledEditControls(boolean isEnabled)
    {
        // THE SELECTION BUTTON NEEDS TO BE CHECKED SEPARATELY

        // THESE ARE EASY, JUST DO AS THEY'RE TOLD
        cutButton.setEnabled(isEnabled);
        copyButton.setEnabled(isEnabled);
        moveToBackButton.setEnabled(isEnabled);
        moveToFrontButton.setEnabled(isEnabled);
                
        // WE ONLY WANT PASTE ENABLED IF THERE IS
        // SOMETHING ON THE CLIPBOARD
        Poseur singleton = Poseur.getPoseur();
        PoseurStateManager state = singleton.getStateManager();
        pasteButton.setEnabled(state.isShapeOnClipboard());
    }
    
    private void setEnabledColorControls(boolean isEnabled)
    {
        outlineColorSelectionButton.setEnabled(isEnabled);
        fillColorSelectionButton.setEnabled(isEnabled);
        customColorSelectorButton.setEnabled(isEnabled);
        colorPallet.setEnabled(isEnabled);
        outlineColorSelectionButton.setSelected(isEnabled);
        alphaLabel.setEnabled(isEnabled);
        transparencySlider.setEnabled(isEnabled);
    }

    private void setEnabledZoomControls(boolean isEnabled)
    {
        zoomOutButton.setEnabled(isEnabled);
        zoomInButton.setEnabled(isEnabled);
        zoomLabel.setEnabled(isEnabled);
        dimensionsButton.setEnabled(isEnabled);
    }
    
    private void setEnabledShapeControls(boolean isEnabled)
    {
        // INIT THEM AS USABLE OR NOT
        lineToggleButton.setEnabled(isEnabled);
        rectToggleButton.setEnabled(isEnabled);
        ellipseToggleButton.setEnabled(isEnabled);
        lineStrokeSelectionComboBox.setEnabled(isEnabled);
        
        // IF THEY'RE USABLE, MAKE THE TOGGLES UNSELECTED
        if (isEnabled)
        {
            shapeButtonGroup.clearSelection();
        }
    }
    public void setDisablePoseButton(boolean isEnabled){
        btnDeletePose.setEnabled(isEnabled);
        btnDuplicatePose.setEnabled(isEnabled);
        btnNewPose.setEnabled(isEnabled);
        //btnReorderPose.setEnabled(isEnabled);
        btnSavePose.setEnabled(isEnabled);
        btnSetDurationPose.setEnabled(isEnabled);
        btnOpenPose.setEnabled(isEnabled);
        btnShifRighttPose.setEnabled(isEnabled);
        btnShiftLeftPose.setEnabled(isEnabled);
        if(!isEnabled){
        labelPoses.clear();
        
        southOfRightCanvas.removeAll();
        }
        repaint();
    }
    
    private void selectCursor(int cursorToUse)
    {
        // AND NOW SWITCH TO A CROSSHAIRS CURSOR
        Cursor arrowCursor = Cursor.getPredefinedCursor(cursorToUse);
        setCursor(arrowCursor);    
    }
    
    /*
     **This returns the entire left panel of the edit panel which is rendering panel
     * 
     * @return appWindow which is AnimaredSpriteViewer
     * 
     */
    public AnimatedSpriteViewer getAnimametedViewerPanel(){
        
        return appWindow;
    }

    public void loadSprite() {
        
       
        String fileName =promptToOpenSprite(); 
        
        // IF THE USER CANCELLED, THEN WE'LL GET A fileName
        // OF NULL, SO LET'S MAKE SURE THE USER REALLY
        // WANTS TO DO THIS ACTION BEFORE MOVING ON
        if ((fileName != null)
                &&
            (fileName.length() > 0))
        {

        appWindow.loadSprites(fileName);
        repaint();
        
        }
      
    }
    
    public void loadSprite(String name) {
        
       
      

        appWindow.loadSprites(name);
        repaint();
        
        
      
    }
    
    
        /*
         * This method 
         */
    
      public String promptToOpenSprite() {
          // WE'LL NEED THE GUI
        Poseur singleton = Poseur.getPoseur();
        PoseurGUI gui = singleton.getGUI();
        
        // AND NOW ASK THE USER FOR THE POSE TO OPEN
        JFileChooser poseFileChooser = new JFileChooser(SPRITES_XML_PATH);
        int buttonPressed = poseFileChooser.showOpenDialog(gui);
        File currentFile;
        String currentFileName = null;
        // ONLY OPEN A NEW FILE IF THE USER SAYS OK
        if (buttonPressed == JFileChooser.APPROVE_OPTION)
        {
            // GET THE FILE THE USER ENTERED
            currentFile = poseFileChooser.getSelectedFile();
            currentFileName = currentFile.getName();
            
 
            // AND PUT THE FILE NAME IN THE TITLE BAR
            String appName = gui.getAppName();
            gui.setTitle(appName + APP_NAME_FILE_NAME_SEPARATOR + currentFile);             
            
            // AND LOAD THE .pose (XML FORMAT) FILE
        }
        return currentFileName;
    
        
    }

    

    public void loadImagesOfStates() {
        // GET RID OF THE OLD SPRITE, IT MAY HAVE HAD A DIFFERENT STATE
        //spriteList.clear();
        Poseur singleton=Poseur.getPoseur();
        AnimatedSpriteViewer viewer=singleton.getSpriteViewer();
        JComboBox combo=viewer.getCombo();
        Object selectedItem = combo.getSelectedItem();
        
         if (selectedItem != null)
        {
            // THIS ISN'T AN ANIMATION STATE
            if (!selectedItem.equals(SELECT_ANIMATION_TEXT))
            {
                
                
                
                ArrayList<Sprite> spriteList=viewer.getSprites();
                // WHICH ONE IS NOW SELECTED?
                
                HashMap<String,SpriteType>spriteTypes=viewer.getSpriteTypes();
                JLabel temp;
                String selectedState = combo.getSelectedItem().toString();
                String selectedSpriteTypeName = (String)viewer.getJlist().getSelectedValue();
                SpriteType selectedSpriteType = spriteTypes.get(selectedSpriteTypeName);
                PoseList list= selectedSpriteType.getPoseList(selectedState);
                Pose tempPose;
                poseIterator=list.getPoseIterator();
                while(poseIterator.hasNext())
                {
                    tempPose = poseIterator.next();
                    //selectedSpriteType.getImage(tempPose.getImageID());
                    Image img=selectedSpriteType.getImage(tempPose.getImageID());
                    BufferedImage bufferedImg=(BufferedImage)img;
                    reSizeImage(bufferedImg);
                    temp=new JLabel(new ImageIcon(bufferedImg));
                    labelPoses.add(new SpriteTypeGUIPanel(temp,tempPose.getImageID()));
                    temp.updateUI();
                   // labelPoseList.add(temp);
                    
                }  
                
               
                
                list.getPoseIterator();
            }
        
        }
       
       southOfRightCanvas.removeAll();
       repaint();
       System.out.println("PRESSED");
    }
      
      /*
       This Method set JLabels on the south of right editing canvas, update UI.
       * 
       * @param LinkedList<JLabel> list where all the jlabels are stored in the link list.
       */
      public void setLabels(LinkedList<JLabel> list){
          //labelPoseList=list;
          southOfRightCanvas.removeAll();
          for(int i=0;i<list.size();i++){
              //southOfRightCanvas.add(list.get(i), BorderLayout.NORTH);
              
              southOfRightCanvas.updateUI();
             
          }
         // this.setLabelsPanel(labelPoses);
         southOfRightCanvas.updateUI();
      }
      
      
       /*
       This Method set JLabels on the south of right editing canvas, update UI.
       * 
       * @param LinkedList<JLabel> list where all the jlabels are stored in the link list.
       */
      public void setLabelsPanel(LinkedList<SpriteTypeGUIPanel> list){
          labelPoses=list;
          southOfRightCanvas.removeAll();
          for(int i=0;i<list.size();i++){
              
              southOfRightCanvas.add(list.get(i), BorderLayout.NORTH);
              
              southOfRightCanvas.updateUI();
             
          }
         southOfRightCanvas.updateUI();
      }
      
      
      /*
       This Method set JLabels on the south of right editing canvas, update UI.
       * 
       * @param LinkedList<JLabel> list where all the jlabels are stored in the link list.
       */
      public void removeLabels(){
       
          southOfRightCanvas.removeAll();
          southOfRightCanvas.updateUI();
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
      /*
       * returns image id
       
       */
    public int getID(){
        return imageID;
    }
    
     public void setID(int id){
      imageID=id;
    }
     
    public JPanel getSouthRightPanel(){
        return southOfRightCanvas;
    }
     
}