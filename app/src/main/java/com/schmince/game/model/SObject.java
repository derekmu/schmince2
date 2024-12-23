package com.schmince.game.model;

import com.schmince.SchminceRenderer;

/**
 * @author Derek Mulvihill - Jan 17, 2014
 */
public abstract class SObject {
	private SBlock currentBlock = null;

	public abstract void draw(SchminceRenderer renderer, SBlock block, boolean visible);

	public abstract void interact(Survivor survivor);

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

	public float getPathCost(boolean canDig, boolean hasPick) {
		return Float.POSITIVE_INFINITY;
	}

	public boolean blocksLOS() {
		return false;
	}
}
