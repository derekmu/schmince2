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
    public boolean hasLOS(int x1, int y1, int x2, int y2) {
        if (x1 > x2 || (x1 == x2 && y1 > y2)) {
            int t = x2;
            x2 = x1;
            x1 = t;
            t = y2;
            y2 = y1;
            y1 = t;
        }
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        if (Math.max(dx, dy) > 10) {
            return false;
        }
        int sx = x1 < x2 ? 1 : -1;
        int sy = y1 < y2 ? 1 : -1;
        int err = dx - dy;
        boolean first = true;
        while (true) {
            if (x1 == x2 && y1 == y2) {
                return true;
            }
            if (!first) {
                SBlock block = blocks[x1][y1];
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
                x1 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y1 += sy;
            }
        }
    }
}
