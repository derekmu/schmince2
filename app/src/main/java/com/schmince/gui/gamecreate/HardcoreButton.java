package com.schmince.gui.gamecreate;

import dgui.button.Button;

/**
 * @author Derek Mulvihill - Dec 20, 2024
 */
public class HardcoreButton extends Button {
    private boolean hardcore;
    private GameCreateModule module;

    public HardcoreButton(boolean hardcore, String name, GameCreateModule module) {
        super(name);
        this.hardcore = hardcore;
        this.module = module;
    }

    public boolean getHardcore() {
        return hardcore;
    }

    @Override
    public void doAction() {
        module.setHardcore(hardcore);
    }
}
