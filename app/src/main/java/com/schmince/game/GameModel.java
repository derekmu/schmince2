package com.schmince.game;

import android.graphics.Point;
import com.schmince.C;
import com.schmince.game.howtoplay.HowToPlayMessage;
import com.schmince.game.howtoplay.HowToPlayStage;
import com.schmince.game.model.*;
import com.schmince.game.model.sprites.GunSprite;
import com.schmince.game.model.sprites.MedkitSprite;
import com.schmince.game.model.sprites.Sprite;
import thed.DRandom;
import thed.noise.SimplexNoise;

import java.util.ArrayList;
import java.util.List;

/**
 * Master Schmince game data/object model.
 *
 * @author Derek Mulvihill - Jan 17, 2014
 */
public class GameModel implements GameModelInterface {
	final List<Flare> flares = new ArrayList<>();
	private final int survivorCount;
	private final int mapSize;
	private final SBlock[][] blocks;
	private final PathFinder pathFinder;
	private final LOSFinder losFinder;
	private final Survivor[] survivors;
	private final List<Enemy> enemies = new ArrayList<>();
	private final List<SoundEvent> soundEvents = new ArrayList<>();
	private final List<Sprite> sprites = new ArrayList<>();
	private int selectedSurvivorIndex = 0;
	private HowToPlayStage howToPlayStage;
	private int howToPlayMessageIndex;

	public GameModel(int survivorCount, int mapSize, int enemyCount, int itemCount) {
		this.survivorCount = survivorCount;
		this.mapSize = mapSize;

		int shipX = Math.min(mapSize - 2 - C.SHIP_SIZE,
				Math.max(1 + C.SHIP_SIZE, DRandom.get().nextInt(mapSize)));
		int shipY = Math.min(mapSize - 2 - C.SHIP_SIZE,
				Math.max(1 + C.SHIP_SIZE, DRandom.get().nextInt(mapSize)));
		int doorSide = DRandom.get().nextInt(4); //0 = north, 1 = east, 2 = south, 3 = west
		double xoff = DRandom.get().nextDouble() * 1000000;
		double yoff = DRandom.get().nextDouble() * 1000000;

		blocks = new SBlock[mapSize][];
		for (int i = 0; i < blocks.length; i++) {
			SBlock[] blocker = blocks[i] = new SBlock[mapSize];
			for (int j = 0; j < blocker.length; j++) {
				SBlock block = blocker[j] = new SBlock(i, j, survivorCount);
				int dx = shipX - i;
				int dy = shipY - j;
				if (Math.abs(dx) <= C.SHIP_SIZE && Math.abs(dy) <= C.SHIP_SIZE) {
					if (Math.abs(dx) == C.SHIP_SIZE) {
						if (doorSide == 1 && dx == C.SHIP_SIZE && Math.abs(dy) <= 1) {
						} else if (doorSide == 3 && -dx == C.SHIP_SIZE && Math.abs(dy) <= 1) {
						} else {
							ShipWall wall = new ShipWall();
							block.setObject(wall);
						}
					} else if (Math.abs(dy) == C.SHIP_SIZE) {
						if (doorSide == 0 && dy == C.SHIP_SIZE && Math.abs(dx) <= 1) {
						} else if (doorSide == 2 && -dy == C.SHIP_SIZE && Math.abs(dx) <= 1) {
						} else {
							ShipWall wall = new ShipWall();
							block.setObject(wall);
						}
					} else {
						block.BlockType = SBlockType.ShipFloor;
					}
					continue;
				}
				float sample = (float) SimplexNoise.noise(xoff + i * 0.2, yoff + j * 0.2);
				if (sample > 0) {
					Rock wall = new Rock(sample);
					block.setObject(wall);
				}
			}
		}

		this.pathFinder = new PathFinder(blocks, mapSize);
		this.losFinder = new LOSFinder(blocks);

		this.survivors = new Survivor[survivorCount];
		for (int i = 0; i < survivorCount; i++) {
			Survivor survivor = this.survivors[i] = new Survivor(C.SURVIVOR_COLORS[i], i);
			int x;
			int y;
			do {
				x = DRandom.get().nextInt(mapSize);
				y = DRandom.get().nextInt(mapSize);
			} while (blocks[x][y].getObject() != null
					|| blocks[x][y].BlockType == SBlockType.ShipFloor);
			blocks[x][y].setObject(survivor);
		}

		ItemType[] itemTypes = ItemType.values();
		itemCount = (int) ((mapSize * mapSize) / 1000f * itemCount);
		for (int i = 0; i < itemCount; i++) {
			Item item = new Item(itemTypes[i % itemTypes.length]);
			int x;
			int y;
			do {
				x = DRandom.get().nextInt(mapSize);
				y = DRandom.get().nextInt(mapSize);
			} while (blocks[x][y].getObject() != null);

			blocks[x][y].setObject(item);
		}

		enemyCount = (int) ((mapSize * mapSize) / 1000f * enemyCount);
		for (int i = 0; i < enemyCount; i++) {
			Enemy enemy = new Enemy();
			int x;
			int y;
			boolean hasSurvivorLOS;
			int loopRestriction = 0;
			do {
				x = DRandom.get().nextInt(mapSize);
				y = DRandom.get().nextInt(mapSize);
				hasSurvivorLOS = false;
				for (Survivor survivor : this.survivors) {
					if (losFinder.hasLOS(x, y, survivor.getX(), survivor.getY())) {
						hasSurvivorLOS = true;
						break;
					}
				}
				loopRestriction++;
			} while ((blocks[x][y].getObject() != null || hasSurvivorLOS) && loopRestriction < 1000);

			if (loopRestriction < 1000) {
				blocks[x][y].setObject(enemy);
				this.enemies.add(enemy);
			}
		}
	}

