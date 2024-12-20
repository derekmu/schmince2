package dgui;

import android.util.SparseArray;
import dgui.button.Button;
import dgui.textfield.TextField;
import dopengl.DGLSurfaceView;
import dopengl.DInputHandler;
import texample.GLTextCache;

import java.util.List;

/**
 * Base implementation of DInputHandler and the abstract parent of any GUIs that want to be rendered using GUIRenderer.<br>
 * The concrete implementations in this class encapsulate interpretation of touch gestures. There are a few callbacks to override/implement in child classes.
 *
 * @author Derek Mulvihill - Aug 24, 2013
 */
public abstract class BaseGUI implements DInputHandler {
	protected DGLSurfaceView surfaceView;
	private SparseArray<PointerState> pointers = new SparseArray<>();
	private PointerState uniquePointer = null;

	@Override
	public void touchDown(int pid, float orthoX, float orthoY, float worldX, float worldY) {
		PointerState ps = pointers.get(pid);
		if (ps != null && ps.onGUI != null) {
			ps.onGUI.HasFocus = false;
		}

		ps = new PointerState();
		ps.ox = orthoX;
		ps.oy = orthoY;
		ps.wx = worldX;
		ps.wy = worldY;
		pointers.put(pid, ps);

		List<GUIItem> items = getGUIItems();
		if (items != null) {
			for (int i = items.size() - 1; i >= 0; i--) {
				GUIItem gu = items.get(i);
				if (gu.Visible && gu.Bounds.contains(orthoX, orthoY)) {
					gu.HasFocus = true;
					ps.onGUI = gu;
					break;
				}
			}
		}
		if (ps.onGUI == null) {
			if (!singleAction(worldX, worldY) && uniquePointer == null) {
				uniqueAction(worldX, worldY);
				uniquePointer = ps;
			}
		}
	}

	@Override
	public void touchUp(int pid, float orthoX, float orthoY, float worldX, float worldY) {
		PointerState ps = pointers.get(pid);
		if (ps == null) {
			return;
		}
		pointers.remove(pid);
		if (ps.onGUI != null) {
			if (ps.onGUI.Bounds.contains(orthoX, orthoY)) {
				if (ps.onGUI instanceof Button) {
					Button selectedButton = (Button) ps.onGUI;
					selectedButton.doAction();
				} else if (ps.onGUI instanceof TextField) {
					TextField selectedTextField = (TextField) ps.onGUI;
					surfaceView.startTextInput(selectedTextField);
				}
			}
			ps.onGUI.HasFocus = false;
		} else {
			if (ps == uniquePointer) {
				uniqueAction(worldX, worldY);
				endUniqueAction(worldX, worldY);
			}
		}
		if (ps == uniquePointer) {
			uniquePointer = null;
		}
	}

	@Override
	public void touchMove(int pid, float orthoX, float orthoY, float worldX, float worldY) {
		PointerState ps = pointers.get(pid);
		if (ps == null) {
			return;
		}
		if (ps.ox == orthoX && ps.oy == orthoY) {
			return;
		}

		if (ps.onGUI != null) {
			if (ps.onGUI.Mover != null) {
				ps.onGUI.Mover.moveGUI(ps.onGUI, orthoX, orthoY);
			} else if (!ps.onGUI.Bounds.contains(orthoX, orthoY)) {
				ps.onGUI.HasFocus = false;
			}
		} else {
			if (ps == uniquePointer) {
				uniqueAction(worldX, worldY);
			}

			if (pointers.size() > 1) {
				float oldax = 0;
				float olday = 0;
				float newax = orthoX;
				float neway = orthoY;
				float oldd = 0f;
				float newd = 0f;
				float dx;
				float dy;
				for (int i = 0; i < pointers.size(); i++) {
					PointerState pi = pointers.valueAt(i);
					oldax += pi.ox;
					olday += pi.oy;
					if (pi != ps) {
						newax += pi.ox;
						neway += pi.oy;

						dx = pi.ox - ps.ox;
						dy = pi.oy - ps.oy;
						oldd += (float) Math.sqrt(dx * dx + dy * dy);

						dx = pi.ox - orthoX;
						dy = pi.oy - orthoY;
						newd += (float) Math.sqrt(dx * dx + dy * dy);
					}
				}
				float[] newcoords = surfaceView.getWorldCoordinates(newax / pointers.size(), neway
						/ pointers.size());
				float[] oldcoords = surfaceView.getWorldCoordinates(oldax / pointers.size(), olday
						/ pointers.size());
				dragAction(newcoords[0] - oldcoords[0], newcoords[1] - oldcoords[1]);
				if (newd != 0) {
					pinchAction(oldd / newd);
				}
			}
			ps.ox = orthoX;
			ps.oy = orthoY;
			ps.wx = worldX;
			ps.wy = worldY;
		}
	}

	@Override
	public void touchCancel(int pid) {
		PointerState ps = pointers.get(pid);
		if (ps == null) {
			return;
		}
		pointers.remove(pid);
		if (ps.onGUI != null) {
			ps.onGUI.HasFocus = false;
		}
		if (uniquePointer == ps) {
			endUniqueAction(ps.wx, ps.wy);
			uniquePointer = null;
		}
	}

	@Override
	public void setGLSurfaceView(DGLSurfaceView textInput) {
		this.surfaceView = textInput;
	}

	/**
	 * Return a unmodifiable List of GUI components.<br>
	 * Can return null if there are no GUI components.<br>
	 * The order of the components is important - ones at the beginning of the list will be drawn
	 * 'underneath' ones at the end and ones at the end will take precedence for user actions (touch).
	 */
	public abstract List<GUIItem> getGUIItems();

	/**
	 * GUIRenderer will call this once before rendering the GUI components.
	 * This allows for implementations to re-position components, especially if the screen size/orientation changes.
	 */
	public abstract void update(int screenWidth, int screenHeight, GLTextCache textCache);

	/**
	 * The unique action allows the game to receive many actions as a touch gesture moves across the screen.<br>
	 * Only one touch gesture registers as a unique action.<br>
	 * Eg. In a game where the survivor can move around, dragging a touch across the screen might be interpreted as selecting the location to move to.
	 */
	protected abstract void uniqueAction(float worldX, float worldY);

	/**
	 * When the touch gesture ends (touch action goes up or is cancelled), this is called with the final location of the gesture.
	 */
	protected abstract void endUniqueAction(float worldX, float worldY);

	/**
	 * Called when a touch down event is registered in the game world (not on a GUI component).<br>
	 * This will only trigger when the touch action hits the screen, not when it rises or moves,
	 * and can happen in conjunction with another touch moves.<br>
	 * The return value should indicate whether the action was 'consumed' and should not turn into the unique action.<br>
	 * Eg. if the single action selects an object in the world, it shouldn't turn into the unique action since it was interpreted as a select gesture.
	 */
	protected abstract boolean singleAction(float worldX, float worldY);

	/**
	 * If there are multiple touches in the world and they move closer or further away from each other,
	 * this will be called with the ratio representing how far apart the touches were before and after moving.
	 */
	protected void pinchAction(float oldToNewRatio) {
	}

	/**
	 * If there are multiple touches in the world and they move along the screen,
	 * this will be called with the change in the average position of the touches.
	 */
	protected void dragAction(float dx, float dy) {
	}

	/**
	 * Called when the user uses the 'back' button during the activity.
	 *
	 * @return return false if the default android activity back button should be performed (Eg. exit from app).
	 */
	public abstract boolean onBackPressed();

	private static class PointerState {
		GUIItem onGUI;
		float ox;
		float oy;
		float wx;
		float wy;
	}
}
