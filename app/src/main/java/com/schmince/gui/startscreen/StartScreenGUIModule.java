package com.schmince.gui.startscreen;

import com.schmince.game.SchminceGame;
import com.schmince.gldraw.GLIconType;
import com.schmince.gui.GUIModule;
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
    private List<GUIItem> startScreenItems = new ArrayList<>();
    private final Panel panelDark = new Panel();
    private final Icon titleIcon = new Icon(GLIconType.Title);
    private final NewGameButton newGameButton = new NewGameButton();
    private final HowToPlayButton howToPlayButton = new HowToPlayButton();
    private final PrivacyButton privacyButton = new PrivacyButton();

    public StartScreenGUIModule() {
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

        privacyButton.TextScale = 1.5f;
        privacyButton.NormalColor.set(1f, 0f, 0f, 0.5f);
        startScreenItems.add(privacyButton);

        startScreenItems = Collections.unmodifiableList(startScreenItems);
    }

    @Override
    public void update(int width, int height, GLTextCache textCache) {
        panelDark.Bounds.set(0, 0, width, height);
        int pad = Math.min(width / 20, height / 20);
        titleIcon.Bounds.set(pad, height / 2f, width - pad * 2, height / 2f);
        if (width > height) {
            int x = pad;
            int y = pad;
            int w = width / 2 - pad * 2;
            int h = height / 2 - pad * 2;
            newGameButton.Bounds.set(x, y, w, h);
            x += w + pad * 2;
            h = height / 4 - pad * 2;
            privacyButton.Bounds.set(x, y, w, h);
            y += h + pad * 2;
            howToPlayButton.Bounds.set(x, y, w, h);
        } else {
            int x = pad;
            int y = pad;
            int w = width - pad * 2;
            int h = height / 2 / 3 - pad * 2;
            privacyButton.Bounds.set(x, y, w, h);
            y += h + pad * 2;
            howToPlayButton.Bounds.set(x, y, w, h);
            y += h + pad * 2;
            newGameButton.Bounds.set(x, y, w, h);
        }
    }

    @Override
    public List<GUIItem> getGUI() {
        return startScreenItems;
    }

    public void setGame(SchminceGame game) {
        newGameButton.setGame(game);
        howToPlayButton.setGame(game);
        privacyButton.setGame(game);
    }
}
