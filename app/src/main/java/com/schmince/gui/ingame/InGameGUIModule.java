package com.schmince.gui.ingame;

import com.schmince.C;
import com.schmince.game.GameModelInterface;
import com.schmince.game.GameState;
import com.schmince.game.SchminceGame;
import com.schmince.game.howtoplay.HowToPlayMessage;
import com.schmince.game.model.ItemType;
import com.schmince.gui.GUIModule;
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

	private List<GUIItem> inGameItems = new ArrayList<>();
	private SelectSurvivorButton[] survivorButtons = new SelectSurvivorButton[C.MAX_SURVIVOR_COUNT];
	private UseItemButton useItemButton = new UseItemButton();
	private NCSLabel labelFPS = new NCSLabel(new NumCharSequence("FPS: ", null));

	private Panel panelHowToPlayMessage = new Panel();
	private Label[] howToPlayMessageLabels = new Label[]{new Label(""), new Label(""), new Label(""), new Label(""), new Label(""), new Label(""), new Label(""), new Label("")};
	private HowToPlayNextButton howToPlayNextButton = new HowToPlayNextButton();

	public InGameGUIModule() {
		for (int i = 0; i < survivorButtons.length; i++) {
			SelectSurvivorButton button = new SelectSurvivorButton(i);
			survivorButtons[i] = button;
			inGameItems.add(button);
		}
		inGameItems.add(useItemButton);

		labelFPS.Visible = false;
		inGameItems.add(labelFPS);

		panelHowToPlayMessage.Color.set(0, 0, 0.5f, 0.5f);
		inGameItems.add(panelHowToPlayMessage);

		howToPlayNextButton.NormalColor.set(0, 0.75f, 0, 0.5f);
		inGameItems.add(howToPlayNextButton);

		Collections.addAll(inGameItems, howToPlayMessageLabels);

		inGameItems = Collections.unmodifiableList(inGameItems);
	}

	@Override
	public void update(int screenWidth, int screenHeight, GLTextCache textCache) {
		GameModelInterface model = game.getModelInterface();
		float x = 0;
		float y = 0;
		float h = Math.min(screenWidth, screenHeight) * 0.1f;
		for (SelectSurvivorButton button : survivorButtons) {
			if (model.getSurvivorCount() <= button.getSurvivorIndex()) {
				button.Visible = false;
				continue;
			}
			button.Visible = true;
			button.setIsAlert(DTimer.get().millis() % 500 < 250
					&& model.isSurivorAlert(button.getSurvivorIndex()));
			button.setSurvivorHealth(model.getSurvivorHealth(button.getSurvivorIndex()));
			float w = h * 1.5f;
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
			panelHowToPlayMessage.Visible = true;

			HowToPlayMessage message = model.getHowToPlayMessage();

			y = screenHeight - 20;
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
				label.Bounds.set(x, y, w, h);
			}
			for (; m < howToPlayMessageLabels.length; m++) {
				howToPlayMessageLabels[m].Visible = false;
			}

			w = howToPlayNextButton.getGLText(textCache).getLength(howToPlayNextButton.Text) * 1.5f;
			h = howToPlayNextButton.getGLText(textCache).getHeight() * 1.5f;
			y -= h + 20;
			howToPlayNextButton.Bounds.set(x, y, w, h);

			y -= 20;
			panelHowToPlayMessage.Bounds.set(0, y, maxWidth + 40, screenHeight - y);
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
		return inGameItems;
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
