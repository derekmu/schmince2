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
	 * A* algorithm, based on Wikipedia.
	 */
	public List<Point> findPath(int x, int y, int targetX, int targetY, boolean canDig, boolean hasPick) {
		Point start = new Point(x, y);
		Set<Point> closedSet = new HashSet<>();
		Set<Point> openSet = new HashSet<>();
		openSet.add(start);
		Map<Point, Point> cameFrom = new HashMap<>();

		Map<Point, Float> gScore = new HashMap<>();
		gScore.put(start, 0f);
		Map<Point, Float> fScore = new HashMap<>();
		fScore.put(start, 0 + hScore(start, targetX, targetY));
		int iteration = 0;

		while (!openSet.isEmpty()) {
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
				List<Point> path = new ArrayList<>();
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
				float tgScore = gScore.get(current) + gScore(neighbor, targetX, targetY, canDig, hasPick);
				if (!openSet.contains(neighbor) || tgScore < gScore.get(neighbor)) {
					cameFrom.put(neighbor, current);
					gScore.put(neighbor, tgScore);
					fScore.put(neighbor, tgScore + hScore(neighbor, targetX, targetY));
					openSet.add(neighbor);
				}
			}
		}

		return new ArrayList<>();
	}

	private Point[] neighbors(Point current) {
		Point[] points = new Point[8];
		for (int i = 0; i < 8; i++) {
			int x;
			int y;
			switch (i) {
				case 0: //top right
					x = current.x + 1;
					y = current.y + 1;
					break;
				case 1: // top middle
					x = current.x;
					y = current.y + 1;
					break;
				case 2: // top left
					x = current.x - 1;
					y = current.y + 1;
					break;
				case 3: // middle left
					x = current.x - 1;
					y = current.y;
					break;
				case 4: // bottom left
					x = current.x - 1;
					y = current.y - 1;
					break;
				case 5: // bottom middle
					x = current.x;
					y = current.y - 1;
					break;
				case 6: // bottom right
					x = current.x + 1;
					y = current.y - 1;
					break;
				case 7:
				default: // middle right
					x = current.x + 1;
					y = current.y;
					break;
			}
			if (inBounds(x, y)) {
				points[i] = new Point(x, y);
			}
		}

		return points;
	}

	private float gScore(Point neighbor, int targetX, int targetY, boolean canDig, boolean hasPick) {
		if (neighbor.x == targetX && neighbor.y == targetY) {
			return 1f;
		}
		SBlock block = blocks[neighbor.x][neighbor.y];
		if (block.getObject() != null) {
			return block.getObject().getPathWeight(canDig, hasPick);
		}
		return 1f;
	}

	/**
	 * Heuristic modified from http://theory.stanford.edu/~amitp/GameProgramming/Heuristics.html.
	 */
	private float hScore(Point from, int targetX, int targetY) {
		int dx = Math.abs(targetX - from.x);
		int dy = Math.abs(targetY - from.y);
		return Math.max(dx, dy) + Math.min(dx, dy) * 0.0001f;
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
