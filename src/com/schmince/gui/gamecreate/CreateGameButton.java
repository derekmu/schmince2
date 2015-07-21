package com.schmince.gui.gamecreate;

import dgui.button.Button;

/**
 * @author Derek Mulvihill - Jan 25, 2014
 */
public class CreateGameButton extends Button {
    private GameCreateModule module;

    public CreateGameButton(GameCreateModule module) {
        super("Create Game");
        this.module = module;
    }

    @Override
    public void doAction() {
        module.createGame();
    }
}
