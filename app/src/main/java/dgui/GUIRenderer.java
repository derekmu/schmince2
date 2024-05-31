package dgui;

import android.opengl.GLES20;
import dopengl.GLLibrary;
import thed.MatrixController;

import java.util.List;

/**
 * Handle drawing the components for the GUI in OpenGL
 *
 * @author Derek Mulvihill - Aug 14, 2013
 */
public class GUIRenderer {
	/**
	 * Temporary matrix to reuse (Eg. array copy and pass as parameter).
	 */
	private final float[] tempMatrix = new float[16];

	private GLLibrary glib;
	private BaseGUI gui;
	private MatrixController matrix;

	public void draw(int screenWidth, int screenHeight) {
		this.gui.update(screenWidth, screenHeight, glib);

		List<GUIItem> gui = this.gui.getGUIItems();
		if (gui == null) {
			return;
		}

		//might want to blend text without using alpha, sometimes it looks bad
		//GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA); //no blending

		for (GUIItem gu : gui) {
			if (!gu.Visible || gu.Bounds.w == 0 || gu.Bounds.h == 0) {
				continue;
			}
			gu.Drawer.draw(gu, this);
		}
	}

	public void setGLib(GLLibrary glib) {
		this.glib = glib;
	}

	public void setGUI(BaseGUI gui) {
		this.gui = gui;
	}

	public void setMatrix(MatrixController matrix) {
		this.matrix = matrix;
	}

	public GLLibrary getGlib() {
		return glib;
	}

	public float[] getVPOrthoMatrix() {
		return matrix.getVPOrthoMatrix(tempMatrix);
	}

	public void disableAlphaBlend() {
		GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA); //no blending
	}

	public void enableAlphaBlend() {
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
	}
}
