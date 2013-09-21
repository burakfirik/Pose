package poseur.files;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import poseur.Poseur;
import static poseur.PoseurSettings.*;
import poseur.state.ColorPalletState;

/**
 * This class can be used to load color pallet data from an XML 
 * file into a constructed ColorPalletState. Note that the XML
 * file must validate against the color_pallet_settings.xsd
 * schema. This class demonstrates how application settings can
 * be loaded dynamically from a file.
 * 
 * @author  Burak
 *          Debugging Enterprises
 * @version 1.0
 */
public class ColorPalletLoader 
{    
    /**
     * This method will extract the data found in the provided
     * XML file argument and use it to load the color pallet
     * argument.
     * 
     * @param colorPalletXMLFile Path and file name to an XML
     * file containing information about a custom color pallet. Note
     * this XML file must validate against the aforementioned schema.
     * 
     * @param colorPalletState The state manager for the color
     * pallet, we'll load all the data found in the XML file
     * inside here.
     */
    public void initColorPallet( String colorPalletXMLFile,
                                 ColorPalletState colorPalletState)
    {
        try
        {
            // LET'S EMPLOY OUR XMLUtilities CLASS
            XMLUtilities xmlUtil = new XMLUtilities();
        
            // WE'LL HOLD THE COLOR PALLET TREE HERE TEMPORARILY
            Document colorPalletDoc = xmlUtil.loadXMLDocument(colorPalletXMLFile, COLOR_PALLET_SETTINGS_SCHEMA);
        
            // FIRST LET'S GET HOW MANY COLORS ARE IN THE PALLET
            int colorPalletSize = xmlUtil.getIntData(colorPalletDoc, PALLET_SIZE_NODE);
            Color[] colorPallet = new Color[colorPalletSize];

            // THEN LET'S SEE HOW MANY ROWS OUR PALLET WILL BE ARRANGED INTO
            int colorPalletRows = xmlUtil.getIntData(colorPalletDoc, PALLET_ROWS_NODE);
        
            // NOW LET'S GET THE DEFAULT COLOR
            // FIRST GET THE NODE
            Node defaultColorNode = xmlUtil.getNodeWithName(colorPalletDoc, DEFAULT_COLOR_NODE);
        
            // AND THEN EXTRACT COLOR INFO FROM THAT NODE
            Color defaultColor = extractColorFromNode(xmlUtil, defaultColorNode);
                
            // LET'S SEE HOW MANY COLORS HAVE ACTUALLY BEEN PROVIDED BECAUSE
            // THIS WILL TELL US WHERE THE CUSTOM COLORS WILL START
            int numberOfFixedColors = xmlUtil.getNumNodesOfElement(colorPalletDoc, PALLET_COLOR_NODE); 

            // THEN LET'S GET THEM ONE AT A TIME
            for (int i = 0; i < numberOfFixedColors; i++)
            {
                // EXTRACT THE COLOR DATA FROM THE XML FILE
                Node colorNode = xmlUtil.getNodeInSequence(colorPalletDoc, PALLET_COLOR_NODE, i);
            
                // AND PUT IT IN OUR COLOR PALLET. NOTE THIS COLOR PALLET
                // WILL BE USED TO INITIALIZE THE GUI
                colorPallet[i] = extractColorFromNode(xmlUtil, colorNode);
            }
        
            // WE HAVE ALL THE DATA IN THE XML FILE
            // SO LET'S GO AHEAD AND LOAD IT
            colorPalletState.loadColorPalletState(  colorPallet,
                                                colorPalletRows,
                                                numberOfFixedColors,
                                                defaultColor);
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
    
    /**
     * Helper method that extracts color data from the colorNode argument and
     * uses it to construct and return a Color object.
     * 
     * @param xmlUtil Provides service methods for extracting info from XML files.
     * 
     * @param colorNode The node that contains the color information we're 
     * looking for.
     * 
     * @return A constructed Color object with the data found inside
     * the colorNode argument.
     */
    private Color extractColorFromNode(XMLUtilities xmlUtil, Node colorNode)
    {
        // GET THE DATA
        Node redNode = xmlUtil.getChildNodeWithName(colorNode, RED_NODE);
        String redText = redNode.getTextContent();
        int red = Integer.parseInt(redText);
        Node greenNode = xmlUtil.getChildNodeWithName(colorNode, GREEN_NODE);
        String greenText = greenNode.getTextContent();
        int green = Integer.parseInt(greenText);
        Node blueNode = xmlUtil.getChildNodeWithName(colorNode, BLUE_NODE);
        String blueText = blueNode.getTextContent();
        int blue = Integer.parseInt(blueText);        
        
        // MAKE AND RETURN THE COLOR
        Color color = new Color(red, green, blue);
        return color;
    }    
}