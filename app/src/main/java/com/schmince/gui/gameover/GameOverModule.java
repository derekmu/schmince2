package com.schmince.gui.gameover;

import androidx.core.graphics.Insets;
import com.schmince.game.SchminceGame;
import com.schmince.gui.GUIModule;
import dgui.GUIItem;
import dgui.button.Button;
import dgui.label.Label;
import dgui.label.NCSLabel;
import dgui.panel.Panel;
import texample.GLTextCache;
import texample.GLTextType;
import thed.Alignment;
import thed.NumCharSequence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Derek Mulvihill - Feb 8, 2014
 */
public class GameOverModule implements GUIModule {
    private List<GUIItem> gui = new ArrayList<>();
    private Panel panelDark = new Panel();
    private Label labelGameOver = new Label("Game Over");
    private NCSLabel labelSurvivors = new NCSLabel(new NumCharSequence("Survivors: ", null));
    private NCSLabel labelDead = new NCSLabel(new NumCharSequence("Dead/Lost: ", null));
    private Button buttonStartScreen = new StartScreenButton(this);
    //Time Spent
    //Items Used
    //Enemies Killed
    //Enemies Left Alive
    //Overall Score?

    private SchminceGame game;

    public GameOverModule() {
        panelDark.Color.set(0f, 0f, 0f, 0.4f);
        gui.add(panelDark);

        labelGameOver.TextScale = 2.5f;
        labelGameOver.TextType = GLTextType.SansBold;
        labelGameOver.Align = Alignment.Center;
        gui.add(labelGameOver);

        labelSurvivors.Align = Alignment.Center;
        gui.add(labelSurvivors);

        labelDead.Align = Alignment.Center;
        gui.add(labelDead);

        buttonStartScreen.TextScale = 1.5f;
        buttonStartScreen.Align = Alignment.Center;
        gui.add(buttonStartScreen);

        gui = Collections.unmodifiableList(gui);
    }

    @Override
    public void update(int width, int height, Insets insets, GLTextCache textCache) {
        panelDark.Bounds.set(0, 0, width, height);

        int left = insets.left;
        int bottom = insets.bottom;
        width -= insets.left + insets.right;
        height -= insets.top + insets.bottom;

        int survivors = game.getSurvivorCount();
        int dead = game.getDeadCount();

        labelSurvivors.TextScale = 1f + (((float) survivors) / (dead + survivors)) * 0.5f;
        labelSurvivors.NCS.setNum(0, survivors);

        labelDead.TextScale = 1f + (((float) dead) / (dead + survivors)) * 0.5f;
        labelDead.NCS.setNum(0, dead);

        if (survivors > 0) {
            labelGameOver.Text = "Game Won";
            labelGameOver.Color.set(0, 0.75f, 0, 1f);
        } else {
            labelGameOver.Text = "Game Lost";
            labelGameOver.Color.set(0.75f, 0, 0, 1f);
        }

        float x = 0;
        float y = height;
        float h;

        h = labelGameOver.getGLText(textCache).getHeight();
        y -= h;
        labelGameOver.Bounds.set(left + x, bottom + y, (float) width, h);

        y -= 20; //spacing

        h = labelSurvivors.getGLText(textCache).getHeight();
        y -= h;
        labelSurvivors.Bounds.set(left + x, bottom + y, (float) width, h);

        h = labelDead.getGLText(textCache).getHeight();
        y -= h;
        labelDead.Bounds.set(left + x, bottom + y, (float) width, h);

        h = buttonStartScreen.getGLText(textCache).getHeight() * 1.5f;
        y = 0;
        buttonStartScreen.Bounds.set(left + x, bottom + y, (float) width, h);
    }

    @Override
    public List<GUIItem> getGUI() {
        return gui;
    }

    public void setGame(SchminceGame game) {
        this.game = game;
    }

    public void endGameOver() {
        game.onEndGameOver();
    }
}
