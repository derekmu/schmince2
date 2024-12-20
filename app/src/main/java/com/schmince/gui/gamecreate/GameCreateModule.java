package com.schmince.gui.gamecreate;

import com.schmince.C;
import com.schmince.game.SchminceGame;
import com.schmince.gui.GUIModule;
import dgui.GUIItem;
import dgui.label.Label;
import dgui.panel.Panel;
import texample.GLTextCache;
import texample.GLTextType;
import thed.Alignment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Derek Mulvihill - Jan 25, 2014
 */
public class GameCreateModule implements GUIModule {
	private List<GUIItem> gameCreateItems = new ArrayList<>();
	private Panel panelDark = new Panel();
	private Label labelSurvivors = new Label("Survivors");
	private SurvivorCountButton[] survivorCountButtons;
	private Label labelWorldSize = new Label("World");
	private WorldSizeButton[] worldSizeButtons;
	private Label labelEnemies = new Label("Enemies");
	private EnemyCountButton[] enemyCountButtons;
	private Label labelItems = new Label("Items");
	private ItemCountButton[] itemCountButtons;

	private StartGameButton startGameButton = new StartGameButton(this);

	private SchminceGame game;
	private int survivorCount = 6;
	private int mapSize = 100;
	private int itemCount = 30;
	private int enemyCount = 10;

	public GameCreateModule() {
		panelDark.Color.set(0f, 0f, 0f, 0.35f);
		gameCreateItems.add(panelDark);

		labelSurvivors.Align = Alignment.Center;
		labelSurvivors.TextType = GLTextType.SansBold;
		gameCreateItems.add(labelSurvivors);

		survivorCountButtons = new SurvivorCountButton[C.MAX_SURVIVOR_COUNT];
		for (int i = 0; i < survivorCountButtons.length; i++) {
			SurvivorCountButton button = survivorCountButtons[i] = new SurvivorCountButton(i + 1, this);
			gameCreateItems.add(button);
		}

		labelWorldSize.Align = Alignment.Center;
		labelWorldSize.TextType = GLTextType.SansBold;
		gameCreateItems.add(labelWorldSize);

		worldSizeButtons = new WorldSizeButton[]{
				new WorldSizeButton(25, "Tiny", this),
				new WorldSizeButton(50, "Small", this),
				new WorldSizeButton(100, "Normal", this),
				new WorldSizeButton(150, "Big", this),
				new WorldSizeButton(200, "Huge", this),
				new WorldSizeButton(300, "Vast", this),
		};
		gameCreateItems.addAll(Arrays.asList(worldSizeButtons));

		startGameButton.TextType = GLTextType.SansBold;
		gameCreateItems.add(startGameButton);

		labelEnemies.Align = Alignment.Center;
		labelEnemies.TextType = GLTextType.SansBold;
		gameCreateItems.add(labelEnemies);

		enemyCountButtons = new EnemyCountButton[]{
				new EnemyCountButton(0, "None", this),
				new EnemyCountButton(5, "Few", this),
				new EnemyCountButton(10, "Normal", this),
				new EnemyCountButton(15, "A lot", this),
				new EnemyCountButton(20, "Many", this),
				new EnemyCountButton(30, "Swarm", this),
		};
		gameCreateItems.addAll(Arrays.asList(enemyCountButtons));

		labelItems.Align = Alignment.Center;
		labelItems.TextType = GLTextType.SansBold;
		gameCreateItems.add(labelItems);

		itemCountButtons = new ItemCountButton[]{
				new ItemCountButton(0, "None", this),
				new ItemCountButton(10, "Scarce", this),
				new ItemCountButton(30, "Normal", this),
				new ItemCountButton(40, "Plenty", this),
				new ItemCountButton(50, "Many", this),
				new ItemCountButton(60, "Abundant", this),
		};
		gameCreateItems.addAll(Arrays.asList(itemCountButtons));

		gameCreateItems = Collections.unmodifiableList(gameCreateItems);
	}

