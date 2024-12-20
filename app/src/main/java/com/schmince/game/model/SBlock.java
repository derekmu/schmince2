package com.schmince.game.model;

/**
 * @author Derek Mulvihill - Jan 17, 2014
 */
public class SBlock {
	public final int X;
	public final int Y;
	public final boolean[] Seen;
	public SBlockType BlockType = SBlockType.None;
	private SObject object;

	public SBlock(int x, int y, int survivors) {
		this.X = x;
		this.Y = y;
		this.Seen = new boolean[survivors];
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

	public float getPathCost(boolean canDig, boolean hasPick, int seenIndex) {
		if ((seenIndex == -1 || Seen[seenIndex]) && object != null) {
			return object.getPathCost(canDig, hasPick);
		}
		return 1f;
	}
}
