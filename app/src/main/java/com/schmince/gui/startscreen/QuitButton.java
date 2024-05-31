package com.schmince.gui.startscreen;

import android.app.Activity;
import dgui.button.Button;

/**
 * Button on start screen to quit out of the game.
 *
 * @author Derek Mulvihill - Jan 25, 2014
 */
public class QuitButton extends Button {
	private Activity activity;

	public QuitButton() {
		super("Quit Game");
	}

	@Override
	public void doAction() {
		activity.finish();
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}
}