	@Override
	public void update(int screenWidth, int screenHeight, GLTextCache textCache) {
		panelDark.Bounds.set(0, 0, screenWidth, screenHeight);

		float w = startGameButton.getGLText(textCache).getLength(startGameButton.Text) * 1.25f;
		float h = startGameButton.getGLText(textCache).getHeight() * 1.25f;
		startGameButton.Bounds.set(screenWidth - w, 0, w, h);

		float xLeft = labelSurvivors.getGLText(textCache).getLength(labelSurvivors.Text) * 1.1f;
		h = labelSurvivors.getGLText(textCache).getHeight();
		float y = screenHeight - h - 10;
		labelSurvivors.Bounds.set(0, y, xLeft, h);

		float x = xLeft;
		y += h;
		h = survivorCountButtons[0].getGLText(textCache).getHeight();
		y -= h;
		w = (screenWidth - xLeft - 5) / 6;
		for (SurvivorCountButton button : survivorCountButtons) {
			if (x + w > screenWidth) {
				x = xLeft;
				y -= h + 5;
			}
			if (button.getSurvivorCount() == survivorCount) {
				button.NormalColor.set(0, 0.5f, 0);
			} else {
				button.NormalColor.set(0, 0, 1);
			}
			button.Bounds.set(x, y, w - 5, h);
			x += w;
		}

		h = labelWorldSize.getGLText(textCache).getHeight();
		y -= h + 20;
		labelWorldSize.Bounds.set(0, y, xLeft, h);

		x = xLeft;
		y += h;
		h = worldSizeButtons[0].getGLText(textCache).getHeight();
		y -= h;
		w = (screenWidth - xLeft - 5) / (worldSizeButtons.length / 2f);
		for (WorldSizeButton button : worldSizeButtons) {
			if (x + w > screenWidth) {
				x = xLeft;
				y -= h + 5;
			}
			if (button.getMapSize() == mapSize) {
				button.NormalColor.set(0, 0.5f, 0);
			} else {
				button.NormalColor.set(0, 0, 1);
			}
			button.Bounds.set(x, y, w - 5, h);
			x += w;
		}

		h = labelEnemies.getGLText(textCache).getHeight();
		y -= h + 20;
		labelEnemies.Bounds.set(0, y, xLeft, h);

		x = xLeft;
		y += h;
		h = enemyCountButtons[0].getGLText(textCache).getHeight();
		y -= h;
		w = (screenWidth - xLeft - 5) / (enemyCountButtons.length / 2f);
		for (EnemyCountButton button : enemyCountButtons) {
			if (x + w > screenWidth) {
				x = xLeft;
				y -= h + 5;
			}
			if (button.getEnemyCount() == enemyCount) {
				button.NormalColor.set(0, 0.5f, 0);
			} else {
				button.NormalColor.set(0, 0, 1);
			}
			button.Bounds.set(x, y, w - 5, h);
			x += w;
		}

		h = labelItems.getGLText(textCache).getHeight();
		y -= h + 20;
		labelItems.Bounds.set(0, y, xLeft, h);

		x = xLeft;
		y += h;
		h = itemCountButtons[0].getGLText(textCache).getHeight();
		y -= h;
		w = (screenWidth - xLeft - 5) / (itemCountButtons.length / 2f);
		for (ItemCountButton button : itemCountButtons) {
			if (x + w > screenWidth) {
				x = xLeft;
				y -= h + 5;
			}
			if (button.getItemCount() == itemCount) {
				button.NormalColor.set(0, 0.5f, 0);
			} else {
				button.NormalColor.set(0, 0, 1);
			}
			button.Bounds.set(x, y, w - 5, h);
			x += w;
		}
	}

	@Override
	public List<GUIItem> getGUI() {
		return gameCreateItems;
	}

	public void setGame(SchminceGame game) {
		this.game = game;
	}

	public void setSurvivorCount(int survivorCount) {
		this.survivorCount = survivorCount;
	}

	public void setMapSize(int mapSize) {
		this.mapSize = mapSize;
	}

	public void setEnemyCount(int enemyCount) {
		this.enemyCount = enemyCount;
	}

	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}

	public void createGame() {
		game.onCreateGame(survivorCount, mapSize, itemCount, enemyCount);
	}
}
