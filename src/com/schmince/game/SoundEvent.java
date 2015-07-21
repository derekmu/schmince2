package com.schmince.game;

/**
 * @author Derek Mulvihill - Feb 7, 2014
 */
public class SoundEvent {
    public final SoundEventType EventType;
    public float LeftVolume = 1f;
    public float RightVolume = 1f;

    public SoundEvent(SoundEventType eventType) {
        this.EventType = eventType;
    }

    public SoundEvent(SoundEventType eventType, float volume) {
        this.EventType = eventType;
        this.LeftVolume = this.RightVolume = volume;
    }
}
