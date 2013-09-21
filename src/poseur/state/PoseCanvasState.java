package poseur.state;

import java.awt.geom.Rectangle2D;
import poseur.gui.PoseCanvas;

/**
 * This class stores some state information regarding the
 * canvas rendering. Note that we'll use one of these for
 * each canvas. One for the true view, and one for the 
 * zoomable view, so two in all.
 * 
 * @author  Burak
 *          Debugging Enterprises
 * @version 1.0
 */
public class PoseCanvasState 
{
    // WE WON'T LET ONE OF THE CANVASES BE ZOOMED,
    // SO THIS WILL HELP PREVENT IT FROM HAPPENING
    private boolean zoomable;
    
    // ZOOM LEVEL FOR A ZOOMABLE CANVAS. NOTE THAT IT
    // WILL START AT 1, WHICH IS THE TRUE REPRESENTATION.
    // AS THIS NUMBER GETS BIGGER, WE ZOOM THE VIEW IN. AS
    // IT GETS SMALLER, WE ZOOM OUT
    private float zoomLevel;
    
    // THIS IS THE INCREMENT FOR ZOOMING
    private float zoomFactor;
    
    // THIS PREVENTS US FROM ZOOMING OUT TOO FAR
    private float minimumZoomLevel;
    
    // HERE'S THE VIEW, WHICH WE'LL NEED TO FORCE
    // A REPAINT WHEN THE ZOOM LEVEL CHANGES
    private PoseCanvas canvas;
    
    // HERE'S THE POSE THAT IS BEING EDITED
    private PoseurPose pose;

    // THIS WILL STORE INFORMATION ABOUT THE REGION ON
    // THE CANVAS WHERE THE POSE IS ACTUALLY BEING
    // RENDERED, SINCE IT'S A CENTERED SUBSET OF
    // THE CANVAS
    private Rectangle2D.Double poseArea;

    /**
     * This constructor initializes this object with default
     * values, but does not set it up ready for use. It will
     * only be ready for use after the canvas has been made
     * visible and has then been loaded into this object
     * using the loadPoseCanvas method.
     * 
     * @param initPose The pose being manipulated by the user
     * and rendered in the pose ares.
     * 
     * @param initZoomLevel The zoom level to use for rendering.
     * 
     * @param initZoomFactor The scale to use in increasing and
     * decreasing the zoom level.
     * 
     * @param initMinimumZoomLevel The furthest out we can zoom.
     */
    public PoseCanvasState( boolean initZoomable,
                            PoseurPose initPose,
                            float initZoomLevel,
                            float initZoomFactor,
                            float initMinimumZoomLevel)
    {
        // INIT WHETHER IT CAN BE ZOOMED OR NOT 
        zoomable = initZoomable;
        
        // WE'LL NEED THE POSE LATER. NOTE THERE IS ONLY
        // EVERY ONE POSE IN THE APP
        pose = initPose;
        
        // INIT THE CUSTOMLY PROVIDED ZOOM INFO
        zoomLevel = initZoomLevel;
        zoomFactor = initZoomFactor;
        minimumZoomLevel = initMinimumZoomLevel;
        
        // THIS WILL BE LOADED LATER, ONCE IT
        // HAS BEEN SIZED
        canvas = null;
        
        // WE'LL FILL IN THIS INFORMATION ONCE THE CANVAS
        // GETS LOADED, SINCE IT WILL HAVE A SIZE THEN
        poseArea = new Rectangle2D.Double();
    }
    
    // ACCESSOR METHODS
    
    /**
     * Accessor method for getting the canvas associated
     * with this canvas state.
     * 
     * @return The canvas that renders this state info.
     */
    public PoseCanvas getCanvas() { return canvas; }

    /**
     * Accessor method for getting the pose used by the
     * canvas for rendering.
     * 
     * @return The pose object associated with this canvas state.
     */
    public PoseurPose getPose() { return pose; }
    
    /**
     * Accessor method for getting the rectangle on the
     * canvas where we'll be rendering the pose.
     * 
     * @return The rectangle where the pose will be rendered.
     */
    public Rectangle2D.Double getPoseArea() { return poseArea; }

