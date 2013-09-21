package poseur.shapes;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import org.w3c.dom.Element;
import static poseur.PoseurSettings.*;

/**
 * This class represents an elliptical shape in the Poseur application. Note
 * that it knows how to render itself and do other important tasks that use
 * its geometry. The geometry for a shape like an ellipse is very different 
 * than for other shapes, like a line, so we specify those differences 
 * inside this class.
 */
public class PoseurEllipse extends PoseurShape
{
    // THIS STORES ALL THE GEOMETRY FOR THIS ELLIPSE
    private Ellipse2D.Double geometry;
    
    // THIS GUY WILL BE USED FOR RENDERNIG ELLIPSES. WE HAVE IT BECAUSE
    // WE'LL BE STORING OUR ELLIPSE GEOMETRY INFORMATION IN "Pose" COORDINATES,
    // WHICH MEANS IN IT'S OWN LITTLE BOX, BUT WE'LL BE RENDERNIG IT ON THE
    // SCREEN INSIDE THAT BOX IN THE MIDDLE OF A PANEL USING PANEL COORDINATES.
    // SO, WE DON'T WANT TO CHANGE geometry EACH TIME WE RENDER, BECAUSE THEN
    // WE'LL HAVE TO CHANGE IT BACK. 
    private static Ellipse2D.Double sharedGeometry = new Ellipse2D.Double();

    /**
     * PoseurEllipse objects are constructed with their geometry, which
     * can be updated later via service methods.
     * 
     * @param initGeometry The geometry to associate with this ellipse.
     */
    public PoseurEllipse( Ellipse2D.Double initGeometry)
    {
        super();
        geometry = initGeometry;
    }
    
    /**
     * This static method constructs and returns a new ellipse with an
     * x location of the poseSpaceX argument, a y location of poseSpaceY,
     * and a width and height of 0.
     * 
     * @param poseSpaceX The requested x value for the new, factory
     * built PoseurEllipse object.
     * 
     * @param poseSpaceY The requested y value for the new, factory
     * built PoseurEllipse object.
     * 
     * @return A constructed PoseurEllipse.
     */
    public static PoseurEllipse factoryBuildEllipse(int poseSpaceX, int poseSpaceY)
    {
        Ellipse2D.Double ellipse = new Ellipse2D.Double(poseSpaceX, poseSpaceY,0,0);
        return new PoseurEllipse(ellipse);        
    }
    
    /**
     * Accessor method for getting this shape type.
     * 
     * @return The PoseurShapeType associated with this object.
     */
    @Override
    public PoseurShapeType getShapeType() { return PoseurShapeType.ELLIPSE; }
    
    /**
     * This method tests if the testPoint argument is inside this
     * ellipse. If it does, we return true, if not, false.
     * 
     * @param testPoint The point we want to test and see if it is
     * inside this ellipse
     * 
     * @return true if the point is inside this ellipse, false otherwise.
     */
    @Override
    public boolean containsPoint(Point2D testPoint)
    {
        return geometry.contains(testPoint);
    }

    /**
     * This method renders this ellipse to whatever context the g2 argument
     * comes from. 
     * 
     * @param g2 The graphics context for rendering. It may refer to that
     * of a canvas or an image.
     * 
     * @param poseOriginX The x coordinate location of the pose box.
     * 
     * @param poseOriginY The y coordinate location of the pose box.
     * 
     * @param zoomLevel Used for scaling all that gets rendered.
     * 
     * @param isSelected Selected items are highlighted.
     */
    @Override
    public void render(Graphics2D g2, 
            int poseOriginX, int poseOriginY, float zoomLevel, boolean isSelected)
    {
        // LOAD THE DATA FOR RENDING THIS SHAPE INTO OUR SHARED ELLIPSE
        sharedGeometry.x = poseOriginX + (geometry.x * zoomLevel);
        sharedGeometry.y = poseOriginY + (geometry.y * zoomLevel);
        sharedGeometry.width = geometry.width * zoomLevel;
        sharedGeometry.height = geometry.height * zoomLevel;
        
        // AND RENDER IT - NOTE THAT THIS renderShape METHOD IS AN
        // INHERITED METHOD INSIDE PoseurShape THAT GENERICALLY
        // OUTLINES AND THEN FILLS SHAPES ACCORDING TO THEIR SETTINGS
        renderShape(g2, sharedGeometry, isSelected);
    }

