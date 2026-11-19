package com.mdmuntakim.mobhead;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.EntityType;

/**
 * Discovers every living entity the server has and turns it into a head.
 * Discovery is registry driven, so a mob added by a future Paper build still
 * gets a head even before a texture and a hand written ability exist for it.
 */
public final class MobHeadRegistry {

    private static final Map<EntityType, Material> VANILLA_HEADS = new EnumMap<>(EntityType.class);

    static {
        VANILLA_HEADS.put(EntityType.CREEPER, Material.CREEPER_HEAD);
        VANILLA_HEADS.put(EntityType.SKELETON, Material.SKELETON_SKULL);
        VANILLA_HEADS.put(EntityType.WITHER_SKELETON, Material.WITHER_SKELETON_SKULL);
        VANILLA_HEADS.put(EntityType.ZOMBIE, Material.ZOMBIE_HEAD);
        VANILLA_HEADS.put(EntityType.PIGLIN, Material.PIGLIN_HEAD);
        VANILLA_HEADS.put(EntityType.ENDER_DRAGON, Material.DRAGON_HEAD);
    }

    private static final Map<EntityType, String> DISPLAY_NAMES = new EnumMap<>(EntityType.class);

    static {
        DISPLAY_NAMES.put(EntityType.MOOSHROOM, "Mooshroom");
        DISPLAY_NAMES.put(EntityType.SNOW_GOLEM, "Snow Golem");
        DISPLAY_NAMES.put(EntityType.TRADER_LLAMA, "Trader Llama");
        DISPLAY_NAMES.put(EntityType.WITHER, "Wither");
        DISPLAY_NAMES.put(EntityType.ENDER_DRAGON, "Ender Dragon");
        DISPLAY_NAMES.put(EntityType.ZOMBIFIED_PIGLIN, "Zombified Piglin");
        DISPLAY_NAMES.put(EntityType.PIGLIN_BRUTE, "Piglin Brute");
        DISPLAY_NAMES.put(EntityType.MAGMA_CUBE, "Magma Cube");
        DISPLAY_NAMES.put(EntityType.CAVE_SPIDER, "Cave Spider");
        DISPLAY_NAMES.put(EntityType.ELDER_GUARDIAN, "Elder Guardian");
        DISPLAY_NAMES.put(EntityType.WITHER_SKELETON, "Wither Skeleton");
        DISPLAY_NAMES.put(EntityType.SKELETON_HORSE, "Skeleton Horse");
        DISPLAY_NAMES.put(EntityType.ZOMBIE_HORSE, "Zombie Horse");
        DISPLAY_NAMES.put(EntityType.ZOMBIE_VILLAGER, "Zombie Villager");
        DISPLAY_NAMES.put(EntityType.WANDERING_TRADER, "Wandering Trader");
        DISPLAY_NAMES.put(EntityType.TROPICAL_FISH, "Tropical Fish");
        DISPLAY_NAMES.put(EntityType.GLOW_SQUID, "Glow Squid");
        DISPLAY_NAMES.put(EntityType.HAPPY_GHAST, "Happy Ghast");
        DISPLAY_NAMES.put(EntityType.CAMEL_HUSK, "Camel Husk");
        DISPLAY_NAMES.put(EntityType.ZOMBIE_NAUTILUS, "Zombie Nautilus");
        DISPLAY_NAMES.put(EntityType.SULFUR_CUBE, "Sulfur Cube");
        DISPLAY_NAMES.put(EntityType.COPPER_GOLEM, "Copper Golem");
        DISPLAY_NAMES.put(EntityType.PARCHED, "Parched");
        DISPLAY_NAMES.put(EntityType.NAUTILUS, "Nautilus");
        DISPLAY_NAMES.put(EntityType.CREAKING, "Creaking");
        DISPLAY_NAMES.put(EntityType.ARMADILLO, "Armadillo");
    }

    /**
     * Entities that are alive in the API but are not mobs a survival player
     * meets and kills (technical entities, the removed giant and the player
     * itself).
     */
    private static final List<EntityType> EXCLUDED = List.of(
            EntityType.UNKNOWN,
            EntityType.PLAYER,
            EntityType.ARMOR_STAND,
            EntityType.GIANT);

