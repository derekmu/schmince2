package com.schmince.game.model.sprites;

import android.opengl.Matrix;
import dopengl.DRenderer;
import dopengl.shapes.GLRectangle;
import thed.DTimer;

/**
 * @author Derek Mulvihill - Mar 16, 2014
 */
public class MedkitSprite implements Sprite {
	private boolean first = true;

	private long startMillis;
	private int x;
	private int y;

	public MedkitSprite(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void drawSprite(DRenderer render) {
		if (first) {
			first = false;
			startMillis = DTimer.get().millis();
		}
		long now = DTimer.get().millis();

		float tx = (float) (Math.sin((now - startMillis) / 100.0) / 5);
		float ty = (now - startMillis) / 2000f;

		GLRectangle rect = render.getGlib().getRectangle();

		//draw vertical bars
		rect.setBounds(-0.06f, -0.17f, 0.12f, 0.34f);

		float[] vpMatrix = render.getVPMatrix();
		Matrix.translateM(vpMatrix, 0, x + tx, y + ty, 0);
		Matrix.translateM(vpMatrix, 0, 0.3f, 0.2f, 0);
		rect.draw(vpMatrix, 1f, 0f, 0f, 0.75f);

		vpMatrix = render.getVPMatrix();
		Matrix.translateM(vpMatrix, 0, x + tx, y + ty, 0);
		Matrix.translateM(vpMatrix, 0, -0.4f, 0.05f, 0);
		rect.draw(vpMatrix, 1f, 0f, 0f, 0.75f);

		vpMatrix = render.getVPMatrix();
		Matrix.translateM(vpMatrix, 0, x + tx, y + ty, 0);
		Matrix.translateM(vpMatrix, 0, 0.1f, -0.4f, 0);
		rect.draw(vpMatrix, 1f, 0f, 0f, 0.75f);

		//draw horizontal bars
		rect.setBounds(-0.17f, -0.06f, 0.34f, 0.12f);

		vpMatrix = render.getVPMatrix();
		Matrix.translateM(vpMatrix, 0, x + tx, y + ty, 0);
		Matrix.translateM(vpMatrix, 0, 0.3f, 0.2f, 0);
		rect.draw(vpMatrix, 1f, 0f, 0f, 0.75f);

		vpMatrix = render.getVPMatrix();
		Matrix.translateM(vpMatrix, 0, x + tx, y + ty, 0);
		Matrix.translateM(vpMatrix, 0, -0.4f, 0.05f, 0);
		rect.draw(vpMatrix, 1f, 0f, 0f, 0.75f);

		vpMatrix = render.getVPMatrix();
		Matrix.translateM(vpMatrix, 0, x + tx, y + ty, 0);
		Matrix.translateM(vpMatrix, 0, 0.1f, -0.4f, 0);
		rect.draw(vpMatrix, 1f, 0f, 0f, 0.75f);
	}

	@Override
	public boolean isExpired() {
		return DTimer.get().millis() > startMillis + 750;
	}
}