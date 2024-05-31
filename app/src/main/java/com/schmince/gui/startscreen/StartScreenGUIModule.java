package com.schmince.gui.startscreen;

import android.app.Activity;
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
	private Panel panelDark = new Panel();
	private Icon titleIcon = new Icon(GLIconType.Title);
	private NewGameButton newGameButton = new NewGameButton();
	private HowToPlayButton howToPlayButton = new HowToPlayButton();
	private QuitButton quitButton = new QuitButton();

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

		quitButton.TextScale = 1.5f;
		quitButton.NormalColor.set(1f, 0f, 0f, 0.5f);
		startScreenItems.add(quitButton);

		startScreenItems = Collections.unmodifiableList(startScreenItems);
	}

	@Override
	public void update(int width, int height, GLTextCache textCache) {
		panelDark.Bounds.set(0, 0, width, height);
		titleIcon.Bounds.set(width * 0.05f, height / 2f, width - width * 0.1f, height / 2f);
		if (width > height) {
			newGameButton.Bounds.set(10, 10, width / 2f - 20, height / 2f - 20);
			howToPlayButton.Bounds.set(width / 2f + 10, height / 4f + 10, width / 2f - 20, height / 4f - 20);
			quitButton.Bounds.set(width / 2f + 10, 10, width / 2f - 20, height / 4f - 20);
		} else {
			newGameButton.Bounds.set(10, height / 4f + 10, width - 20, height / 4f - 20);
			howToPlayButton.Bounds.set(10, height / 8f + 10, width - 20, height / 8f - 20);
			quitButton.Bounds.set(10, 10, width - 20, height / 8f - 20);
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
