package dgui.icon;

import dgui.GUIItem;
import dopengl.drawable.GLDrawableProvider;

/**
 * GUI item that just draws a GLDrawableProvider as an Icon.
 *
 * @author Derek Mulvihill - Jan 23, 2014
 */
public class Icon extends GUIItem {
	private static final IconDrawer drawer = new IconDrawer();
	public GLDrawableProvider Icon = null;
	/**
	 * Zero or less (default), means that the Icon scales to fill the button bounds.<br>
	 * Greater than zero means the Icon will draw with that aspect ratio within the button bounds.<br>
	 * Ratio is width:height.
	 */
	public float AspectRatio = -1;

	public Icon(GLDrawableProvider icon) {
		super(drawer);
		this.Icon = icon;
	}
}
