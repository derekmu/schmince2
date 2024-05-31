package com.schmince.game;

import android.graphics.Point;
import com.schmince.C;
import com.schmince.game.howtoplay.HTPMessage;
import com.schmince.game.howtoplay.HTPStage;
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
	private final int playerCount;
	private final int mapSize;
	private final SBlock[][] blocks;
	private final PathFinder pathFinder;
	private final LOSFinder losFinder;
	private final Player[] players;
	private final List<Enemy> enemies = new ArrayList<>();
	private final List<SoundEvent> soundEvents = new ArrayList<>();
	private final List<Sprite> sprites = new ArrayList<>();
	private volatile int selectedPlayer = 0;
	private HTPStage htpStage;
	private int htpMessageIndex;

	public GameModel(int playerCount, int mapSize, int enemyPer, int itemPer) {
		this.playerCount = playerCount;
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
				SBlock block = blocker[j] = new SBlock(i, j);
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
					MudWall wall = new MudWall(sample);
					block.setObject(wall);
					continue;
				}
			}
		}

		this.pathFinder = new PathFinder(blocks, mapSize);
		this.losFinder = new LOSFinder(blocks);

		this.players = new Player[playerCount];
		for (int i = 0; i < playerCount; i++) {
			Player player = players[i] = new Player(C.PLAYER_COLORS[i]);
			int x;
			int y;
			do {
				x = DRandom.get().nextInt(mapSize);
				y = DRandom.get().nextInt(mapSize);
			} while (blocks[x][y].getObject() != null
					|| blocks[x][y].BlockType == SBlockType.ShipFloor);
			blocks[x][y].setObject(player);
		}

		ItemType[] itemTypes = ItemType.values();
		int itemCount = (int) ((mapSize * mapSize) / 1000f * itemPer);
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

		int enemyCount = (int) ((mapSize * mapSize) / 1000f * enemyPer);
		for (int i = 0; i < enemyCount; i++) {
			Enemy enemy = new Enemy();
			int x;
			int y;
			boolean hasPlayerLOS;
			int loopRestriction = 0;
			do {
				x = DRandom.get().nextInt(mapSize);
				y = DRandom.get().nextInt(mapSize);
				hasPlayerLOS = false;
				for (Player player : players) {
					if (losFinder.hasLOS(x, y, player.getX(), player.getY())) {
						hasPlayerLOS = true;
						break;
					}
				}
				loopRestriction++;
			} while ((blocks[x][y].getObject() != null || hasPlayerLOS) && loopRestriction < 1000);

			if (loopRestriction < 1000) {
				blocks[x][y].setObject(enemy);
				enemies.add(enemy);
			}
		}
	}

	public GameModel(HTPStage stage, int messageIndex) {
		this.htpStage = stage;
		this.htpMessageIndex = messageIndex;
		this.playerCount = stage.getPlayerLocations().length;
		this.mapSize = stage.getMapSize();

		blocks = new SBlock[mapSize][];
		for (int i = 0; i < blocks.length; i++) {
			SBlock[] blocker = blocks[i] = new SBlock[mapSize];
			for (int j = 0; j < blocker.length; j++) {
				SBlock block = blocker[j] = new SBlock(i, j);
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
						continue;
					}
				}
			}
		}

		this.pathFinder = new PathFinder(blocks, mapSize);
		this.losFinder = new LOSFinder(blocks);

		int mudCount = stage.getMudLocations().length;
		for (int i = 0; i < mudCount; i++) {
			MudWall wall = new MudWall(DRandom.get().nextFloat());
			int x = stage.getMudLocations()[i].x;
			int y = stage.getMudLocations()[i].y;
			blocks[x][y].setObject(wall);
		}

		this.players = new Player[playerCount];
		for (int i = 0; i < playerCount; i++) {
			Player player = players[i] = new Player(C.PLAYER_COLORS[i]);
			int x = stage.getPlayerLocations()[i].x;
			int y = stage.getPlayerLocations()[i].y;
			blocks[x][y].setObject(player);
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

	private Player getSelectedPlayer() {
		return players[selectedPlayer];
	}

	public void setSelectedPlayer(int selectedPlayer) {
		this.selectedPlayer = selectedPlayer;
	}

	public void worldSelected(float worldX, float worldY) {
		int x = Math.round(worldX);
		int y = Math.round(worldY);
		if (!inBounds(x, y)) {
			return;
		}
		SBlock block = blocks[x][y];
		if (block.getObject() != null && block.getObject() != getSelectedPlayer()
				&& !block.getObject().isInteractable()) {
			Point pt = findNearestUnoccupied(getSelectedPlayer().getX(),
					getSelectedPlayer().getY(), x, y);
			if (pt != null) {
				x = pt.x;
				y = pt.y;
			}
		}
		getSelectedPlayer().setTarget(x, y, this);
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
		getSelectedPlayer().useItem(this);
	}

	public Player getPlayer(int i) {
		return players[i];
	}

	public List<SoundEvent> getSoundEvents() {
		return soundEvents;
	}

	private void makeRelativeSound(SoundEventType eventType, int x, int y) {
		Player player = getSelectedPlayer();
		int dx = player.getX() - x;
		int dy = player.getY() - y;
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

	public void onPlayerMoved(Player mover) {
		makeRelativeSound(SoundEventType.PlayerMoved, mover.getX(), mover.getY());
	}

	public void onAttackPlayer(Enemy enemy, Player chasePlayer) {
		SoundEventType eventType = SoundEventType.Hit;
		if (chasePlayer.getItem() == ItemType.Armor) {
			eventType = SoundEventType.ArmorHit;
			chasePlayer.setHealth(chasePlayer.getHealth() - 1);
		} else {
			chasePlayer.setHealth(chasePlayer.getHealth() - 2);
		}
		makeRelativeSound(eventType, chasePlayer.getX(), chasePlayer.getY());
	}

	public void onGunShot(Player player, Enemy enemy) {
		enemy.setDead(true);
		enemies.remove(enemy);
		newSprite(new GunSprite(player.getX(), player.getY(), enemy.getX(), enemy.getY()));
		makeRelativeSound(SoundEventType.GunShot, player.getX(), player.getY());
	}

	public void onMedkitUsed(Player player) {
		newSprite(new MedkitSprite(player.getX(), player.getY()));
		soundEvents.add(new SoundEvent(SoundEventType.Medkit));
	}

	public void onRadarUsed(Player player) {
		soundEvents.add(new SoundEvent(SoundEventType.Radar));
	}

	public void onFlareUsed(Player player) {
		soundEvents.add(new SoundEvent(SoundEventType.Flare));
	}

	public void onErrorUsed() {
		soundEvents.add(new SoundEvent(SoundEventType.Error));
	}

	public boolean nextHTPMessage() {
		if (htpMessageIndex + 1 >= htpStage.getMessages().length) {
			return false;
		}
		htpMessageIndex++;
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
	public int getPlayerCount() {
		return playerCount;
	}

	@Override
	public int getSelectedPlayerIndex() {
		return selectedPlayer;
	}

	@Override
	public int getPlayerX(int i) {
		return players[i].getX();
	}

	@Override
	public int getPlayerY(int i) {
		return players[i].getY();
	}

	@Override
	public boolean isLocating() {
		return getSelectedPlayer().isLocating();
	}

	@Override
	public boolean isFlared() {
		return getSelectedPlayer().isFlared();
	}

	@Override
	public ItemType getItem() {
		return getSelectedPlayer().getItem();
	}

	@Override
	public boolean isPlayerAlert(int i) {
		return players[i].isAlert();
	}

	@Override
	public int getPlayerHealth(int i) {
		return players[i].getHealth();
	}

	@Override
	public HTPMessage getHTPMessage() {
		if (htpStage != null) {
			return htpStage.getMessages()[htpMessageIndex];
		}
		return null;
	}
}
