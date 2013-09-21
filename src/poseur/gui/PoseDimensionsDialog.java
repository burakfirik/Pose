package poseur.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import poseur.Poseur;
import static poseur.PoseurSettings.*;
import poseur.state.PoseurPose;
import poseur.state.PoseurStateManager;

/**
 * This class is used for prompting the user for new dimensions
 * for the pose.
 * 
 * @author  Burak
 *          Debugging Enterprises
 * @version 1.0
 */
public class PoseDimensionsDialog extends JDialog
{
    // WE'LL NEED ALL THESE CONTROLS FOR OUR DIALOG'S GUI
    private JLabel promptLabel;
    private JLabel poseWidthPromptLabel;
    private JTextField poseWidthPromptTextField;
    private JLabel poseHeightPromptLabel;
    private JTextField poseHeightPromptTextField;
    private JPanel buttonPanel;
    private JButton okButton;
    private JButton cancelButton;
    
    /**
     * Default constructor fully sets up the dialog for use, constructing
     * all components, laying them out, and registering all necessary
     * event handlers.
     */
    public PoseDimensionsDialog()
    {
        initDialog();
        initDialogGUI();
        initHandlers();
    }

    /**
     * This method positions this dialog in the center of the screen.
     */
    public void initLocation()
    {
        // GET THE DIMENSIONS OF THE COMPUTER'S RESOLUTION
        // FROM THE SINGLETON TOOLKIT OBJECT
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        // AND CENTER THIS DIALOG WINDOW
        int x = (screenSize.width/2) - (this.getWidth()/2);
        int y = (screenSize.height/2) - (this.getHeight()/2);
        setLocation(x, y);
    }

    public void changePoseDimensions()
    {
                String newPoseWidth = poseWidthPromptTextField.getText();
                String newPoseHeight = poseHeightPromptTextField.getText();
                try
                {
                    int poseWidth = Integer.parseInt(newPoseWidth);
                    int poseHeight = Integer.parseInt(newPoseHeight);
                    if (    (poseWidth < MIN_POSE_DIM) || 
                            (poseHeight < MIN_POSE_DIM) ||
                            (poseWidth > MAX_POSE_DIM) ||
                            (poseHeight > MAX_POSE_DIM))
                    {
                        JOptionPane.showMessageDialog(PoseDimensionsDialog.this, INVALID_POSE_DIM_TEXT, 
                                INVALID_POSE_DIM_TITLE_TEXT, JOptionPane.ERROR_MESSAGE);
                    }
                    else
                    {
                        Poseur singleton = Poseur.getPoseur();
                        PoseurStateManager state = singleton.getStateManager();
                        state.setPoseDimensions(poseWidth, poseHeight); 
                        setVisible(false);
                    }
                }
                catch(NumberFormatException | NullPointerException ex)
                {
                        JOptionPane.showMessageDialog(PoseDimensionsDialog.this, INVALID_POSE_DIM_TEXT, 
                                INVALID_POSE_DIM_TITLE_TEXT, JOptionPane.ERROR_MESSAGE);                    
                }
    }        
    
    // PRIVATE HELPER METHODS
    
    /**
     * Dialogs are just like frames, only with fewer capabilities
     * and thus overhead (memory requirements).
     */
    private void initDialog()
    {
        // DON'T LET THE USER SWITCH BACK AND FORTH BETWEEN THIS
        // DIALOG AND THE Poseur APP
        PoseurGUI gui = Poseur.getPoseur().getGUI();
        setModal(true);
        
        // GIVE THIS DIALOG A TITLE BAR
        setTitle(POSE_DIMENSIONS_DIALOG_TITLE);
    }    
    
