package dgui.button;

import android.opengl.Matrix;
import dgui.GUIDrawer;
import dgui.GUIItem;
import dgui.GUIRenderer;
import texample.GLText;
import thed.Alignment;
import thed.DColor;
import thed.Rectangle;

/**
 * GUIDrawer for the Button GUIItem.
 *
 * @author Derek Mulvihill - Oct 2, 2013
 */
public class ButtonDrawer implements GUIDrawer {
	@Override
	public void draw(GUIItem item, GUIRenderer render) {
		Button button = (Button) item;
		Rectangle bounds = button.Bounds;
		render.getGlib().getRectangle().setBounds(bounds.x, bounds.y, bounds.w, bounds.h);
		DColor color = button.HasFocus ? button.FocusColor : button.NormalColor;
		render.getGlib().getRectangle()
				.draw(render.getVPOrthoMatrix(), color.Red, color.Green, color.Blue, color.Alpha);

		if (button.BackgroundIcon != null) {
			render.getGlib().getDrawer(button.BackgroundIcon)
					.draw(getBackgroundVPMatrix(render, button));
		}

		if (button.Text != null && button.Text.length() > 0) {
			render.disableAlphaBlend();

			GLText text = render.getGlib().getText(button.TextType, button.TextScale);
			float length = text.getLength(button.Text);
			float height = text.getHeight();
			text.begin(button.TextColor.Red, button.TextColor.Green, button.TextColor.Blue,
					button.TextColor.Alpha, render.getVPOrthoMatrix());
			if (button.Align == Alignment.CenterLeft) {
				text.draw(button.Text, bounds.x, bounds.centerY() - height / 2f, 0, button.Bounds);
			} else {
				text.draw(button.Text, bounds.centerX() - length / 2f, bounds.centerY() - height
						/ 2f, 0, button.Bounds);
			}
			text.end();

			render.enableAlphaBlend();
		}
	}

	protected float[] getBackgroundVPMatrix(GUIRenderer render, Button button) {
		float[] matrix = render.getVPOrthoMatrix();
		Matrix.translateM(matrix, 0, button.Bounds.centerX(), button.Bounds.centerY(), 0);
		if (button.BackgroundAspectRatio <= 0) {
			Matrix.scaleM(matrix, 0, button.Bounds.w / 2, button.Bounds.h / 2, 0);
		} else {
			float w = button.Bounds.w;
			float h = button.Bounds.h;
			if (h * button.BackgroundAspectRatio > w) {
				h = w / button.BackgroundAspectRatio;
			} else {
				w = h * button.BackgroundAspectRatio;
			}
			Matrix.scaleM(matrix, 0, w / 2, h / 2, 0);
		}
		return matrix;
	}
}
