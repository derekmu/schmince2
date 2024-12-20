package com.schmince.gui.gamecreate;

import dgui.button.Button;

/**
 * @author Derek Mulvihill - Jan 25, 2014
 */
public class SurvivorCountButton extends Button {
	private int survivorCount;
	private GameCreateModule module;

	public SurvivorCountButton(int survivorCount, GameCreateModule module) {
		super(Integer.toString(survivorCount));
		this.survivorCount = survivorCount;
		this.module = module;
	}

	public int getSurvivorCount() {
		return survivorCount;
	}

	@Override
	public void doAction() {
		module.setSurvivorCount(survivorCount);
	}
}
