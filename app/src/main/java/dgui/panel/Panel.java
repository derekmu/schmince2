package dgui.panel;

import dgui.GUIItem;
import thed.DColor;

/**
 * Simple color GUIItem.
 *
 * @author Derek Mulvihill - Oct 18, 2013
 */
public class Panel extends GUIItem {
	private static final PanelDrawer drawer = new PanelDrawer();

	public final DColor Color = new DColor(0f, 0f, 0.15f);

	public Panel() {
		super(drawer);
	}
}
