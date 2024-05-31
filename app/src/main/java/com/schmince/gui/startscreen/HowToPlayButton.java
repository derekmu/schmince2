package com.schmince.gui.startscreen;

import com.schmince.game.SchminceGame;
import dgui.button.Button;

/**
 * @author Derek Mulvihill - Jan 15, 2014
 */
public class HowToPlayButton extends Button {
	private SchminceGame game;

	public HowToPlayButton() {
		super("How To Play");
	}

	@Override
	public void doAction() {
		game.onHowToPlay();
	}

	public void setGame(SchminceGame game) {
		this.game = game;
	}
}
