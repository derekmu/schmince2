package dgui;

/**
 * Interface to implement so BaseGUI can issue events when the user drags/moves on a GUIItem.
 *
 * @author Derek Mulvihill - Oct 2, 2013
 */
public interface GUIMover {
	void moveGUI(GUIItem item, float x, float y);
}
