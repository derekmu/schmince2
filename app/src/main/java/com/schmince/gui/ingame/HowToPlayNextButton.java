package com.schmince.gui.ingame;

import com.schmince.game.SchminceGame;
import dgui.button.Button;

/**
 * Button used for the tutorial/how to play to move from message to message and stage to stage
 *
 * @author Derek Mulvihill - Mar 15, 2014
 */
public class HowToPlayNextButton extends Button {
	private SchminceGame game;

	public HowToPlayNextButton() {
		super("Next");
	}

	@Override
	public void doAction() {
		game.onNextHowToPlay();
	}

	public void setGame(SchminceGame game) {
		this.game = game;
	}
}
