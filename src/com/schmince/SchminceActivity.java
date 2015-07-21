package com.schmince;

import android.os.Bundle;
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
        gui.setActivity(this);

        renderer = new SchminceRenderer(this, game, gui);
        super.onCreate(savedInstanceState);
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
