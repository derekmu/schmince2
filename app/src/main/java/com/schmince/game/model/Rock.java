package com.schmince.game.model;

import com.schmince.SchminceRenderer;
import dopengl.shapes.GLRock;

/**
 * @author Derek Mulvihill - Jan 18, 2014
 */
public class Rock extends SObject {
	private int health;
	private final double angleOffset;

	public Rock(float sample) {
		this.health = Math.max(1, Math.round(sample * 10f));
		this.angleOffset = thed.DRandom.get().nextDouble() * Math.PI * 2;
	}

	@Override
	public void draw(SchminceRenderer renderer, SBlock block, boolean cantSee) {
		GLRock rect = renderer.getGlib().getRock();
		rect.setPolygon(block.X, block.Y, 0.5f, 3 + health, this.angleOffset);
		rect.draw(renderer.getVPMatrix(), 0.1f + 0.4f * (health / 10f), 0.1f + 0.15f * (health / 10f), 0f, 1f);
	}

	@Override
	public void interact(Survivor survivor) {
		if (survivor.getItem() == ItemType.Pick) {
			health = 0;
		} else {
			health -= 1;
		}
		if (health < 1) {
			getCurrentBlock().setObject(null);
		}
	}

	@Override
	public boolean isInteractable() {
		return true;
	}

	@Override
	public float getPathCost(boolean canDig, boolean hasPick) {
		if (!canDig) {
			return Float.POSITIVE_INFINITY;
		} else if (hasPick) {
			return 2;
		} else {
			return 1 + health;
		}
	}

	@Override
	public boolean blocksLOS() {
		return true;
	}
}
