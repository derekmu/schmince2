package dgui.label;

import dgui.GUIDrawer;
import dgui.GUIItem;
import dgui.GUIRenderer;
import texample.GLText;
import thed.Alignment;

/**
 * GUIDrawer for the Label GUIItem.
 *
 * @author Derek Mulvihill - Oct 2, 2013
 */
public class LabelDrawer implements GUIDrawer {
    @Override
    public void draw(GUIItem item, GUIRenderer render) {
        Label label = (Label) item;
        if (label.Text != null && label.Text.length() > 0) {
            GLText text = render.getGlib().getText(label.TextType, label.TextScale);
            float height = text.getHeight();
            text.begin(label.Color.Red, label.Color.Green, label.Color.Blue, label.Color.Alpha,
                    render.getVPOrthoMatrix());
            if (label.Align == Alignment.CenterLeft) {
                text.draw(label.Text, label.Bounds.x, label.Bounds.centerY() - height / 2f, 0,
                        label.Bounds);
            } else {
                float width = text.getLength(label.Text);
                text.draw(label.Text, label.Bounds.centerX() - width / 2f, label.Bounds.centerY()
                        - height / 2f, 0, label.Bounds);
            }
            text.end();
        }
    }
}
