package com.schmince.game.model;

import com.schmince.SchminceRenderer;
import dopengl.shapes.GLRectangle;

/**
 * @author Derek Mulvihill - Feb 4, 2014
 */
public class ShipWall extends SObject {
	@Override
	public void draw(SchminceRenderer renderer, SBlock block, boolean cantSee) {
		GLRectangle rect = renderer.getGlib().getRectangle();
		rect.setBounds(block.X - 0.5f, block.Y - 0.5f, 1f, 1f);
		rect.draw(renderer.getVPMatrix(), 0.15f, 0.15f, 0.15f, 1f);
	}

	@Override
	public void interact(Player player) {
	}

	@Override
	public boolean isInteractable() {
		return false;
	}

	@Override
	public boolean blocksLOS() {
		return true;
	}
}
