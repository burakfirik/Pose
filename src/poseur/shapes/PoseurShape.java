package poseur.shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import org.w3c.dom.Element;
import poseur.Poseur;
import static poseur.PoseurSettings.*;
import poseur.gui.PoseurGUI;

/**
 * This abstract class provides a template for shapes to be rendered inside
 * the Poseur application.
 * 
 * @author  Burak
 *          Debugging Enterprises
 * @version 1.0
 */
public abstract class PoseurShape 
{
    // LINE THICKNESS OF THE SHAPE OUTLINE
    protected BasicStroke outlineThickness;
    
    // COLOR TO USE FOR RENDERING THE SHAPE OUTLINE
    protected Color outlineColor;
    
    // COLOR TO USE FOR FILLING THE SHAPE
    protected Color fillColor;
    
    // SHAPE TRANSPARENCY FOR THIS SHAPE
    protected int alpha;

    /**
     * Default constructor for this abstract class, it initializes
     */
    public PoseurShape()
    {
        // LET'S FILL IN THIS CLASS' DATA WITH SETTINGS
        // FROM THE GUI CONTROLS
        Poseur singleton = Poseur.getPoseur();
        PoseurGUI gui = singleton.getGUI();
        
        // ASK THE GUI FOR ALL THESE VALUES
        outlineColor = gui.getOutlineColor();
        fillColor = gui.getFillColor();
        int lineThickness = gui.getLineThickness();
        outlineThickness = new BasicStroke(lineThickness);        
        alpha = gui.getAlphaTransparency();
     }

    // ACCESSOR METHODS
    
    /**
     * Accessor method for getting this shape's line thickness.
     * 
     * @return The line thickness of this shape.
     */
    public BasicStroke  getOutlineThickness()   { return outlineThickness;  }
    
    /**
     * Accessor method for getting this shape's outline color.
     * 
     * @return The outline color of this shape.
     */
    public Color        getOutlineColor()       { return outlineColor;      }
    
    /**
     * Accessor method for getting this shape's fill color.
     * 
     * @return The fill color of this shape.
     */
    public Color        getFillColor()          { return fillColor;         }
    
    /**
     * Accessor method for getting this shape's alpha value.
     * 
     * @return The alpha value (0 is transparent, 255 opaque) for this shape.
     */
    public int          getAlpha()              { return alpha;             }

    // MUTATOR METHODS
    
    /**
     * Mutator method for setting this shape's outline thickness.
     * 
     * @param initStroke The stroke to use for rendering this shape.
     */
    public void setOutlineThickness(BasicStroke initStroke) { outlineThickness = initStroke;    }

    /**
     * Mutator method for setting this shape's outline color.
     * 
     * @param initOutlineColor The color to use for the outline of this shape.
     */
    public void setOutlineColor(Color initOutlineColor)     { outlineColor = initOutlineColor;  }
    
    /**
     * Mutator method for setting this shape's fill color.
     * 
     * @param initFillColor The color to use for filling this shape.
     */
    public void setFillColor(Color initFillColor)           { fillColor = initFillColor;        }
    
    /**
     * Mutator method for setting this shape's alpha value.
     * 
     * @param initAlpha The alpha to use (0 is transparent, 255 opaque) for 
     * rendering this shape.
     */
    public void setAlpha(int initAlpha)                     { alpha = initAlpha;                }
    
    /**
     * This method renders the shapeGeometry to the g2 context using
     * this shape's line thickness, colors, and alpha values.
     * 
     * @param g2 The rendering context, which could be a panel on the
     * screen or an image to be saved to a file.
     * 
     * @param shapeGeometry The geometry to use for rendering.
     * 
     * @param isSelected Marks if this shape is to be rendered as 
     * highlighted (i.e. selected) or not.
     */
    public void renderShape(Graphics2D g2, Shape shapeGeometry, boolean isSelected)
    {
        // FILL FIRST
        Color colorWithAlpha = getColorWithAlpha(fillColor);
        g2.setColor(colorWithAlpha);
        g2.fill(shapeGeometry);
        
        // THEN DRAW THE OUTLINE
        g2.setStroke(outlineThickness);
        if (isSelected)
        {
            colorWithAlpha = getColorWithAlpha(SELECTED_OUTLINE_COLOR);
            g2.setColor(colorWithAlpha);
        }
        else
        {
            colorWithAlpha = getColorWithAlpha(outlineColor);
            g2.setColor(colorWithAlpha);
        }
        g2.draw(shapeGeometry);        
    }      
    
