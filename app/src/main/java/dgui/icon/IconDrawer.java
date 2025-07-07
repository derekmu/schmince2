package dgui.icon;

import android.opengl.Matrix;
import dgui.GUIDrawer;
import dgui.GUIItem;
import dgui.GUIRenderer;
import thed.Rectangle;

/**
 * GUIDrawer for the Icon GUIItem.
 *
 * @author Derek Mulvihill - Jan 23, 2014
 */
public class IconDrawer implements GUIDrawer {
    @Override
    public void draw(GUIItem item, GUIRenderer render) {
        Icon icon = (Icon) item;

        if (icon.Icon != null) {
            Rectangle bounds = icon.Bounds;
            float[] matrix = render.getVPOrthoMatrix();
            Matrix.translateM(matrix, 0, bounds.centerX(), bounds.centerY(), 0);
            if (icon.AspectRatio <= 0) {
                Matrix.scaleM(matrix, 0, bounds.w / 2, bounds.h / 2, 0);
            } else {
                float w = bounds.w;
                float h = bounds.h;
                if (h * icon.AspectRatio > w) {
                    h = w / icon.AspectRatio;
                } else {
                    w = h * icon.AspectRatio;
                }
                Matrix.scaleM(matrix, 0, w / 2, h / 2, 0);
            }
            render.getGlib().getDrawer(icon.Icon).draw(matrix);
        }
    }
}
