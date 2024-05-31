package com.schmince.gui.gamecreate;

import dgui.button.Button;

/**
 * @author Derek Mulvihill - Jan 25, 2014
 */
public class PlayerCountButton extends Button {
	private int playerCount;
	private GameCreateModule module;

	public PlayerCountButton(int playerCount, GameCreateModule module) {
		super(Integer.toString(playerCount));
		this.playerCount = playerCount;
		this.module = module;
	}

	public int getPlayerCount() {
		return playerCount;
	}

	@Override
	public void doAction() {
		module.setPlayerCount(playerCount);
	}
}
