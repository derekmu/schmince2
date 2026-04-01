package com.schmince.gui.gamecreate;

import androidx.core.graphics.Insets;
import com.schmince.C;
import com.schmince.game.SchminceGame;
import com.schmince.gui.GUIModule;
import dgui.GUIItem;
import dgui.label.Label;
import dgui.panel.Panel;
import texample.GLText;
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
    private List<GUIItem> gui = new ArrayList<>();
    private Panel panelDark = new Panel();
    private Label labelSurvivors = new Label("Survivors");
    private SurvivorCountButton[] survivorCountButtons;
    private Label labelWorldSize = new Label("World");
    private WorldSizeButton[] worldSizeButtons;
    private Label labelEnemies = new Label("Enemies");
    private EnemyCountButton[] enemyCountButtons;
    private Label labelItems = new Label("Items");
    private ItemCountButton[] itemCountButtons;
    private Label labelHardcore = new Label("Hardcore?");
    private HardcoreButton[] hardcoreButtons;
    private StartGameButton startGameButton = new StartGameButton(this);

    private SchminceGame game;
    private int survivorCount = 6;
    private int mapSize = 100;
    private int itemCount = 30;
    private int enemyCount = 10;
    private boolean hardcore = false;

    public GameCreateModule() {
        panelDark.Color.set(0f, 0f, 0f, 0.35f);
        gui.add(panelDark);

        labelSurvivors.Align = Alignment.Center;
        labelSurvivors.TextType = GLTextType.SansBold;
        gui.add(labelSurvivors);

        survivorCountButtons = new SurvivorCountButton[C.MAX_SURVIVOR_COUNT];
        for (int i = 0; i < survivorCountButtons.length; i++) {
            SurvivorCountButton button = survivorCountButtons[i] = new SurvivorCountButton(i + 1, this);
            gui.add(button);
        }

        labelWorldSize.Align = Alignment.Center;
        labelWorldSize.TextType = GLTextType.SansBold;
        gui.add(labelWorldSize);

        worldSizeButtons = new WorldSizeButton[]{
                new WorldSizeButton(25, "Tiny", this),
                new WorldSizeButton(50, "Small", this),
                new WorldSizeButton(100, "Normal", this),
                new WorldSizeButton(150, "Big", this),
                new WorldSizeButton(200, "Huge", this),
                new WorldSizeButton(300, "Vast", this),
        };
        gui.addAll(Arrays.asList(worldSizeButtons));

        startGameButton.TextType = GLTextType.SansBold;
        gui.add(startGameButton);

        labelEnemies.Align = Alignment.Center;
        labelEnemies.TextType = GLTextType.SansBold;
        gui.add(labelEnemies);

        enemyCountButtons = new EnemyCountButton[]{
                new EnemyCountButton(0, "None", this),
                new EnemyCountButton(5, "Few", this),
                new EnemyCountButton(10, "Normal", this),
                new EnemyCountButton(15, "A lot", this),
                new EnemyCountButton(20, "Many", this),
                new EnemyCountButton(30, "Swarm", this),
        };
        gui.addAll(Arrays.asList(enemyCountButtons));

        labelItems.Align = Alignment.Center;
        labelItems.TextType = GLTextType.SansBold;
        gui.add(labelItems);

        itemCountButtons = new ItemCountButton[]{
                new ItemCountButton(0, "None", this),
                new ItemCountButton(10, "Scarce", this),
                new ItemCountButton(30, "Normal", this),
                new ItemCountButton(40, "Plenty", this),
                new ItemCountButton(50, "Many", this),
                new ItemCountButton(60, "Abundant", this),
        };
        gui.addAll(Arrays.asList(itemCountButtons));

        labelHardcore.Align = Alignment.Center;
        labelHardcore.TextType = GLTextType.SansBold;
        gui.add(labelHardcore);

        hardcoreButtons = new HardcoreButton[]{
                new HardcoreButton(true, "Yes", this),
                new HardcoreButton(false, "No", this),
        };
        gui.addAll(Arrays.asList(hardcoreButtons));

        gui = Collections.unmodifiableList(gui);
    }

    @Override
    public void update(int width, int height, Insets insets, GLTextCache textCache) {
        panelDark.Bounds.set(0, 0, width, height);

        int left = insets.left;
        int bottom = insets.bottom;
        width -= insets.left + insets.right;
        height -= insets.top + insets.bottom;


        GLText glText = labelSurvivors.getGLText(textCache);
        float xLeft = glText.getLength(labelSurvivors.Text) * 1.1f;
        float h = glText.getHeight();
        float y = bottom + height - h - 10;
        labelSurvivors.Bounds.set(left, y, xLeft, h);

        float x = xLeft;
        y += h;
        h = survivorCountButtons[0].getGLText(textCache).getHeight();
        y -= h;
        float w = (width - xLeft - 5) / 6;
        for (SurvivorCountButton button : survivorCountButtons) {
            if (x + w > width) {
                x = xLeft;
                y -= h + 5;
            }
            if (button.getSurvivorCount() == survivorCount) {
                button.NormalColor.set(0, 0.5f, 0);
            } else {
                button.NormalColor.set(0, 0, 1);
            }
            button.Bounds.set(left + x, y, w - 5, h);
            x += w;
        }

        h = labelWorldSize.getGLText(textCache).getHeight();
        y -= h + 20;
        labelWorldSize.Bounds.set(left, y, xLeft, h);

        x = xLeft;
        y += h;
        h = worldSizeButtons[0].getGLText(textCache).getHeight();
        y -= h;
        w = (width - xLeft - 5) / (worldSizeButtons.length / 2f);
        for (WorldSizeButton button : worldSizeButtons) {
            if (x + w > width) {
                x = xLeft;
                y -= h + 5;
            }
            if (button.getMapSize() == mapSize) {
                button.NormalColor.set(0, 0.5f, 0);
            } else {
                button.NormalColor.set(0, 0, 1);
            }
            button.Bounds.set(left + x, y, w - 5, h);
            x += w;
        }

        h = labelEnemies.getGLText(textCache).getHeight();
        y -= h + 20;
        labelEnemies.Bounds.set(left, y, xLeft, h);

        x = xLeft;
        y += h;
        h = enemyCountButtons[0].getGLText(textCache).getHeight();
        y -= h;
        w = (width - xLeft - 5) / (enemyCountButtons.length / 2f);
        for (EnemyCountButton button : enemyCountButtons) {
            if (x + w > width) {
                x = xLeft;
                y -= h + 5;
            }
            if (button.getEnemyCount() == enemyCount) {
                button.NormalColor.set(0, 0.5f, 0);
            } else {
                button.NormalColor.set(0, 0, 1);
            }
            button.Bounds.set(left + x, y, w - 5, h);
            x += w;
        }

        h = labelItems.getGLText(textCache).getHeight();
        y -= h + 20;
        labelItems.Bounds.set(left, y, xLeft, h);

        x = xLeft;
        y += h;
        h = itemCountButtons[0].getGLText(textCache).getHeight();
        y -= h;
        w = (width - xLeft - 5) / (itemCountButtons.length / 2f);
        for (ItemCountButton button : itemCountButtons) {
            if (x + w > width) {
                x = xLeft;
                y -= h + 5;
            }
            if (button.getItemCount() == itemCount) {
                button.NormalColor.set(0, 0.5f, 0);
            } else {
                button.NormalColor.set(0, 0, 1);
            }
            button.Bounds.set(left + x, y, w - 5, h);
            x += w;
        }

        h = labelHardcore.getGLText(textCache).getHeight();
        y -= h + 20;
        labelHardcore.Bounds.set(left, y, xLeft, h);

        x = xLeft;
        y += h;
        h = hardcoreButtons[0].getGLText(textCache).getHeight();
        y -= h;
        w = (width - xLeft - 5) / hardcoreButtons.length;
        for (HardcoreButton button : hardcoreButtons) {
            if (x + w > width) {
                x = xLeft;
                y -= h + 5;
            }
            if (button.getHardcore() == hardcore) {
                button.NormalColor.set(0, 0.5f, 0);
            } else {
                button.NormalColor.set(0, 0, 1);
            }
            button.Bounds.set(left + x, y, w - 5, h);
            x += w;
        }

        glText = startGameButton.getGLText(textCache);
        w = glText.getLength(startGameButton.Text) * 1.25f;
        h = glText.getHeight() * 1.25f;
        startGameButton.Bounds.set(left + (width / 2) - w / 2, y - h * 1.5f, w, h);
    }

    @Override
    public List<GUIItem> getGUI() {
        return gui;
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

    public void setHardcore(boolean hardcore) {
        this.hardcore = hardcore;
    }

    public void createGame() {
        game.onCreateGame(survivorCount, mapSize, itemCount, enemyCount, hardcore);
    }
}
