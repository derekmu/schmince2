package com.schmince.gui.gamecreate;

import dgui.button.Button;

/**
 * @author Derek Mulvihill - Dec 20, 2024
 */
public class EnemyCountButton extends Button {
	private int enemyCount;
	private GameCreateModule module;

	public EnemyCountButton(int enemyCount, String name, GameCreateModule module) {
		super(name);
		this.enemyCount = enemyCount;
		this.module = module;
	}

	public int getEnemyCount() {
		return enemyCount;
	}

	@Override
	public void doAction() {
		module.setEnemyCount(enemyCount);
	}
}
