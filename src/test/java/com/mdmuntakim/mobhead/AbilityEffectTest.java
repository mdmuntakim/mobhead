package com.mdmuntakim.mobhead;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;

class AbilityEffectTest {

    @Test
    void describesHumanReadableNames() {
        AbilityEffect speed = new AbilityEffect("speed", 1);
        assertEquals("Speed", speed.displayName());
        assertEquals("II", speed.romanAmplifier());
        assertEquals("Speed II", speed.describe());
    }

    @Test
    void romanNumeralsCoverEffectRange() {
        assertEquals("I", AbilityEffect.roman(1));
        assertEquals("IV", AbilityEffect.roman(4));
        assertEquals("X", AbilityEffect.roman(10));
        assertEquals("12", AbilityEffect.roman(12));
    }

    @Test
    void amplifierMustBePositive() {
        assertThrows(IllegalArgumentException.class, () -> new AbilityEffect("speed", -1));
        assertThrows(IllegalArgumentException.class, () -> new AbilityEffect(" ", 0));
    }

    @Test
    void abilityCopiesEffects() {
        Ability ability = Ability.builder(Tier.RARE)
                .effect(new AbilityEffect("speed", 0))
                .build();
        assertEquals(List.of(new AbilityEffect("speed", 0)), ability.effects());
        assertEquals(Tier.RARE.healthBonus(), ability.healthBonus());
    }
}
