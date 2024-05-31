package com.schmince.game.model.sprites;

import dopengl.DRenderer;

/**
 * @author Derek Mulvihill - Mar 16, 2014
 */
public interface Sprite {
	void drawSprite(DRenderer render);

	boolean isExpired();
}
