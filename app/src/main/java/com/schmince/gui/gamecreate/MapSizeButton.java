package com.schmince.gui.gamecreate;

import dgui.button.Button;

/**
 * @author Derek Mulvihill - Jan 25, 2014
 */
public class MapSizeButton extends Button {
	private int mapSize;
	private GameCreateModule module;

	public MapSizeButton(int mapSize, GameCreateModule module) {
		super(Integer.toString(mapSize));
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
