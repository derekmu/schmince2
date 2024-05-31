package dopengl;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import thed.MatrixController;

import javax.microedition.khronos.opengles.GL10;

/**
 * Abstract implementation of the Renderer interface for use with DGLSurfaceView.<br>
 * Many common stuff for my own applications.
 *
 * @author Derek Mulvihill
 */
public abstract class DRenderer implements Renderer {
	/**
	 * For all your view/projection matrix needs.
	 */
	protected final MatrixController matrix = new MatrixController();
	protected final GLLibrary glib;
	/**
	 * Temporary matrix to reuse (Eg. array copy and pass as parameter).
	 */
	private final float[] tempMatrix = new float[16];
	private volatile int screenWidth;
	private volatile int screenHeight;

	public DRenderer(Context context) {
		//NOTE: don't do anything with OpenGL in the constructor, it won't work - do all of it on the openGL thread which is executing in onSurfaceCreated...
		glib = new GLLibrary(context);

		matrix.setMinZ(getMinZ());
		matrix.setMaxZ(getMaxZ());
	}

	@Override
	public void onSurfaceChanged(GL10 ignore, int width, int height) {
		this.screenWidth = width;
		this.screenHeight = height;

		//set the window area that OpenGL will display on
		GLES20.glViewport(0, 0, width, height);

		matrix.setScreenSize(width, height);
	}

	/**
	 * A static value for the minimum Z location for the camera in world coordinates.<br>
	 * This is just used for MatrixController initialization, it has to be static because it has to be consistent between the view's MatrixController and the renderer's MatrixController.
	 */
	public abstract float getMinZ();

	/**
	 * A static value for the maximum Z location for the camera in world coordinates.<br>
	 * This is just used for MatrixController initialization, it has to be static because it has to be consistent between the view's MatrixController and the renderer's MatrixController.
	 */
	public abstract float getMaxZ();

	/**
	 * Change the current camera location on the Z axis.
	 */
	public void setEyeZ(float distance) {
		matrix.setEyeZ(distance);
	}

	/**
	 * The current width of the screen in pixels.
	 */
	public int getScreenWidth() {
		return screenWidth;
	}

	/**
	 * The current height of the screen in pixels.
	 */
	public int getScreenHeight() {
		return screenHeight;
	}

	/**
	 * Get the current location of the camera on the X axis.
	 */
	public abstract float getCameraX();

	/**
	 * Get the current location of the camera on the Y axis.
	 */
	public abstract float getCameraY();

	/**
	 * Return the current VPmatrix.
	 */
	public float[] getVPMatrix() {
		return matrix.getVPMatrix(tempMatrix);
	}

	/**
	 * Get the GLibrary used by this renderer's components.
	 */
	public GLLibrary getGlib() {
		return glib;
	}
}
