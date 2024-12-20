package com.schmince.game;

/**
 * @author Derek Mulvihill - Feb 7, 2014
 */
public enum SoundEventType {
	EnemyMoved("enemyMoved.wav"),
	GunShot("gunShot.wav"),
	Medkit("medkit.wav"),
	Radar("radar.wav"),
	Flare("flare.wav"),
	Error("error.wav"),
	Hit("hit.wav"),
	ArmorHit("hitArmor.wav"),
	SurvivorMoved("survivorMoved.wav"),
	;

	private final String fileName;

	SoundEventType(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}
}
