package com.schmince.gui.gamecreate;

import dgui.slider.Slider;

/**
 * @author Derek Mulvihill - Feb 1, 2014
 */
public class EnemyPerSlider extends Slider {
	private GameCreateModule module;

	public EnemyPerSlider(GameCreateModule module) {
		this.module = module;
	}

	@Override
	protected void updatedValue(float value) {
		module.setEnemyPer(Math.round(value));
	}
}
