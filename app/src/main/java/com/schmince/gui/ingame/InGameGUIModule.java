package com.schmince.gui.ingame;

import androidx.core.graphics.Insets;
import com.schmince.C;
import com.schmince.game.GameModelInterface;
import com.schmince.game.GameState;
import com.schmince.game.SchminceGame;
import com.schmince.game.howtoplay.HowToPlayMessage;
import com.schmince.game.model.ItemType;
import com.schmince.gui.GUIModule;
import dgui.GUIItem;
import dgui.label.Label;
import dgui.panel.Panel;
import texample.GLText;
import texample.GLTextCache;
import texample.GLTextType;
import thed.DTimer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Derek Mulvihill - Jan 15, 2014
 */
public class InGameGUIModule implements GUIModule {
    private SchminceGame game;

    private List<GUIItem> gui = new ArrayList<>();
    private SelectSurvivorButton[] survivorButtons = new SelectSurvivorButton[C.MAX_SURVIVOR_COUNT];
    private UseItemButton useItemButton = new UseItemButton();

    private Panel panelHowToPlayMessage = new Panel();
    private Label[] howToPlayMessageLabels = new Label[]{new Label(""), new Label(""), new Label(""), new Label(""), new Label(""), new Label(""), new Label(""), new Label("")};
    private HowToPlayNextButton howToPlayNextButton = new HowToPlayNextButton();

    public InGameGUIModule() {
        for (int i = 0; i < survivorButtons.length; i++) {
            SelectSurvivorButton button = new SelectSurvivorButton(i);
            survivorButtons[i] = button;
            gui.add(button);
        }
        gui.add(useItemButton);

        panelHowToPlayMessage.Color.set(0, 0, 0.5f, 0.5f);
        gui.add(panelHowToPlayMessage);

        howToPlayNextButton.NormalColor.set(0, 0.75f, 0, 0.5f);
        gui.add(howToPlayNextButton);

        Collections.addAll(gui, howToPlayMessageLabels);

        gui = Collections.unmodifiableList(gui);
    }

    @Override
    public void update(int width, int height, Insets insets, GLTextCache textCache) {
        int left = insets.left;
        int bottom = insets.bottom;
        width -= insets.left + insets.right;
        height -= insets.top + insets.bottom;

        GameModelInterface model = game.getModelInterface();
        float x = 0;
        float y = 0;
        float w = 0;
        float h = Math.min(width, height) * 0.1f;
        for (SelectSurvivorButton button : survivorButtons) {
            if (model.getSurvivorCount() <= button.getSurvivorIndex()) {
                button.Visible = false;
                continue;
            }
            button.Visible = true;
            button.setIsAlert(DTimer.get().millis() % 400 < 200
                    && model.isSurvivorAlert(button.getSurvivorIndex()));
            button.setSurvivorHealth(model.getSurvivorHealth(button.getSurvivorIndex()));
            w = h * 1.5f;
            if (x + w > width) {
                y += h + 5;
                x = 0;
            }
            button.Bounds.set(left + x, bottom + y, w, h);
            x += w + 5;
        }

        ItemType item = model.getItem();
        useItemButton.Visible = item != null;
        if (item != null) {
            useItemButton.BackgroundIcon = item;
            useItemButton.Bounds.set(left + width - h * 2, bottom + height - h, h * 2, h);
        }

        if (game.getGameState() == GameState.HowToPlay) {
            panelHowToPlayMessage.Visible = true;

            HowToPlayMessage message = model.getHowToPlayMessage();

            y = height - 20;
            x = 20;
            float maxWidth = 0;

            int m = 0;
            for (; m < message.getMessages().length && m < howToPlayMessageLabels.length; m++) {
                Label label = howToPlayMessageLabels[m];
                label.Visible = true;
                label.Text = message.getMessages()[m];
                if (message.isTitle()) {
                    label.TextType = GLTextType.SansBold;
                } else {
                    label.TextType = GLTextType.Sans;
                }
                w = label.getGLText(textCache).getLength(label.Text);
                h = label.getGLText(textCache).getHeight();
                if (w > maxWidth) {
                    maxWidth = w;
                }
                y -= h;
                label.Bounds.set(left + x, bottom + y, w, h);
            }
            for (; m < howToPlayMessageLabels.length; m++) {
                howToPlayMessageLabels[m].Visible = false;
            }

            GLText glText = howToPlayNextButton.getGLText(textCache);
            w = glText.getLength(howToPlayNextButton.Text) * 1.5f;
            h = glText.getHeight() * 1.5f;
            y -= h + 20;
            howToPlayNextButton.Bounds.set(left + x, bottom + y, w, h);

            y -= 20;
            panelHowToPlayMessage.Bounds.set(left, bottom + y, maxWidth + 40, height - y);
        } else {
            panelHowToPlayMessage.Visible = false;
            howToPlayNextButton.Visible = false;
            for (Label howToPlayMessageLabel : howToPlayMessageLabels) {
                howToPlayMessageLabel.Visible = false;
            }
        }
    }

    @Override
    public List<GUIItem> getGUI() {
        return gui;
    }

    public void setGame(SchminceGame game) {
        this.game = game;
        for (SelectSurvivorButton button : survivorButtons) {
            button.setGame(game);
        }
        useItemButton.setGame(game);
        howToPlayNextButton.setGame(game);
    }
}
