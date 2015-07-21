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
        int x1 = xf;
        int y1 = yf;
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        if (Math.max(dx, dy) > 15) {
            return false;
        }
        int sx;
        int sy;
        if (x0 < x1) {
            sx = 1;
        } else {
            sx = -1;
        }
        if (y0 < y1) {
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

            if (x0 == x1 && y0 == y1) {
                break;
            }
            int e2 = 2 * err;
            if (e2 > -dy) {
                err = err - dy;
                x0 = x0 + sx;
            }
            if (x0 == x1 && y0 == y1) {
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
