package com.schmince.gui.ingame;

import com.schmince.C;
import com.schmince.game.SchminceGame;
import com.schmince.gldraw.GLIconType;
import com.schmince.gldraw.GLPlayer;
import dgui.GUIItem;
import dgui.GUIRenderer;
import dgui.button.Button;
import dgui.button.ButtonDrawer;
import dopengl.shapes.GLRectangle;
import texample.GLTextType;
import thed.*;

/**
 * Button in game to select which player is focused on.
 *
 * @author Derek Mulvihill - Jan 15, 2014
 */
public class SelectPlayerButton extends Button {
	private int playerIndex;
	private SchminceGame game;
	private boolean isAlert = false;
	private int playerHealth = 10;
	private long drawSeed = DRandom.get().nextInt(10000);
	private GLColor glColor;

	public SelectPlayerButton(int playerIndex) {
		super("", new SelectPlayerButtonDrawer());
		super.TextType = GLTextType.SansBold;
		super.TextColor.set(0f, 0f, 0f, 1f);
		super.BackgroundIcon = GLIconType.Player;
		super.BackgroundAspectRatio = 1f;
		this.playerIndex = playerIndex;
		DColor color = C.PLAYER_COLORS[playerIndex];
		this.glColor = new GLColor(color.Red, color.Green, color.Blue);
	}

	public int getPlayerIndex() {
		return playerIndex;
	}

	@Override
	public void doAction() {
		game.onSelectPlayer(playerIndex);
	}

	public void setGame(SchminceGame game) {
		this.game = game;
	}

	public void setIsAlert(boolean isAlert) {
		this.isAlert = isAlert;
	}

	public void setPlayerHealth(int playerHealth) {
		this.playerHealth = playerHealth;
	}

	private static class SelectPlayerButtonDrawer extends ButtonDrawer {
		private DColor tempColor = new DColor();

		@Override
		public void draw(GUIItem item, GUIRenderer render) {
			SelectPlayerButton button = (SelectPlayerButton) item;
			Rectangle bounds = button.Bounds;
			GLRectangle rect = render.getGlib().getRectangle();
			rect.setBounds(bounds.x, bounds.y, bounds.w, bounds.h);
			DColor color;
			if (button.HasFocus) {
				color = button.FocusColor;
			} else {
				color = tempColor;
				float rgb = 0.8f * button.playerHealth / C.MAX_PLAYER_HEALTH;
				color.set(rgb, rgb, rgb);
			}
			rect.draw(render.getVPOrthoMatrix(), color.Red, color.Green, color.Blue, color.Alpha);

			if (!button.isAlert && button.BackgroundIcon != null) {
				GLPlayer glPlayer = (GLPlayer) render.getGlib().getDrawer(button.BackgroundIcon);
				long seed = button.drawSeed;
				if (button.playerHealth > 0) {
					seed += DTimer.get().millis();
				}
				glPlayer.draw(getBackgroundVPMatrix(render, button), seed, button.glColor);
			}
		}
	}

}
