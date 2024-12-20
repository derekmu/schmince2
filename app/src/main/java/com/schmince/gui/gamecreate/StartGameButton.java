package com.schmince.gui.gamecreate;

import dgui.button.Button;

/**
 * @author Derek Mulvihill - Jan 25, 2014
 */
public class StartGameButton extends Button {
	private GameCreateModule module;

	public StartGameButton(GameCreateModule module) {
		super("Start Game");
		this.module = module;
	}

	@Override
	public void doAction() {
		module.createGame();
	}
}
