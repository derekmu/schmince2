package dgui.textfield;

import dgui.GUIDrawer;
import dgui.GUIItem;
import dgui.GUIRenderer;
import texample.GLText;
import thed.Rectangle;

/**
 * GUIDrawer for the TextField GUIItem.
 *
 * @author Derek Mulvihill - Oct 2, 2013
 */
public class TextFieldDrawer implements GUIDrawer {
    @Override
    public void draw(GUIItem item, GUIRenderer render) {
        TextField field = (TextField) item;
        Rectangle bounds = field.Bounds;
        render.getGlib().getRectangle().setBounds(bounds.x, bounds.y, bounds.w, bounds.h);
        render.getGlib()
                .getRectangle()
                .draw(render.getVPOrthoMatrix(), field.BackgroundColor.Red,
                        field.BackgroundColor.Green, field.BackgroundColor.Blue,
                        field.BackgroundColor.Alpha);

        if (field.getTextValue().length() > 0) {
            GLText text = render.getGlib().getText(field.TextType, field.TextScale);
            float height = text.getHeight();
            text.begin(field.TextColor.Red, field.TextColor.Green, field.TextColor.Blue,
                    field.TextColor.Alpha, render.getVPOrthoMatrix());
            text.draw(field.getTextValue(), bounds.x, bounds.centerY() - height / 2f, 0);
            text.end();
        }
    }
}