    /**
     * This method constructs and lays out all the components inside
     * this dialog.
     */
    private void initDialogGUI()
    {
        // WE'LL NEED THESE THINGS
        Poseur singleton = Poseur.getPoseur();
        PoseurStateManager state = singleton.getStateManager();
        PoseurPose pose = state.getPose();
        
        // CONSTRUCT EVERYTHING
        promptLabel = new JLabel(POSE_DIMENSIONS_PROMPT_TEXT);
        poseWidthPromptLabel = new JLabel(POSE_WIDTH_PROMPT_TEXT);
        poseWidthPromptTextField = new JTextField(10);
        poseWidthPromptTextField.setText("" + pose.getPoseWidth());
        poseHeightPromptLabel = new JLabel(POSE_HEIGHT_PROMPT_TEXT);
        poseHeightPromptTextField = new JTextField(10);
        poseHeightPromptTextField.setText("" + pose.getPoseHeight());
        buttonPanel = new JPanel();
        okButton = new JButton(OK_TEXT);
        cancelButton = new JButton(CANCEL_TEXT);

        // WE'LL KEEP THESE TWO BUTTONS TOGETHER
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        // WE LIKE PUTTING COMPONENTS IN GRIDS IN THE 
        // MIDDLE OF DIALOGS, SO WE'LL USE GridBagLayout
        // FOR THIS
        this.setLayout(new GridBagLayout());
        addComponent(promptLabel,               0, 0, 2, 1);
        addComponent(poseWidthPromptLabel,      0, 1, 1, 1);
        addComponent(poseWidthPromptTextField,  1, 1, 1, 1);
        addComponent(poseHeightPromptLabel,     0, 2, 1, 1);
        addComponent(poseHeightPromptTextField, 1, 2, 1, 1);
        addComponent(buttonPanel,               0, 3, 2, 1);
        
        // THIS SHRINKS THE DIALOG AND THE COMPONENTS
        // TO FIT WHAT'S BEEN PUT INSIDE
        pack();
    }
    
    /**
     * This helper method lets us easily add components to our
     * dialog using GridBagLayout. 
     * 
     * @param c The control to add to the dialog, like a button.
     * 
     * @param col The column in the grid where it will start.
     * 
     * @param row The row in the grid where it will start.
     * 
     * @param colSpan The number of columns in the grid this 
     * control will span.
     * 
     * @param rowSpan The number of rows in the grid this control
     * will span.
     */
    private void addComponent(Component c, int col, int row, int colSpan, int rowSpan)
    {
        // THIS PROVIDES INTERNAL SPACING
        Insets insets = new Insets(CONTROL_INSET, CONTROL_INSET, CONTROL_INSET, CONTROL_INSET);
        
        // SETUP ALL THE SETTINGS FOR ADDING THE COMPONENT
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = col;
        gbc.gridy = row;
        gbc.gridwidth = colSpan;
        gbc.gridheight = rowSpan;
        gbc.insets = insets;
        gbc.anchor = GridBagConstraints.LINE_START;
        
        // AND HERE IT GOES
        this.add(c, gbc);
    }        
    
    /**
     * This method initializes all handlers. Because there
     * are few controls with handlers in this dialog, we
     * can simply define them here as anonymous classes, meaning
     * we define the listener classes without giving them names.
     */
    private void initHandlers()
    {
        // THIS ANONYMOUS CLASS LISTENER WILL RESPOND
        // TO WHEN THE USER PRESSES OK, OR HITS ENTER
        // WHILE IN ONE OF THE TEXT FIELDS.
        ActionListener okListener = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                // PROCESS THE CHANGE
                changePoseDimensions();
            }
        };
        // THESE 3 WILL ALL SHARE THIS HANDLER
        okButton.addActionListener(okListener);
        poseWidthPromptTextField.addActionListener(okListener);
        poseHeightPromptTextField.addActionListener(okListener);

        // THE CANCEL BUTTON WILL HAVE AN ANONYMOUS CLASS
        // HANDLER THAT JUST CLOSES THIS WINDOW
        cancelButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                setVisible(false);
            }
        });
    }       
}