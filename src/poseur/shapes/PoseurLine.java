package poseur.shapes;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D.Double;
import org.w3c.dom.Element;
import static poseur.PoseurSettings.*;

/**
 * This class represents a line shape in the Poseur application. Note
 * that it knows how to render itself and do other important tasks that use
 * its geometry. The geometry for a shape like a line is very different 
 * than for other shapes, like an ellipse, so we specify those differences 
 * inside this class.
 * 
 * @author  Burak
 *          Debugging Enterprises
 * @version 1.0
 */
public class PoseurLine extends PoseurShape
{
    // THIS STORES ALL THE GEOMETRY FOR THIS LINE
    private Line2D.Double geometry;

    // THIS GUY WILL BE USED FOR RENDERNIG LINES. WE HAVE IT BECAUSE
    // WE'LL BE STORING OUR LINE GEOMETRY INFORMATION IN "Pose" COORDINATES,
    // WHICH MEANS IN IT'S OWN LITTLE BOX, BUT WE'LL BE RENDERNIG IT ON THE
    // SCREEN INSIDE THAT BOX IN THE MIDDLE OF A PANEL USING PANEL COORDINATES.
    // SO, WE DON'T WANT TO CHANGE geometry EACH TIME WE RENDER, BECAUSE THEN
    // WE'LL HAVE TO CHANGE IT BACK. 
    private static Line2D.Double sharedGeometry = new Line2D.Double(0,0,0,0);
    
    /**
     * PoseurLine objects are constructed with their geometry, which
     * can be updated later via service methods.
     * 
     * @param initGeometry The geometry to associate with this line.
     */    
    public PoseurLine(Line2D.Double initGeometry)
    {
        super();
        geometry = initGeometry;
    }

    /**
     * This static method constructs and returns a new line with an
     * x location of the poseSpaceX argument, a y location of poseSpaceY,
     * and a width and height of 0.
     * 
     * @param poseSpaceX The requested x value for the new, factory
     * built PoseurLine object.
     * 
     * @param poseSpaceY The requested y value for the new, factory
     * built PoseurLine object.
     * 
     * @return A constructed PoseurLine.
     */    
    public static PoseurLine factoryBuildLine(int poseSpaceX, int poseSpaceY)
    {
        Line2D.Double line = new Line2D.Double(poseSpaceX, poseSpaceY, poseSpaceX, poseSpaceY);
        return new PoseurLine(line);                
    }
    
    /**
     * Accessor method for getting this shape type.
     * 
     * @return The PoseurShapeType associated with this object.
     */    
    @Override
    public PoseurShapeType getShapeType() { return PoseurShapeType.LINE; }

    /**
     * This method tests if the testPoint argument is approximately on this
     * line. If it does, we return true, if not, false.
     * 
     * @param testPoint The point we want to test and see if it is
     * on this line
     * 
     * @return true if the point is on this line, false otherwise.
     */    
    @Override
    public boolean containsPoint(Point2D testPoint)
    {
        double distance = geometry.ptLineDist(testPoint);
        return distance < (super.outlineThickness.getLineWidth() + LINE_SELECTION_TOLERANCE);
    }

    /**
     * This method renders this line to whatever context the g2 argument
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
    public void render(Graphics2D g2, int poseOriginX, int poseOriginY, float zoomLevel, boolean isSelected)
    {
        sharedGeometry.x1 = poseOriginX + (geometry.x1 * zoomLevel);
        sharedGeometry.y1 = poseOriginY + (geometry.y1 * zoomLevel);
        sharedGeometry.x2 = poseOriginX + (geometry.x2 * zoomLevel);
        sharedGeometry.y2 = poseOriginY + (geometry.y2 * zoomLevel);
        renderShape(g2, sharedGeometry, isSelected);
    }

    /**
     * This method makes a clone, i.e. a duplicate, of this line. This
     * is useful for cut/copy/paste types of operations in applications.
     * 
     * @return A constructed object that is identical to this one.
     */
    @Override
    public PoseurShape clone()
    {
        Line2D.Double copyGeometry = (Line2D.Double)geometry.clone();
        
        // SINCE Color AND Stroke ARE IMMUTABLE,
        // WE DON'T MIND SHARING THEM 
        PoseurShape copy = new PoseurLine( copyGeometry);
        copy.fillColor = this.fillColor;
        copy.outlineColor = this.outlineColor;
        copy.outlineThickness = this.outlineThickness;
        
        return copy;        
    }

    /**
     * This method moves this shape to the x, y location without doing
     * any error checking on whether it's a good location or not.
     * 
     * @param x The x coordinate of where to move this line.
     * 
     * @param y The y coordinate of wehre to move this line.
     */
    @Override
    public void move(int x, int y)
    {
        double diffX = x - geometry.x1;
        double diffY = y - geometry.y1;
        geometry.x1 = x;
        geometry.y1 = y;
        geometry.x2 += diffX;
        geometry.y2 += diffY;
    }

    /**
     * This is a smarter method for moving this line, it considers
     * the pose area and prevents it from being moved off the pose area
     * by clamping at the edges.
     * 
     * @param incX The amount to move this line in the x-axis.
     * 
     * @param incY The amount to move this line in the y-axis.
     * 
     * @param poseArea The box in the middle of the rendering canvas
     * where the shapes are being rendered.
     */
    @Override
    public void moveShape(int incX, int incY, Double poseArea)
    {
        int minX = (int)Math.min(geometry.x1, geometry.x2);
        if ((minX + incX) < 0)
        {
            incX = -minX;
        }
        int minY = (int)Math.min(geometry.y1, geometry.y2);
        if ((minY + incY) < 0)
        {
            incY = -minY;
        }
        int maxX = (int)Math.max(geometry.x1, geometry.x2);
        if ((maxX + incX) > poseArea.width)
        {
            incX = (int)(poseArea.width - maxX);
        }
        int maxY = (int)Math.max(geometry.y1, geometry.y2);
        if ((maxY + incY) > poseArea.height)
        {
            incY = (int)(poseArea.height - maxY);
        }
        geometry.x1 += incX;
        geometry.x2 += incX;
        geometry.y1 += incY;
        geometry.y2 += incY;
    }
    
    /**
     * This method tests to see if the x,y arguments would be valid
     * second point for a line in progress.
     * 
     * @param x The x-axis coordinate for the test point.
     * 
     * @param y The y-axis coordinate for the test point.
     * 
     * @return true if (x,y) would be a valid point for this line.
     * Note that this will always be true, since we don't care which
     * direction the line is drawn is in.
     */  
    @Override
    public boolean completesValidShape(int x, int y)
    {
        return true;
    }

    /**
     * This method helps to update the a line that's being
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
        geometry.x2 = updateX;
        geometry.y2 = updateY;
    }

    /**
     * This method helps to build a .pose file. Lines know what data
     * they have, so this fills in the geometryNode argument DOC element
     * with the line data that would be needed to recreate it when
     * it's loaded back from the .pose (xml) file.
     * 
     * @param geometryNode The node where we'll put attributes regarding
     * the geometry of this line.
     */    
    @Override
    public void addNodeData(Element geometryNode)
    {
        geometryNode.setAttribute(SHAPE_TYPE_ATTRIBUTE, getShapeType().name());
        geometryNode.setAttribute(X1_ATTRIBUTE, "" + geometry.x1);
        geometryNode.setAttribute(Y1_ATTRIBUTE, "" + geometry.y1);
        geometryNode.setAttribute(X2_ATTRIBUTE, "" + geometry.x2);
        geometryNode.setAttribute(Y2_ATTRIBUTE, "" + geometry.y2);
    }
}