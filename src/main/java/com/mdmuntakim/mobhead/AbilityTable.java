package com.mdmuntakim.mobhead;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import org.bukkit.entity.EntityType;

/**
 * Curated ability profiles. Every mob that exists in a survival world has an
 * entry here (or falls back to {@link Ability#of(Tier)}); the tier follows the
 * effort and rarity of defeating the mob, and the effects follow its theme.
 */
public final class AbilityTable {

    private static final Map<EntityType, Ability> DEFINITIONS = new EnumMap<>(EntityType.class);

    private AbilityTable() {
    }

    public static Ability get(EntityType type) {
        return DEFINITIONS.get(type);
    }

    public static Map<EntityType, Ability> all() {
        return Collections.unmodifiableMap(DEFINITIONS);
    }

    private static AbilityEffect fx(String type, int amplifier) {
        return new AbilityEffect(type, amplifier);
    }

    private static Ability simple(Tier tier, String... types) {
        Ability.Builder builder = Ability.builder(tier);
        for (String type : types) {
            builder.effect(fx(type, 0));
        }
        return builder.build();
    }

    private static void define(String entity, Tier tier, Ability ability) {
        try {
            DEFINITIONS.put(EntityType.valueOf(entity), ability);
        } catch (IllegalArgumentException ignored) {
            // The mob does not exist in this server version; the registry
            // simply builds heads from the entities that do exist.
        }
    }

