package com.mdmuntakim.mobhead;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class TierTest {

    @Test
    void tiersScaleUp() {
        Tier[] tiers = Tier.values();
        for (int i = 1; i < tiers.length; i++) {
            assertTrue(tiers[i].baseDropChance() > tiers[i - 1].baseDropChance(),
                    tiers[i] + " should drop more often than " + tiers[i - 1]);
            assertTrue(tiers[i].healthBonus() > tiers[i - 1].healthBonus(),
                    tiers[i] + " should give more health than " + tiers[i - 1]);
            assertTrue(tiers[i].armorBonus() >= tiers[i - 1].armorBonus(),
                    tiers[i] + " should not give less armor than " + tiers[i - 1]);
        }
        assertEquals(0.02, Tier.NONE.baseDropChance(), 1.0E-9);
        assertEquals(1.00, Tier.LEGENDARY.baseDropChance(), 1.0E-9);
    }

    @Test
    void legendaryBeatsEpic() {
        Ability legendary = Ability.of(Tier.LEGENDARY);
        Ability epic = Ability.of(Tier.EPIC);
        assertTrue(legendary.healthBonus() > epic.healthBonus());
        assertTrue(legendary.armorBonus() > epic.armorBonus());
        assertTrue(legendary.knockbackResistance() > epic.knockbackResistance());
    }
}