    /**
     * Accessor method for getting the zoom level for this
     * canvas state.
     * 
     * @return The zoom level, where 1 means the true state,
     * greater than 1 is zoomed in, and less than 1 is out.
     */
    public float getZoomLevel() { return zoomLevel; }
    
    /**
     * Accessor method for testing if this canvas can be
     * zoomed or not.
     * 
     * @return true is returned if this canvas can be zoomed,
     * false otherwise.
     */
    public boolean isZoomable() { return zoomable; }
    
    /**
     * This mutator method sets the reference to the canvas
     * for which this state is associated.
     * 
     * @param initCanvas The canvas associated with this state.
     */
    public void setPoseCanvas(PoseCanvas initCanvas)
    {
        // KEEP THE CANVAS FOR LATER
        canvas = initCanvas;
    }
    
    /**
     * This method updates the values representing the pose
     * area according to the current zoom level.
     */
    public void updatePoseArea()
    {
        // CALCULATE THE CENTER OF THE CANVAS
        int centerX = canvas.getWidth()/2;
        int centerY = canvas.getHeight()/2;
        
        // WE MAY HAVE DIFFERENT SIZED POSES, SO 
        // GET THE POSE DIMENSIONS AND SCALE THEM
        // ACCORDING TO THE ZOOM LEVEL
        poseArea.width = (int)(pose.getPoseWidth() * zoomLevel);
        poseArea.height = (int)(pose.getPoseHeight() * zoomLevel);

        // AND NOW FIGURE OUT THE TOP-LEFT CORNER
        // OF THE POSE AREA
        poseArea.x = centerX - (poseArea.width/2);
        poseArea.y = centerY - (poseArea.height/2);
    }
    
    /**
     * This method zooms in the view on the canvas. Note that
     * it will not zoom so far that the pose area is larger
     * than the canvas. After updating the zoom level, this
     * method will update the canvas view.
     */
    public void zoomIn()
    {
        // DON'T DO ANYTHING IF IT'S NOT ZOOMABLE
        if (!zoomable)
        {
            return;
        }
        
        // MAKE SURE WE AREN'T ZOOMING IN TOO FAR
        float testZoomLevel = zoomLevel + zoomFactor;
        
        // FIRST MAKE SURE IT'S NOT TOO WIDE TO FIT
        // IN THE CANVAS
        int testZoomedPoseAreaWidth = (int)(pose.getPoseWidth() * testZoomLevel);
        int canvasWidth = canvas.getWidth();
        if (testZoomedPoseAreaWidth > canvasWidth)
        {
            // IT'S TOO WIDE, SO DON'T DO ANYTHING ELSE
            return;
        }

        // NOW MAKE SURE IT ISN'T TOO TALL
        int testZoomedPoseAreaHeight = (int)(pose.getPoseHeight() * testZoomLevel);
        int canvasHeight = canvas.getHeight();
        if (testZoomedPoseAreaHeight > canvasHeight)
        {
            // IT'S TOO TALL, SO DON'T DO ANYTHING ELSE
            return;
        }
        
        // IF IT MADE IT THIS FAR, THEN THE POSE AREA WILL FIT
        // INSIDE THE CANVAS, SO LET'S GO AHEAD AND UPDATE THE
        // ZOOM LEVEL
        zoomLevel = testZoomLevel;
        
        // WE NEED TO MAKE SURE WE UPDATE THE POSE AREA TOO
        updatePoseArea();
        
        // NOW UPDATE THE VIEW IN THE CANVAS
        canvas.repaint();
    }
    
    /**
     * This method zooms out the canvas, which in turn
     * will update the canvas view. Note that this method
     * will not allow zooming out too far according to
     * the minimum zoom.
     */
    public void zoomOut()
    {        
        // DON'T DO ANYTHING IF IT'S NOT ZOOMABLE
        if (!zoomable)
        {
            return;
        }
        
        // DON'S LET IT UNDER THE MINIMUM
        float testZoom = zoomLevel - zoomFactor;
        if (testZoom < minimumZoomLevel)
        {
            return;
        }
        
        // UPDATE THE ZOOM LEVEL
        zoomLevel = testZoom;
        
        // WE NEED TO MAKE SURE WE UPDATE THE POSE AREA TOO
        updatePoseArea();
        
        // NOW UPDATE THE VIEW IN THE CANVAS
        canvas.repaint();
    } 
}