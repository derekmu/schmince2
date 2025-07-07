package com.schmince.gui.gameover;

import dgui.button.Button;

/**
 * @author Derek Mulvihill - Feb 8, 2014
 */
public class StartScreenButton extends Button {
    private GameOverModule module;

    public StartScreenButton(GameOverModule module) {
        super("Start Screen");
        this.module = module;
    }

    @Override
    public void doAction() {
        module.endGameOver();
    }
}
