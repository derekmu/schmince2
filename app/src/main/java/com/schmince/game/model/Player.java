package com.schmince.game.model;

import android.graphics.Point;
import android.opengl.Matrix;
import com.schmince.C;
import com.schmince.SchminceRenderer;
import com.schmince.game.GameModel;
import com.schmince.gldraw.GLIconType;
import com.schmince.gldraw.GLPlayer;
import dopengl.shapes.GLLine;
import dopengl.shapes.GLRectangle;
import thed.DColor;
import thed.DRandom;
import thed.DTimer;
import thed.GLColor;

import java.util.List;

/**
 * @author Derek Mulvihill - Jan 18, 2014
 */
public class Player extends SObject {
	private static final int ACTION_MILLI = 1000;
	private static final int FLARE_MILLI = 10000;
	private static final int LOCATE_MILLI = 10000;
	private final int drawSeed = DRandom.get().nextInt(10000);
	private final DColor color;
	private final GLColor glColor;
	private int targetX = -1;
	private int targetY = -1;
	private List<Point> path = null;
	private int pathLocation = 0;
	private long lastActionMilli = 0;
	private long timerMilli = 0;
	private ItemType item = null;

	private volatile boolean isAlert;
	private long lastFlareMilli = 0;
	private volatile boolean isFlared;
	private long lastLocateMilli = 0;
	private volatile boolean isLocating;

	private volatile int health = C.MAX_PLAYER_HEALTH;

	public Player(DColor color) {
		this.color = color;
		this.glColor = new GLColor(color.Red, color.Green, color.Blue);
	}

	public void update(GameModel gameModel) {
		updateFlared();
		updateLocate();
		updateAlert(gameModel);
		if (health <= 0) {
			return;
		}

		timerMilli = DTimer.get().millis() - ACTION_MILLI;
		if (lastActionMilli > timerMilli) {
			return;
		}

		if (targetX == -1 || targetY == -1) {
			return;
		}

		if (path == null) {
			setTarget(targetX, targetY, gameModel);
		}

		Point next;
		if (path.size() > pathLocation) {
			next = path.get(pathLocation);
			if (next.x == getX() && next.y == getY()) {
				setTarget(-1, -1, null);
				return;
			}
		} else {
			return;
		}

		SBlock block = gameModel.getBlock(next.x, next.y);
		if (block.getObject() == null) {
			getCurrentBlock().setObject(null);
			block.setObject(this);
			gameModel.onPlayerMoved(this);
			pathLocation++;
			lastActionMilli = DTimer.get().millis();
			if (item == ItemType.Boots) {
				lastActionMilli -= 250;
			}
		} else if (block.getObject().isInteractable() && block.X == targetX && block.Y == targetY) {
			block.getObject().interact(this);
			gameModel.onPlayerMoved(this);
			lastActionMilli = DTimer.get().millis();
		} else if (block.getObject().isInteractable()
				&& block.getObject().getPathWeight(true, false) < 100000f) {
			block.getObject().interact(this);
			gameModel.onPlayerMoved(this);
			lastActionMilli = DTimer.get().millis();
		} else {
			setTarget(targetX, targetY, gameModel);
		}
	}

	@Override
	public void draw(SchminceRenderer renderer, SBlock block) {
		float[] vpMatrix = renderer.getVPMatrix();
		Matrix.translateM(vpMatrix, 0, block.X, block.Y, 0);
		Matrix.scaleM(vpMatrix, 0, 0.5f, 0.5f, 1f);
		GLPlayer icon = (GLPlayer) renderer.getGlib().getDrawer(GLIconType.Player);
		icon.draw(vpMatrix, health <= 0 ? drawSeed : DTimer.get().millis() + drawSeed, glColor);

		vpMatrix = renderer.getVPMatrix();
		Matrix.translateM(vpMatrix, 0, 0, 0, 0.01f);

		if (health > 0) {
			List<Point> path = this.path;
			int pathLocation = this.pathLocation;
			if (path != null) {
				GLLine line = renderer.getGlib().getLine();
				int lx = getX();
				int ly = getY();
				for (int i = pathLocation; i < path.size(); i++) {
					Point point = path.get(i);
					line.draw(vpMatrix, lx, ly, point.x, point.y, this.color.Red, this.color.Green, this.color.Blue, 1f, 1);
					lx = point.x;
					ly = point.y;
				}
			}
			if (lastActionMilli > timerMilli) {
				GLRectangle rect = renderer.getGlib().getRectangle();
				rect.setBounds(getX() - 0.5f, getY() - 0.5f,
						(ACTION_MILLI - (lastActionMilli - timerMilli)) / (float) ACTION_MILLI,
						0.1f);
				rect.draw(vpMatrix, 1f, 1f, 1f, 1f);
			}
		}
	}

	@Override
	public void interact(Player player) {
		//nothing for now?
	}

	@Override
	public boolean isInteractable() {
		return false;
	}

	/**
	 * Health of a player, once it is 0 or less, the player is dead.
	 */
	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public void setTarget(int targetX, int targetY, GameModel gameModel) {
		this.targetX = targetX;
		this.targetY = targetY;
		this.path = null;
		if (targetX != -1 && targetY != -1 && gameModel != null) {
			path = gameModel.path().findPath(getX(), getY(), targetX, targetY, true, this.item == ItemType.Pick);
			pathLocation = 1; //0 is current location
		}
	}

	public ItemType getItem() {
		return item;
	}

	public void setItem(ItemType item) {
		this.item = item;
	}

	private void updateAlert(GameModel gameModel) {
		isAlert = false;
		if (health <= 0) {
			return;
		}
		for (Enemy enemy : gameModel.getEnemies()) {
			if (gameModel.los().hasLOS(enemy.getX(), enemy.getY(), getX(), getY())) {
				isAlert = true;
				return;
			}
		}
	}

	public boolean isAlert() {
		return isAlert;
	}

	private void updateFlared() {
		isFlared = lastFlareMilli > DTimer.get().millis() - FLARE_MILLI && health > 0;
	}

	public boolean isFlared() {
		return isFlared;
	}

	private void updateLocate() {
		isLocating = lastLocateMilli > DTimer.get().millis() - LOCATE_MILLI && health > 0;
	}

	public boolean isLocating() {
		return isLocating;
	}

	public void useItem(GameModel gameModel) {
		if (item == null || health <= 0) {
			return;
		}
		switch (item) {
			case Flare: {
				item = null;
				lastFlareMilli = DTimer.get().millis();
				gameModel.onFlareUsed(this);
				break;
			}
			case Radar: {
				item = null;
				lastLocateMilli = DTimer.get().millis();
				gameModel.onRadarUsed(this);
				break;
			}
			case Gun: {
				Enemy nearest = null;
				int minDist = 10;
				for (Enemy enemy : gameModel.getEnemies()) {
					if (!enemy.isDead()
							&& gameModel.los().hasLOS(getX(), getY(), enemy.getX(), enemy.getY())) {
						int dist = Math.max(Math.abs(enemy.getX() - getX()),
								Math.abs(enemy.getY() - getY()));
						if (dist < minDist) {
							nearest = enemy;
							minDist = dist;
						}
					}
				}
				if (nearest != null) {
					item = null;
					gameModel.onGunShot(this, nearest);
				}
				break;
			}
			case Medkit: {
				item = null;
				health = 10;
				gameModel.onMedkitUsed(this);
				break;
			}
			case Armor:
			case Boots:
			case Pick: {
				gameModel.onErrorUsed();
				break;
			}
		}
	}
}
