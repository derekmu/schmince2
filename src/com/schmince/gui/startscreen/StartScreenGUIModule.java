package com.schmince.gui.startscreen;

import android.app.Activity;
import com.schmince.game.SchminceGame;
import com.schmince.gldraw.GLIconType;
import com.schmince.gui.GUIModule;
import com.schmince.gui.SchminceGUI;
import dgui.GUIItem;
import dgui.icon.Icon;
import dgui.panel.Panel;
import texample.GLTextCache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * GUIModule implementation for the start screen.
 *
 * @author Derek Mulvihill - Jan 15, 2014
 */
public class StartScreenGUIModule implements GUIModule {
    private List<GUIItem> startScreenItems = new ArrayList<GUIItem>();
    private Panel panelDark = new Panel();
    private Icon titleIcon = new Icon(GLIconType.Title);
    private NewGameButton newGameButton = new NewGameButton();
    private HowToPlayButton howToPlayButton = new HowToPlayButton();
    private QuitButton quitButton = new QuitButton();

    public StartScreenGUIModule(SchminceGUI gui) {
        panelDark.Color.set(0f, 0f, 0f, 0.35f);
        startScreenItems.add(panelDark);

        titleIcon.AspectRatio = 6f;
        startScreenItems.add(titleIcon);

        newGameButton.TextScale = 1.5f;
        newGameButton.NormalColor.set(0f, 0f, 1f, 0.5f);
        startScreenItems.add(newGameButton);

        howToPlayButton.TextScale = 1.5f;
        howToPlayButton.NormalColor.set(0f, 1f, 0f, 0.5f);
        startScreenItems.add(howToPlayButton);

        quitButton.TextScale = 1.5f;
        quitButton.NormalColor.set(1f, 0f, 0f, 0.5f);
        startScreenItems.add(quitButton);

        startScreenItems = Collections.unmodifiableList(startScreenItems);
    }

    @Override
    public void update(int screenWidth, int screenHeight, GLTextCache textCache) {
        panelDark.Bounds.set(0, 0, screenWidth, screenHeight);
        titleIcon.Bounds.set(screenWidth * 0.05f, screenHeight / 2, screenWidth - screenWidth
                * 0.1f, screenHeight / 2);
        if (screenWidth > screenHeight) {
            newGameButton.Bounds.set(10, 10, screenWidth / 2 - 20, screenHeight / 2 - 20);
            howToPlayButton.Bounds.set(screenWidth / 2 + 10, screenHeight / 4 + 10,
                    screenWidth / 2 - 20, screenHeight / 4 - 20);
            quitButton.Bounds.set(screenWidth / 2 + 10, 10, screenWidth / 2 - 20,
                    screenHeight / 4 - 20);
        } else {
            newGameButton.Bounds.set(10, screenHeight / 4 + 10, screenWidth - 20,
                    screenHeight / 4 - 20);
            howToPlayButton.Bounds.set(10, screenHeight / 8 + 10, screenWidth - 20,
                    screenHeight / 8 - 20);
            quitButton.Bounds.set(10, 10, screenWidth - 20, screenHeight / 8 - 20);
        }
    }

    @Override
    public List<GUIItem> getGUI() {
        return startScreenItems;
    }

    public void setGame(SchminceGame game) {
        newGameButton.setGame(game);
        howToPlayButton.setGame(game);
    }

    public void setActivity(Activity activity) {
        quitButton.setActivity(activity);
    }
}
