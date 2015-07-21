package dgame;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import dgui.BaseGUI;
import dopengl.DGLSurfaceView;
import dopengl.DRenderer;

/**
 * Base class for activities for android games that I create.
 *
 * @author Derek Mulvihill - Jan 11, 2014
 */
public abstract class BaseActivity extends Activity {
    private DGLSurfaceView viewGL;
    private BaseGame<?> game;
    private BaseGUI gui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //Hide the status bar

        game = createGame();

        gui = createGUI();

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        viewGL = new DGLSurfaceView(this, null);
        viewGL.setDRenderer(createRenderer());
        viewGL.setDInputHandler(gui);
        addContentView(viewGL, lp);

        game.start();
    }

    /**
     * Provide the BaseGame implementation.
     */
    protected abstract BaseGame<?> createGame();

    /**
     * Provide the BaseGUI implementation.
     */
    protected abstract BaseGUI createGUI();

    /**
     * Provide the DRenderer implementation.
     */
    protected abstract DRenderer createRenderer();

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupSystemUI(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE); //makes buttons on the navigation bar less visible (can use View.SYSTEM_UI_FLAG_HIDE_NAVIGATION to make it hide temporarily (until the user interacts))
        }
    }

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

    @Override
    public void onBackPressed() {
        if (!gui.onBackPressed()) {
            super.onBackPressed();
        }
    }
}
