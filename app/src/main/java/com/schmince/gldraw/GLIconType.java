package com.schmince.gldraw;

import dopengl.drawable.GLDrawable;
import dopengl.drawable.GLDrawableProvider;

/**
 * @author Derek Mulvihill - Jan 18, 2014
 */
public enum GLIconType implements GLDrawableProvider {
	Enemy(GLEnemy.class),
	Player(GLPlayer.class),
	Title(GLTitle.class),
	//
	;

	private final Class<? extends GLDrawable> clazz;

	GLIconType(Class<? extends GLDrawable> clazz) {
		this.clazz = clazz;
	}

	@Override
	public Class<? extends GLDrawable> getDrawClass() {
		return clazz;
	}
}
