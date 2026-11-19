package com.mdmuntakim.mobhead;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.minecraft.entity.Head;
import org.bukkit.entity.EntityType;
import org.junit.jupiter.api.Test;

/**
 * Guards the generated {@code net.minecraft.entity.Head} table against drift:
 * the plugin still builds its runtime abilities from {@link AbilityTable}, so
 * the two have to agree on tier, potion effects and event driven extras.
 */
class HeadTableConsistencyTest {

    @Test
    void generatedTableMatchesTheAbilityTable() {
        int checked = 0;
        for (Head.Entry entry : Head.all()) {
            EntityType type = EntityType.valueOf(entry.id().toUpperCase(Locale.ROOT));
            assertNotNull(type, entry.id());
            Ability ability = AbilityTable.get(type);

            if (ability == null) {
                assertTrue(entry.potions().isEmpty(), entry.id() + " should not have potions");
                assertTrue(entry.abilities().isEmpty(), entry.id() + " should not have abilities");
                assertEquals("NONE", entry.tier(), entry.id() + " should fall back to NONE");
                continue;
            }

            assertEquals(ability.tier().name(), entry.tier(), entry.id());

            List<String> expectedPotions = new ArrayList<>();
            for (AbilityEffect effect : ability.effects()) {
                expectedPotions.add(effect.effect() + ":" + effect.amplifier());
            }
            List<String> actualPotions = entry.potions().stream()
                    .map(potion -> potion.id() + ":" + potion.amplifier())
                    .toList();
            assertEquals(expectedPotions, actualPotions, entry.id() + " potions");

            List<String> externals = new ArrayList<>();
            if (ability.retaliation() != Retaliation.NONE) {
                externals.add("retaliation:" + ability.retaliation().name().toLowerCase(Locale.ROOT));
            }
            if (ability.explosionImmune()) {
                externals.add("explosion_immune");
            }
            if (ability.freezeImmune()) {
                externals.add("freeze_immune");
            }
            if (ability.teleportOnHit()) {
                externals.add("teleport_on_hit");
            }
            for (String external : externals) {
                assertTrue(entry.abilities().contains(external),
                        entry.id() + " is missing " + external);
            }
            for (String external : entry.abilities()) {
                if (external.startsWith("health:")) {
                    assertEquals(ability.healthBonus(), Double.parseDouble(external.substring(7)), 1.0e-9, entry.id());
                } else if (external.startsWith("armor:")) {
                    assertEquals(ability.armorBonus(), Double.parseDouble(external.substring(6)), 1.0e-9, entry.id());
                } else if (external.startsWith("knockback_resistance:")) {
                    assertEquals(ability.knockbackResistance(),
                            Double.parseDouble(external.substring(21)), 1.0e-9, entry.id());
                } else {
                    assertTrue(externals.contains(external), entry.id() + " has stale " + external);
                }
            }
            checked++;
        }
        assertEquals(Head.size() - 2, checked, "cod and pufferfish have no explicit ability");
        assertFalse(Head.withAbility().isEmpty());
    }
}
