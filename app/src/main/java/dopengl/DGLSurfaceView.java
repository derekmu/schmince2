package dopengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import dopengl.DInputHandler.DTextInputHolder;
import thed.MatrixController;

/**
 * Extension of GLSurfaceView to handle common input functions and delegate to handlers.
 *
 * @author Derek Mulvihill
 */
public class DGLSurfaceView extends GLSurfaceView {
	private DRenderer renderer;
	private DInputHandler handler;
	private MatrixController matrix = new MatrixController();

	private DTextInputHolder textInputHolder = null;
	private BaseInputConnection textInputConnection = new BaseInputConnection(this, true) {
		@Override
		public boolean endBatchEdit() {
			boolean retval = super.endBatchEdit();

			DTextInputHolder holder = textInputHolder;
			if (holder != null) {
				Editable e = getEditable();
				CharSequence wat = e.subSequence(0, e.length());
				holder.setTextValue(wat);
			}

			return retval;
		}

		@Override
		public boolean sendKeyEvent(KeyEvent event) {
			if (event.getAction() == KeyEvent.ACTION_UP) {
				int ch = event.getKeyCharacterMap().get(event.getKeyCode(), event.getMetaState());
				if (ch != 0) {
					Editable ed = getEditable();
					int cursor = Selection.getSelectionEnd(ed);
					commitText(Character.toString((char) ch), cursor);
				}
			}
			return true;
		}
	};

	public DGLSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);

		//need these 2 lines in order to show the keyboard...
		setFocusable(true);
		setFocusableInTouchMode(true);

		setEGLContextClientVersion(2);
		//need a depth buffer
		setEGLConfigChooser(8, 8, 8, 8, 16, 0);

		//have to call requestRender() every frame if we keep this line
		//setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	}

	public void setDRenderer(DRenderer renderer) {
		this.renderer = renderer;
		super.setRenderer(renderer);

		matrix.setMinZ(renderer.getMinZ());
		matrix.setMaxZ(renderer.getMaxZ());
	}

	public void setDInputHandler(DInputHandler handler) {
		this.handler = handler;
		handler.setGLSurfaceView(this);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		final int action = ev.getActionMasked();
		int index = ev.getActionIndex();
		int pid = ev.getPointerId(index);
		float px = ev.getX(index);
		matrix.setScreenSize(renderer.getScreenWidth(), renderer.getScreenHeight());
		float py = matrix.deviceToOrthoY(ev.getY(index));

		if (textInputHolder != null) {
			InputMethodManager imm = (InputMethodManager) getContext().getSystemService(
					Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getWindowToken(), 0);
			textInputHolder = null;
		}

		switch (action) {
			case MotionEvent.ACTION_DOWN: //first pointer goes down
			case MotionEvent.ACTION_POINTER_DOWN: //any additional pointers go down
			{
				float[] world = getWorldCoordinates(px, py);
				handler.touchDown(pid, px, py, world[0], world[1]);
				break;
			}
			case MotionEvent.ACTION_CANCEL: //i don't know when cancel gets triggered...
			{
				handler.touchCancel(pid);
				break;
			}
			case MotionEvent.ACTION_UP: //all pointers go up
			case MotionEvent.ACTION_POINTER_UP: //secondary pointers go up
			{
				float[] world = getWorldCoordinates(px, py);
				handler.touchUp(pid, px, py, world[0], world[1]);
				break;
			}
			case MotionEvent.ACTION_MOVE: {
				for (int i = 0; i < ev.getPointerCount(); i++) {
					pid = ev.getPointerId(i);
					px = ev.getX(i);
					py = matrix.deviceToOrthoY(ev.getY(i));
					float[] world = getWorldCoordinates(px, py);
					handler.touchMove(pid, px, py, world[0], world[1]);
				}
				break;
			}
			default:
				Log.d(getClass().getName(), "Unrecognized MotionEvent action: " + action);
				break;
		}
		return true;
	}

	@Override
	public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
		outAttrs.inputType = InputType.TYPE_TEXT_FLAG_CAP_WORDS;
		outAttrs.imeOptions |= EditorInfo.IME_FLAG_NO_EXTRACT_UI;
		return textInputConnection;
	}

	/**
	 * Bring up text input access for the user and set the receiver of text updates.
	 */
	public void startTextInput(final DTextInputHolder textHolder) {
		post(() -> {
			textInputHolder = textHolder;
			CharSequence value = textHolder.getTextValue();
			Editable edit = textInputConnection.getEditable();
			edit.replace(0, edit.length(), value);
			InputMethodManager imm = (InputMethodManager) getContext().getSystemService(
					Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(DGLSurfaceView.this, 0);
		});
	}

	/**
	 * Returns the world coordinates based on a set of orthographic coordinates.
	 */
	public float[] getWorldCoordinates(float p1x, float p1y) {
		matrix.setScreenSize(renderer.getScreenWidth(), renderer.getScreenHeight());
		matrix.updateVPMatrix(renderer.getCameraX(), renderer.getCameraY());
		return matrix.orthoToWorld(p1x, p1y);
	}
}