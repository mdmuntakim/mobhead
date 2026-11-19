package com.mdmuntakim.mobhead;

import org.bukkit.potion.PotionEffectType;

/**
 * What a worn head does to a mob or player that damages its wearer.
 */
public enum Retaliation {
    NONE(null, 0, 0),
    FIRE(null, 0, 0),
    POISON("poison", 100, 0),
    WITHER("wither", 100, 0),
    SLOWNESS("slowness", 100, 1),
    LEVITATION("levitation", 60, 0);

    private final String effectId;
    private final int duration;
    private final int amplifier;

    Retaliation(String effectId, int duration, int amplifier) {
        this.effectId = effectId;
        this.duration = duration;
        this.amplifier = amplifier;
    }

    public PotionEffectType effect() {
        return effectId == null ? null : PotionEffects.resolve(effectId);
    }

    public int duration() {
        return duration;
    }

    public int amplifier() {
        return amplifier;
    }
}