	public GameModel(HowToPlayStage stage, int messageIndex) {
		this.howToPlayStage = stage;
		this.howToPlayMessageIndex = messageIndex;
		this.survivorCount = stage.getSurvivorLocations().length;
		this.mapSize = stage.getMapSize();

		blocks = new SBlock[mapSize][];
		for (int i = 0; i < blocks.length; i++) {
			SBlock[] blocker = blocks[i] = new SBlock[mapSize];
			for (int j = 0; j < blocker.length; j++) {
				SBlock block = blocker[j] = new SBlock(i, j, survivorCount);
				if (stage.getShipLocation() != null) {
					int dx = stage.getShipLocation().x - i;
					int dy = stage.getShipLocation().y - j;
					if (Math.abs(dx) <= C.SHIP_SIZE && Math.abs(dy) <= C.SHIP_SIZE) {
						if (Math.abs(dx) == C.SHIP_SIZE) {
							ShipWall wall = new ShipWall();
							block.setObject(wall);
						} else if (Math.abs(dy) == C.SHIP_SIZE) {
							if (dy == C.SHIP_SIZE && Math.abs(dx) <= 1) {
							} else {
								ShipWall wall = new ShipWall();
								block.setObject(wall);
							}
						} else {
							block.BlockType = SBlockType.ShipFloor;
						}
					}
				}
			}
		}

		this.pathFinder = new PathFinder(blocks, mapSize);
		this.losFinder = new LOSFinder(blocks);

		int rockCount = stage.getRockLocations().length;
		for (int i = 0; i < rockCount; i++) {
			Rock rock = new Rock(DRandom.get().nextFloat());
			int x = stage.getRockLocations()[i].x;
			int y = stage.getRockLocations()[i].y;
			blocks[x][y].setObject(rock);
		}

		this.survivors = new Survivor[survivorCount];
		for (int i = 0; i < survivorCount; i++) {
			Survivor survivor = survivors[i] = new Survivor(C.SURVIVOR_COLORS[i], i);
			int x = stage.getSurvivorLocations()[i].x;
			int y = stage.getSurvivorLocations()[i].y;
			blocks[x][y].setObject(survivor);
		}

		int itemCount = stage.getItemLocations().length;
		for (int i = 0; i < itemCount; i++) {
			Item item = new Item(stage.getItemTypes()[i]);
			int x = stage.getItemLocations()[i].x;
			int y = stage.getItemLocations()[i].y;
			blocks[x][y].setObject(item);
		}

		int enemyCount = stage.getEnemyLocations().length;
		for (int i = 0; i < enemyCount; i++) {
			Enemy enemy = new Enemy();
			int x = stage.getEnemyLocations()[i].x;
			int y = stage.getEnemyLocations()[i].y;
			blocks[x][y].setObject(enemy);
			enemies.add(enemy);
		}
	}

