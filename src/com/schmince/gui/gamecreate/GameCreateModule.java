package com.schmince.gui.gamecreate;

import com.schmince.C;
import com.schmince.game.SchminceGame;
import com.schmince.gui.GUIModule;
import com.schmince.gui.SchminceGUI;
import dgui.GUIItem;
import dgui.label.Label;
import dgui.panel.Panel;
import texample.GLTextCache;
import texample.GLTextType;
import thed.Alignment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Derek Mulvihill - Jan 25, 2014
 */
public class GameCreateModule implements GUIModule {
    private List<GUIItem> gameCreateItems = new ArrayList<GUIItem>();
    private Panel panelDark = new Panel();
    private Label labelPlayerCount = new Label("Survivor Count");
    private PlayerCountButton[] playerCountButtons;
    private Label labelMapSize = new Label("World Size");
    private MapSizeButton[] mapSizeButtons;
    private Label labelEnemyPer = new Label("Enemy Count");
    private EnemyPerSlider sliderEnemyPer;
    private Label labelItemPer = new Label("Item Count");
    private ItemPerSlider sliderItemPer;

    private CreateGameButton createGameButton = new CreateGameButton(this);

    private SchminceGame game;
    private int playerCount = 6;
    private int mapSize = 100;
    private int itemPer = 30;
    private int enemyPer = 10;

    public GameCreateModule(SchminceGUI gui) {
        panelDark.Color.set(0f, 0f, 0f, 0.35f);
        gameCreateItems.add(panelDark);

        labelPlayerCount.Align = Alignment.Center;
        labelPlayerCount.TextType = GLTextType.SansBold;
        gameCreateItems.add(labelPlayerCount);

        playerCountButtons = new PlayerCountButton[C.MAX_PLAYER_COUNT];
        for (int i = 0; i < playerCountButtons.length; i++) {
            PlayerCountButton button = playerCountButtons[i] = new PlayerCountButton(i + 1, this);
            gameCreateItems.add(button);
        }

        labelMapSize.Align = Alignment.Center;
        labelMapSize.TextType = GLTextType.SansBold;
        gameCreateItems.add(labelMapSize);

        mapSizeButtons = new MapSizeButton[C.MAX_MAP_SIZE / 25];
        for (int i = 0; i < mapSizeButtons.length; i++) {
            MapSizeButton button = mapSizeButtons[i] = new MapSizeButton((i + 1) * 25, this);
            gameCreateItems.add(button);
        }

        createGameButton.TextType = GLTextType.SansBold;
        gameCreateItems.add(createGameButton);

        labelEnemyPer.Align = Alignment.Center;
        labelEnemyPer.TextType = GLTextType.SansBold;
        gameCreateItems.add(labelEnemyPer);

        sliderEnemyPer = new EnemyPerSlider(this);
        sliderEnemyPer.Value = enemyPer;
        sliderEnemyPer.Minimum = 0;
        sliderEnemyPer.Maximum = 20;
        sliderEnemyPer.Color.set(1f, 0f, 1f, 1f);
        gameCreateItems.add(sliderEnemyPer);

        labelItemPer.Align = Alignment.Center;
        labelItemPer.TextType = GLTextType.SansBold;
        gameCreateItems.add(labelItemPer);

        sliderItemPer = new ItemPerSlider(this);
        sliderItemPer.Value = itemPer;
        sliderItemPer.Minimum = 0;
        sliderItemPer.Maximum = 60;
        sliderItemPer.Color.set(0f, 0f, 0.7f);
        gameCreateItems.add(sliderItemPer);

        gameCreateItems = Collections.unmodifiableList(gameCreateItems);
    }

    @Override
    public void update(int screenWidth, int screenHeight, GLTextCache textCache) {
        panelDark.Bounds.set(0, 0, screenWidth, screenHeight);

        float w = createGameButton.getGLText(textCache).getLength(createGameButton.Text) * 1.25f;
        float h = createGameButton.getGLText(textCache).getHeight() * 1.25f;
        createGameButton.Bounds.set(screenWidth - w, 0, w, h);

        float xLeft = labelPlayerCount.getGLText(textCache).getLength(labelPlayerCount.Text) * 1.1f;
        h = labelPlayerCount.getGLText(textCache).getHeight();
        float y = screenHeight - h - 10;
        labelPlayerCount.Bounds.set(0, y, xLeft, h);

        float x = xLeft;
        y += h;
        h = playerCountButtons[0].getGLText(textCache).getHeight();
        y -= h;
        w = (screenWidth - xLeft - 5) / 6;
        for (PlayerCountButton button : playerCountButtons) {
            if (x + w > screenWidth) {
                x = xLeft;
                y -= h + 5;
            }
            if (button.getPlayerCount() == playerCount) {
                button.NormalColor.set(0, 1, 0);
            } else {
                button.NormalColor.set(0, 0, 1);
            }
            button.Bounds.set(x, y, w - 5, h);
            x += w;
        }

        h = labelMapSize.getGLText(textCache).getHeight();
        y -= h + 20;
        labelMapSize.Bounds.set(0, y, xLeft, h);

        x = xLeft;
        y += h;
        h = mapSizeButtons[0].getGLText(textCache).getHeight();
        y -= h;
        w = (screenWidth - xLeft - 5) / (mapSizeButtons.length / 2);
        for (MapSizeButton button : mapSizeButtons) {
            if (x + w > screenWidth) {
                x = xLeft;
                y -= h + 5;
            }
            if (button.getMapSize() == mapSize) {
                button.NormalColor.set(0, 1, 0);
            } else {
                button.NormalColor.set(0, 0, 1);
            }
            button.Bounds.set(x, y, w - 5, h);
            x += w;
        }

        h = labelEnemyPer.getGLText(textCache).getHeight();
        y -= h + 20;
        labelEnemyPer.Bounds.set(0, y, xLeft, h);

        x = xLeft;
        w = (screenWidth - xLeft - 5);
        sliderEnemyPer.Bounds.set(x, y, w, h);

        h = labelItemPer.getGLText(textCache).getHeight();
        y -= h + 20;
        labelItemPer.Bounds.set(0, y, xLeft, h);

        x = xLeft;
        w = (screenWidth - xLeft - 5);
        sliderItemPer.Bounds.set(x, y, w, h);
    }

    @Override
    public List<GUIItem> getGUI() {
        return gameCreateItems;
    }

    public void setGame(SchminceGame game) {
        this.game = game;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public void setMapSize(int mapSize) {
        this.mapSize = mapSize;
    }

    public void setEnemyPer(int enemyPer) {
        this.enemyPer = enemyPer;
    }

    public void setItemPer(int itemPer) {
        this.itemPer = itemPer;
    }

    public void createGame() {
        game.onCreateGame(playerCount, mapSize, itemPer, enemyPer);
    }
}
