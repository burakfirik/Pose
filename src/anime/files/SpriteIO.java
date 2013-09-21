/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package anime.files;

import animated_sprite_viewer.AnimatedSpriteViewer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.apache.commons.io.FileUtils;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import poseur.Poseur;
import static poseur.PoseurSettings.*;
import poseur.files.InvalidXMLFileFormatException;
import poseur.files.XMLUtilities;
import poseur.gui.PoseCanvas;
import poseur.gui.PoseurGUI;
import poseur.gui.SpriteTypeGUIPanel;
import poseur.shapes.PoseurEllipse;
import poseur.shapes.PoseurLine;
import poseur.shapes.PoseurRectangle;
import poseur.shapes.PoseurShape;
import poseur.shapes.PoseurShapeType;
import poseur.state.PoseurPose;
import poseur.state.PoseurState;
import poseur.state.PoseurStateManager;
import sprite_renderer.Pose;
import sprite_renderer.PoseList;
import sprite_renderer.SpriteType;

/**
 *
 * @author burak
 */
public class SpriteIO {
    
    
     /**
     * This method loads the contents of the spriteFileName .xml file into
     * the rendering panel. Note that this file must validate against
     * the poseur_pose.xsd schema.
     * 
     * @param poseFileName The Pose to load for editing.
     */
    public void loadSprite(String poseFileName)
    {
        // THIS WILL HELP US LOAD STUFF
        XMLUtilities xmlUtil = new XMLUtilities();
        
        // WE'RE GOING TO LOAD IT INTO THE POSE
        try
        {
            // LOAD THE XML FILE
            Document doc = xmlUtil.loadXMLDocument(poseFileName, POSE_SCHEMA);
            
            // AND THEN EXTRACT ALL THE DATA
            
            // LET'S START WITH THE POSE DIMENSIONS
            int poseWidth = xmlUtil.getIntData(doc, POSE_WIDTH_NODE);
            int poseHeight = xmlUtil.getIntData(doc, POSE_HEIGHT_NODE);
                        
            // WE'RE USING A TEMP POSE IN CASE SOMETHING GOES WRONG
            PoseurPose tempPose = new PoseurPose(poseWidth, poseHeight);
            LinkedList<PoseurShape> shapesList = tempPose.getShapesList();
            
            // LET'S GET THE SHAPE LIST
            NodeList shapeNodes = doc.getElementsByTagName(POSEUR_SHAPE_NODE);
            for (int i = 0; i < shapeNodes.getLength(); i++)
            {
                // GET THE NODE, THEN WE'LL EXTRACT DATA FROM IT
                // TO FILL IN THE shapeToAdd SHAPE
                Node node = shapeNodes.item(i);
                PoseurShape shapeToAdd;
                               
                // WHAT TYPE IS IT?
                Node geometryNode = xmlUtil.getChildNodeWithName(node, GEOMETRY_NODE);
                NamedNodeMap attributes = geometryNode.getAttributes();
                String shapeTypeText = attributes.getNamedItem(SHAPE_TYPE_ATTRIBUTE).getTextContent();
                PoseurShapeType shapeType = PoseurShapeType.valueOf(shapeTypeText);
                
                // A RECTANGLE?
                if (shapeType == PoseurShapeType.RECTANGLE)
                {
                    double x = Double.parseDouble(attributes.getNamedItem(X_ATTRIBUTE).getTextContent());
                    double y = Double.parseDouble(attributes.getNamedItem(Y_ATTRIBUTE).getTextContent());
                    double width = Double.parseDouble(attributes.getNamedItem(WIDTH_ATTRIBUTE).getTextContent());
                    double height = Double.parseDouble(attributes.getNamedItem(HEIGHT_ATTRIBUTE).getTextContent());
                    Rectangle2D.Double geometry = new Rectangle2D.Double(x, y, width, height);
                    shapeToAdd = new PoseurRectangle(geometry);
                }
                // AN ELLIPSE?
                else if (shapeType == PoseurShapeType.ELLIPSE)
                {
                    double x = Double.parseDouble(attributes.getNamedItem(X_ATTRIBUTE).getTextContent());
                    double y = Double.parseDouble(attributes.getNamedItem(Y_ATTRIBUTE).getTextContent());
                    double width = Double.parseDouble(attributes.getNamedItem(WIDTH_ATTRIBUTE).getTextContent());
                    double height = Double.parseDouble(attributes.getNamedItem(HEIGHT_ATTRIBUTE).getTextContent());
                    Ellipse2D.Double geometry = new Ellipse2D.Double(x, y, width, height);
                    shapeToAdd = new PoseurEllipse(geometry);                        
                }
                // IT MUST BE A LINE
                else
                {
                    double x1 = Double.parseDouble(attributes.getNamedItem(X1_ATTRIBUTE).getTextContent());
                    double y1 = Double.parseDouble(attributes.getNamedItem(Y1_ATTRIBUTE).getTextContent());
                    double x2 = Double.parseDouble(attributes.getNamedItem(X2_ATTRIBUTE).getTextContent());
                    double y2 = Double.parseDouble(attributes.getNamedItem(Y2_ATTRIBUTE).getTextContent());
                    Line2D.Double geometry = new Line2D.Double(x1, y1, x2, y2);
                    shapeToAdd = new PoseurLine(geometry);                   
                }
                
                // FIRST GET THE OUTLINE THICKNESS
                Node outlineNode = xmlUtil.getChildNodeWithName(node, OUTLINE_THICKNESS_NODE);
                int outlineThickness = Integer.parseInt(outlineNode.getTextContent());
                BasicStroke outlineStroke = new BasicStroke(outlineThickness);
                
                // THEN THE OUTLINE COLOR
                //Color outlineColor = extractColor(xmlUtil, node, OUTLINE_COLOR_NODE);
                
                // THEN THE FILL COLOR
                //Color fillColor = extractColor(xmlUtil, node, FILL_COLOR_NODE);

                // AND THE TRANSPARENCY
                Node alphaNode = xmlUtil.getChildNodeWithName(node, ALPHA_NODE);
                int alpha = Integer.parseInt(alphaNode.getTextContent());

                // AND FILL IN THE REST OF THE SHAPE DATA
                shapeToAdd.setAlpha(alpha);
                //shapeToAdd.setFillColor(fillColor);
                //shapeToAdd.setOutlineColor(outlineColor);
                shapeToAdd.setOutlineThickness(outlineStroke);
                
                // WE'VE LOADED THE SHAPE, NOW GIVE IT TO THE POSE
                shapesList.add(shapeToAdd);
            }
           
            // EVERYTHING HAS LOADED WITHOUT FAILING, SO LET'S
            // FIRST LOAD THE DATA INTO THE REAL POSE
            Poseur singleton = Poseur.getPoseur();
            PoseurStateManager stateManager = singleton.getStateManager();
            PoseurPose actualPose = stateManager.getPose();
            actualPose.loadPoseData(tempPose);            
            
            // TELL THE USER ABOUT OUR SUCCESS
            PoseurGUI gui = singleton.getGUI();
            JOptionPane.showMessageDialog(
                gui,
                POSE_LOADED_TEXT,
                POSE_LOADED_TITLE_TEXT,
                JOptionPane.INFORMATION_MESSAGE);

            // AND ASK THE GUI TO UPDATE
            singleton.getStateManager().setState(PoseurState.SELECT_SHAPE_STATE);
        }
        catch(InvalidXMLFileFormatException | DOMException | HeadlessException ex)
        {
            // SOMETHING WENT WRONG LOADING THE .pose XML FILE
            Poseur singleton = Poseur.getPoseur();
            PoseurGUI gui = singleton.getGUI();
            JOptionPane.showMessageDialog(
                gui,
                POSE_LOADING_ERROR_TEXT,
                POSE_LOADING_ERROR_TITLE_TEXT,
                JOptionPane.ERROR_MESSAGE);            
        }    
    }
    
    
     /**
     * This method saves the pose currently being edited to the poseFile. Note
     * that it will be saved as a .pose file, which is an XML-format that will
     * conform to the poseur_pose.xsd schema.
     * 
     * @param poseFile The file to write the pose to.
     * 
     * @param spriteFolder where all the sprite locations
     * 
     * @return true if the file is successfully saved, false otherwise. It's
     * possible that another program could lock out ours from writing to it,
     * so we need to let the caller know when this happens.
     */
    public boolean saveSprite(File spriteFile, File spriteFolder)
    {
        // GET THE POSE AND ITS DATA THAT WE HAVE TO SAVE
        
        Poseur singleton = Poseur.getPoseur();
        PoseurStateManager poseurStateManager = singleton.getStateManager();
        PoseurPose poseToSave = poseurStateManager.getPose();
        LinkedList<PoseurShape> shapesList = poseToSave.getShapesList();
        
        
        
        
        try 
        {
            // THESE WILL US BUILD A DOC
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            
            
            // FIRST MAKE THE DOCUMENT
            Document doc = docBuilder.newDocument();
            Document docFolder=docBuilder.newDocument();
            
            // THEN THE ROOT ELEMENT
            Element rootElement = doc.createElement(SPRITE_TYPES_NODE);
            Element rootElementFolder = docFolder.createElement(SPRITE_TYPE_NODE);
            doc.appendChild(rootElement);
            docFolder.appendChild(rootElementFolder);
            
            // THEN MAKE AND ADD THE WIDTH, HEIGHT, AND NUM SHAPES
            Element spriteTypeElement = makeElement(doc, rootElement, 
                    SPRITE_TYPE_NODE, "" + spriteFile.getName().substring(0, spriteFile.getName().length()-4));
            Element widthTypeElement = makeElement(docFolder, rootElementFolder, 
                    WIDTH_NODE, "128" );
            Element heightTypeElement = makeElement(docFolder, rootElementFolder, 
                    HEIGHT_NODE, "128" );
            Element imagesListTypeElement = makeElement(docFolder, rootElementFolder, 
                    IMAGES_LIST_NODE, "" );   
            Element imageFileTypeElement = makeElement(docFolder, imagesListTypeElement, 
                    IMAGE_FILE_NODE);
            imageFileTypeElement.setAttribute("id", "1000");
            imageFileTypeElement.setAttribute("file_name", "0.png");
            Element animationsListTypeElement = makeElement(docFolder, rootElementFolder, 
                    ANIMATIONS_LIST_NODE, "" ); 
            Element animationStateElement = makeElement(docFolder, animationsListTypeElement, 
                    ANIMATION_STATE, "" );
            Element stateElement = makeElement(docFolder, animationStateElement, 
                   STATE,"NULL");
            Element animationSequenceElement = makeElement(docFolder, animationStateElement, 
                    ANIMATION_SEQUENCE );
            //stateElement.setNodeValue("DILE");
            Element poseElement = makeElement(docFolder, animationSequenceElement, 
                    POSE_NODE );
            poseElement.setAttribute("image_id", "1000");
            poseElement.setAttribute("duration", "10");
            
           
           //Save all the xml tree data structure into a ectual xml file 
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, YES_VALUE);
            transformer.setOutputProperty(XML_INDENT_PROPERTY, XML_INDENT_VALUE);
            DOMSource source = new DOMSource(doc);
            DOMSource sourceFolder = new DOMSource(docFolder);
            StreamResult result = new StreamResult(spriteFile);
            StreamResult result2 = new StreamResult(spriteFolder);
            
            
            // SAVE THE POSE TO AN XML FILE
            transformer.transform(source, result);  
            transformer.transform(sourceFolder, result2);
            
            // WE MADE IT THIS FAR WITH NO ERRORS
            PoseurGUI gui = singleton.getGUI();
            JOptionPane.showMessageDialog(
                gui,
                SPRITE_SAVED_TEXT,
                SPRITE_SAVED_TITLE_TEXT,
                JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        catch( TransformerException|ParserConfigurationException | DOMException | HeadlessException ex)
        {
            // SOMETHING WENT WRONG WRITING THE XML FILE
            PoseurGUI gui = singleton.getGUI();
            JOptionPane.showMessageDialog(
                gui,
                POSE_SAVING_ERROR_TEXT,
                POSE_SAVING_ERROR_TITLE_TEXT,
                JOptionPane.ERROR_MESSAGE);  
            return false;
        }    
    }
    
    
     /**
     * This method saves the state currently being edited.. Note
     * that it will be saved as a .pose file, which is an XML-format that will
     * conform to the poseur_pose.xsd schema.
     * 
     * @param newState name of the new state.
     * 
     * @return true if the file is successfully saved, false otherwise. It's
     * possible that another program could lock out ours from writing to it,
     * so we need to let the caller know when this happens.
     */
    
     public boolean saveState(String newState) {
         //String spriteTypeName ;
         //HashMap<String, SpriteType>
        Poseur singleton = Poseur.getPoseur();
        PoseurGUI gui = singleton.getGUI();
        AnimatedSpriteViewer viewer=gui.getAnimametedViewerPanel();
          
            String spriteTypeName=viewer.getJlist().getSelectedValue().toString();
            HashMap<String, SpriteType> spriteTypes=viewer.getSpriteTypes();
        try{
            
          
            
            SpriteType spriteType=spriteTypes.get(spriteTypeName);
            String xsdFile=SPRITES_DATA_PATH+SPRITE_TYPE_SCHEMA_FILE;
        
            SpriteType sprite=viewer.getSpriteTypes().get(spriteTypeName);
        
            File newXML=new File((SPRITE_TYPES_XML_PATH+spriteTypeName+"/"+spriteTypeName+".xml"));
        try{
         // THESE WILL US BUILD A DOC
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        
        
        // FIRST MAKE THE DOCUMENT
        Document doc = docBuilder.newDocument();
        
        // THEN THE ROOT ELEMENT
        Element rootElement = doc.createElement(SPRITE_TYPE_NODE);
        
        doc.appendChild(rootElement);
        
        // THEN MAKE AND ADD THE WIDTH, HEIGHT, AND NUM SHAPES
            
            Element widthTypeElement = makeElement(doc, rootElement, 
                    WIDTH_NODE, ""+sprite.getWidth());
            Element heightTypeElement = makeElement(doc, rootElement, 
                    HEIGHT_NODE, ""+sprite.getHeight());
            Element imagesListTypeElement = makeElement(doc, rootElement, 
                    IMAGES_LIST_NODE, "" );   
            //Min and max needed to find the pic id name.
            int max=1;
            int min=100;
            int maxID=0;
            //iterate though the states
             int y=1;
            Iterator <String> state=sprite.getAnimationStates();
            for(int i=1;i<=sprite.getSpriteImages().size();i++){
                
               //as long as there is state, keep iterating. 
                out2:
                while(state.hasNext()){
                    //iterate through each single state to find max pose number 
                    String st=state.next(); 
                  
                    //for each state get the entire pose list.
                    Iterator <Pose> pose=sprite.getPoseList(st).getPoseIterator();
                    
                    //iterate in the pose list to get the image id number.
                    for(int j=0;j<sprite.getPoseList(st).getNumOfPoses();j++){
                        //check the image id of each pose, identify the min and max
                        Pose p1=pose.next();
                        int id=p1.getImageID();
                           
                            if(id>=max){
                                max=id;
                            }
                            if(id<=min){
                                min=id;
                            }
                            //if default value, if it is one then skip go forward
                            if(id==1000){
                                min=max=1;
                            }
                            if(id>maxID){
                                maxID=id;
                            }
                        
                    }
                    
                    //create a node from min to max and add them under the imagesList node.
                   
                    for(int k=min;k<=max;k++){
                        int t=sprite.getPoseList(st).getNumOfPoses();
                        Element imageFileTypeElement = makeElement(doc, imagesListTypeElement, 
                        IMAGE_FILE_NODE);
                        imageFileTypeElement.setAttribute("id", ""+y);
                        imageFileTypeElement.setAttribute("file_name", ""+spriteTypeName+"_"+st.toUpperCase()+"_"+y+".png");
                        y++;
                    }
                    min=100;
                    max=1;
                }
                
                
            }
            
            Element animationsListTypeElement = makeElement(doc, rootElement, 
                    ANIMATIONS_LIST_NODE, "" ); 
            state=sprite.getAnimationStates();
            out:
            while(state.hasNext()){
                
                String st=state.next();
                if(st.equals("NULL")){
                    
                    continue out;
                }
                Element animationStateElement = makeElement(doc, animationsListTypeElement, 
                        ANIMATION_STATE, "" );
                //new state element added to the node
                Element stateElement = makeElement(doc, animationStateElement, 
                        STATE,""+st); 
                Element animationSequenceElement = makeElement(doc, animationStateElement, 
                        ANIMATION_SEQUENCE );
                Iterator <Pose> poseList=sprite.getPoseList(st).getPoseIterator();
                for(int j=0;j<sprite.getPoseList(st).getNumOfPoses();j++){
                        //check the image id of each pose, identify the min and max
                    Pose p1=poseList.next();
                    Element poseElement = makeElement(doc, animationSequenceElement, 
                        POSE_NODE );
                    poseElement.setAttribute("image_id", ""+p1.getImageID());
                    poseElement.setAttribute("duration", ""+p1.getDurationInFrames());
               }
            
            }
            
            Element animationStateElement = makeElement(doc, animationsListTypeElement, 
                    ANIMATION_STATE, "" );
            
            //new state element added to the node
            Element stateElement = makeElement(doc, animationStateElement, 
                   STATE,""+newState);
            
            Element animationSequenceElement = makeElement(doc, animationStateElement, 
                    ANIMATION_SEQUENCE );
            //stateElement.setNodeValue("DILE");
            Element poseElement = makeElement(doc, animationSequenceElement, 
                    POSE_NODE );
            poseElement.setAttribute("image_id", "1000");
            poseElement.setAttribute("duration", "10");
            maxID=0;
            
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, YES_VALUE);
            transformer.setOutputProperty(XML_INDENT_PROPERTY, XML_INDENT_VALUE);
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(newXML);
            
            // SAVE THE POSE TO AN XML FILE
            transformer.transform(source, result); 
          
       
            
           AnimatedSpriteViewer view=gui.getAnimametedViewerPanel();
           view.loadSpritesFromState(spriteTypeName);
            
            
            JOptionPane.showMessageDialog(
                gui,
                STATE_SAVED_TEXT,
                STATE_SAVED_TITLE_TEXT,
                JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        catch( ParserConfigurationException | TransformerException | DOMException | HeadlessException ex)
        {
            // SOMETHING WENT WRONG WRITING THE XML FILE
           
            JOptionPane.showMessageDialog(
                gui,
                POSE_SAVING_ERROR_TEXT,
                POSE_SAVING_ERROR_TITLE_TEXT,
                JOptionPane.ERROR_MESSAGE);  
            ex.printStackTrace();
            return false;
        }   
        
        }catch(NullPointerException e){
            JOptionPane.showMessageDialog(viewer, "You did not select a spritetype to create state for!", STATE, TRANSPARENT);
            System.out.println("You did not select a Sprite to create state for");
            e.printStackTrace();
            return false;
        }
    }
    
     
     public boolean deleteState(String filePath) {
        
        
        Poseur singleton = Poseur.getPoseur();
        PoseurGUI gui = singleton.getGUI();
        AnimatedSpriteViewer viewer=gui.getAnimametedViewerPanel();
        String stateToDelete=viewer.getCombo().getSelectedItem().toString();
        try{
            String spriteTypeName=viewer.getJlist().getSelectedValue().toString();
            HashMap<String, SpriteType> spriteTypes=viewer.getSpriteTypes();
            SpriteType spriteType=spriteTypes.get(spriteTypeName);
            String xsdFile=SPRITES_DATA_PATH+SPRITE_TYPE_SCHEMA_FILE;
        
            SpriteType sprite=viewer.getSpriteTypes().get(spriteTypeName);
            File newXML=new File(filePath);
            //File newXML=new File((SPRITE_TYPES_XML_PATH+spriteTypeName+"/"+spriteTypeName+".xml"));
        try{
         // THESE WILL US BUILD A DOC
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        
        
        // FIRST MAKE THE DOCUMENT
        Document doc = docBuilder.newDocument();
        
        // THEN THE ROOT ELEMENT
        Element rootElement = doc.createElement(SPRITE_TYPE_NODE);
        
        doc.appendChild(rootElement);
        
        // THEN MAKE AND ADD THE WIDTH, HEIGHT, AND NUM SHAPES
           
            Element widthTypeElement = makeElement(doc, rootElement, 
                    WIDTH_NODE, ""+sprite.getWidth());
            Element heightTypeElement = makeElement(doc, rootElement, 
                    HEIGHT_NODE, ""+sprite.getHeight());
            Element imagesListTypeElement = makeElement(doc, rootElement, 
                    IMAGES_LIST_NODE, "" );   
            //Min and max needed to find the pic id name.
            
            
            
            Iterator <String> state=sprite.getAnimationStates();
            while(state.hasNext()){
                    //iterate through each single state to find max pose number 
                    String st=state.next();  
                    //for each state get the entire pose list.
                    Iterator <Pose> pose=sprite.getPoseList(st).getPoseIterator();
                    //iterate in the pose list to get the image id number.
                    HashMap<String, String> idMap=new HashMap();
                    HashMap<String, String> idMapDelete=new HashMap();
                    for(int j=0;j<sprite.getPoseList(st).getNumOfPoses();j++){
                        //check the image id of each pose, identify the min and max
                        Pose p1=pose.next();
                        int id=p1.getImageID();
                          
                        if(id!=1000&&!stateToDelete.equals(st))
                        idMap.put(""+p1.getImageID(), ""+spriteTypeName+"_"+(st+"_"+id).toUpperCase()+".png");
                        if(id!=1000&&stateToDelete.equals(st))
                        idMapDelete.put(""+p1.getImageID(), ""+spriteTypeName+"_"+(st+"_"+id).toUpperCase()+".png");
                        
                        
                        
                        
                    }
                    //create a node from min to max and add them under the imagesList node.
                    //int y=1;
                    
                    if(stateToDelete!=st){
                       
                            Iterator<Entry<String, String>>idsIterator=idMap.entrySet().iterator();
                        
                            while(idsIterator.hasNext()){
                                Entry<String, String>idToken=idsIterator.next();
                      
                                Element imageFileTypeElement = makeElement(doc, imagesListTypeElement, 
                                IMAGE_FILE_NODE);
                                imageFileTypeElement.setAttribute("id", ""+idToken.getKey());
                                imageFileTypeElement.setAttribute("file_name", ""+idToken.getValue());
                            }
     
                   }
                    
                   if(stateToDelete==st){
                       
                            Iterator<Entry<String, String>>idsIterator=idMapDelete.entrySet().iterator();
                            
                            while(idsIterator.hasNext()){
                                
                                Entry<String, String>idToken=idsIterator.next();
                                Element imageFileTypeElement = makeElement(doc, imagesListTypeElement, 
                                IMAGE_FILE_NODE);
                                imageFileTypeElement.setAttribute("id", ""+idToken.getKey());
                                imageFileTypeElement.setAttribute("file_name", ""+idToken.getValue());
                                
                                
                                File name=new File("./data/sprite_types/"+spriteTypeName+"/"+spriteTypeName+"_"+st.toUpperCase()+"_"+idToken.getKey()+".png");
                                File namePose=new File("./data/sprite_types/"+spriteTypeName+"/"+spriteTypeName+"_"+st.toUpperCase()+"_"+idToken.getKey()+".pose");
                                
                               
                                FileUtils.deleteQuietly(name);
                                FileUtils.deleteQuietly(namePose);
                                
                            }
     
                   } 
                    
                    
                    
                   idMap.clear();
                }
            
            Element animationsListTypeElement = makeElement(doc, rootElement, 
                    ANIMATIONS_LIST_NODE, "" ); 
            state=sprite.getAnimationStates();
            String selectedState=viewer.getCombo().getSelectedItem().toString();
            out:
            while(state.hasNext()){
                
                String st=state.next();
                if(st.equals("NULL")){
                    continue out;
                }
                //only skip the those that is selected state, rest keep continue 
                if(st.equals(selectedState)){
                    continue out;
                }
                Element animationStateElement = makeElement(doc, animationsListTypeElement, 
                        ANIMATION_STATE, "" );
                //new state element added to the node
                Element stateElement = makeElement(doc, animationStateElement, 
                        STATE,""+st); 
                Element animationSequenceElement = makeElement(doc, animationStateElement, 
                        ANIMATION_SEQUENCE );
                Iterator <Pose> poseList=sprite.getPoseList(st).getPoseIterator();
                for(int j=0;j<sprite.getPoseList(st).getNumOfPoses();j++){
                        //check the image id of each pose, identify the min and max
                    Pose p1=poseList.next();
                    Element poseElement = makeElement(doc, animationSequenceElement, 
                        POSE_NODE );
                    poseElement.setAttribute("image_id", ""+p1.getImageID());
                    poseElement.setAttribute("duration", ""+p1.getDurationInFrames());
               }
            
            }
            
            //iterator to state through
            state=sprite.getAnimationStates();
            //int y=0;
            int y=0;
            while(state.hasNext()){
                y++;
                state.next();
            }
            //when iy only has one state, needs to add one more as NULL with default values.
            if(y==1){
                Element animationStateElement = makeElement(doc, animationsListTypeElement, 
                    ANIMATION_STATE, "" );
                //new state element added to the node
                Element stateElement = makeElement(doc, animationStateElement, 
                   STATE,"NULL");
                Element animationSequenceElement = makeElement(doc, animationStateElement, 
                    ANIMATION_SEQUENCE );
                Element poseElement = makeElement(doc, animationSequenceElement, 
                    POSE_NODE );
                poseElement.setAttribute("image_id", "1000");
                poseElement.setAttribute("duration", "1000");
            
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, YES_VALUE);
            transformer.setOutputProperty(XML_INDENT_PROPERTY, XML_INDENT_VALUE);
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(newXML);
            
            // SAVE THE POSE TO AN XML FILE
            transformer.transform(source, result); 
          
       
            
           AnimatedSpriteViewer view=gui.getAnimametedViewerPanel();
           view.loadSpritesFromState(spriteTypeName);
            
            
            JOptionPane.showMessageDialog(
                gui,
                STATE_DELETED_TEXT,
                STATE_DELETED_TITLE_TEXT,
                JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        catch( ParserConfigurationException | TransformerException | DOMException | HeadlessException ex)
        {
            // SOMETHING WENT WRONG WRITING THE XML FILE
           
            JOptionPane.showMessageDialog(
                gui,
                POSE_SAVING_ERROR_TEXT,
                POSE_SAVING_ERROR_TITLE_TEXT,
                JOptionPane.ERROR_MESSAGE);  
            ex.printStackTrace();
            return false;
        }   
        
        }catch(NullPointerException e){
            JOptionPane.showMessageDialog(viewer, "You did not select a state to delete for!", STATE, TRANSPARENT);
            System.out.println("You did not select a state to delete for");
            e.printStackTrace();
            return false;
        }
        
        
      
    }
     
     public boolean deleteState(String filePath, int iD) {
        
        
        Poseur singleton = Poseur.getPoseur();
        PoseurGUI gui = singleton.getGUI();
        AnimatedSpriteViewer viewer=gui.getAnimametedViewerPanel();
        String stateToDelete=viewer.getCombo().getSelectedItem().toString();
        try{
            String spriteTypeName=viewer.getJlist().getSelectedValue().toString();
            HashMap<String, SpriteType> spriteTypes=viewer.getSpriteTypes();
            SpriteType spriteType=spriteTypes.get(spriteTypeName);
            String xsdFile=SPRITES_DATA_PATH+SPRITE_TYPE_SCHEMA_FILE;
        
            SpriteType sprite=viewer.getSpriteTypes().get(spriteTypeName);
            File newXML=new File(filePath);
            //File newXML=new File((SPRITE_TYPES_XML_PATH+spriteTypeName+"/"+spriteTypeName+".xml"));
        try{
         // THESE WILL US BUILD A DOC
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        
        
        // FIRST MAKE THE DOCUMENT
        Document doc = docBuilder.newDocument();
        
        // THEN THE ROOT ELEMENT
        Element rootElement = doc.createElement(SPRITE_TYPE_NODE);
        
        doc.appendChild(rootElement);
        
        // THEN MAKE AND ADD THE WIDTH, HEIGHT, AND NUM SHAPES
           
            Element widthTypeElement = makeElement(doc, rootElement, 
                    WIDTH_NODE, ""+sprite.getWidth());
            Element heightTypeElement = makeElement(doc, rootElement, 
                    HEIGHT_NODE, ""+sprite.getHeight());
            Element imagesListTypeElement = makeElement(doc, rootElement, 
                    IMAGES_LIST_NODE, "" );   
            //Min and max needed to find the pic id name.
            
            
            
            Iterator <String> state=sprite.getAnimationStates();
            while(state.hasNext()){
                    //iterate through each single state to find max pose number 
                    String st=state.next();  
                    //for each state get the entire pose list.
                    Iterator <Pose> pose=sprite.getPoseList(st).getPoseIterator();
                    //iterate in the pose list to get the image id number.
                    HashMap<String, String> idMap=new HashMap();
                    HashMap<String, String> idMapDelete=new HashMap();
                    for(int j=0;j<sprite.getPoseList(st).getNumOfPoses();j++){
                        //check the image id of each pose, identify the min and max
                        Pose p1=pose.next();
                        int id=p1.getImageID();
                          
                        if(id!=1000&&!stateToDelete.equals(st))
                        idMap.put(""+p1.getImageID(), ""+spriteTypeName+"_"+(st+"_"+id).toUpperCase()+".png");
                        if(id!=1000&&stateToDelete.equals(st))
                        idMapDelete.put(""+p1.getImageID(), ""+spriteTypeName+"_"+(st+"_"+id).toUpperCase()+".png");
                        
                        
                        
                        
                    }
                    //create a node from min to max and add them under the imagesList node.
                    //int y=1;
                    
                    if(stateToDelete!=st){
                       
                            Iterator<Entry<String, String>>idsIterator=idMap.entrySet().iterator();
                        
                            while(idsIterator.hasNext()){
                                Entry<String, String>idToken=idsIterator.next();
                      
                                Element imageFileTypeElement = makeElement(doc, imagesListTypeElement, 
                                IMAGE_FILE_NODE);
                                imageFileTypeElement.setAttribute("id", ""+idToken.getKey());
                                imageFileTypeElement.setAttribute("file_name", ""+idToken.getValue());
                            }
     
                   }
                    
                   if(stateToDelete==st){
                       
                            Iterator<Entry<String, String>>idsIterator=idMapDelete.entrySet().iterator();
                            
                            while(idsIterator.hasNext()){
                                
                                Entry<String, String>idToken=idsIterator.next();
                                Element imageFileTypeElement = makeElement(doc, imagesListTypeElement, 
                                IMAGE_FILE_NODE);
                                imageFileTypeElement.setAttribute("id", ""+idToken.getKey());
                                imageFileTypeElement.setAttribute("file_name", ""+idToken.getValue());
                                
                                
                                File name=new File("./data/sprite_types/"+spriteTypeName+"/"+spriteTypeName+"_"+st.toUpperCase()+"_"+idToken.getKey()+".png");
                                File namePose=new File("./data/sprite_types/"+spriteTypeName+"/"+spriteTypeName+"_"+st.toUpperCase()+"_"+idToken.getKey()+".pose");
                                
                               
                                FileUtils.deleteQuietly(name);
                                FileUtils.deleteQuietly(namePose);
                                
                            }
     
                   } 
                    
                    
                    
                   idMap.clear();
                }
            
            Element animationsListTypeElement = makeElement(doc, rootElement, 
                    ANIMATIONS_LIST_NODE, "" ); 
            state=sprite.getAnimationStates();
            String selectedState=viewer.getCombo().getSelectedItem().toString();
            out:
            while(state.hasNext()){
                
                String st=state.next();
                if(st.equals("NULL")){
                    continue out;
                }
                //only skip the those that is selected state, rest keep continue 
                if(st.equals(selectedState)){
                    continue out;
                }
                Element animationStateElement = makeElement(doc, animationsListTypeElement, 
                        ANIMATION_STATE, "" );
                //new state element added to the node
                Element stateElement = makeElement(doc, animationStateElement, 
                        STATE,""+st); 
                Element animationSequenceElement = makeElement(doc, animationStateElement, 
                        ANIMATION_SEQUENCE );
                Iterator <Pose> poseList=sprite.getPoseList(st).getPoseIterator();
                for(int j=0;j<sprite.getPoseList(st).getNumOfPoses();j++){
                        //check the image id of each pose, identify the min and max
                    Pose p1=poseList.next();
                    Element poseElement = makeElement(doc, animationSequenceElement, 
                        POSE_NODE );
                    poseElement.setAttribute("image_id", ""+p1.getImageID());
                    poseElement.setAttribute("duration", ""+p1.getDurationInFrames());
               }
            
            }
            
            //iterator to state through
            state=sprite.getAnimationStates();
            //int y=0;
            int y=0;
            while(state.hasNext()){
                y++;
                state.next();
            }
            //when iy only has one state, needs to add one more as NULL with default values.
            if(y==1){
                Element animationStateElement = makeElement(doc, animationsListTypeElement, 
                    ANIMATION_STATE, "" );
                //new state element added to the node
                Element stateElement = makeElement(doc, animationStateElement, 
                   STATE,"NULL");
                Element animationSequenceElement = makeElement(doc, animationStateElement, 
                    ANIMATION_SEQUENCE );
                Element poseElement = makeElement(doc, animationSequenceElement, 
                    POSE_NODE );
                poseElement.setAttribute("image_id", "1000");
                poseElement.setAttribute("duration", "1000");
            
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, YES_VALUE);
            transformer.setOutputProperty(XML_INDENT_PROPERTY, XML_INDENT_VALUE);
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(newXML);
            
            // SAVE THE POSE TO AN XML FILE
            transformer.transform(source, result); 
          
       
            
           AnimatedSpriteViewer view=gui.getAnimametedViewerPanel();
           view.loadSpritesFromState(spriteTypeName);
            
            
           
            return true;
        }
        catch( ParserConfigurationException | TransformerException | DOMException | HeadlessException ex)
        {
            // SOMETHING WENT WRONG WRITING THE XML FILE
           
            JOptionPane.showMessageDialog(
                gui,
                POSE_SAVING_ERROR_TEXT,
                POSE_SAVING_ERROR_TITLE_TEXT,
                JOptionPane.ERROR_MESSAGE);  
            ex.printStackTrace();
            return false;
        }   
        
        }catch(NullPointerException e){
            JOptionPane.showMessageDialog(viewer, "You did not select a state to delete for!", STATE, TRANSPARENT);
            System.out.println("You did not select a state to delete for");
            e.printStackTrace();
            return false;
        }
        
        
      
    }
     
     
     
     public void removeAll(Node node) 
    {
        for(int i=0 ;i< node.getChildNodes().getLength();i++)
        {
             Node n=node.getChildNodes().item(i);
            if(n.hasChildNodes()) //edit to remove children of children
            {
              removeAll(n);
              node.removeChild(n);
            }
            else
              node.removeChild(n);
        }
    }
    
    
    
     /**
     * This helper method builds elements (nodes) for us to help with building
     * a Doc which we would then save to a file.
     * 
     * @param doc The document we're building.
     * 
     * @param parent The node we'll add our new node to.
     * 
     * @param elementName The name of the node we're making.
     * 
     * @param textContent The data associated with the node we're making.
     * 
     * @return A node of name elementName, with textComponent as data, in the doc
     * document, with parent as its parent node.
     */
    private Element makeElement(Document doc, Element parent, String elementName, String textContent)
    {
        Element element = doc.createElement(elementName);
        element.setTextContent(textContent);
        parent.appendChild(element);
        return element;
    }
    
    private Element makeElement(Document doc, Element parent, String elementName)
    {
        Element element = doc.createElement(elementName);
       // element.setTextContent(textContent);
        parent.appendChild(element);
        return element;
    }
    
    /**
     * This helper method can be used to build a node to store color data. We'll
     * use this for building a Doc for the purpose of saving it to a file.
     * 
     * @param doc The document where we'll put the node.
     * 
     * @param parent The node where we'll add the color node as a child.
     * 
     * @param elementName The name of the color node to make. Colors may be
     * used for different purposes, so may have different names.
     * 
     * @param color The color data we'll use to build the node is in here.
     * 
     * @return A Element (i.e. Node) with the color information inside.
     */
    private Element makeColorNode(Document doc, Element parent, String elementName, Color color)
    {
        // MAKE THE COLOR NODE
        Element colorNode = makeElement(doc, parent, elementName, "");
        
        // AND THE COLOR COMPONENTS
        Element redNode = makeElement(doc, colorNode, RED_NODE, "" + color.getRed());
        Element greenNode = makeElement(doc, colorNode, GREEN_NODE, "" + color.getGreen());
        Element blueNode = makeElement(doc, colorNode, BLUE_NODE, "" + color.getBlue());
        
        // AND RETURN OUR NEW ELEMENT (NODE)
        return colorNode;
    }

    void renameState(String filePath) {
         
        Poseur singleton = Poseur.getPoseur();
        PoseurGUI gui = singleton.getGUI();
        AnimatedSpriteViewer viewer=gui.getAnimametedViewerPanel();
        String stateName=viewer.getCombo().getSelectedItem().toString();
        String newStateName=JOptionPane.showInputDialog(viewer,"Please enter new State name");
        String spriteName=viewer.getJlist().getSelectedValue().toString();
        try{
            String spriteTypeName=viewer.getJlist().getSelectedValue().toString();
            HashMap<String, SpriteType> spriteTypes=viewer.getSpriteTypes();
            SpriteType spriteType=spriteTypes.get(spriteTypeName);
            String xsdFile=SPRITES_DATA_PATH+SPRITE_TYPE_SCHEMA_FILE;
        
            SpriteType sprite=viewer.getSpriteTypes().get(spriteTypeName);
            File newXML=new File(filePath);
            //File newXML=new File((SPRITE_TYPES_XML_PATH+spriteTypeName+"/"+spriteTypeName+".xml"));
        try{
         // THESE WILL US BUILD A DOC
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        
        
        // FIRST MAKE THE DOCUMENT
        Document doc = docBuilder.newDocument();
        
        // THEN THE ROOT ELEMENT
        Element rootElement = doc.createElement(SPRITE_TYPE_NODE);
        
        doc.appendChild(rootElement);
        
        // THEN MAKE AND ADD THE WIDTH, HEIGHT, AND NUM SHAPES
           
            Element widthTypeElement = makeElement(doc, rootElement, 
                    WIDTH_NODE, ""+sprite.getWidth());
            Element heightTypeElement = makeElement(doc, rootElement, 
                    HEIGHT_NODE, ""+sprite.getHeight());
            Element imagesListTypeElement = makeElement(doc, rootElement, 
                    IMAGES_LIST_NODE, "" );   
            //Min and max needed to find the pic id name.
           
            int y=1;
            Iterator <String> state=sprite.getAnimationStates();
            
                
               //as long as there is state, keep iterating. 
                
                while(state.hasNext()){
                    //iterate through each single state to find max pose number 
                    String st=state.next();  
                    //for each state get the entire pose list.
                    Iterator <Pose> pose=sprite.getPoseList(st).getPoseIterator();
                    //iterate in the pose list to get the image id number.
                    HashMap<String, String> idMap=new HashMap();
                    for(int j=0;j<sprite.getPoseList(st).getNumOfPoses();j++){
                        //check the image id of each pose, identify the min and max
                        Pose p1=pose.next();
                        int id=p1.getImageID();
                          
                        if(id!=1000&&!stateName.equals(st))
                        idMap.put(""+p1.getImageID(), ""+spriteTypeName+"_"+(st+"_"+id).toUpperCase()+".png");
                        if(id!=1000&&stateName.equals(st))
                        idMap.put(""+p1.getImageID(), ""+spriteTypeName+"_"+(newStateName+"_"+id).toUpperCase()+".png");
                        
                        
                    }
                    //create a node from min to max and add them under the imagesList node.
                    //int y=1;
                    
                    if(stateName!=st){
                       
                            Iterator<Entry<String, String>>idsIterator=idMap.entrySet().iterator();
                        
                            while(idsIterator.hasNext()){
                                Entry<String, String>idToken=idsIterator.next();
                      
                                Element imageFileTypeElement = makeElement(doc, imagesListTypeElement, 
                                IMAGE_FILE_NODE);
                                imageFileTypeElement.setAttribute("id", ""+idToken.getKey());
                                imageFileTypeElement.setAttribute("file_name", ""+idToken.getValue());
                            }
                    }
                    if(stateName==st){
                        Iterator<Entry<String, String>>idsIterator=idMap.entrySet().iterator();
                        
                            while(idsIterator.hasNext()){
                                Entry<String, String>idToken=idsIterator.next();
                      
                                Element imageFileTypeElement = makeElement(doc, imagesListTypeElement, 
                                IMAGE_FILE_NODE);
                                imageFileTypeElement.setAttribute("id", ""+idToken.getKey());
                                imageFileTypeElement.setAttribute("file_name", ""+idToken.getValue());
                            
                        
                        
                        File name=new File("./data/sprite_types/"+spriteTypeName+"/"+spriteTypeName+"_"+st.toUpperCase()+"_"+idToken.getKey()+".png");
                        File newName=new File("./data/sprite_types/"+spriteTypeName+"/"+spriteTypeName+"_"+newStateName.toUpperCase()+"_"+idToken.getKey()+".png");
                        File namePose=new File("./data/sprite_types/"+spriteTypeName+"/"+spriteTypeName+"_"+st.toUpperCase()+"_"+idToken.getKey()+".pose");
                        File newNamePose=new File("./data/sprite_types/"+spriteTypeName+"/"+spriteTypeName+"_"+newStateName.toUpperCase()+"_"+idToken.getKey()+".pose");
                        name.renameTo(newName);
                        namePose.renameTo(newNamePose);
                       
                     } 
                        
                     idMap.clear();
                   }
                    
                   idMap.clear();
                }
                
                
            
            
            Element animationsListTypeElement = makeElement(doc, rootElement, 
                    ANIMATIONS_LIST_NODE, "" ); 
            state=sprite.getAnimationStates();
            String selectedState=viewer.getCombo().getSelectedItem().toString();
            out:
            while(state.hasNext()){
                
                String st=state.next();
                if(st.equals("NULL")){
                    continue out;
                }
                
                //that's where the naming of the state will be changed
                //if is is selected state, pass everyhing and chage the names 
                if(st.equals(selectedState)){
                        Element animationStateElement = makeElement(doc, animationsListTypeElement, 
                        ANIMATION_STATE, "" );
                        //new state element added to the node
                        Element stateElement = makeElement(doc, animationStateElement, 
                        STATE,""+newStateName); 
                        Element animationSequenceElement = makeElement(doc, animationStateElement, 
                        ANIMATION_SEQUENCE );
                        Iterator <Pose> poseList=sprite.getPoseList(st).getPoseIterator();
                        for(int j=0;j<sprite.getPoseList(st).getNumOfPoses();j++){
                        //check the image id of each pose, identify the min and max
                            Pose p1=poseList.next();
                            Element poseElement = makeElement(doc, animationSequenceElement, 
                            POSE_NODE);
                            poseElement.setAttribute("image_id", ""+p1.getImageID());
                            poseElement.setAttribute("duration", ""+p1.getDurationInFrames());
                        }
                    continue out;
                }
                Element animationStateElement = makeElement(doc, animationsListTypeElement, 
                        ANIMATION_STATE, "" );
                //new state element added to the node
                Element stateElement = makeElement(doc, animationStateElement, 
                        STATE,""+st); 
                Element animationSequenceElement = makeElement(doc, animationStateElement, 
                        ANIMATION_SEQUENCE );
                Iterator <Pose> poseList=sprite.getPoseList(st).getPoseIterator();
                for(int j=0;j<sprite.getPoseList(st).getNumOfPoses();j++){
                        //check the image id of each pose, identify the min and max
                    Pose p1=poseList.next();
                    Element poseElement = makeElement(doc, animationSequenceElement, 
                        POSE_NODE );
                    poseElement.setAttribute("image_id", ""+p1.getImageID());
                    poseElement.setAttribute("duration", ""+p1.getDurationInFrames());
               }
            
            }
            
            //iterator to state through
            state=sprite.getAnimationStates();
            //int y=0;
            
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, YES_VALUE);
            transformer.setOutputProperty(XML_INDENT_PROPERTY, XML_INDENT_VALUE);
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(newXML);
            
            // SAVE THE POSE TO AN XML FILE
            transformer.transform(source, result); 
          
       
            
           AnimatedSpriteViewer view=gui.getAnimametedViewerPanel();
           view.loadSpritesFromState(spriteTypeName);
           
            
            
            JOptionPane.showMessageDialog(
                gui,
                STATE_DELETED_TEXT,
                STATE_DELETED_TITLE_TEXT,
                JOptionPane.INFORMATION_MESSAGE);
            
        }
        catch( ParserConfigurationException | TransformerException | DOMException | HeadlessException ex)
        {
            // SOMETHING WENT WRONG WRITING THE XML FILE
           
            JOptionPane.showMessageDialog(
                gui,
                POSE_SAVING_ERROR_TEXT,
                POSE_SAVING_ERROR_TITLE_TEXT,
                JOptionPane.ERROR_MESSAGE);  
            ex.printStackTrace();
          
        }   
        
        }catch(NullPointerException e){
            JOptionPane.showMessageDialog(viewer, "You did not select a state to delete for!", STATE, TRANSPARENT);
            System.out.println("You did not select a state to delete for");
            e.printStackTrace();
           
        }
        
        
    }

   
void renameStateFor(String filePath, String stName) {
         
        Poseur singleton = Poseur.getPoseur();
        PoseurGUI gui = singleton.getGUI();
        AnimatedSpriteViewer viewer=gui.getAnimametedViewerPanel();
        String stateName=viewer.getCombo().getSelectedItem().toString();
        //String newStateName=JOptionPane.showInputDialog(viewer,"Please enter new State name");
        String newStateName=stName;
        try{
            String spriteTypeName=viewer.getJlist().getSelectedValue().toString();
            HashMap<String, SpriteType> spriteTypes=viewer.getSpriteTypes();
            SpriteType spriteType=spriteTypes.get(spriteTypeName);
            String xsdFile=SPRITES_DATA_PATH+SPRITE_TYPE_SCHEMA_FILE;
        
            SpriteType sprite=viewer.getSpriteTypes().get(spriteTypeName);
            File newXML=new File(filePath);
            //File newXML=new File((SPRITE_TYPES_XML_PATH+spriteTypeName+"/"+spriteTypeName+".xml"));
        try{
         // THESE WILL US BUILD A DOC
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        
        
        // FIRST MAKE THE DOCUMENT
        Document doc = docBuilder.newDocument();
        
        // THEN THE ROOT ELEMENT
        Element rootElement = doc.createElement(SPRITE_TYPE_NODE);
        
        doc.appendChild(rootElement);
        
        // THEN MAKE AND ADD THE WIDTH, HEIGHT, AND NUM SHAPES
           
            Element widthTypeElement = makeElement(doc, rootElement, 
                    WIDTH_NODE, ""+sprite.getWidth());
            Element heightTypeElement = makeElement(doc, rootElement, 
                    HEIGHT_NODE, ""+sprite.getHeight());
            Element imagesListTypeElement = makeElement(doc, rootElement, 
                    IMAGES_LIST_NODE, "" );   
            //Min and max needed to find the pic id name.
           
            int y=1;
            Iterator <String> state=sprite.getAnimationStates();
            
                
               //as long as there is state, keep iterating. 
                
                while(state.hasNext()){
                    //iterate through each single state to find max pose number 
                    String st=state.next();  
                    //for each state get the entire pose list.
                    Iterator <Pose> pose=sprite.getPoseList(st).getPoseIterator();
                    //iterate in the pose list to get the image id number.
                    HashMap<String, String> idMap=new HashMap();
                    for(int j=0;j<sprite.getPoseList(st).getNumOfPoses();j++){
                        //check the image id of each pose, identify the min and max
                        Pose p1=pose.next();
                        int id=p1.getImageID();
                          
                        if(id!=1000&&!stateName.equals(st))
                        idMap.put(""+p1.getImageID(), ""+spriteTypeName+"_"+(st+"_"+id).toUpperCase()+".png");
                        if(id!=1000&&stateName.equals(st))
                        idMap.put(""+p1.getImageID(), ""+spriteTypeName+"_"+(newStateName+"_"+id).toUpperCase()+".png");
                        
                        
                    }
                    //create a node from min to max and add them under the imagesList node.
                    //int y=1;
                    
                    if(stateName!=st){
                       
                            Iterator<Entry<String, String>>idsIterator=idMap.entrySet().iterator();
                        
                            while(idsIterator.hasNext()){
                                Entry<String, String>idToken=idsIterator.next();
                      
                                Element imageFileTypeElement = makeElement(doc, imagesListTypeElement, 
                                IMAGE_FILE_NODE);
                                imageFileTypeElement.setAttribute("id", ""+idToken.getKey());
                                imageFileTypeElement.setAttribute("file_name", ""+idToken.getValue());
                            }
                    }
                    if(stateName==st){
                        Iterator<Entry<String, String>>idsIterator=idMap.entrySet().iterator();
                        
                            while(idsIterator.hasNext()){
                                Entry<String, String>idToken=idsIterator.next();
                      
                                Element imageFileTypeElement = makeElement(doc, imagesListTypeElement, 
                                IMAGE_FILE_NODE);
                                imageFileTypeElement.setAttribute("id", ""+idToken.getKey());
                                imageFileTypeElement.setAttribute("file_name", ""+idToken.getValue());
                            
                        
                        
                        File name=new File("./data/sprite_types/"+spriteTypeName+"/"+spriteTypeName+"_"+st.toUpperCase()+"_"+idToken.getKey()+".png");
                        File newName=new File("./data/sprite_types/"+spriteTypeName+"/"+spriteTypeName+"_"+newStateName.toUpperCase()+"_"+idToken.getKey()+".png");
                        File namePose=new File("./data/sprite_types/"+spriteTypeName+"/"+spriteTypeName+"_"+st.toUpperCase()+"_"+idToken.getKey()+".pose");
                        File newNamePose=new File("./data/sprite_types/"+spriteTypeName+"/"+spriteTypeName+"_"+newStateName.toUpperCase()+"_"+idToken.getKey()+".pose");
                        name.renameTo(newName);
                        namePose.renameTo(newNamePose);
                            
                       
                     } 
                        
                     idMap.clear();
                   }
                    
                   idMap.clear();
                }
                
                
            
            
            Element animationsListTypeElement = makeElement(doc, rootElement, 
                    ANIMATIONS_LIST_NODE, "" ); 
            state=sprite.getAnimationStates();
            String selectedState=viewer.getCombo().getSelectedItem().toString();
            out:
            while(state.hasNext()){
                
                String st=state.next();
                if(st.equals("NULL")){
                    continue out;
                }
                
                //that's where the naming of the state will be changed
                //if is is selected state, pass everyhing and chage the names 
                if(st.equals(selectedState)){
                        Element animationStateElement = makeElement(doc, animationsListTypeElement, 
                        ANIMATION_STATE, "" );
                        //new state element added to the node
                        Element stateElement = makeElement(doc, animationStateElement, 
                        STATE,""+newStateName); 
                        Element animationSequenceElement = makeElement(doc, animationStateElement, 
                        ANIMATION_SEQUENCE );
                        Iterator <Pose> poseList=sprite.getPoseList(st).getPoseIterator();
                        for(int j=0;j<sprite.getPoseList(st).getNumOfPoses();j++){
                        //check the image id of each pose, identify the min and max
                            Pose p1=poseList.next();
                            Element poseElement = makeElement(doc, animationSequenceElement, 
                            POSE_NODE);
                            poseElement.setAttribute("image_id", ""+p1.getImageID());
                            poseElement.setAttribute("duration", ""+p1.getDurationInFrames());
                        }
                    continue out;
                }
                Element animationStateElement = makeElement(doc, animationsListTypeElement, 
                        ANIMATION_STATE, "" );
                //new state element added to the node
                Element stateElement = makeElement(doc, animationStateElement, 
                        STATE,""+st); 
                Element animationSequenceElement = makeElement(doc, animationStateElement, 
                        ANIMATION_SEQUENCE );
                Iterator <Pose> poseList=sprite.getPoseList(st).getPoseIterator();
                for(int j=0;j<sprite.getPoseList(st).getNumOfPoses();j++){
                        //check the image id of each pose, identify the min and max
                    Pose p1=poseList.next();
                    Element poseElement = makeElement(doc, animationSequenceElement, 
                        POSE_NODE );
                    poseElement.setAttribute("image_id", ""+p1.getImageID());
                    poseElement.setAttribute("duration", ""+p1.getDurationInFrames());
               }
            
            }
            
            //iterator to state through
            state=sprite.getAnimationStates();
            //int y=0;
            
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, YES_VALUE);
            transformer.setOutputProperty(XML_INDENT_PROPERTY, XML_INDENT_VALUE);
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(newXML);
            
            // SAVE THE POSE TO AN XML FILE
            transformer.transform(source, result); 
          
       
            
           AnimatedSpriteViewer view=gui.getAnimametedViewerPanel();
           view.loadSpritesFromState(spriteTypeName);
           view.getJlist().setSelectedValue(spriteTypeName, DEFAULT_DEBUG_TEXT_ENABLED);
           view.getCombo().setSelectedItem(newStateName);
            
            
            
            
        }
        catch( ParserConfigurationException | TransformerException | DOMException | HeadlessException ex)
        {
            // SOMETHING WENT WRONG WRITING THE XML FILE
           
            JOptionPane.showMessageDialog(
                gui,
                POSE_SAVING_ERROR_TEXT,
                POSE_SAVING_ERROR_TITLE_TEXT,
                JOptionPane.ERROR_MESSAGE);  
            ex.printStackTrace();
          
        }   
        
        }catch(NullPointerException e){
            JOptionPane.showMessageDialog(viewer, "You did not select a state to delete for!", STATE, TRANSPARENT);
            System.out.println("You did not select a state to delete for");
            e.printStackTrace();
           
        }
        
        
    }

    void duplicateState(String filePath) {
        Poseur singleton = Poseur.getPoseur();
        PoseurGUI gui = singleton.getGUI();
        AnimatedSpriteViewer viewer=gui.getAnimametedViewerPanel();
        String stateName=viewer.getCombo().getSelectedItem().toString();
        String newStateName=JOptionPane.showInputDialog(viewer,"Please enter new State name to duplicate");
        String spriteName=viewer.getJlist().getSelectedValue().toString();
        
        try{
            String spriteTypeName=viewer.getJlist().getSelectedValue().toString();
            HashMap<String, SpriteType> spriteTypes=viewer.getSpriteTypes();
            SpriteType spriteType=spriteTypes.get(spriteTypeName);
            String xsdFile=SPRITES_DATA_PATH+SPRITE_TYPE_SCHEMA_FILE;
        
            SpriteType sprite=viewer.getSpriteTypes().get(spriteTypeName);
            File newXML=new File(filePath);
            //File newXML=new File((SPRITE_TYPES_XML_PATH+spriteTypeName+"/"+spriteTypeName+".xml"));
        try{
         // THESE WILL US BUILD A DOC
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        
        
        // FIRST MAKE THE DOCUMENT
        Document doc = docBuilder.newDocument();
        
        // THEN THE ROOT ELEMENT
        Element rootElement = doc.createElement(SPRITE_TYPE_NODE);
        
        doc.appendChild(rootElement);
        
        // THEN MAKE AND ADD THE WIDTH, HEIGHT, AND NUM SHAPES
           
            Element widthTypeElement = makeElement(doc, rootElement, 
                    WIDTH_NODE, ""+sprite.getWidth());
            Element heightTypeElement = makeElement(doc, rootElement, 
                    HEIGHT_NODE, ""+sprite.getHeight());
            Element imagesListTypeElement = makeElement(doc, rootElement, 
                    IMAGES_LIST_NODE, "" );   
            //Min and max needed to find the pic id name.
            
            int size=0;
            
            Iterator<String>states=spriteType.getAnimationStates();
            //to find the max id number
            while(states.hasNext()){
                String spriteState=states.next();
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
            
            int sizeForState=size;
           
            int y=1;
            Iterator <String> state=sprite.getAnimationStates();
            
                
               //as long as there is state, keep iterating. 
                
                while(state.hasNext()){
                    //iterate through each single state to find max pose number 
                    String st=state.next();  
                    //for each state get the entire pose list.
                    Iterator <Pose> pose=sprite.getPoseList(st).getPoseIterator();
                    //iterate in the pose list to get the image id number.
                    HashMap<String, String> idMap=new HashMap();
                    HashMap<String, String> poseMap=new HashMap();
                    for(int j=0;j<sprite.getPoseList(st).getNumOfPoses();j++){
                        //check the image id of each pose, identify the min and max
                        Pose p1=pose.next();
                        int id=p1.getImageID();
                        
                          
                        if(id!=1000)
                        idMap.put(""+p1.getImageID(), ""+spriteTypeName+"_"+(st+"_"+id).toUpperCase()+".png");
                        if(stateName.equals(st)){
                            poseMap.put(""+p1.getImageID(), ""+p1.getDurationInFrames());
                        }
                        
                    }
                    //create a node from min to max and add them under the imagesList node.
                    //int y=1;
                    
                    //copy all the data from the state
                       
                            Iterator<Entry<String, String>>idsIterator=idMap.entrySet().iterator();
                        
                            while(idsIterator.hasNext()){
                                Entry<String, String>idToken=idsIterator.next();
                      
                                Element imageFileTypeElement = makeElement(doc, imagesListTypeElement, 
                                IMAGE_FILE_NODE);
                                imageFileTypeElement.setAttribute("id", ""+idToken.getKey());
                                imageFileTypeElement.setAttribute("file_name", ""+idToken.getValue());
                            }
                    
                    
                    
            
                    
                    size++;
                    if(st.equals(stateName)){
                   
                        idsIterator=idMap.entrySet().iterator();
                        
                        while(idsIterator.hasNext()){
                                Entry<String, String>idToken=idsIterator.next();
                      
                                Element imageFileTypeElement = makeElement(doc, imagesListTypeElement, 
                                IMAGE_FILE_NODE);
                                imageFileTypeElement.setAttribute("id", ""+size);
                                imageFileTypeElement.setAttribute("file_name", ""+spriteTypeName+"_"+newStateName.toUpperCase()+"_"+size+".png");
                            
                       
                        
                        File name=new File("./data/sprite_types/"+spriteTypeName+"/"+spriteTypeName+"_"+st.toUpperCase()+"_"+idToken.getKey()+".png");
                        File newName=new File("./data/sprite_types/"+spriteTypeName+"/"+spriteTypeName+"_"+newStateName.toUpperCase()+"_"+size+".png");
                        File namePose=new File("./data/sprite_types/"+spriteTypeName+"/"+spriteTypeName+"_"+st.toUpperCase()+"_"+idToken.getKey()+".pose");
                        File newNamePose=new File("./data/sprite_types/"+spriteTypeName+"/"+spriteTypeName+"_"+newStateName.toUpperCase()+"_"+size+".pose");
                        
                        //name.renameTo(newName);
                        //  / namePose.renameTo(newNamePose);
                        
                            try {
                                FileUtils.copyFile(name, newName);
                                FileUtils.copyFile(namePose, newNamePose);
                                
                            } catch (IOException ex) {
                                Logger.getLogger(SpriteIO.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        
                        
                        size++;
                        
                        } 
                        
                     idMap.clear();
                  }
      }
                    
                  
                
                
            
            
            Element animationsListTypeElement = makeElement(doc, rootElement, 
                    ANIMATIONS_LIST_NODE, "" ); 
            state=sprite.getAnimationStates();
            String selectedState=viewer.getCombo().getSelectedItem().toString();
            out:
            while(state.hasNext()){
                
                String st=state.next();
                if(st.equals("NULL")){
                    continue out;
                }
                
                //that's where the naming of the state will be changed
                //if is is selected state, pass everyhing and chage the names 
                if(st.equals(selectedState)){
                        Element animationStateElement = makeElement(doc, animationsListTypeElement, 
                        ANIMATION_STATE, "" );
                        //new state element added to the node
                        Element stateElement = makeElement(doc, animationStateElement, 
                        STATE,""+newStateName); 
                        Element animationSequenceElement = makeElement(doc, animationStateElement, 
                        ANIMATION_SEQUENCE );
                        Iterator <Pose> poseList=sprite.getPoseList(st).getPoseIterator();
                        sizeForState++;
                        for(int j=0;j<sprite.getPoseList(st).getNumOfPoses();j++){
                        //check the image id of each pose, identify the min and max
                            Pose p1=poseList.next();
                            Element poseElement = makeElement(doc, animationSequenceElement, 
                            POSE_NODE);
                            poseElement.setAttribute("image_id", ""+sizeForState);
                            poseElement.setAttribute("duration", ""+p1.getDurationInFrames());
                            sizeForState++;
                        }
                    //continue out;
                }
                Element animationStateElement = makeElement(doc, animationsListTypeElement, 
                        ANIMATION_STATE, "" );
                //new state element added to the node
                Element stateElement = makeElement(doc, animationStateElement, 
                        STATE,""+st); 
                Element animationSequenceElement = makeElement(doc, animationStateElement, 
                        ANIMATION_SEQUENCE );
                Iterator <Pose> poseList=sprite.getPoseList(st).getPoseIterator();
                for(int j=0;j<sprite.getPoseList(st).getNumOfPoses();j++){
                        //check the image id of each pose, identify the min and max
                    Pose p1=poseList.next();
                    Element poseElement = makeElement(doc, animationSequenceElement, 
                        POSE_NODE );
                    poseElement.setAttribute("image_id", ""+p1.getImageID());
                    poseElement.setAttribute("duration", ""+p1.getDurationInFrames());
               }
            
            }
            
            //iterator to state through
            state=sprite.getAnimationStates();
            //int y=0;
            
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, YES_VALUE);
            transformer.setOutputProperty(XML_INDENT_PROPERTY, XML_INDENT_VALUE);
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(newXML);
            
            // SAVE THE POSE TO AN XML FILE
            transformer.transform(source, result); 
          
       
            
           AnimatedSpriteViewer view=gui.getAnimametedViewerPanel();
           view.loadSpritesFromState(spriteTypeName);
           
            
            
            JOptionPane.showMessageDialog(
                gui,
                STATE_DELETED_TEXT,
                STATE_DELETED_TITLE_TEXT,
                JOptionPane.INFORMATION_MESSAGE);
            
        }
        catch( ParserConfigurationException | TransformerException | DOMException | HeadlessException ex)
        {
            // SOMETHING WENT WRONG WRITING THE XML FILE
           
            JOptionPane.showMessageDialog(
                gui,
                POSE_SAVING_ERROR_TEXT,
                POSE_SAVING_ERROR_TITLE_TEXT,
                JOptionPane.ERROR_MESSAGE);  
            ex.printStackTrace();
          
        }   
        
        }catch(NullPointerException e){
            JOptionPane.showMessageDialog(viewer, "You did not select a state to delete for!", STATE, TRANSPARENT);
            System.out.println("You did not select a state to delete for");
            e.printStackTrace();
           
        }
        
    }

    void setDuration(int iD) {
        Poseur singleton = Poseur.getPoseur();
        PoseurGUI gui = singleton.getGUI();
        AnimatedSpriteViewer viewer=gui.getAnimametedViewerPanel();
        String stateName=viewer.getCombo().getSelectedItem().toString();
        
        String durationString=JOptionPane.showInputDialog(viewer,"Please enter a new duration for the selected Pose");
        int duration=Integer.parseInt(durationString);
       
        
        String spriteName=viewer.getJlist().getSelectedValue().toString();
        String filePath="./data/sprite_types/"+spriteName+"/"+spriteName+".xml";
        try{
            String spriteTypeName=viewer.getJlist().getSelectedValue().toString();
            HashMap<String, SpriteType> spriteTypes=viewer.getSpriteTypes();
            SpriteType spriteType=spriteTypes.get(spriteTypeName);
            String xsdFile=SPRITES_DATA_PATH+SPRITE_TYPE_SCHEMA_FILE;
        
            SpriteType sprite=viewer.getSpriteTypes().get(spriteTypeName);
            File newXML=new File(filePath);
            //File newXML=new File((SPRITE_TYPES_XML_PATH+spriteTypeName+"/"+spriteTypeName+".xml"));
        try{
         // THESE WILL US BUILD A DOC
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        
        
        // FIRST MAKE THE DOCUMENT
        Document doc = docBuilder.newDocument();
        
        // THEN THE ROOT ELEMENT
        Element rootElement = doc.createElement(SPRITE_TYPE_NODE);
        
        doc.appendChild(rootElement);
        
        // THEN MAKE AND ADD THE WIDTH, HEIGHT, AND NUM SHAPES
           
            Element widthTypeElement = makeElement(doc, rootElement, 
                    WIDTH_NODE, ""+sprite.getWidth());
            Element heightTypeElement = makeElement(doc, rootElement, 
                    HEIGHT_NODE, ""+sprite.getHeight());
            Element imagesListTypeElement = makeElement(doc, rootElement, 
                    IMAGES_LIST_NODE, "" );   
            //Min and max needed to find the pic id name.
            
            int size=0;
            
            Iterator<String>states=spriteType.getAnimationStates();
            //to find the max id number
            while(states.hasNext()){
                String spriteState=states.next();
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
            
            int sizeForState=size;
           
            int y=1;
            Iterator <String> state=sprite.getAnimationStates();
            
                
               //as long as there is state, keep iterating. 
                
                while(state.hasNext()){
                    //iterate through each single state to find max pose number 
                    String st=state.next();  
                    //for each state get the entire pose list.
                    Iterator <Pose> pose=sprite.getPoseList(st).getPoseIterator();
                    //iterate in the pose list to get the image id number.
                    HashMap<String, String> idMap=new HashMap();
                    HashMap<String, String> poseMap=new HashMap();
                    for(int j=0;j<sprite.getPoseList(st).getNumOfPoses();j++){
                        //check the image id of each pose, identify the min and max
                        Pose p1=pose.next();
                        int id=p1.getImageID();
                        
                          
                        if(id!=1000)
                        idMap.put(""+p1.getImageID(), ""+spriteTypeName+"_"+(st+"_"+id).toUpperCase()+".png");
                        if(stateName.equals(st)){
                            poseMap.put(""+p1.getImageID(), ""+p1.getDurationInFrames());
                        }
                        
                    }
                    //create a node from min to max and add them under the imagesList node.
                    //int y=1;
                    
                    //copy all the data from the state
                       
                            Iterator<Entry<String, String>>idsIterator=idMap.entrySet().iterator();
                        
                            while(idsIterator.hasNext()){
                                Entry<String, String>idToken=idsIterator.next();
                      
                                Element imageFileTypeElement = makeElement(doc, imagesListTypeElement, 
                                IMAGE_FILE_NODE);
                                imageFileTypeElement.setAttribute("id", ""+idToken.getKey());
                                imageFileTypeElement.setAttribute("file_name", ""+idToken.getValue());
                            }
                    
                }
                    
                  
                
                
            
            
            Element animationsListTypeElement = makeElement(doc, rootElement, 
                    ANIMATIONS_LIST_NODE, "" ); 
            state=sprite.getAnimationStates();
            String selectedState=viewer.getCombo().getSelectedItem().toString();
            out:
            while(state.hasNext()){
                
                String st=state.next();
                if(st.equals("NULL")){
                    continue out;
                }
                
                //that's where the naming of the state will be changed
                //if is is selected state, pass everyhing and chage the names 
                if(st.equals(selectedState)){
                        Element animationStateElement = makeElement(doc, animationsListTypeElement, 
                        ANIMATION_STATE, "" );
                        //new state element added to the node
                        Element stateElement = makeElement(doc, animationStateElement, 
                        STATE,""+st); 
                        Element animationSequenceElement = makeElement(doc, animationStateElement, 
                        ANIMATION_SEQUENCE );
                        Iterator <Pose> poseList=sprite.getPoseList(st).getPoseIterator();
                        
                        for(int j=0;j<sprite.getPoseList(st).getNumOfPoses();j++){
                        //check the image id of each pose, identify the min and max
                            Pose p1=poseList.next();
                            Element poseElement = makeElement(doc, animationSequenceElement, 
                            POSE_NODE);
                            poseElement.setAttribute("image_id", ""+p1.getImageID());
                            
                            if(gui.getID()==p1.getImageID()){
                                poseElement.setAttribute("duration", ""+duration);
                            }else{
                                poseElement.setAttribute("duration", ""+p1.getDurationInFrames());
                            }
                        }
                    continue out;
                }
                Element animationStateElement = makeElement(doc, animationsListTypeElement, 
                        ANIMATION_STATE, "" );
                //new state element added to the node
                Element stateElement = makeElement(doc, animationStateElement, 
                        STATE,""+st); 
                Element animationSequenceElement = makeElement(doc, animationStateElement, 
                        ANIMATION_SEQUENCE );
                Iterator <Pose> poseList=sprite.getPoseList(st).getPoseIterator();
                for(int j=0;j<sprite.getPoseList(st).getNumOfPoses();j++){
                        //check the image id of each pose, identify the min and max
                    Pose p1=poseList.next();
                    Element poseElement = makeElement(doc, animationSequenceElement, 
                        POSE_NODE );
                    poseElement.setAttribute("image_id", ""+p1.getImageID());
                    poseElement.setAttribute("duration", ""+p1.getDurationInFrames());
               }
            
            }
            
            //iterator to state through
            state=sprite.getAnimationStates();
            //int y=0;
            
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, YES_VALUE);
            transformer.setOutputProperty(XML_INDENT_PROPERTY, XML_INDENT_VALUE);
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(newXML);
            
            // SAVE THE POSE TO AN XML FILE
            transformer.transform(source, result); 
          
       
            
           AnimatedSpriteViewer view=gui.getAnimametedViewerPanel();
           view.loadSpritesFromState(spriteTypeName);
           
           view.getJlist().setSelectedValue(spriteTypeName, DEFAULT_DEBUG_TEXT_ENABLED);
           view.getCombo().setSelectedItem(stateName);
           
            
         
            
        }
        catch( ParserConfigurationException | TransformerException | DOMException | HeadlessException ex)
        {
            // SOMETHING WENT WRONG WRITING THE XML FILE
           
            JOptionPane.showMessageDialog(
                gui,
                POSE_SAVING_ERROR_TEXT,
                POSE_SAVING_ERROR_TITLE_TEXT,
                JOptionPane.ERROR_MESSAGE);  
            ex.printStackTrace();
          
        }   
        
        }catch(NullPointerException e){
            JOptionPane.showMessageDialog(viewer, "You did not select a state to delete for!", STATE, TRANSPARENT);
            System.out.println("You did not select a state to delete for");
            e.printStackTrace();
           
        }
    }

    void poseShiftRight(int iD) {
        Poseur singleton = Poseur.getPoseur();
        PoseurGUI gui = singleton.getGUI();
        AnimatedSpriteViewer viewer=gui.getAnimametedViewerPanel();
        String stateName=viewer.getCombo().getSelectedItem().toString();
        LinkedList<SpriteTypeGUIPanel> labelPoses=viewer.getLabelPoses();
        int idRight=1000;
        int idLeft=iD;
        int durationRight = 0;
        int durationLeft = 0;
        //String durationString=JOptionPane.showInputDialog(viewer,"Please enter a new duration for the selected Pose");
        //int duration=Integer.parseInt(durationString);
       
        
        //To find the ID number which is on the right
        for(int k=0;k<labelPoses.size();k++){
            SpriteTypeGUIPanel tempPanel=labelPoses.get(k);
            int idNumber=tempPanel.getID();
            if(idNumber==idLeft){
                //durationRight=labelPoses.get(k+1).getD
                 try{
                     idRight=labelPoses.get(k+1).getID();
                }catch(IndexOutOfBoundsException e){
                    idRight=labelPoses.get(0).getID();
                }
            }
        }
        
        
        
        String spriteName=viewer.getJlist().getSelectedValue().toString();
        String filePath="./data/sprite_types/"+spriteName+"/"+spriteName+".xml";
        try{
            String spriteTypeName=viewer.getJlist().getSelectedValue().toString();
            HashMap<String, SpriteType> spriteTypes=viewer.getSpriteTypes();
            SpriteType spriteType=spriteTypes.get(spriteTypeName);
            String xsdFile=SPRITES_DATA_PATH+SPRITE_TYPE_SCHEMA_FILE;
        
            SpriteType sprite=viewer.getSpriteTypes().get(spriteTypeName);
            File newXML=new File(filePath);
            //File newXML=new File((SPRITE_TYPES_XML_PATH+spriteTypeName+"/"+spriteTypeName+".xml"));
        try{
         // THESE WILL US BUILD A DOC
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        
        
        // FIRST MAKE THE DOCUMENT
        Document doc = docBuilder.newDocument();
        
        // THEN THE ROOT ELEMENT
        Element rootElement = doc.createElement(SPRITE_TYPE_NODE);
        
        doc.appendChild(rootElement);
        
        // THEN MAKE AND ADD THE WIDTH, HEIGHT, AND NUM SHAPES
           
            Element widthTypeElement = makeElement(doc, rootElement, 
                    WIDTH_NODE, ""+sprite.getWidth());
            Element heightTypeElement = makeElement(doc, rootElement, 
                    HEIGHT_NODE, ""+sprite.getHeight());
            Element imagesListTypeElement = makeElement(doc, rootElement, 
                    IMAGES_LIST_NODE, "" );   
            //Min and max needed to find the pic id name.
            
            int size=0;
            
            Iterator<String>states=spriteType.getAnimationStates();
            //to find the max id number
            while(states.hasNext()){
                String spriteState=states.next();
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
            
            int sizeForState=size;
           
            int y=1;
            Iterator <String> state=sprite.getAnimationStates();
            
                
               //as long as there is state, keep iterating. 
                
                while(state.hasNext()){
                    //iterate through each single state to find max pose number 
                    String st=state.next();  
                    //for each state get the entire pose list.
                    Iterator <Pose> pose=sprite.getPoseList(st).getPoseIterator();
                    //iterate in the pose list to get the image id number.
                    HashMap<String, String> idMap=new HashMap();
                    HashMap<String, String> poseMap=new HashMap();
                    for(int j=0;j<sprite.getPoseList(st).getNumOfPoses();j++){
                        //check the image id of each pose, identify the min and max
                        Pose p1=pose.next();
                        int id=p1.getImageID();
                        
                          
                        if(id!=1000)
                        idMap.put(""+p1.getImageID(), ""+spriteTypeName+"_"+(st+"_"+id).toUpperCase()+".png");
                        if(stateName.equals(st)){
                            poseMap.put(""+p1.getImageID(), ""+p1.getDurationInFrames());
                        }
                        //to get the duration for left and right 
                        if(st.equals(stateName)&&id==idLeft){
                            durationLeft=p1.getDurationInFrames();
                            
                        }
                        if(st.equals(stateName)&&id==idRight){
                            durationRight=p1.getDurationInFrames();
                           
                        }
                        
                    }
                    //create a node from min to max and add them under the imagesList node.
                    //int y=1;
                    
                    //copy all the data from the state
                       
                            Iterator<Entry<String, String>>idsIterator=idMap.entrySet().iterator();
                        
                            while(idsIterator.hasNext()){
                                Entry<String, String>idToken=idsIterator.next();
                      
                                Element imageFileTypeElement = makeElement(doc, imagesListTypeElement, 
                                IMAGE_FILE_NODE);
                                imageFileTypeElement.setAttribute("id", ""+idToken.getKey());
                                imageFileTypeElement.setAttribute("file_name", ""+idToken.getValue());
                            }
                    
                }
                    
                  
                
            int flag1=0;
            int flag2=0;    
            
            
            Element animationsListTypeElement = makeElement(doc, rootElement, 
                    ANIMATIONS_LIST_NODE, "" ); 
            state=sprite.getAnimationStates();
            String selectedState=viewer.getCombo().getSelectedItem().toString();
            out:
            while(state.hasNext()){
                
                String st=state.next();
                if(st.equals("NULL")){
                    continue out;
                }
                
                //that's where the naming of the state will be changed
                //if is is selected state, pass everyhing and chage the names 
                if(st.equals(selectedState)){
                        Element animationStateElement = makeElement(doc, animationsListTypeElement, 
                        ANIMATION_STATE, "" );
                        //new state element added to the node
                        Element stateElement = makeElement(doc, animationStateElement, 
                        STATE,""+st); 
                        Element animationSequenceElement = makeElement(doc, animationStateElement, 
                        ANIMATION_SEQUENCE );
                        Iterator <Pose> poseList=sprite.getPoseList(st).getPoseIterator();
                        
                        
                        next:
                        for(int j=0;j<sprite.getPoseList(st).getNumOfPoses();j++){
                        //check the image id of each pose, identify the min and max
                            Pose p1=poseList.next();
                            Element poseElement = makeElement(doc, animationSequenceElement, 
                            POSE_NODE);
                            if(p1.getImageID()==idRight&&flag1==0){
                                
                                poseElement.setAttribute("image_id", ""+idLeft);
                                poseElement.setAttribute("duration", ""+durationLeft);
                                flag1=1;
                                continue next;
                                
                            }else if(p1.getImageID()==idLeft&&flag2==0){
                                poseElement.setAttribute("image_id", ""+idRight);
                                poseElement.setAttribute("duration", ""+durationRight);
                                flag2=1;
                                continue next;
                            }
                            
                            poseElement.setAttribute("image_id", ""+p1.getImageID());
                            poseElement.setAttribute("duration", ""+p1.getDurationInFrames());
                            
                        }
                    continue out;
                }
                Element animationStateElement = makeElement(doc, animationsListTypeElement, 
                        ANIMATION_STATE, "" );
                //new state element added to the node
                Element stateElement = makeElement(doc, animationStateElement, 
                        STATE,""+st); 
                Element animationSequenceElement = makeElement(doc, animationStateElement, 
                        ANIMATION_SEQUENCE );
                Iterator <Pose> poseList=sprite.getPoseList(st).getPoseIterator();
                for(int j=0;j<sprite.getPoseList(st).getNumOfPoses();j++){
                        //check the image id of each pose, identify the min and max
                    Pose p1=poseList.next();
                    Element poseElement = makeElement(doc, animationSequenceElement, 
                        POSE_NODE );
                    poseElement.setAttribute("image_id", ""+p1.getImageID());
                    poseElement.setAttribute("duration", ""+p1.getDurationInFrames());
               }
            
            }
            
            //iterator to state through
            state=sprite.getAnimationStates();
            //int y=0;
            
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, YES_VALUE);
            transformer.setOutputProperty(XML_INDENT_PROPERTY, XML_INDENT_VALUE);
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(newXML);
            
            // SAVE THE POSE TO AN XML FILE
            transformer.transform(source, result); 
          
       
            
           AnimatedSpriteViewer view=gui.getAnimametedViewerPanel();
           view.loadSpritesFromState(spriteTypeName);
           
           view.loadSpritesFromState(spriteTypeName);
           view.getJlist().setSelectedValue(spriteTypeName, DEFAULT_DEBUG_TEXT_ENABLED);
           view.getCombo().setSelectedItem(stateName);
            
            
          
            
        }
        catch( ParserConfigurationException | TransformerException | DOMException | HeadlessException ex)
        {
            // SOMETHING WENT WRONG WRITING THE XML FILE
           
            JOptionPane.showMessageDialog(
                gui,
                POSE_SAVING_ERROR_TEXT,
                POSE_SAVING_ERROR_TITLE_TEXT,
                JOptionPane.ERROR_MESSAGE);  
            ex.printStackTrace();
          
        }   
        
        }catch(NullPointerException e){
            JOptionPane.showMessageDialog(viewer, "You did not select a state to delete for!", STATE, TRANSPARENT);
            System.out.println("You did not select a state to delete for");
            e.printStackTrace();
           
        }
    }

   void poseShiftLeft(int iD) {
        Poseur singleton = Poseur.getPoseur();
        PoseurGUI gui = singleton.getGUI();
        AnimatedSpriteViewer viewer=gui.getAnimametedViewerPanel();
        String stateName=viewer.getCombo().getSelectedItem().toString();
        LinkedList<SpriteTypeGUIPanel> labelPoses=viewer.getLabelPoses();
        int idRight=1000;
        int idLeft=iD;
        int durationRight = 0;
        int durationLeft = 0;
        //String durationString=JOptionPane.showInputDialog(viewer,"Please enter a new duration for the selected Pose");
        //int duration=Integer.parseInt(durationString);
       
        
        //To find the ID number which is on the right
        for(int k=0;k<labelPoses.size();k++){
            SpriteTypeGUIPanel tempPanel=labelPoses.get(k);
            int idNumber=tempPanel.getID();
            if(idNumber==idLeft){
                //durationRight=labelPoses.get(k+1).getD
                
                try{
                     idRight=labelPoses.get(k-1).getID();
                }catch(IndexOutOfBoundsException e){
                     idRight=labelPoses.get(labelPoses.size()-1).getID();
                }
                
               
            }
        }
        
        
        
        String spriteName=viewer.getJlist().getSelectedValue().toString();
        String filePath="./data/sprite_types/"+spriteName+"/"+spriteName+".xml";
        try{
            String spriteTypeName=viewer.getJlist().getSelectedValue().toString();
            HashMap<String, SpriteType> spriteTypes=viewer.getSpriteTypes();
            SpriteType spriteType=spriteTypes.get(spriteTypeName);
            String xsdFile=SPRITES_DATA_PATH+SPRITE_TYPE_SCHEMA_FILE;
        
            SpriteType sprite=viewer.getSpriteTypes().get(spriteTypeName);
            File newXML=new File(filePath);
            //File newXML=new File((SPRITE_TYPES_XML_PATH+spriteTypeName+"/"+spriteTypeName+".xml"));
        try{
         // THESE WILL US BUILD A DOC
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        
        
        // FIRST MAKE THE DOCUMENT
        Document doc = docBuilder.newDocument();
        
        // THEN THE ROOT ELEMENT
        Element rootElement = doc.createElement(SPRITE_TYPE_NODE);
        
        doc.appendChild(rootElement);
        
        // THEN MAKE AND ADD THE WIDTH, HEIGHT, AND NUM SHAPES
           
            Element widthTypeElement = makeElement(doc, rootElement, 
                    WIDTH_NODE, ""+sprite.getWidth());
            Element heightTypeElement = makeElement(doc, rootElement, 
                    HEIGHT_NODE, ""+sprite.getHeight());
            Element imagesListTypeElement = makeElement(doc, rootElement, 
                    IMAGES_LIST_NODE, "" );   
            //Min and max needed to find the pic id name.
            
            int size=0;
            
            Iterator<String>states=spriteType.getAnimationStates();
            //to find the max id number
            while(states.hasNext()){
                String spriteState=states.next();
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
            
            int sizeForState=size;
           
            int y=1;
            Iterator <String> state=sprite.getAnimationStates();
            
                
               //as long as there is state, keep iterating. 
                
                while(state.hasNext()){
                    //iterate through each single state to find max pose number 
                    String st=state.next();  
                    //for each state get the entire pose list.
                    Iterator <Pose> pose=sprite.getPoseList(st).getPoseIterator();
                    //iterate in the pose list to get the image id number.
                    HashMap<String, String> idMap=new HashMap();
                    HashMap<String, String> poseMap=new HashMap();
                    for(int j=0;j<sprite.getPoseList(st).getNumOfPoses();j++){
                        //check the image id of each pose, identify the min and max
                        Pose p1=pose.next();
                        int id=p1.getImageID();
                        
                          
                        if(id!=1000)
                        idMap.put(""+p1.getImageID(), ""+spriteTypeName+"_"+(st+"_"+id).toUpperCase()+".png");
                        if(stateName.equals(st)){
                            poseMap.put(""+p1.getImageID(), ""+p1.getDurationInFrames());
                        }
                        //to get the duration for left and right 
                        if(st.equals(stateName)&&id==idLeft){
                            durationLeft=p1.getDurationInFrames();
                            
                        }
                        if(st.equals(stateName)&&id==idRight){
                            durationRight=p1.getDurationInFrames();
                           
                        }
                        
                    }
                    //create a node from min to max and add them under the imagesList node.
                    //int y=1;
                    
                    //copy all the data from the state
                       
                            Iterator<Entry<String, String>>idsIterator=idMap.entrySet().iterator();
                        
                            while(idsIterator.hasNext()){
                                Entry<String, String>idToken=idsIterator.next();
                      
                                Element imageFileTypeElement = makeElement(doc, imagesListTypeElement, 
                                IMAGE_FILE_NODE);
                                imageFileTypeElement.setAttribute("id", ""+idToken.getKey());
                                imageFileTypeElement.setAttribute("file_name", ""+idToken.getValue());
                            }
                    
                }
                    
                  
                
            int flag1=0;
            int flag2=0;    
            
            
            Element animationsListTypeElement = makeElement(doc, rootElement, 
                    ANIMATIONS_LIST_NODE, "" ); 
            state=sprite.getAnimationStates();
            String selectedState=viewer.getCombo().getSelectedItem().toString();
            out:
            while(state.hasNext()){
                
                String st=state.next();
                if(st.equals("NULL")){
                    continue out;
                }
                
                //that's where the naming of the state will be changed
                //if is is selected state, pass everyhing and chage the names 
                if(st.equals(selectedState)){
                        Element animationStateElement = makeElement(doc, animationsListTypeElement, 
                        ANIMATION_STATE, "" );
                        //new state element added to the node
                        Element stateElement = makeElement(doc, animationStateElement, 
                        STATE,""+st); 
                        Element animationSequenceElement = makeElement(doc, animationStateElement, 
                        ANIMATION_SEQUENCE );
                        Iterator <Pose> poseList=sprite.getPoseList(st).getPoseIterator();
                        
                        
                        next:
                        for(int j=0;j<sprite.getPoseList(st).getNumOfPoses();j++){
                        //check the image id of each pose, identify the min and max
                            Pose p1=poseList.next();
                            Element poseElement = makeElement(doc, animationSequenceElement, 
                            POSE_NODE);
                            if(p1.getImageID()==idLeft&&flag1==0){
                                poseElement.setAttribute("image_id", ""+idRight);
                                poseElement.setAttribute("duration", ""+durationRight);
                                flag1=1;
                                continue next;
                            }else if(p1.getImageID()==idRight&&flag2==0){
                                poseElement.setAttribute("image_id", ""+idLeft);
                                poseElement.setAttribute("duration", ""+durationLeft);
                                flag2=1;
                                continue next;
                            }
                            
                            poseElement.setAttribute("image_id", ""+p1.getImageID());
                            poseElement.setAttribute("duration", ""+p1.getDurationInFrames());
                            
                        }
                    continue out;
                }
                Element animationStateElement = makeElement(doc, animationsListTypeElement, 
                        ANIMATION_STATE, "" );
                //new state element added to the node
                Element stateElement = makeElement(doc, animationStateElement, 
                        STATE,""+st); 
                Element animationSequenceElement = makeElement(doc, animationStateElement, 
                        ANIMATION_SEQUENCE );
                Iterator <Pose> poseList=sprite.getPoseList(st).getPoseIterator();
                for(int j=0;j<sprite.getPoseList(st).getNumOfPoses();j++){
                        //check the image id of each pose, identify the min and max
                    Pose p1=poseList.next();
                    Element poseElement = makeElement(doc, animationSequenceElement, 
                        POSE_NODE );
                    poseElement.setAttribute("image_id", ""+p1.getImageID());
                    poseElement.setAttribute("duration", ""+p1.getDurationInFrames());
               }
            
            }
            
            //iterator to state through
            state=sprite.getAnimationStates();
            //int y=0;
            
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, YES_VALUE);
            transformer.setOutputProperty(XML_INDENT_PROPERTY, XML_INDENT_VALUE);
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(newXML);
            
            // SAVE THE POSE TO AN XML FILE
            transformer.transform(source, result); 
          
       
            
           AnimatedSpriteViewer view=gui.getAnimametedViewerPanel();
           view.loadSpritesFromState(spriteTypeName);
           
           view.loadSpritesFromState(spriteTypeName);
           view.getJlist().setSelectedValue(spriteTypeName, DEFAULT_DEBUG_TEXT_ENABLED);
           view.getCombo().setSelectedItem(stateName);
            
            
          
            
        }
        catch( ParserConfigurationException | TransformerException | DOMException | HeadlessException ex)
        {
            // SOMETHING WENT WRONG WRITING THE XML FILE
           
            JOptionPane.showMessageDialog(
                gui,
                POSE_SAVING_ERROR_TEXT,
                POSE_SAVING_ERROR_TITLE_TEXT,
                JOptionPane.ERROR_MESSAGE);  
            ex.printStackTrace();
          
        }   
        
        }catch(NullPointerException e){
            JOptionPane.showMessageDialog(viewer, "You did not select a state to delete for!", STATE, TRANSPARENT);
            System.out.println("You did not select a state to delete for");
            e.printStackTrace();
           
        }
    }

    void duplicateState(String filePath, String k) {
        Poseur singleton = Poseur.getPoseur();
        PoseurGUI gui = singleton.getGUI();
        AnimatedSpriteViewer viewer=gui.getAnimametedViewerPanel();
        String stateName=viewer.getCombo().getSelectedItem().toString();
        //String newStateName=JOptionPane.showInputDialog(viewer,"Please enter new State name to duplicate");
        String newStateName=k;
        String spriteName=viewer.getJlist().getSelectedValue().toString();
        
        try{
            String spriteTypeName=viewer.getJlist().getSelectedValue().toString();
            HashMap<String, SpriteType> spriteTypes=viewer.getSpriteTypes();
            SpriteType spriteType=spriteTypes.get(spriteTypeName);
            String xsdFile=SPRITES_DATA_PATH+SPRITE_TYPE_SCHEMA_FILE;
        
            SpriteType sprite=viewer.getSpriteTypes().get(spriteTypeName);
            File newXML=new File(filePath);
            //File newXML=new File((SPRITE_TYPES_XML_PATH+spriteTypeName+"/"+spriteTypeName+".xml"));
        try{
         // THESE WILL US BUILD A DOC
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        
        
        // FIRST MAKE THE DOCUMENT
        Document doc = docBuilder.newDocument();
        
        // THEN THE ROOT ELEMENT
        Element rootElement = doc.createElement(SPRITE_TYPE_NODE);
        
        doc.appendChild(rootElement);
        
        // THEN MAKE AND ADD THE WIDTH, HEIGHT, AND NUM SHAPES
           
            Element widthTypeElement = makeElement(doc, rootElement, 
                    WIDTH_NODE, ""+sprite.getWidth());
            Element heightTypeElement = makeElement(doc, rootElement, 
                    HEIGHT_NODE, ""+sprite.getHeight());
            Element imagesListTypeElement = makeElement(doc, rootElement, 
                    IMAGES_LIST_NODE, "" );   
            //Min and max needed to find the pic id name.
            
            int size=0;
            
            Iterator<String>states=spriteType.getAnimationStates();
            //to find the max id number
            while(states.hasNext()){
                String spriteState=states.next();
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
            
            int sizeForState=size;
           
            int y=1;
            Iterator <String> state=sprite.getAnimationStates();
            
                
               //as long as there is state, keep iterating. 
                
                while(state.hasNext()){
                    //iterate through each single state to find max pose number 
                    String st=state.next();  
                    //for each state get the entire pose list.
                    Iterator <Pose> pose=sprite.getPoseList(st).getPoseIterator();
                    //iterate in the pose list to get the image id number.
                    HashMap<String, String> idMap=new HashMap();
                    HashMap<String, String> poseMap=new HashMap();
                    for(int j=0;j<sprite.getPoseList(st).getNumOfPoses();j++){
                        //check the image id of each pose, identify the min and max
                        Pose p1=pose.next();
                        int id=p1.getImageID();
                        
                          
                        if(id!=1000)
                        idMap.put(""+p1.getImageID(), ""+spriteTypeName+"_"+(st+"_"+id).toUpperCase()+".png");
                        if(stateName.equals(st)){
                            poseMap.put(""+p1.getImageID(), ""+p1.getDurationInFrames());
                        }
                        
                    }
                    //create a node from min to max and add them under the imagesList node.
                    //int y=1;
                    
                    //copy all the data from the state
                       
                            Iterator<Entry<String, String>>idsIterator=idMap.entrySet().iterator();
                        
                            while(idsIterator.hasNext()){
                                Entry<String, String>idToken=idsIterator.next();
                      
                                Element imageFileTypeElement = makeElement(doc, imagesListTypeElement, 
                                IMAGE_FILE_NODE);
                                imageFileTypeElement.setAttribute("id", ""+idToken.getKey());
                                imageFileTypeElement.setAttribute("file_name", ""+idToken.getValue());
                            }
                    
                    
                    
            
                    
                    
                    if(st.equals(stateName)){
                                                
                        size++;
                        idsIterator=idMap.entrySet().iterator();
                        
                        while(idsIterator.hasNext()){
                                Entry<String, String>idToken=idsIterator.next();
                      
                                Element imageFileTypeElement = makeElement(doc, imagesListTypeElement, 
                                IMAGE_FILE_NODE);
                                imageFileTypeElement.setAttribute("id", ""+size);
                                imageFileTypeElement.setAttribute("file_name", ""+spriteTypeName+"_"+newStateName.toUpperCase()+"_"+size+".png");
                            
                       
                        
                        File name=new File("./data/sprite_types/"+spriteTypeName+"/"+spriteTypeName+"_"+st.toUpperCase()+"_"+idToken.getKey()+".png");
                        File newName=new File("./data/sprite_types/"+spriteTypeName+"/"+spriteTypeName+"_"+newStateName.toUpperCase()+"_"+size+".png");
                        File namePose=new File("./data/sprite_types/"+spriteTypeName+"/"+spriteTypeName+"_"+st.toUpperCase()+"_"+idToken.getKey()+".pose");
                        File newNamePose=new File("./data/sprite_types/"+spriteTypeName+"/"+spriteTypeName+"_"+newStateName.toUpperCase()+"_"+size+".pose");
                        
                        //name.renameTo(newName);
                        //  / namePose.renameTo(newNamePose);
                        
                            try {
                                FileUtils.copyFile(name, newName);
                                FileUtils.copyFile(namePose, newNamePose);
                                
                            } catch (IOException ex) {
                                Logger.getLogger(SpriteIO.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        
                        
                        size++;
                        
                        } 
                        
                     idMap.clear();
                  }
      }
                    
                  
                
                
            
            
            Element animationsListTypeElement = makeElement(doc, rootElement, 
                    ANIMATIONS_LIST_NODE, "" ); 
            state=sprite.getAnimationStates();
            String selectedState=viewer.getCombo().getSelectedItem().toString();
            out:
            while(state.hasNext()){
                
                String st=state.next();
                if(st.equals("NULL")){
                    continue out;
                }
                
                //that's where the naming of the state will be changed
                //if is is selected state, pass everyhing and chage the names 
                if(st.equals(selectedState)){
                        Element animationStateElement = makeElement(doc, animationsListTypeElement, 
                        ANIMATION_STATE, "" );
                        //new state element added to the node
                        Element stateElement = makeElement(doc, animationStateElement, 
                        STATE,""+newStateName); 
                        Element animationSequenceElement = makeElement(doc, animationStateElement, 
                        ANIMATION_SEQUENCE );
                        Iterator <Pose> poseList=sprite.getPoseList(st).getPoseIterator();
                        sizeForState++;
                        for(int j=0;j<sprite.getPoseList(st).getNumOfPoses();j++){
                        //check the image id of each pose, identify the min and max
                            Pose p1=poseList.next();
                            Element poseElement = makeElement(doc, animationSequenceElement, 
                            POSE_NODE);
                            poseElement.setAttribute("image_id", ""+sizeForState);
                            poseElement.setAttribute("duration", ""+p1.getDurationInFrames());
                            sizeForState++;
                        }
                    //continue out;
                }
                Element animationStateElement = makeElement(doc, animationsListTypeElement, 
                        ANIMATION_STATE, "" );
                //new state element added to the node
                Element stateElement = makeElement(doc, animationStateElement, 
                        STATE,""+st); 
                Element animationSequenceElement = makeElement(doc, animationStateElement, 
                        ANIMATION_SEQUENCE );
                Iterator <Pose> poseList=sprite.getPoseList(st).getPoseIterator();
                for(int j=0;j<sprite.getPoseList(st).getNumOfPoses();j++){
                        //check the image id of each pose, identify the min and max
                    Pose p1=poseList.next();
                    Element poseElement = makeElement(doc, animationSequenceElement, 
                        POSE_NODE );
                    poseElement.setAttribute("image_id", ""+p1.getImageID());
                    poseElement.setAttribute("duration", ""+p1.getDurationInFrames());
               }
            
            }
            
            //iterator to state through
            state=sprite.getAnimationStates();
            //int y=0;
            
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, YES_VALUE);
            transformer.setOutputProperty(XML_INDENT_PROPERTY, XML_INDENT_VALUE);
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(newXML);
            
            // SAVE THE POSE TO AN XML FILE
            transformer.transform(source, result); 
          
       
            
           AnimatedSpriteViewer view=gui.getAnimametedViewerPanel();
           view.loadSpritesFromState(spriteTypeName);
           //view.getJlist().setSelectedValue(spriteTypeName, DEFAULT_DEBUG_TEXT_ENABLED);
           //view.getCombo().setSelectedItem(stateName);
            
            
           
            
        }
        catch( ParserConfigurationException | TransformerException | DOMException | HeadlessException ex)
        {
            // SOMETHING WENT WRONG WRITING THE XML FILE
           
            JOptionPane.showMessageDialog(
                gui,
                POSE_SAVING_ERROR_TEXT,
                POSE_SAVING_ERROR_TITLE_TEXT,
                JOptionPane.ERROR_MESSAGE);  
            ex.printStackTrace();
          
        }   
        
        }catch(NullPointerException e){
            JOptionPane.showMessageDialog(viewer, "You did not select a state to delete for!", STATE, TRANSPARENT);
            System.out.println("You did not select a state to delete for");
            e.printStackTrace();
           
        }
    }
    

   
    
}
