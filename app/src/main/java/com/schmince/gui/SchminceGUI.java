package com.schmince.gui;

import androidx.core.graphics.Insets;
import com.schmince.game.SchminceGame;
import com.schmince.gui.gamecreate.GameCreateModule;
import com.schmince.gui.gameover.GameOverModule;
import com.schmince.gui.ingame.InGameGUIModule;
import com.schmince.gui.startscreen.StartScreenGUIModule;
import dgui.BaseGUI;
import dgui.GUIItem;
import texample.GLTextCache;

import java.util.List;

/**
 * @author Derek Mulvihill - Jan 9, 2014
 */
public class SchminceGUI extends BaseGUI {
    private final GUIState state = GUIState.None;
    private final StartScreenGUIModule startScreenModule = new StartScreenGUIModule();
    private final InGameGUIModule inGameModule = new InGameGUIModule();
    private final GameCreateModule gameCreateModule = new GameCreateModule();
    private final GameOverModule gameOverModule = new GameOverModule();
    Insets insets;
    private SchminceGame game;

    public void setGame(SchminceGame game) {
        this.game = game;
        startScreenModule.setGame(game);
        inGameModule.setGame(game);
        gameCreateModule.setGame(game);
        gameOverModule.setGame(game);
    }

    @Override
    public List<GUIItem> getGUIItems() {
        GUIModule module = getModule();
        if (module != null) {
            return module.getGUI();
        }
        return null;
    }

    private GUIModule getModule() {
        switch (game.getGameState()) {
            case StartScreen: {
                return startScreenModule;
            }
            case HowToPlay:
            case InGame: {
                if (state == GUIState.None) {
                    return inGameModule;
                }
                throw new RuntimeException("Invalid GUIState");
            }
            case GameCreate: {
                return gameCreateModule;
            }
            case GameStarting: {
                return null;
            }
            case GameOver: {
                return gameOverModule;
            }
            default: {
                throw new RuntimeException("Invalid GameState");
            }
        }
    }

    @Override
    public void update(int screenWidth, int screenHeight, GLTextCache textCache) {
        GUIModule module = getModule();
        if (module != null) {
            module.update(screenWidth, screenHeight, insets, textCache);
        }
    }

    @Override
    protected void uniqueAction(float worldX, float worldY) {

    }

    @Override
    protected void endUniqueAction(float worldX, float worldY) {

    }

    @Override
    protected boolean singleAction(float worldX, float worldY) {
        game.onTap(worldX, worldY);
        return true;
    }

    @Override
    public boolean onBackPressed() {
        switch (game.getGameState()) {
            case GameCreate:
                game.onCancelGameCreate();
                return true;
            case GameOver:
                game.onEndGameOver();
                return true;
            case HowToPlay:
                game.onCancelHowToPlay();
                return true;
            case GameStarting:
            case InGame:
            case StartScreen:
                break;
        }
        return false;
    }

    public void updateInsets(Insets insets) {
        this.insets = insets;
    }
}