	public void worldSelected(float worldX, float worldY) {
		int x = Math.round(worldX);
		int y = Math.round(worldY);
		if (!inBounds(x, y)) {
			return;
		}
		SBlock block = blocks[x][y];
		if (block.getObject() != null && block.getObject() != getSelectedSurvivor()
				&& !block.getObject().isInteractable()) {
			Point pt = findNearestUnoccupied(getSelectedSurvivor().getX(),
					getSelectedSurvivor().getY(), x, y);
			if (pt != null) {
				x = pt.x;
				y = pt.y;
			}
		}
		getSelectedSurvivor().setTarget(x, y, this);
	}

	private Point findNearestUnoccupied(final int px, final int py, final int x, final int y) {
		Point minPt = null;
		int minScore = 1000000000;
		for (int level = 1; level <= 3; level++) {
			int dx = x - level;
			int dy = y - level;
			int cx = 1; //move right
			int cy = 0;
			for (int r = 0; r < 4; r++) {
				for (int i = 0; i < level * 2; i++) {
					if (inBounds(dx, dy) && blocks[dx][dy].getObject() == null) {
						int score = simpleScore(px, py, dx, dy);
						if (score < minScore) {
							if (minPt == null) {
								minPt = new Point(dx, dy);
							} else {
								minPt.set(dx, dy);
							}
							minScore = score;
						}
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
			if (minPt != null) {
				break;
			}
		}
		return minPt;
	}

	private int simpleScore(final int x1, int y1, int x2, int y2) {
		int dx = Math.abs(x2 - x1);
		int dy = Math.abs(y2 - y1);
		return Math.max(dx, dy);
	}

	public boolean inBounds(int x, int y) {
		return x < mapSize && y < mapSize && x >= 0 && y >= 0;
	}

	public SBlock getBlock(int x, int y) {
		return blocks[x][y];
	}

	public PathFinder path() {
		return pathFinder;
	}

	public List<Enemy> getEnemies() {
		return enemies;
	}

	public void useItem() {
		getSelectedSurvivor().useItem(this);
	}

	public Survivor getSurvivor(int i) {
		return survivors[i];
	}

	public List<SoundEvent> getSoundEvents() {
		return soundEvents;
	}

	private void makeRelativeSound(SoundEventType eventType, int x, int y) {
		Survivor survivor = getSelectedSurvivor();
		int dx = survivor.getX() - x;
		int dy = survivor.getY() - y;
		if (Math.sqrt(dx * dx + dy * dy) < 8) {
			SoundEvent event = new SoundEvent(eventType);
			event.RightVolume = event.LeftVolume = (float) Math.max(0,
					Math.min(1, 1 - Math.sqrt(dx * dx + dy * dy) / 8));
			soundEvents.add(event);
		}
	}

	public void onEnemyMoved(Enemy enemy) {
		makeRelativeSound(SoundEventType.EnemyMoved, enemy.getX(), enemy.getY());
	}

	public void onSurvivorMoved(Survivor mover) {
		makeRelativeSound(SoundEventType.SurvivorMoved, mover.getX(), mover.getY());
	}

	public void onAttackSurvivor(Enemy enemy, Survivor chaseSurvivor) {
		SoundEventType eventType = SoundEventType.Hit;
		if (chaseSurvivor.getItem() == ItemType.Armor) {
			eventType = SoundEventType.ArmorHit;
			chaseSurvivor.setHealth(chaseSurvivor.getHealth() - 1);
		} else {
			chaseSurvivor.setHealth(chaseSurvivor.getHealth() - 2);
		}
		makeRelativeSound(eventType, chaseSurvivor.getX(), chaseSurvivor.getY());
	}

	public void onGunShot(Survivor survivor, Enemy enemy) {
		enemy.setDead(true);
		enemies.remove(enemy);
		newSprite(new GunSprite(survivor.getX(), survivor.getY(), enemy.getX(), enemy.getY()));
		makeRelativeSound(SoundEventType.GunShot, survivor.getX(), survivor.getY());
	}

	public void onMedkitUsed(Survivor survivor) {
		newSprite(new MedkitSprite(survivor.getX(), survivor.getY()));
		soundEvents.add(new SoundEvent(SoundEventType.Medkit));
	}

	public void onRadarUsed(Survivor survivor) {
		soundEvents.add(new SoundEvent(SoundEventType.Radar));
	}

	public void onFlareUsed(Survivor survivor) {
		flares.add(new Flare(survivor.getX(), survivor.getY()));
		soundEvents.add(new SoundEvent(SoundEventType.Flare));
	}

	public void onErrorUsed() {
		soundEvents.add(new SoundEvent(SoundEventType.Error));
	}

	public boolean nextHowToPlayMessage() {
		if (howToPlayMessageIndex + 1 >= howToPlayStage.getMessages().length) {
			return false;
		}
		howToPlayMessageIndex++;
		return true;
	}

	public void newSprite(Sprite sprite) {
		synchronized (sprites) {
			sprites.add(sprite);
		}
	}

	@Override
	public void getSprites(List<Sprite> spriteHolder) {
		synchronized (sprites) {
			spriteHolder.addAll(sprites);
			sprites.clear();
		}
	}

	@Override
	public void predrawObjects() {
		for (Survivor survivor : survivors) {
			survivor.predraw();
		}
		for (Enemy enemy : enemies) {
			enemy.predraw();
		}
	}

	@Override
	public boolean isVisible(int fromX, int fromY, int toX, int toY) {
		for (Flare flare : flares) {
			if (flare.isFlared(toX, toY)) {
				return true;
			}
		}
		return los().hasLOS(fromX, fromY, toX, toY);
	}

	@Override
	public void forBlock(int xs, int ys, int xe, int ye, ForSBlock forBlock) {
		xs = Math.max(0, xs);
		ys = Math.max(0, ys);
		xe = Math.min(mapSize - 1, xe);
		ye = Math.min(mapSize - 1, ye);

		for (int x = xs; x <= xe; x++) {
			for (int y = ys; y <= ye; y++) {
				forBlock.forBlock(blocks[x][y]);
			}
		}
	}

	@Override
	public LOSFinder los() {
		return losFinder;
	}

	@Override
	public int getSurvivorCount() {
		return survivorCount;
	}

	private Survivor getSelectedSurvivor() {
		return survivors[selectedSurvivorIndex];
	}

	@Override
	public int getSelectedSurvivorIndex() {
		return selectedSurvivorIndex;
	}

	public void setSelectedSurvivorIndex(int selectedSurvivorIndex) {
		this.selectedSurvivorIndex = selectedSurvivorIndex;
	}

	@Override
	public int getSurvivorX(int i) {
		return survivors[i].getX();
	}

	@Override
	public int getSurvivorY(int i) {
		return survivors[i].getY();
	}

	@Override
	public boolean isLocating() {
		return getSelectedSurvivor().isLocating();
	}


	@Override
	public ItemType getItem() {
		return getSelectedSurvivor().getItem();
	}

	@Override
	public boolean isSurivorAlert(int i) {
		return survivors[i].isAlert();
	}

	@Override
	public int getSurvivorHealth(int i) {
		return survivors[i].getHealth();
	}

	@Override
	public HowToPlayMessage getHowToPlayMessage() {
		if (howToPlayStage != null) {
			return howToPlayStage.getMessages()[howToPlayMessageIndex];
		}
		return null;
	}
}
