package dgui.button;

import dgui.GUIItem;
import dopengl.drawable.GLDrawableProvider;
import texample.GLText;
import texample.GLTextCache;
import texample.GLTextType;
import thed.Alignment;
import thed.DColor;

/**
 * GUI button.
 *
 * @author Derek Mulvihill
 */
public abstract class Button extends GUIItem {
	private static final ButtonDrawer drawer = new ButtonDrawer();
	public final DColor FocusColor = new DColor(0f, 0.5f, 0f);
	public final DColor NormalColor = new DColor(0f, 0f, 0.5f);
	public final DColor TextColor = new DColor();
	public CharSequence Text;
	public GLDrawableProvider BackgroundIcon = null;
	/**
	 * Zero or less (default), means that the BackgroundIcon scales to fill the button bounds.<br>
	 * Greater than zero means the BackgroundIcon will draw with that aspect ratio within the button bounds.<br>
	 * Ratio is width:height.
	 */
	public float BackgroundAspectRatio = -1;
	public GLTextType TextType = GLTextType.Sans;
	public float TextScale = 1f;
	public Alignment Align = Alignment.Center;

	public Button(CharSequence text) {
		super(drawer);
		this.Text = text;
	}

	public Button(CharSequence text, ButtonDrawer drawer) {
		super(drawer);
		this.Text = text;
	}

	public abstract void doAction();

	public GLText getGLText(GLTextCache cache) {
		return cache.getText(TextType, TextScale);
	}
}
