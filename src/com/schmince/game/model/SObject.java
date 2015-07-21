package com.schmince.game.model;

import com.schmince.SchminceRenderer;

/**
 * @author Derek Mulvihill - Jan 17, 2014
 */
public abstract class SObject {
    public abstract void draw(SchminceRenderer renderer, SBlock block);

    public abstract void interact(Player player);

    private SBlock currentBlock = null;

    public void onNewBlock(SBlock block) {
        this.currentBlock = block;
    }

    public SBlock getCurrentBlock() {
        return currentBlock;
    }

    public int getX() {
        return currentBlock.X;
    }

    public int getY() {
        return currentBlock.Y;
    }

    public abstract boolean isInteractable();

    public float getPathWeight() {
        return 1000000f;
    }

    public boolean blocksLOS() {
        return false;
    }
}