    /**
     * This method makes a clone, i.e. a duplicate, of this ellipse. This
     * is useful for cut/copy/paste types of operations in applications.
     * 
     * @return A constructed object that is identical to this one.
     */
    @Override
    public PoseurShape clone()
    {
        // LET'S USE THE Ellipse2D'S clone METHOD TO GET THE GEOMETRY
        Ellipse2D.Double copyGeometry = (Ellipse2D.Double)geometry.clone();
        
        // SINCE Color AND Stroke ARE IMMUTABLE,
        // WE DON'T MIND SHARING THEM 
        PoseurShape copy = new PoseurEllipse( copyGeometry);
        copy.fillColor = this.fillColor;
        copy.outlineColor = this.outlineColor;
        copy.outlineThickness = this.outlineThickness;

        // AND SEND IT BACK
        return copy;    
    }
    
    /**
     * This method moves this shape to the x, y location without doing
     * any error checking on whether it's a good location or not.
     * 
     * @param x The x coordinate of where to move this ellipse.
     * 
     * @param y The y coordinate of wehre to move this ellipse.
     */
    @Override
    public void move(int x, int y)
    {
        geometry.x = x;
        geometry.y = y;
    }

    /**
     * This is a smarter method for moving this ellipse, it considers
     * the pose area and prevents it from being moved off the pose area
     * by clamping at the edges.
     * 
     * @param incX The amount to move this ellipse in the x-axis.
     * 
     * @param incY The amount to move this ellipse in the y-axis.
     * 
     * @param poseArea The box in the middle of the rendering canvas
     * where the shapes are being rendered.
     */
    @Override
    public void moveShape(int incX, int incY, 
                     Rectangle2D.Double poseArea)
    {
        // MOVE THE SHAPE
        geometry.x += incX;
        geometry.y += incY;
        
        // AND NOW CLAMP IT SO IT DOESN'T GO OFF THE EDGE
        
        // CLAMP ON LEFT SIDE
        if (geometry.x < 0)
        {
            geometry.x = 0;
        }
        // CLAMP ON RIGHT
        if ((geometry.x + geometry.width) > poseArea.width)
        {
            geometry.x = poseArea.width - geometry.width -1;
        }
        // CLAMP ON TOP
        if (geometry.y < 0)
        {
            geometry.y = 0;
        }
        // CLAMP ON BOTTOM
        if ((geometry.y + geometry.height) > poseArea.height)
        {
            geometry.y = poseArea.height - geometry.height - 1;
        }
    }
    
    /**
     * This method tests to see if the x,y arguments would be valid
     * lower-right corner points for an ellipse in progress.
     * 
     * @param x The x-axis coordinate for the test point.
     * 
     * @param y The y-axis coordinate for the test point.
     * 
     * @return true if (x,y) would be a valid lower-right corner
     * point based on where this ellipse is currently located.
     */    
    @Override
    public boolean completesValidShape(int x, int y)
    {
         // WE ONLY LET SHAPES BE BUILT TOP LEFT TO BOTTOM RIGHT
        if ( (x < geometry.x) ||
             (y < geometry.y))
        {
            return false;
        }
        else
        {
            return true;
        }
    }
        
    /**
     * This method helps to update the an ellipse that's being
     * sized, testing to make sure it doesn't draw in illegal 
     * coordinates.
     * 
     * @param updateX The x-axis coordinate for the update point.
     * 
     * @param updateY The y-axis coordinate for the update point.
     */        
    @Override
    public void updateShapeInProgress(int updateX, int updateY)
    {
        // CLAMP TO THE LEFT
        if (updateX < geometry.x)
        {
            geometry.width = 0;
        }
        // IT'S GOOD IN THE X
        else
        {
            geometry.width = updateX - geometry.x;
        }

        // CLAMP TO THE TOP
        if (updateY < geometry.y)
        {
            geometry.height = 0;
        }
        // IT'S GOOD IN THE Y
        else
        {
            geometry.height = updateY - geometry.y;
        }    
    }

    /**
     * This method helps to build a .pose file. Ellipses know what data
     * they have, so this fills in the geometryNode argument DOC element
     * with the ellipse data that would be needed to recreate it when
     * it's loaded back from the .pose (xml) file.
     * 
     * @param geometryNode The node where we'll put attributes regarding
     * the geometry of this ellipse.
     */
    @Override
    public void addNodeData(Element geometryNode)
    {
        geometryNode.setAttribute(SHAPE_TYPE_ATTRIBUTE, getShapeType().name());
        geometryNode.setAttribute(X_ATTRIBUTE, "" + geometry.x);
        geometryNode.setAttribute(Y_ATTRIBUTE, "" + geometry.y);
        geometryNode.setAttribute(WIDTH_ATTRIBUTE, "" + geometry.width);
        geometryNode.setAttribute(HEIGHT_ATTRIBUTE, "" + geometry.height);
    }
}