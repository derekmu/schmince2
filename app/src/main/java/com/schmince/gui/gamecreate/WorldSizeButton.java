package com.schmince.gui.gamecreate;

import dgui.button.Button;

/**
 * @author Derek Mulvihill - Jan 25, 2014
 */
public class WorldSizeButton extends Button {
	private int mapSize;
	private GameCreateModule module;

	public WorldSizeButton(int mapSize, String name, GameCreateModule module) {
		super(name);
		this.mapSize = mapSize;
		this.module = module;
	}

	public int getMapSize() {
		return mapSize;
	}

	@Override
	public void doAction() {
		module.setMapSize(mapSize);
	}
}
