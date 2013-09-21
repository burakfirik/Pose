package poseur.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import javax.swing.JPanel;
import poseur.Poseur;
import static poseur.PoseurSettings.*;
import poseur.shapes.PoseurShape;
import poseur.state.PoseCanvasState;
import poseur.state.PoseurPose;
import poseur.state.PoseurStateManager;

/**
 * A PoseCanvas will render the Pose in the center of its surface.
 * Note that these may be zoomed or not, depending on their
 * state.
 * 
 * @author  Burak
 *          Debugging Enterprises
 * @version 1.0
 */
public class PoseCanvas extends JPanel
{
    // THE STATE WILL DICTATE HOW WE RENDER THE
    // POSE AS WELL AS STORING USEFUL RENDERING DATA
    private PoseCanvasState state;
    
    /**
     * This constructor makes sure the canvas has
     * the state from the beginning.
     * 
     * @param initState The state to be used with
     * rendering data for this canvas.
     */
    public PoseCanvas(PoseCanvasState initState)
    {
        // KEEP IT FOR LATER
        state = initState;
    }
    
    /**
     * Accessor method for getting the state associated
     * with this canvas.
     * 
     * @return The canvas state object associated with this
     * object, which contains information about zoom levels
     * and the pose area for rendering.
     */
    public PoseCanvasState getState() { return state; }
    
    /**
     * This is where all the rendering of the canvas is done. Note
     * that this method breaks down rendering into the shapes already
     * on the canvas, and the shape in progress, which the user
     * is working on.
     * 
     * @param g The Graphics context for this panel.
     */
    @Override
    public void paintComponent(Graphics g)
    {
        // ONLY RENDER IF THE PANEL IS VISIBLE
        int renderingSurfaceWidth = getWidth();
        if (renderingSurfaceWidth > 0)
        {
            // GET THE G2 
            Graphics2D g2 = (Graphics2D)g;
            
            // CLEAR CANVAS USING BACKGROUND COLOR
            super.paintComponent(g2);
            
            // GET THE POSE AREA
            Rectangle2D.Double poseArea = state.getPoseArea();
            
            // RENDER THE POSE AREA
            renderPoseAreaBackground(g2, poseArea);
            
            // RENDER WHAT'S INSIDE THE CANVAS
            renderShapes(g2, poseArea); 
            
            // AND RENDER WHAT'S BEING EDITED
            if (state.isZoomable())
            {
                // THE USER MUST HAVE PUT DOWN THE 
                // FIRST POINT OF A NEW SHAPE AND
                // HAS NOT YET RELEASED THE MOUSE BUTTON
                renderShapeInProgress(g2, poseArea);
            }
            
            // AND FINALLY, THE DEBUG TEXT, IF THE OPTION IS TURNED ON
            renderDebugText(g2);
        }
    }
    
    /**
     * This method renders what would be in the pose area into the
     * imageToPaintTo image argument. The paintComponent method 
     * paints to a canvas (i.e. the screen), which this one is
     * paints to an image that can then be saved to a file
     * 
     * @param imageToPaintTo The image we will paint what is
     * in this canvas' pose area to.
     */
    public void paintToImage(BufferedImage imageToPaintTo)
    {
        Graphics2D imageG2 = (Graphics2D)imageToPaintTo.getGraphics();
        Poseur singleton = Poseur.getPoseur();
        PoseurStateManager poseurStateManager = singleton.getStateManager();
        PoseurPose pose = poseurStateManager.getPose();
        
        // FIRST LET'S PUT A TRANSPARENT BACKGROUND THE SIZE OF THE POSE
        Rectangle2D.Double transparentRect = new Rectangle2D.Double(
                0, 0, pose.getPoseWidth(), pose.getPoseHeight());
        imageG2.setColor(TRANSPARENT_BACKGROUND_COLOR);
        //imageG2.setColor(Color.WHITE);
        imageG2.fill(transparentRect);
        imageG2.draw(transparentRect);
        
        // NOW LET'S DRAW ALL THE SHAPES ON TOP, BUT WE'LL
        // FAKE THE POSE AREA SO WE ONLY DRAW THE MIDDLE PART
        Rectangle2D.Double poseArea = new Rectangle2D.Double(
                0, 0, pose.getPoseWidth(), pose.getPoseHeight());
        renderShapes(imageG2, poseArea);
        
        // ALL DONE, WE'VE JUST PAINTED TO THE IMAGE WHAT WE
        // WOULD NORMALLY DRAW INSIDE THE POSE AREA
    }
    
