package dgui.panel;

import dgui.GUIDrawer;
import dgui.GUIItem;
import dgui.GUIRenderer;
import thed.Rectangle;

/**
 * GUIDrawer for Panel class.
 *
 * @author Derek Mulvihill - Oct 18, 2013
 */
public class PanelDrawer implements GUIDrawer {
    @Override
    public void draw(GUIItem item, GUIRenderer render) {
        Panel panel = (Panel) item;
        Rectangle bounds = panel.Bounds;
        render.getGlib().getRectangle().setBounds(bounds.x, bounds.y, bounds.w, bounds.h);
        render.getGlib()
                .getRectangle()
                .draw(render.getVPOrthoMatrix(), panel.Color.Red, panel.Color.Green,
                        panel.Color.Blue, panel.Color.Alpha);
    }
}
