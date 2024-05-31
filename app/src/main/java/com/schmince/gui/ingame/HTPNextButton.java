package com.schmince.gui.ingame;

import com.schmince.game.SchminceGame;
import dgui.button.Button;

/**
 * Button used for the tutorial/how to play to move from message to message and stage to stage
 *
 * @author Derek Mulvihill - Mar 15, 2014
 */
public class HTPNextButton extends Button {
	private SchminceGame game;

	public HTPNextButton() {
		super("Next");
	}

	@Override
	public void doAction() {
		game.onNextHTP();
	}

	public void setGame(SchminceGame game) {
		this.game = game;
	}
}