    private final MobHeadPlugin plugin;
    private final Map<String, MobHead> byKey = new LinkedHashMap<>();
    private final Map<EntityType, MobHead> byType = new EnumMap<>(EntityType.class);
    private HeadCatalog catalog = HeadCatalog.empty();
    private List<String> missingTextures = List.of();

    public MobHeadRegistry(MobHeadPlugin plugin) {
        this.plugin = plugin;
    }

    public void reload() {
        this.catalog = HeadCatalog.load(plugin);
        byKey.clear();
        byType.clear();
        List<String> missing = new ArrayList<>();

        for (EntityType type : Registry.ENTITY_TYPE) {
            if (type == null || EXCLUDED.contains(type) || !type.isAlive() || type.getEntityClass() == null) {
                continue;
            }
            String key = type.getKey().getKey();
            Ability explicit = AbilityTable.get(type);
            Tier tier = explicit != null ? explicit.tier() : autoTier(type);
            Ability ability = explicit != null ? explicit : Ability.of(tier);
            Material material = VANILLA_HEADS.getOrDefault(type, Material.PLAYER_HEAD);
            String texture = null;
            if (material == Material.PLAYER_HEAD) {
                HeadCatalog.Texture entry = catalog.get(key);
                if (entry != null) {
                    texture = entry.value();
                } else {
                    missing.add(key);
                }
            }
            MobHead head = new MobHead(type, key, displayName(type), tier, ability,
                    material, texture, tier.baseDropChance());
            byKey.put(key, head);
            byType.put(type, head);
        }
        missingTextures = List.copyOf(missing);
    }

    public HeadCatalog catalog() {
        return catalog;
    }

    public MobHead byKey(String input) {
        if (input == null) {
            return null;
        }
        String normalized = input.trim().toLowerCase(Locale.ROOT)
                .replace(' ', '_')
                .replace("minecraft:", "");
        MobHead direct = byKey.get(normalized);
        if (direct != null) {
            return direct;
        }
        try {
            return byType.get(EntityType.valueOf(normalized.toUpperCase(Locale.ROOT)));
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    public MobHead byType(EntityType type) {
        return byType.get(type);
    }

    public List<MobHead> all() {
        return List.copyOf(byKey.values());
    }

    public int size() {
        return byKey.size();
    }

    public List<String> missingTextures() {
        return missingTextures;
    }

    public long withEffects() {
        return byKey.values().stream().filter(head -> head.ability().hasEffects()).count();
    }

    public long withoutAbility() {
        return byKey.values().stream().filter(head -> !head.hasAnyAbility()).count();
    }

    private static String displayName(EntityType type) {
        String known = DISPLAY_NAMES.get(type);
        if (known != null) {
            return known;
        }
        String[] parts = type.getKey().getKey().split("_");
        StringBuilder out = new StringBuilder();
        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }
            if (!out.isEmpty()) {
                out.append(' ');
            }
            out.append(part.substring(0, 1).toUpperCase(Locale.ROOT)).append(part.substring(1));
        }
        return out.toString();
    }

    /**
     * Fallback tier for a mob that has no hand written entry: derived from its
     * default max health and attack damage, so it still scales with how hard
     * the mob is to defeat.
     */
    static Tier autoTier(EntityType type) {
        double health = 0;
        double attack = 0;
        try {
            Attributable attributes = type.getDefaultAttributes();
            if (attributes != null) {
                AttributeInstance maxHealth = attributes.getAttribute(Attribute.MAX_HEALTH);
                if (maxHealth != null) {
                    health = maxHealth.getDefaultValue();
                }
                AttributeInstance damage = attributes.getAttribute(Attribute.ATTACK_DAMAGE);
                if (damage != null) {
                    attack = damage.getDefaultValue();
                }
            }
        } catch (RuntimeException ignored) {
            // Some entities have no default attributes; the default tier is fine.
        }
        double score = health + attack * 5.0;
        if (score >= 80) {
            return Tier.LEGENDARY;
        }
        if (score >= 40) {
            return Tier.EPIC;
        }
        if (score >= 20) {
            return Tier.RARE;
        }
        if (score >= 10) {
            return Tier.UNCOMMON;
        }
        if (score >= 4) {
            return Tier.COMMON;
        }
        return Tier.NONE;
    }

    public static Material vanillaHead(EntityType type) {
        return VANILLA_HEADS.get(type);
    }

    public static Map<EntityType, Material> vanillaHeads() {
        return Collections.unmodifiableMap(VANILLA_HEADS);
    }
}
