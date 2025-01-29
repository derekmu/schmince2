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
	public List<Point> findPath(int x, int y, int tx, int ty, boolean canDig, boolean hasPick, int seenIndex) {
		Point start = new Point(x, y);
		Point target = new Point(tx, ty);
		Set<Point> closedSet = new HashSet<>();
		Set<Point> openSet = new HashSet<>();
		openSet.add(start);
		Map<Point, Point> cameFrom = new HashMap<>();

		// lowest cost to get to a point from the start
		Map<Point, Float> toScore = new HashMap<>();
		toScore.put(start, 0f);
		// lowest heuristic cost to get to the target via a point
		Map<Point, Float> viaScore = new HashMap<>();
		viaScore.put(start, heuristic(start, target));
		int iteration = 0;

		Point closest = start;
		float closestScore = heuristic(start, target);
		while (!openSet.isEmpty()) {
			iteration++;
			if (iteration > 400) {
				break;
			}

			// find the point with the lowest estimated cost in the open set (this should be a heap for efficiency)
			Point current = null;
			Float currentScore = null;
			for (Point p : openSet) {
				Float score = viaScore.get(p);
				if (currentScore == null || score < currentScore) {
					current = p;
					currentScore = score;
				}
			}

			// reached our target
			if (current.x == tx && current.y == ty) {
				break;
			}

			openSet.remove(current);
			closedSet.add(current);

			for (Point neighbor : neighbors(current)) {
				if (neighbor == null) {
					continue;
				}
				float cost = cost(neighbor, target, canDig, hasPick, seenIndex);
				if (Float.isInfinite(cost)) {
					continue;
				}
				float heuristic = heuristic(neighbor, target);
				if (heuristic < closestScore) {
					closest = neighbor;
					closestScore = heuristic;
				}
				Float oldToScore = toScore.get(neighbor);
				float newToScore = toScore.get(current) + cost;
				if (oldToScore == null || newToScore < oldToScore) {
					openSet.remove(neighbor);
					closedSet.remove(neighbor);
				}
				if (!openSet.contains(neighbor) && !closedSet.contains(neighbor)) {
					cameFrom.put(neighbor, current);
					toScore.put(neighbor, newToScore);
					viaScore.put(neighbor, newToScore + heuristic(neighbor, target));
					openSet.add(neighbor);
				}
			}
		}

		List<Point> path = new ArrayList<>();
		reconstructPath(cameFrom, closest, path);
		return path;
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

	/**
	 * Cost of moving into a point.
	 */
	private float cost(Point to, Point target, boolean canDig, boolean hasPick, int seenIndex) {
		// no cost associated with moving into the actual target
		if (to.x == target.x && to.y == target.y) {
			return 0f;
		}
		SBlock block = blocks[to.x][to.y];
		return block.getPathCost(canDig, hasPick, seenIndex);
	}

	/**
	 * Euclidian distance heuristic from http://theory.stanford.edu/~amitp/GameProgramming/Heuristics.html.
	 */
	private float heuristic(Point from, Point to) {
		int dx = Math.abs(to.x - from.x);
		int dy = Math.abs(to.y - from.y);
		return (float) Math.sqrt(dx * dx + dy * dy);
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
