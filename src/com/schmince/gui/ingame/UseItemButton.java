package com.schmince.gui.ingame;

import com.schmince.game.SchminceGame;
import dgui.button.Button;

/**
 * @author Derek Mulvihill - Jan 15, 2014
 */
public class UseItemButton extends Button {
    private SchminceGame game;

    public UseItemButton() {
        super("");
        NormalColor.set(0f, 0f, 0.75f);
        BackgroundAspectRatio = 1f;
    }

    @Override
    public void doAction() {
        game.onUseItem();
    }

    public void setGame(SchminceGame game) {
        this.game = game;
    }
}
