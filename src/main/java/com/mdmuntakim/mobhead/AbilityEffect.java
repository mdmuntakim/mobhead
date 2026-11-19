package com.mdmuntakim.mobhead;

import org.bukkit.potion.PotionEffectType;

/**
 * One potion effect granted while a head is worn. The effect is stored as its
 * registry id so abilities can be built and tested without a running server.
 */
public record AbilityEffect(String effect, int amplifier) {

    public AbilityEffect {
        if (effect == null || effect.isBlank()) {
            throw new IllegalArgumentException("effect id must not be blank");
        }
        if (amplifier < 0) {
            throw new IllegalArgumentException("amplifier must not be negative");
        }
    }

    public PotionEffectType type() {
        return PotionEffects.resolve(effect);
    }

    public String displayName() {
        return PotionEffects.pretty(effect);
    }

    public String romanAmplifier() {
        return roman(amplifier + 1);
    }

    public String describe() {
        return displayName() + " " + romanAmplifier();
    }

    static String roman(int value) {
        return switch (value) {
            case 1 -> "I";
            case 2 -> "II";
            case 3 -> "III";
            case 4 -> "IV";
            case 5 -> "V";
            case 6 -> "VI";
            case 7 -> "VII";
            case 8 -> "VIII";
            case 9 -> "IX";
            case 10 -> "X";
            default -> Integer.toString(value);
        };
    }
}
