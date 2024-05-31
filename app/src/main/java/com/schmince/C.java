package com.schmince;

import thed.DColor;

/**
 * Constants for Schmince.
 *
 * @author Derek Mulvihill - Jan 9, 2014
 */
public class C {
	public static final float BASE_TILE_COLOR = 0.2f;
	public static final int MAX_MAP_SIZE = 250;
	public static final int MAX_PLAYER_HEALTH = 10;
	public static final int SHIP_SIZE = 3;
	private static final DColor RED = new DColor(1, 0, 0);
	private static final DColor GREEN = new DColor(0, 1, 0);
	private static final DColor BLUE = new DColor(0, 0, 1);
	private static final DColor MAGENTA = new DColor(1, 0, 1);
	private static final DColor YELLOW = new DColor(1, 1, 0);
	private static final DColor TURQUOISE = new DColor(0, 1, 1);
	private static final DColor LIGHTRED = new DColor(1, 0.5f, 0.5f);
	private static final DColor LIGHTGREEN = new DColor(0.5f, 1, 0.5f);
	private static final DColor LIGHTBLUE = new DColor(0.5f, 0.5f, 1);
	private static final DColor DARKRED = new DColor(0.5f, 0.25f, 0.25f);
	private static final DColor DARKGREEN = new DColor(0.25f, 0.5f, 0.25f);
	private static final DColor DARKBLUE = new DColor(0.25f, 0.25f, 0.5f);
	public static final DColor[] PLAYER_COLORS = {RED, GREEN, BLUE, MAGENTA, YELLOW, TURQUOISE,
			LIGHTRED, LIGHTGREEN, LIGHTBLUE, DARKRED, DARKGREEN, DARKBLUE};
	public static final int MAX_PLAYER_COUNT = PLAYER_COLORS.length;
}
