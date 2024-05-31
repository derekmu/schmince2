package com.schmince.gui.startscreen;

import com.schmince.game.SchminceGame;
import dgui.button.Button;

/**
 * Button on the start screen to trigger creation of a new game.
 *
 * @author Derek Mulvihill - Jan 15, 2014
 */
public class NewGameButton extends Button {
	private SchminceGame game;

	public NewGameButton() {
		super("New Game");
	}

	@Override
	public void doAction() {
		game.onNewGame();
	}

	public void setGame(SchminceGame game) {
		this.game = game;
	}
}
