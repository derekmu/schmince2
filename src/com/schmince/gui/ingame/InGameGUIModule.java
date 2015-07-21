package com.schmince.gui.ingame;

import com.schmince.C;
import com.schmince.game.GameModelInterface;
import com.schmince.game.GameState;
import com.schmince.game.SchminceGame;
import com.schmince.game.howtoplay.HTPMessage;
import com.schmince.game.model.ItemType;
import com.schmince.gui.GUIModule;
import com.schmince.gui.SchminceGUI;
import dgui.GUIItem;
import dgui.label.Label;
import dgui.label.NCSLabel;
import dgui.panel.Panel;
import texample.GLTextCache;
import texample.GLTextType;
import thed.DFrameMonitor;
import thed.DTimer;
import thed.NumCharSequence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Derek Mulvihill - Jan 15, 2014
 */
public class InGameGUIModule implements GUIModule {
    private SchminceGame game;

    private DFrameMonitor fpsMonitor = new DFrameMonitor(1000);

    private List<GUIItem> inGameItems = new ArrayList<GUIItem>();
    private SelectPlayerButton[] playerButtons = new SelectPlayerButton[C.MAX_PLAYER_COUNT];
    private UseItemButton useItemButton = new UseItemButton();
    private NCSLabel labelFPS = new NCSLabel(new NumCharSequence("FPS: ", null));

    private Panel panelHTPMessage = new Panel();
    private Label[] htpMessageLabels = new Label[]{new Label(""), new Label(""), new Label("")};
    private HTPNextButton htpNextButton = new HTPNextButton();

    public InGameGUIModule(SchminceGUI gui) {
        for (int i = 0; i < playerButtons.length; i++) {
            SelectPlayerButton button = new SelectPlayerButton(i);
            playerButtons[i] = button;
            inGameItems.add(button);
        }
        inGameItems.add(useItemButton);

        labelFPS.Visible = false;
        inGameItems.add(labelFPS);

        panelHTPMessage.Color.set(0, 0, 0.5f, 0.5f);
        inGameItems.add(panelHTPMessage);

        htpNextButton.TextScale = 0.75f;
        htpNextButton.NormalColor.set(0, 0.75f, 0, 0.5f);
        inGameItems.add(htpNextButton);

        for (Label label : htpMessageLabels) {
            inGameItems.add(label);
        }

        inGameItems = Collections.unmodifiableList(inGameItems);
    }

    @Override
    public void update(int screenWidth, int screenHeight, GLTextCache textCache) {
        GameModelInterface model = game.getModelInterface();
        float x = 0;
        float y = 0;
        float h = Math.min(screenWidth, screenHeight) * 0.1f;
        for (SelectPlayerButton button : playerButtons) {
            if (model.getPlayerCount() <= button.getPlayerIndex()) {
                button.Visible = false;
                continue;
            }
            button.Visible = true;
            button.setIsAlert(DTimer.get().millis() % 500 < 250
                    && model.isPlayerAlert(button.getPlayerIndex()));
            button.setPlayerHealth(model.getPlayerHealth(button.getPlayerIndex()));
            float w = h * 2;
            if (x + w > screenWidth) {
                y += h + 5;
                x = 0;
            }
            button.Bounds.set(x, y, w, h);
            x += w + 5;
        }

        ItemType item = model.getItem();
        useItemButton.Visible = item != null;
        if (item != null) {
            useItemButton.BackgroundIcon = item;
            useItemButton.Bounds.set(screenWidth - h * 2, screenHeight - h, h * 2, h);
        }

        fpsMonitor.addFrame(DTimer.get().millis());
        labelFPS.NCS.setNum(0, fpsMonitor.getFrames());
        h = labelFPS.getGLText(textCache).getHeight();
        float w = labelFPS.getGLText(textCache).getLength(labelFPS.Text);
        labelFPS.Bounds.set(0, screenHeight - h, w, h);

        if (game.getGameState() == GameState.HowToPlay) {
            panelHTPMessage.Visible = true;

            HTPMessage message = model.getHTPMessage();

            y = screenHeight - 5;
            x = 5;
            float maxWidth = 0;

            int m = 0;
            for (; m < message.getMessages().length && m < htpMessageLabels.length; m++) {
                Label label = htpMessageLabels[m];
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
                label.Bounds.set(x, y, w, h);
            }
            for (; m < htpMessageLabels.length; m++) {
                htpMessageLabels[m].Visible = false;
            }

            w = htpNextButton.getGLText(textCache).getLength(htpNextButton.Text) * 1.2f;
            h = htpNextButton.getGLText(textCache).getHeight() * 1.2f;
            y -= h;
            htpNextButton.Bounds.set(x + maxWidth - w, y, w, h);

            y -= 5;
            panelHTPMessage.Bounds.set(0, y, maxWidth + 10, screenHeight - y);
        } else {
            panelHTPMessage.Visible = false;
            htpNextButton.Visible = false;
            for (int m = 0; m < htpMessageLabels.length; m++) {
                htpMessageLabels[m].Visible = false;
            }
        }
    }

    @Override
    public List<GUIItem> getGUI() {
        return inGameItems;
    }

    public void setGame(SchminceGame game) {
        this.game = game;
        for (SelectPlayerButton button : playerButtons) {
            button.setGame(game);
        }
        useItemButton.setGame(game);
        htpNextButton.setGame(game);
    }
}
