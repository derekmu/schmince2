package com.schmince.game;

import com.schmince.game.model.SBlock;
import com.schmince.game.model.SObject;

/**
 * @author Derek Mulvihill - Jan 19, 2014
 */
public class LOSFinder {
	private SBlock[][] blocks;

	public LOSFinder(SBlock[][] blocks) {
		this.blocks = blocks;
	}

	/**
	 * Bresenham line algorithm.
	 */
	public boolean hasLOS(int xf, int yf, int xp, int yp) {
		if (xf > xp || yf > yp) {
			int t = xp;
			xp = xf;
			xf = t;
			t = yp;
			yp = yf;
			yf = t;
		}
		int dx = Math.abs(xp - xf);
		int dy = Math.abs(yp - yf);
		if (Math.max(dx, dy) > 10) {
			return false;
		}
		int sx = xf < xp ? 1 : -1;
		int sy = yf < yp ? 1 : -1;
		int err = dx - dy;
		boolean first = true;
		while (true) {
			if (xf == xp && yf == yp) {
				return true;
			}
			if (!first) {
				SBlock block = blocks[xf][yf];
				SObject object = block.getObject();
				if (object != null && object.blocksLOS()) {
					return false;
				}
			} else {
				first = false;
			}
			int e2 = 2 * err;
			if (e2 > -dy) {
				err -= dy;
				xf += sx;
			}
			if (e2 < dx) {
				err += dx;
				yf += sy;
			}
		}
	}
}