    // PRIVATE HELPER METHOD
    
    /**
     * We store the alpha value separately because of the color pallet. We may
     * want to apply different alpha values to different shapes but use the
     * same color. But, when it comes time to render our shapes, we need
     * the alpha value, so this method creates a color that combines the
     * colorWithoutAlpha argument with this shape's alpha value. That 
     * constructed Color object is then returned.
     * 
     * @param colorWithoutAlpha The color to process. It does not have
     * an alpha value.
     * 
     * @return A Color object with the same rgb values as the colorWithAlpha
     * argument and an added alpha value from this class's instance variable.
     */
    private Color getColorWithAlpha(Color colorWithoutAlpha)
    {
        return new Color(   colorWithoutAlpha.getRed(),
                            colorWithoutAlpha.getGreen(),
                            colorWithoutAlpha.getBlue(),
                            alpha);
    } 
    
    /***** ABSTRACT METHODS *****/
    // THESE METHODS MUST BE OVERRIDDEN BY ALL CHILD CLASSES
    
    /**
     * Accessor method for getting this shape's type.
     * 
     * @return The PoseurShapeType associated with this object.
     */  
    public abstract PoseurShapeType getShapeType();
    
    /**
     * This method tests if the testPoint argument is inside this
     * shape. If it does, we return true, if not, false.
     * 
     * @param testPoint The point we want to test and see if it is
     * inside this shape.
     * 
     * @return true if the point is inside this shape, false otherwise.
     */    
    public abstract boolean containsPoint(Point2D testPoint);
    
    /**
     * This method renders this shape to whatever context the g2 argument
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
    public abstract void    render( Graphics2D g2, 
                                    int poseOriginX, int poseOriginY, 
                                    float zoomLevel, 
                                    boolean isSelected);    
    
    /**
     * This method makes a clone, i.e. a duplicate, of this shape. This
     * is useful for cut/copy/paste types of operations in applications.
     * 
     * @return A constructed object that is identical to this one.
     */    
    @Override
    public abstract PoseurShape clone();

    /**
     * This method moves this shape to the x, y location without doing
     * any error checking on whether it's a good location or not.
     * 
     * @param x The x coordinate of where to move this shape.
     * 
     * @param y The y coordinate of where to move this shape.
     */    
    public abstract void move(int x, int y);
    
    /**
     * This is a smarter method for moving this shape, it considers
     * the pose area and prevents it from being moved off the pose area
     * by clamping at the edges.
     * 
     * @param incX The amount to move this shape in the x-axis.
     * 
     * @param incY The amount to move this shape in the y-axis.
     * 
     * @param poseArea The box in the middle of the rendering canvas
     * where the shapes are being rendered.
     */    
    public abstract void    moveShape(  int incX, int incY, 
                                        Rectangle2D.Double poseArea);
    
    /**
     * This method tests to see if the x,y arguments would be valid
     * next geometric valuee for a shape in progress.
     * 
     * @param x The x-axis coordinate for the test point.
     * 
     * @param y The y-axis coordinate for the test point.
     * 
     * @return true if (x,y) would be a valid second coordinate
     * for the shape in progress based on where it is currentyl
     * located.
     */     
    public abstract boolean completesValidShape(int x, int y);
    
    /**
     * This method helps to update the shape that's being
     * sized, testing to make sure it doesn't draw in illegal 
     * coordinates.
     * 
     * @param updateX The x-axis coordinate for the update point.
     * 
     * @param updateY The y-axis coordinate for the update point.
     */  
    public abstract void updateShapeInProgress(int updateX, int updateY);

    /**
     * This method helps to build a .pose file. Shapes know what data
     * they have, so this fills in the geometryNode argument DOC element
     * with the shape data that would be needed to recreate it when
     * it's loaded back from the .pose (xml) file.
     * 
     * @param geometryNode The node where we'll put attributes regarding
     * the geometry of this shape.
     */       
    public abstract void addNodeData(Element geometryNode);
}