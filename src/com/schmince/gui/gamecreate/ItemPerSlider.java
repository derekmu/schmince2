package com.schmince.gui.gamecreate;

import dgui.slider.Slider;

/**
 * @author Derek Mulvihill - Feb 1, 2014
 */
public class ItemPerSlider extends Slider {
    private GameCreateModule module;

    public ItemPerSlider(GameCreateModule module) {
        this.module = module;
    }

    @Override
    protected void updatedValue(float value) {
        module.setItemPer(Math.round(value));
    }
}