    // HELPER METHODS

    /**
     * This method renders all debug text in the canvas. Doing
     * is useful in a graphical application because it's tricky
     * stopping the debugger while rendering, so using this 
     * technique we can view variable data in the app while
     * it is running.
     * 
     * @param g2 The graphics context for this canvas.
     */
    private void renderDebugText(Graphics2D g2)
    {
        // AND FINALLY, THE DEBUG TEXT, IF THE OPTION IS TURNED ON
        Poseur singleton = Poseur.getPoseur();
        if (singleton.isDebugTextEnabled())
        {
            // SET THE CORRECT FONT AND COLOR
            g2.setFont(DEBUG_TEXT_FONT);
            g2.setColor(DEBUG_TEXT_COLOR);
            
            // GET EACH ITEM ONE AT A TIME
            Iterator<String> debugTextIt = singleton.getDebugTextIterator();
            int x = DEBUG_TEXT_START_X;
            int y = DEBUG_TEXT_START_Y;
            while (debugTextIt.hasNext())
            {
                String text = debugTextIt.next();
                g2.drawString(text, x, y);
                y += DEBUG_TEXT_LINE_SPACING;
            }
            singleton.clearDebugText();
        } 
    }
    
    /**
     * This method fills in the background of the pose area,
     * which is in the center of the canvas.
     * 
     * @param g2 The graphics context for this panel.
     */
    private void renderPoseAreaBackground(Graphics2D g2, Rectangle2D.Double poseArea)
    {
        // WE'LL USE A DIFFERENT COLOR FOR THIS
        g2.setColor(POSE_BACKGROUND_COLOR);
        //g2.setColor(Color.WHITE);
        // FILL IT IN
        g2.fill(poseArea); 
    }

    /**
     * Renders all the shapes in the corresponding canvas
     * state object for this canvas.
     * 
     * @param g2 The graphics context of this panel.
     */
    private void renderShapes(Graphics2D g2, Rectangle2D.Double poseArea)
    {
        // LET'S GET THE POSE AREA AND THE POSE
        PoseurPose pose = state.getPose();
        float zoomLevel = state.getZoomLevel();        
        
        // RENDER THE ENTIRE POSE
        Iterator<PoseurShape> shapesIt = pose.getShapesIterator();
        Poseur singleton = Poseur.getPoseur();
        PoseurStateManager poseurStateManager = singleton.getStateManager();
        while (shapesIt.hasNext())
        {
            PoseurShape shape = shapesIt.next();
            boolean isSelected = poseurStateManager.isSelectedShape(shape);
            
            // NOTE THAT WE NEVER DEPICT SELECTED SHAPES DIFFERENTLY
            // IN THE TRUE CANVAS, ONLY THE ZOOMABLE CANVAS
            if (!state.isZoomable())
            {
                isSelected = false;
            }
            shape.render(g2, (int)poseArea.getX(), (int)poseArea.getY(), zoomLevel, isSelected);
        }        
    }
    
    /**
     * Renders the shape that is currently being sized by the user as they
     * drag the mouse across the canvas. We render this separately because
     * we have not yet put it in the shapes list for the pose.
     * 
     * @param g2 The graphics context for this canvas.
     * 
     * @param poseArea The area in the middle of the canvas where the
     * pose will be rendered.
     */
    private void renderShapeInProgress(Graphics2D g2, Rectangle2D.Double poseArea)
    {
        Poseur singleton = Poseur.getPoseur();
        PoseurStateManager poseurStateManager = singleton.getStateManager();
        PoseurShape shapeInProgress = poseurStateManager.getShapeInProgress();

        // ONLY DO THIS ON THE RIGHT SIDE
        if (state.isZoomable() && (shapeInProgress != null))
        {
            float zoomLevel = state.getZoomLevel();        
            shapeInProgress.render(g2, (int)poseArea.getX(), (int)poseArea.getY(), zoomLevel, true);
        }
    } 
}