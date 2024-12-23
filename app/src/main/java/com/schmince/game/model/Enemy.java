package com.schmince.game.model;

import android.graphics.Point;
import android.opengl.Matrix;
import com.schmince.SchminceRenderer;
import com.schmince.game.GameModel;
import com.schmince.gldraw.GLEnemy;
import com.schmince.gldraw.GLIconType;
import thed.DRandom;
import thed.DTimer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Derek Mulvihill - Jan 18, 2014
 */
public class Enemy extends SObject {
	private static final int MOVE_MILLI = 2000;
	private final int drawSeed = DRandom.get().nextInt(10000);
	long moveMillis;
	private long lastMoveMilli = DTimer.get().millis() + DRandom.get().nextInt(MOVE_MILLI);
	private boolean dead = false;
	private List<Point> openPath = new ArrayList<>();
	private int nextX = -1, nextY = -1;
	private float previousX = -1, previousY = -1;

	public void update(GameModel gameModel) {
		if (this.dead || lastMoveMilli > DTimer.get().millis() - MOVE_MILLI) {
			return;
		}

		lastMoveMilli = DTimer.get().millis();

		if (chaseSurvivor(gameModel)) {
			return;
		}

		moveRandom(gameModel);
	}

	private boolean chaseSurvivor(GameModel gameModel) {
		Survivor chaseSurvivor = null;
		float mind = 100000000;

		for (int i = 0; i < gameModel.getSurvivorCount(); i++) {
			Survivor survivor = gameModel.getSurvivor(i);
			if (survivor.getHealth() > 0
					&& gameModel.los().hasLOS(getX(), getY(), survivor.getX(), survivor.getY())) {
				int dx = survivor.getX() - getX();
				int dy = survivor.getY() - getY();
				float d = (float) Math.sqrt(dx * dx + dy * dy);
				if (d < mind) {
					mind = d;
					chaseSurvivor = survivor;
				}
			}
		}

		if (chaseSurvivor != null) {
			if (Math.abs(chaseSurvivor.getX() - getX()) <= 1 && Math.abs(chaseSurvivor.getY() - getY()) <= 1) {
				gameModel.onAttackSurvivor(this, chaseSurvivor);
				return true;
			} else {
				List<Point> path = gameModel.path().findPath(getX(), getY(), chaseSurvivor.getX(), chaseSurvivor.getY(), false, false, -1);
				if (path.size() >= 2) {
					Point next = path.get(1); //0 will be the current location
					if (next != null) {
						SBlock block = gameModel.getBlock(next.x, next.y);
						if (block.getObject() == null) {
							getCurrentBlock().setObject(null);
							block.setObject(this);
							gameModel.onEnemyMoved(this);
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	private void moveRandom(GameModel gameModel) {
		openPath.clear();
		int dx = getX() - 1;
		int dy = getY() - 1;
		int cx = 1; //move right
		int cy = 0;
		for (int r = 0; r < 4; r++) {
			for (int i = 0; i < 2; i++) {
				if (gameModel.inBounds(dx, dy) && gameModel.getBlock(dx, dy).getObject() == null) {
					openPath.add(new Point(dx, dy));
				}
				dx += cx;
				dy += cy;
			}
			if (cx == 1) {
				cy = 1; //move up
				cx = 0;
			} else if (cy == 1) {
				cx = -1; //move left
				cy = 0;
			} else if (cx == -1) {
				cy = -1; //move down
				cx = 0;
			}
		}
		if (!openPath.isEmpty()) {
			Point next = openPath.get(DRandom.get().nextInt(openPath.size()));
			SBlock block = gameModel.getBlock(next.x, next.y);
			getCurrentBlock().setObject(null);
			block.setObject(this);
			gameModel.onEnemyMoved(this);
		}
	}

	public void predraw() {
		SBlock block = getCurrentBlock();
		if (nextX == -1 && nextY == -1) {
			moveMillis = DTimer.get().millis();
			nextX = block.X;
			nextY = block.Y;
			previousX = block.X;
			previousY = block.Y;
		}
		if (block.X != nextX || block.Y != nextY) {
			moveMillis = DTimer.get().millis();
			previousX = nextX;
			previousY = nextY;
			nextX = block.X;
			nextY = block.Y;
		}
	}

	@Override
	public void draw(SchminceRenderer renderer, SBlock block, boolean visible) {
		if (!visible) {
			return;
		}
		long dt = DTimer.get().millis() - moveMillis;
		long durationMillis = 500;
		float x = dt > durationMillis ? nextX : (previousX + (nextX - previousX) * dt / durationMillis);
		float y = dt > durationMillis ? nextY : (previousY + (nextY - previousY) * dt / durationMillis);
		float[] vpMatrix = renderer.getVPMatrix();
		Matrix.translateM(vpMatrix, 0, x, y, 0);
		Matrix.scaleM(vpMatrix, 0, 0.5f, 0.5f, 1f);
		GLEnemy icon = (GLEnemy) renderer.getGlib().getDrawer(GLIconType.Enemy);
		icon.draw(vpMatrix, dead ? drawSeed : DTimer.get().millis() + drawSeed);
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	@Override
	public void interact(Survivor survivor) {
		//nothing for now?
	}

	@Override
	public boolean isInteractable() {
		return false;
	}
}
