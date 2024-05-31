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
	 * Algorithm from http://en.wikipedia.org/wiki/Bresenham%27s_line_algorithm
	 */
	public boolean hasLOS(final int xf, final int yf, final int xp, final int yp) {
		int x0 = xp;
		int y0 = yp;
		int dx = Math.abs(xf - x0);
		int dy = Math.abs(yf - y0);
		if (Math.max(dx, dy) > 10) {
			return false;
		}
		int sx;
		int sy;
		if (x0 < xf) {
			sx = 1;
		} else {
			sx = -1;
		}
		if (y0 < yf) {
			sy = 1;
		} else {
			sy = -1;
		}
		int err = dx - dy;

		while (true) {
			SBlock block = blocks[x0][y0];
			SObject object = block.getObject();
			if (object != null && object.blocksLOS() && !(x0 == xp && y0 == yp)) {
				return false;
			}

			if (x0 == xf && y0 == yf) {
				break;
			}
			int e2 = 2 * err;
			if (e2 > -dy) {
				err = err - dy;
				x0 = x0 + sx;
			}
			if (x0 == xf && y0 == yf) {
				break;
			}
			if (e2 < dx) {
				err = err + dx;
				y0 = y0 + sy;
			}
		}
		return true;
	}
}
