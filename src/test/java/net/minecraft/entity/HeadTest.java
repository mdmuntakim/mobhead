package net.minecraft.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class HeadTest {

    @Test
    void containsEveryMobFromTheCatalogue() {
        assertEquals(90, Head.size());
        assertEquals(90, Head.all().size());
    }

    @Test
    void everyEntryHasAnIdANameAndATexture() {
        Set<String> ids = new HashSet<>();
        Set<String> names = new HashSet<>();
        for (Head.Entry entry : Head.all()) {
            assertFalse(entry.id().isBlank());
            assertFalse(entry.name().isBlank());
            assertTrue(entry.hasTexture(), entry.id() + " has no texture");
            assertEquals("minecraft:" + entry.id(), entry.namespacedId());
            assertTrue(ids.add(entry.id()), "duplicate id " + entry.id());
            assertTrue(names.add(entry.name()), "duplicate name " + entry.name());
        }
    }

    @Test
    void sixHeadsUseTheirVanillaSkullItem() {
        assertEquals(6, Head.vanillaHeads().size());
        assertEquals("ZOMBIE_HEAD", Head.require("zombie").headItem());
        assertEquals("DRAGON_HEAD", Head.require("ender_dragon").headItem());
        assertFalse(Head.require("cow").vanilla());
        assertNull(Head.require("cow").headItem());
    }

    @Test
    void zombieCarriesItsPotionValues() {
        Head.Entry zombie = Head.require("zombie");
        assertEquals("Zombie", zombie.name());
        assertEquals("humanoid", zombie.category());
        assertEquals("COMMON", zombie.tier());
        assertEquals(List.of(new Head.Potion("strength", 0), new Head.Potion("resistance", 0)),
                zombie.potions());
        assertTrue(zombie.abilities().isEmpty());
    }

    @Test
    void dragonCarriesItsExternalAbilities() {
        Head.Entry dragon = Head.require("ender_dragon");
        assertEquals("LEGENDARY", dragon.tier());
        assertEquals(List.of("health:24", "armor:8", "knockback_resistance:0.8"),
                dragon.abilities());
        assertEquals("Strength III", dragon.potions().get(0).describe());
    }

    @Test
    void eventDrivenExtrasArePartOfTheTable() {
        assertTrue(Head.require("creeper").abilities().contains("explosion_immune"));
        assertTrue(Head.require("snow_golem").abilities().contains("freeze_immune"));
        assertTrue(Head.require("enderman").abilities().contains("teleport_on_hit"));
        assertTrue(Head.require("wither").abilities().contains("retaliation:wither"));
    }

    @Test
    void lookupAcceptsKeysNamespacedIdsAndDisplayNames() {
        assertEquals("zombie", Head.of("Zombie").id());
        assertEquals("zombie", Head.of(" minecraft:zombie ").id());
        assertEquals("iron_golem", Head.byName("iron golem").id());
        assertNull(Head.of("not_a_mob"));
        assertNull(Head.byName(""));
        assertThrows(IllegalArgumentException.class, () -> Head.require("not_a_mob"));
    }

    @Test
    void generatedMobClassesResolveTheSameEntries() {
        assertEquals(Head.require("zombie"), ZombieHead.entry());
        assertEquals(Head.require("camel_husk"), CamelHuskHead.entry());
        assertEquals(Head.require("sulfur_cube"), SulfurCubeHead.entry());
        assertEquals(Head.require("trader_llama"), TraderLlamaHead.entry());
        assertEquals("sulfur_cube", SulfurCubeHead.ID);
        assertNotNull(WitherHead.entry());
    }

    @Test
    void potionDescriptionUsesRomanLevels() {
        assertEquals("Speed I", new Head.Potion("speed", 0).describe());
        assertEquals("Strength III", new Head.Potion("strength", 2).describe());
        assertEquals("Regeneration II", new Head.Potion("regeneration", 1).describe());
        assertThrows(IllegalArgumentException.class, () -> new Head.Potion("", 0));
        assertThrows(IllegalArgumentException.class, () -> new Head.Potion("speed", -1));
    }
}
