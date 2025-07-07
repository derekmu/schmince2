package com.schmince;

import android.os.Bundle;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.schmince.game.SchminceGame;
import com.schmince.gui.SchminceGUI;
import dgame.BaseActivity;
import dgame.BaseGame;
import dgui.BaseGUI;
import dopengl.DRenderer;

/**
 * Main activity for Schmince game.
 *
 * @author Derek Mulvihill - Jan 11, 2014
 */
public class SchminceActivity extends BaseActivity {
    private SchminceGame game;
    private SchminceGUI gui;
    private SchminceRenderer renderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        game = new SchminceGame(this);

        gui = new SchminceGUI();
        gui.setGame(game);

        renderer = new SchminceRenderer(this, game, gui);
        super.onCreate(savedInstanceState);

        ViewCompat.setOnApplyWindowInsetsListener(viewGL, (v, insets) -> {
            Insets system = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            Insets cutout = insets.getInsets(WindowInsetsCompat.Type.displayCutout());
            Insets total = Insets.of(
                    Math.max(system.left, cutout.left),
                    Math.max(system.top, cutout.top),
                    Math.max(system.right, cutout.right),
                    Math.max(system.bottom, cutout.bottom)
            );
            gui.updateInsets(total);
            return insets;
        });
    }

    @Override
    protected BaseGame<?> createGame() {
        return game;
    }

    @Override
    protected BaseGUI createGUI() {
        return gui;
    }

    @Override
    protected DRenderer createRenderer() {
        return renderer;
    }
}
