package com.schmince.game.model;

import thed.DTimer;

public class Flare {
	public long flareMilli = DTimer.get().millis();
	public int x;
	public int y;

	public Flare(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public boolean isFlared(int targetX, int targetY) {
		int dx = this.x - targetX;
		int dy = this.y - targetY;
		return dx * dx + dy * dy < 15 * 15;
	}
}