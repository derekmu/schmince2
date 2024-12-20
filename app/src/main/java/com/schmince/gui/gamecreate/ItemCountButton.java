package com.schmince.gui.gamecreate;

import dgui.button.Button;

/**
 * @author Derek Mulvihill - Dec 20, 2024
 */
public class ItemCountButton extends Button {
	private int itemCount;
	private GameCreateModule module;

	public ItemCountButton(int itemCount, String name, GameCreateModule module) {
		super(name);
		this.itemCount = itemCount;
		this.module = module;
	}

	public int getItemCount() {
		return itemCount;
	}

	@Override
	public void doAction() {
		module.setItemCount(itemCount);
	}
}
