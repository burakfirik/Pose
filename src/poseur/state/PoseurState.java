package poseur.state;

/**
 * This enum represents the states that are possible for our
 * Poseur editor. States represent the following:
 * 
 * STARTUP_STATE - This is the mode when the application first
 * opens, before a file has been loaded or a new pose has
 * been started. In this mode, most controls are inactive. Once
 * the user starts a new pose or loads an existing one we enter
 * the select shape mode.
 * 
 * SELECT_SHAPE_STATE - This is the default mode when a pose
 * is first started or loaded from a file. In this mode, the
 * user has an arrow cursor and can press various editing buttons
 * to get to work on creating content. In this mode, the user
 * may also select shapes on the pose area.
 * 
 * SHAPE_SELECTED_STATE - If the user selects a shape by clicking
 * on it, it moves into this mode. In this mode, the shape would
 * be highlighted and can be resized. This mode still uses the
 * arrow cursor.
 * 
 * CREATE_SHAPE_STATE - When the user selects one of the toggle
 * buttons to create a shape like a rectangle, the cursor switches
 * to crosshairs and enters this mode. In this mode, the user must
 * press on the pose area to start making a shape.
 * 
 * COMPLETE_SHAPE_STATE - Once the user has pressed on the pose area
 * it moves into this mode, where the user may drag the mouse
 * across the pose area to make the shape. If the user releases
 * the mouse on the pose area, a shape will be made there and we'll
 * return to select shape mode. If the mouse moves outside the 
 * pose area, the shape in progress will be reset but we'll stay
 * in this mode.
 * 
 * DRAG_SHAPE_STATE - When the user clicks on the pose area in 
 * select shape mode and selects a shape, we enter this mode
 * and remain until either leaving the pose area. In the meantime,
 * the selected shape moves wherever the mouse goes.
 * 
 * @author  Burak
 *          Debugging Enterprises
 * @version 1.0
 */
public enum PoseurState 
{
    STARTUP_STATE,
    SELECT_SHAPE_STATE,
    SHAPE_SELECTED_STATE,
    CREATE_SHAPE_STATE,
    COMPLETE_SHAPE_STATE,
    DRAG_SHAPE_STATE
}
