package dgui;

import thed.Rectangle;

/**
 * Superclass of any GUI component.
 *
 * @author Derek Mulvihill
 */
public abstract class GUIItem {
    /**
     * Location in Orthographic coordinates where this GUIItem is located on the screen.
     */
    public final Rectangle Bounds = new Rectangle();
    /**
     * Whether this GUIItem is visible at the current time.
     */
    public boolean Visible = true;
    /**
     * Whether this GUIItem is the receiver of the current touch gesture.
     * This should only be modified by GUI.
     */
    public boolean HasFocus = false;

    public final GUIDrawer Drawer;
    public final GUIMover Mover;

    public GUIItem(GUIDrawer drawer) {
        this.Drawer = drawer;
        this.Mover = null;
    }

    public GUIItem(GUIDrawer drawer, GUIMover mover) {
        this.Drawer = drawer;
        this.Mover = mover;
    }
}
