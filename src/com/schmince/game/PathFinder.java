package com.schmince.game;

import android.graphics.Point;
import com.schmince.game.model.SBlock;

import java.util.*;

/**
 * @author Derek Mulvihill - Jan 19, 2014
 */
public class PathFinder {
    private SBlock[][] blocks;
    private int mapSize;

    public PathFinder(SBlock[][] blocks, int mapSize) {
        this.blocks = blocks;
        this.mapSize = mapSize;
    }

    /**
     * A* algorithm, followed from wikipedia
     */
    public List<Point> findPath(final int x, final int y, final int targetX, final int targetY) {
        Point start = new Point(x, y);
        Set<Point> closedSet = new HashSet<Point>();
        Set<Point> openSet = new HashSet<Point>();
        openSet.add(start);
        Map<Point, Point> cameFrom = new HashMap<Point, Point>();

        Map<Point, Float> gScore = new HashMap<Point, Float>();
        gScore.put(start, 0f);
        Map<Point, Float> fScore = new HashMap<Point, Float>();
        fScore.put(start, 0 + hScore(start, targetX, targetY));
        int iteration = 0;

        while (openSet.size() > 0) {
            iteration++;
            if (iteration > 1000) {
                break;
            }
            Point current = null;
            Float curval = null;
            for (Point pt : openSet) {
                Float ptValue = fScore.get(pt);
                if (curval == null || ptValue < curval) {
                    current = pt;
                    curval = ptValue;
                }
            }

            if (current.x == targetX && current.y == targetY) {
                List<Point> path = new ArrayList<Point>();
                reconstructPath(cameFrom, current, path);
                return path;
            }

            openSet.remove(current);
            closedSet.add(current);

            for (Point neighbor : neighbors(current)) {
                if (neighbor == null) {
                    continue;
                }
                if (closedSet.contains(neighbor)) {
                    continue;
                }
                float tgScore = gScore.get(current) + gScore(neighbor, targetX, targetY);
                if (!openSet.contains(neighbor) || tgScore < gScore.get(neighbor)) {
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tgScore);
                    fScore.put(neighbor, tgScore + hScore(neighbor, targetX, targetY));
                    openSet.add(neighbor);
                }
            }
        }

        return new ArrayList<Point>();
    }

    private Point[] neighbors(Point current) {
        Point[] points = new Point[8];
        for (int i = 0; i < 8; i++) {
            int x;
            int y;
            switch (i) {
                case 0:
                    x = current.x + 1;
                    y = current.y + 1;//top right
                    break;
                case 1:
                    x = current.x;
                    y = current.y + 1; // top middle
                    break;
                case 2:
                    x = current.x - 1;
                    y = current.y + 1;//top left
                    break;
                case 3:
                    x = current.x - 1;
                    y = current.y; //middle left;
                    break;
                case 4:
                    x = current.x - 1;
                    y = current.y - 1; //bottom left
                    break;
                case 5:
                    x = current.x;
                    y = current.y - 1; //bottom middle
                    break;
                case 6:
                    x = current.x + 1;
                    y = current.y - 1; //bottom right
                    break;
                case 7:
                default:
                    x = current.x + 1;
                    y = current.y; //middle right
                    break;
            }
            if (inBounds(x, y)) {
                points[i] = new Point(x, y);
            }
        }

        return points;
    }

    private float gScore(Point neighbor, int targetX, int targetY) {
        if (neighbor.x == targetX && neighbor.y == targetY) {
            return 1f;
        }
        SBlock block = blocks[neighbor.x][neighbor.y];
        if (block.getObject() != null) {
            return block.getObject().getPathWeight();
        }
        return 1f;
    }

    /**
     * Heuristic modified from http://theory.stanford.edu/~amitp/GameProgramming/Heuristics.html.
     */
    private float hScore(Point from, int targetX, int targetY) {
        int dx = Math.abs(targetX - from.x);
        int dy = Math.abs(targetY - from.y);
        float value = Math.max(dx, dy) + Math.min(dx, dy) * 0.0001f;
        return value;
    }

    private void reconstructPath(Map<Point, Point> cameFrom, Point current, List<Point> path) {
        if (cameFrom.containsKey(current)) {
            reconstructPath(cameFrom, cameFrom.get(current), path);
            path.add(current);
        } else {
            path.add(current);
        }
    }

    public boolean inBounds(int x, int y) {
        return x < mapSize && y < mapSize && x >= 0 && y >= 0;
    }
}
