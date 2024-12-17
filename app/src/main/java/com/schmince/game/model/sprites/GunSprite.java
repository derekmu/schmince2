package com.schmince.game.model.sprites;

import android.opengl.Matrix;
import dopengl.DRenderer;
import dopengl.shapes.GLTriangle;
import thed.DTimer;
import thed.DUtil;
import thed.GLColor;

/**
 * @author Derek Mulvihill - Mar 16, 2014
 */
public class GunSprite implements Sprite {
	private static final float[] vertices = DUtil.getScaledEqualTriangle(0.15f);
	private static final GLColor color = new GLColor(0.8f, 0.7f, 0.5f);
	boolean first = true;
	long startMillis;
	double durationMillis;
	int sourceX;
	int sourceY;
	int targetX;
	int targetY;

	public GunSprite(int sourceX, int sourceY, int targetX, int targetY) {
		this.sourceX = sourceX;
		this.sourceY = sourceY;
		this.targetX = targetX;
		this.targetY = targetY;
	}

	@Override
	public void drawSprite(DRenderer render) {
		if (first) {
			first = false;
			startMillis = DTimer.get().millis();
			int dx = targetX - sourceX;
			int dy = targetY - sourceY;
			double dist = Math.sqrt(dx * dx + dy * dy);
			durationMillis = dist * 50;
		}
		long dt = DTimer.get().millis() - startMillis;
		float x = dt > durationMillis ? targetX : (float) (sourceX + (targetX - sourceX) * dt / durationMillis);
		float y = dt > durationMillis ? targetY : (float) (sourceY + (targetY - sourceY) * dt / durationMillis);
		GLTriangle triangle = render.getGlib().getTriangle();
		triangle.setVertices(vertices);
		triangle.setColor(color);
		float[] vpMatrix = render.getVPMatrix();
		Matrix.translateM(vpMatrix, 0, x, y, 0);
		triangle.draw(vpMatrix);
	}

	@Override
	public boolean isExpired() {
		return DTimer.get().millis() >= startMillis + durationMillis;
	}
}
