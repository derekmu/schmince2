package com.schmince.gui.ingame;

import com.schmince.C;
import com.schmince.game.SchminceGame;
import com.schmince.gldraw.GLIconType;
import com.schmince.gldraw.GLSurvivor;
import dgui.GUIItem;
import dgui.GUIRenderer;
import dgui.button.Button;
import dgui.button.ButtonDrawer;
import dopengl.shapes.GLRectangle;
import texample.GLTextType;
import thed.*;

/**
 * Button in game to select which survivor is focused on.
 *
 * @author Derek Mulvihill - Jan 15, 2014
 */
public class SelectSurvivorButton extends Button {
	private int survivorIndex;
	private SchminceGame game;
	private boolean isAlert = false;
	private int survivorHealth = 10;
	private long drawSeed = DRandom.get().nextInt(10000);
	private GLColor glColor;

	public SelectSurvivorButton(int survivorIndex) {
		super("", new SelectSurvivorButtonDrawer());
		super.TextType = GLTextType.SansBold;
		super.TextColor.set(0f, 0f, 0f, 1f);
		super.BackgroundIcon = GLIconType.Survivor;
		super.BackgroundAspectRatio = 1f;
		this.survivorIndex = survivorIndex;
		DColor color = C.SURVIVOR_COLORS[survivorIndex];
		this.glColor = new GLColor(color.Red, color.Green, color.Blue);
	}

	public int getSurvivorIndex() {
		return survivorIndex;
	}

	@Override
	public void doAction() {
		game.onSelectSurvivor(survivorIndex);
	}

	public void setGame(SchminceGame game) {
		this.game = game;
	}

	public void setIsAlert(boolean isAlert) {
		this.isAlert = isAlert;
	}

	public void setSurvivorHealth(int survivorHealth) {
		this.survivorHealth = survivorHealth;
	}

	private static class SelectSurvivorButtonDrawer extends ButtonDrawer {
		private DColor tempColor = new DColor();

		@Override
		public void draw(GUIItem item, GUIRenderer render) {
			SelectSurvivorButton button = (SelectSurvivorButton) item;
			Rectangle bounds = button.Bounds;
			GLRectangle rect = render.getGlib().getRectangle();
			rect.setBounds(bounds.x, bounds.y, bounds.w, bounds.h);
			DColor color;
			if (button.HasFocus) {
				color = button.FocusColor;
			} else {
				color = tempColor;
				float rgb = 0.8f * button.survivorHealth / C.MAX_SURVIVOR_HEALTH;
				color.set(rgb, rgb, rgb);
			}
			rect.draw(render.getVPOrthoMatrix(), color.Red, color.Green, color.Blue, color.Alpha);

			if (!button.isAlert && button.BackgroundIcon != null) {
				GLSurvivor glSurvivor = (GLSurvivor) render.getGlib().getDrawer(button.BackgroundIcon);
				long seed = button.drawSeed;
				if (button.survivorHealth > 0) {
					seed += DTimer.get().millis();
				}
				glSurvivor.draw(getBackgroundVPMatrix(render, button), seed, button.glColor);
			}
		}
	}

}
