package dgame;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import dgui.BaseGUI;
import dopengl.DGLSurfaceView;
import dopengl.DRenderer;
import thed.DTimer;

/**
 * Base class for activities.
 *
 * @author Derek Mulvihill - Jan 11, 2014
 */
public abstract class BaseActivity extends Activity {
	private BaseGame<?> game;
	private BaseGUI gui;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		game = createGame();
		gui = createGUI();

		DGLSurfaceView viewGL = new DGLSurfaceView(this, null);
		viewGL.setDRenderer(createRenderer());
		viewGL.setDInputHandler(gui);
		ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		addContentView(viewGL, lp);

		DTimer.get().setPauseProvider(game);
	}

	protected abstract BaseGame<?> createGame();

	protected abstract BaseGUI createGUI();

	protected abstract DRenderer createRenderer();

	@Override
	protected void onPause() {
		super.onPause();
		game.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		game.onResume();
	}

	@Override
	protected void onDestroy() {
		game.onDestroy();
		super.onDestroy();
	}

	// ignore deprecation because callbacks don't seem to allow canceling the back button press
	@Override
	public void onBackPressed() {
		if (!gui.onBackPressed()) {
			super.onBackPressed();
		}
	}
}
