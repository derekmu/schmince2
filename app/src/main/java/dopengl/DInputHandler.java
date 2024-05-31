package dopengl;

/**
 * Interface to handle input events from a DGLSurfaceView.
 *
 * @author Derek Mulvihill
 */
public interface DInputHandler {
	/**
	 * When the first touch down event is registered. This is only called on the first touch down event, not if the user uses multitouch.
	 */
	void touchDown(int pid, float orthoX, float orthoY, float worldX, float worldY);

	/**
	 * This will follow the touchDown event to signify that no touchUp will occur.
	 * I don't actually know when this is called...
	 */
	void touchCancel(int pid);

	/**
	 * When a touch up event occurs without multitouch.
	 */
	void touchUp(int pid, float orthoX, float orthoY, float worldX, float worldY);

	/**
	 * When the user moves along the screen, this will be called many times.
	 */
	void touchMove(int pid, float orthoX, float orthoY, float worldX, float worldY);

	/**
	 * Register the view that will be providing text input access to the InputHandler.
	 */
	void setGLSurfaceView(DGLSurfaceView textInput);

	/**
	 * Simple interface for a container for some text that can be input by the user.
	 *
	 * @author Derek Mulvihill
	 */
	interface DTextInputHolder {
		CharSequence getTextValue();

		void setTextValue(CharSequence value);
	}
}
