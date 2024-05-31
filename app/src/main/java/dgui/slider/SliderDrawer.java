package dgui.slider;

import dgui.GUIDrawer;
import dgui.GUIItem;
import dgui.GUIRenderer;
import dopengl.shapes.GLRectangle;
import thed.Rectangle;

/**
 * GUIDrawer for the Slider GUIItem.
 *
 * @author Derek Mulvihill - Oct 2, 2013
 */
public class SliderDrawer implements GUIDrawer {
	@Override
	public void draw(GUIItem item, GUIRenderer render) {
		Slider slider = (Slider) item;
		Rectangle bounds = slider.Bounds;
		GLRectangle rect = render.getGlib().getRectangle();
		rect.setBounds(bounds.x, bounds.y, bounds.w, bounds.h);
		rect.draw(render.getVPOrthoMatrix(), slider.Color.Red, slider.Color.Green,
				slider.Color.Blue, slider.Color.Alpha);

		rect.setBounds(bounds.x + 2, bounds.y + 2, bounds.w - 4, bounds.h - 4);
		rect.draw(render.getVPOrthoMatrix(), 0f, 0f, 0f, 1f);

		if (slider.Maximum - slider.Minimum != 0f) {
			rect.setBounds(bounds.x, bounds.y, bounds.w * (slider.Value - slider.Minimum)
					/ (slider.Maximum - slider.Minimum), bounds.h);
			rect.draw(render.getVPOrthoMatrix(), slider.Color.Red, slider.Color.Green,
					slider.Color.Blue, slider.Color.Alpha);
		}
	}
}
