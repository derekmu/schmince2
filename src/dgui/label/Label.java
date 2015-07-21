package dgui.label;

import dgui.GUIItem;
import texample.GLText;
import texample.GLTextCache;
import texample.GLTextType;
import thed.Alignment;
import thed.DColor;

/**
 * GUI label
 *
 * @author Derek Mulvihill - Sep 23, 2013
 */
public class Label extends GUIItem {
    private static final LabelDrawer drawer = new LabelDrawer();

    public CharSequence Text;

    public final DColor Color = new DColor();

    public GLTextType TextType = GLTextType.Sans;
    public float TextScale = 1f;
    public Alignment Align = Alignment.CenterLeft;

    public Label(CharSequence text) {
        super(drawer);
        this.Text = text;
    }

    public GLText getGLText(GLTextCache cache) {
        return cache.getText(TextType, TextScale);
    }
}