    static {
        define("ALLAY", Tier.UNCOMMON, simple(Tier.UNCOMMON,
                "speed", "slow_falling"));

        define("ARMADILLO", Tier.UNCOMMON, simple(Tier.UNCOMMON,
                "resistance"));

        define("AXOLOTL", Tier.RARE, simple(Tier.RARE,
                "water_breathing",
                "dolphins_grace",
                "regeneration"));

        define("BAT", Tier.COMMON, simple(Tier.COMMON,
                "night_vision"));

        define("BEE", Tier.COMMON, simple(Tier.COMMON,
                "speed", "slow_falling"));

        define("BLAZE", Tier.RARE, Ability.builder(Tier.RARE)
                .effect(fx("fire_resistance", 0))
                .effect(fx("strength", 0))
                .retaliation(Retaliation.FIRE)
                .build());

        define("BOGGED", Tier.UNCOMMON, Ability.builder(Tier.UNCOMMON)
                .effect(fx("resistance", 0))
                .retaliation(Retaliation.POISON)
                .build());

        define("BREEZE", Tier.RARE, simple(Tier.RARE,
                "speed",
                "jump_boost",
                "slow_falling"));

        define("CAMEL", Tier.UNCOMMON, Ability.builder(Tier.UNCOMMON)
                .effect(fx("speed", 1))
                .effect(fx("jump_boost", 1))
                .build());

        define("CAMEL_HUSK", Tier.EPIC, simple(Tier.EPIC,
                "strength",
                "speed",
                "resistance"));

        define("CAT", Tier.COMMON, simple(Tier.COMMON,
                "speed",
                "night_vision",
                "slow_falling"));

        define("CAVE_SPIDER", Tier.UNCOMMON, Ability.builder(Tier.UNCOMMON)
                .effect(fx("speed", 1))
                .effect(fx("night_vision", 0))
                .retaliation(Retaliation.POISON)
                .build());

        define("CHICKEN", Tier.COMMON, simple(Tier.COMMON,
                "slow_falling"));

        define("COPPER_GOLEM", Tier.RARE, simple(Tier.RARE,
                "haste",
                "strength"));

        define("COW", Tier.NONE, Ability.of(Tier.NONE));

        define("CREAKING", Tier.EPIC, simple(Tier.EPIC,
                "resistance",
                "speed",
                "night_vision"));

        define("CREEPER", Tier.UNCOMMON, Ability.builder(Tier.UNCOMMON)
                .effect(fx("resistance", 0))
                .effect(fx("speed", 1))
                .explosionImmune()
                .build());

        define("DOLPHIN", Tier.RARE, simple(Tier.RARE,
                "dolphins_grace",
                "water_breathing",
                "speed"));

        define("DONKEY", Tier.COMMON, simple(Tier.COMMON,
                "speed", "jump_boost"));

        define("DROWNED", Tier.UNCOMMON, simple(Tier.UNCOMMON,
                "water_breathing",
                "dolphins_grace",
                "conduit_power"));

        define("ELDER_GUARDIAN", Tier.EPIC, simple(Tier.EPIC,
                "water_breathing",
                "conduit_power",
                "resistance",
                "night_vision"));

        define("ENDER_DRAGON", Tier.LEGENDARY, Ability.builder(Tier.LEGENDARY)
                .effect(fx("strength", 2))
                .effect(fx("resistance", 2))
                .effect(fx("regeneration", 1))
                .effect(fx("slow_falling", 0))
                .health(24)
                .armor(8)
                .knockbackResistance(0.8)
                .build());

        define("ENDERMAN", Tier.RARE, Ability.builder(Tier.RARE)
                .effect(fx("speed", 2))
                .effect(fx("night_vision", 0))
                .teleportOnHit()
                .build());

        define("ENDERMITE", Tier.UNCOMMON, simple(Tier.UNCOMMON,
                "speed", "haste"));

        define("EVOKER", Tier.EPIC, simple(Tier.EPIC,
                "strength", "resistance"));

        define("FOX", Tier.UNCOMMON, simple(Tier.UNCOMMON,
                "speed",
                "night_vision",
                "slow_falling"));

        define("FROG", Tier.COMMON, simple(Tier.COMMON,
                "jump_boost", "slow_falling"));

        define("GHAST", Tier.RARE, simple(Tier.RARE,
                "fire_resistance", "slow_falling"));

        define("GLOW_SQUID", Tier.COMMON, simple(Tier.COMMON,
                "water_breathing",
                "night_vision"));

        define("GOAT", Tier.UNCOMMON, Ability.builder(Tier.UNCOMMON)
                .effect(fx("jump_boost", 2))
                .effect(fx("speed", 0))
                .build());

        define("GUARDIAN", Tier.RARE, simple(Tier.RARE,
                "water_breathing",
                "conduit_power",
                "resistance"));

        define("HAPPY_GHAST", Tier.RARE, simple(Tier.RARE,
                "slow_falling", "regeneration"));

        define("HOGLIN", Tier.RARE, simple(Tier.RARE,
                "strength", "speed"));

        define("HORSE", Tier.COMMON, simple(Tier.COMMON,
                "speed", "jump_boost"));

        define("HUSK", Tier.UNCOMMON, simple(Tier.UNCOMMON,
                "strength", "resistance"));

        define("ILLUSIONER", Tier.EPIC, simple(Tier.EPIC,
                "speed", "night_vision"));

        define("IRON_GOLEM", Tier.EPIC, Ability.builder(Tier.EPIC)
                .effect(fx("strength", 1))
                .effect(fx("resistance", 0))
                .health(16)
                .armor(6)
                .knockbackResistance(0.75)
                .build());

        define("LLAMA", Tier.COMMON, Ability.builder(Tier.COMMON)
                .effect(fx("speed", 0))
                .effect(fx("jump_boost", 1))
                .build());

        define("MAGMA_CUBE", Tier.UNCOMMON, Ability.builder(Tier.UNCOMMON)
                .effect(fx("fire_resistance", 1))
                .effect(fx("jump_boost", 1))
                .retaliation(Retaliation.FIRE)
                .build());

        define("MANNEQUIN", Tier.NONE, Ability.of(Tier.NONE));

        define("MOOSHROOM", Tier.COMMON, simple(Tier.COMMON,
                "regeneration"));

        define("MULE", Tier.COMMON, simple(Tier.COMMON,
                "speed", "jump_boost"));

        define("NAUTILUS", Tier.RARE, simple(Tier.RARE,
                "water_breathing",
                "dolphins_grace",
                "conduit_power"));

        define("OCELOT", Tier.COMMON, simple(Tier.COMMON,
                "speed",
                "night_vision",
                "slow_falling"));

        define("PANDA", Tier.UNCOMMON, simple(Tier.UNCOMMON,
                "resistance"));

        define("PARCHED", Tier.EPIC, simple(Tier.EPIC,
                "fire_resistance",
                "resistance",
                "strength"));

        define("PARROT", Tier.COMMON, simple(Tier.COMMON,
                "slow_falling", "speed"));

        define("PHANTOM", Tier.RARE, simple(Tier.RARE,
                "slow_falling",
                "night_vision",
                "speed"));

        define("PIG", Tier.NONE, Ability.of(Tier.NONE));

        define("PIGLIN", Tier.UNCOMMON, simple(Tier.UNCOMMON,
                "strength", "fire_resistance"));

        define("PIGLIN_BRUTE", Tier.EPIC, simple(Tier.EPIC,
                "strength", "resistance"));

        define("PILLAGER", Tier.UNCOMMON, simple(Tier.UNCOMMON,
                "speed", "strength"));

        define("POLAR_BEAR", Tier.RARE, simple(Tier.RARE,
                "strength", "resistance"));

        define("RABBIT", Tier.COMMON, Ability.builder(Tier.COMMON)
                .effect(fx("jump_boost", 2))
                .effect(fx("speed", 1))
                .effect(fx("slow_falling", 0))
                .build());

        define("RAVAGER", Tier.EPIC, Ability.builder(Tier.EPIC)
                .effect(fx("strength", 2))
                .effect(fx("resistance", 1))
                .effect(fx("speed", 0))
                .health(14)
                .armor(4)
                .knockbackResistance(0.5)
                .build());

        define("SALMON", Tier.NONE, Ability.of(Tier.NONE));

        define("SHEEP", Tier.NONE, Ability.of(Tier.NONE));

        define("SHULKER", Tier.RARE, Ability.builder(Tier.RARE)
                .effect(fx("resistance", 2))
                .effect(fx("slow_falling", 1))
                .build());

        define("SILVERFISH", Tier.UNCOMMON, Ability.builder(Tier.UNCOMMON)
                .effect(fx("speed", 2))
                .effect(fx("haste", 1))
                .build());

        define("SKELETON", Tier.COMMON, simple(Tier.COMMON,
                "strength", "speed"));

        define("SKELETON_HORSE", Tier.UNCOMMON, simple(Tier.UNCOMMON,
                "speed",
                "jump_boost",
                "night_vision"));

        define("SLIME", Tier.COMMON, simple(Tier.COMMON,
                "jump_boost", "slow_falling"));

        define("SNIFFER", Tier.RARE, simple(Tier.RARE,
                "resistance", "regeneration"));

        define("SNOW_GOLEM", Tier.UNCOMMON, Ability.builder(Tier.UNCOMMON)
                .effect(fx("slow_falling", 0))
                .freezeImmune()
                .build());

        define("SPIDER", Tier.UNCOMMON, simple(Tier.UNCOMMON,
                "speed",
                "night_vision",
                "jump_boost"));

        define("SQUID", Tier.COMMON, simple(Tier.COMMON,
                "water_breathing"));

        define("STRAY", Tier.UNCOMMON, Ability.builder(Tier.UNCOMMON)
                .effect(fx("resistance", 0))
                .retaliation(Retaliation.SLOWNESS)
                .build());

        define("STRIDER", Tier.UNCOMMON, simple(Tier.UNCOMMON,
                "fire_resistance", "speed"));

        define("SULFUR_CUBE", Tier.UNCOMMON, simple(Tier.UNCOMMON,
                "fire_resistance", "jump_boost"));

        define("TADPOLE", Tier.NONE, Ability.of(Tier.NONE));

        define("TRADER_LLAMA", Tier.UNCOMMON, Ability.builder(Tier.UNCOMMON)
                .effect(fx("speed", 1))
                .effect(fx("jump_boost", 1))
                .build());

        define("TROPICAL_FISH", Tier.NONE, Ability.of(Tier.NONE));

        define("TURTLE", Tier.RARE, simple(Tier.RARE,
                "water_breathing",
                "resistance",
                "dolphins_grace"));

        define("VEX", Tier.RARE, simple(Tier.RARE,
                "speed", "night_vision"));

        define("VILLAGER", Tier.COMMON, simple(Tier.COMMON,
                "luck", "hero_of_the_village"));

        define("VINDICATOR", Tier.RARE, simple(Tier.RARE,
                "strength", "speed"));

        define("WANDERING_TRADER", Tier.UNCOMMON, simple(Tier.UNCOMMON,
                "speed",
                "luck",
                "hero_of_the_village"));

        define("WARDEN", Tier.LEGENDARY, Ability.builder(Tier.LEGENDARY)
                .effect(fx("strength", 2))
                .effect(fx("resistance", 2))
                .effect(fx("night_vision", 0))
                .health(20)
                .armor(6)
                .knockbackResistance(0.8)
                .build());

        define("WITCH", Tier.RARE, simple(Tier.RARE,
                "regeneration", "resistance"));

        define("WITHER", Tier.LEGENDARY, Ability.builder(Tier.LEGENDARY)
                .effect(fx("strength", 2))
                .effect(fx("resistance", 2))
                .effect(fx("fire_resistance", 0))
                .retaliation(Retaliation.WITHER)
                .health(16)
                .armor(6)
                .knockbackResistance(0.75)
                .build());

        define("WITHER_SKELETON", Tier.RARE, Ability.builder(Tier.RARE)
                .effect(fx("strength", 1))
                .effect(fx("fire_resistance", 0))
                .retaliation(Retaliation.WITHER)
                .build());

        define("WOLF", Tier.COMMON, simple(Tier.COMMON,
                "speed", "strength"));

        define("ZOGLIN", Tier.RARE, simple(Tier.RARE,
                "strength", "speed"));

        define("ZOMBIE", Tier.COMMON, simple(Tier.COMMON,
                "strength", "resistance"));

        define("ZOMBIE_HORSE", Tier.UNCOMMON, simple(Tier.UNCOMMON,
                "speed",
                "jump_boost",
                "night_vision"));

        define("ZOMBIE_NAUTILUS", Tier.RARE, simple(Tier.RARE,
                "water_breathing",
                "conduit_power",
                "strength"));

        define("ZOMBIE_VILLAGER", Tier.COMMON, simple(Tier.COMMON,
                "strength", "resistance"));

        define("ZOMBIFIED_PIGLIN", Tier.UNCOMMON, simple(Tier.UNCOMMON,
                "strength", "fire_resistance"));
    }
}
