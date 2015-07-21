package com.schmince.game;

/**
 * @author Derek Mulvihill - Feb 7, 2014
 */
public enum SoundEventType {
    EnemyMoved("enemyMoved.wav"),
    GunShot("gunShot.wav"),
    MedicKit("medicKit.wav"),
    Locator("locator.wav"),
    Flare("flare.wav"),
    Error("error.wav"),
    Hit("hit.wav"),
    ArmorHit("hitArmor.wav"),
    PlayerMoved("playerMoved.wav"),
    //
    ;

    private String fileName;

    private SoundEventType(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
