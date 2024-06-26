package com.schmince.game.model;

/**
 * @author Derek Mulvihill - Jan 17, 2014
 */
public class SBlock {
	public final int X;
	public final int Y;
	public SBlockType BlockType = SBlockType.None;
	private volatile SObject object;

	public SBlock(int x, int y) {
		this.X = x;
		this.Y = y;
	}

	public SObject getObject() {
		return object;
	}

	public void setObject(SObject object) {
		this.object = object;
		if (object != null) {
			object.onNewBlock(this);
		}
	}
}
