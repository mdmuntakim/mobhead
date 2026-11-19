package com.mdmuntakim.mobhead;

import net.kyori.adventure.text.format.NamedTextColor;

/**
 * Difficulty and rarity tier of a mob. The tier decides the base head drop
 * chance and how much max health, armor and knockback resistance a worn head
 * grants. Explicit ability definitions may override the attribute bonuses.
 */
public enum Tier {
    NONE("No ability", NamedTextColor.DARK_GRAY, 0.02, 0, 0, 0.0),
    COMMON("Common", NamedTextColor.WHITE, 0.05, 2, 0, 0.0),
    UNCOMMON("Uncommon", NamedTextColor.GREEN, 0.10, 4, 1, 0.0),
    RARE("Rare", NamedTextColor.AQUA, 0.25, 6, 2, 0.0),
    EPIC("Epic", NamedTextColor.LIGHT_PURPLE, 0.50, 10, 3, 0.25),
    LEGENDARY("Legendary", NamedTextColor.GOLD, 1.00, 16, 5, 0.50);

    private final String label;
    private final NamedTextColor color;
    private final double baseDropChance;
    private final double healthBonus;
    private final double armorBonus;
    private final double knockbackResistance;

    Tier(String label, NamedTextColor color, double baseDropChance,
         double healthBonus, double armorBonus, double knockbackResistance) {
        this.label = label;
        this.color = color;
        this.baseDropChance = baseDropChance;
        this.healthBonus = healthBonus;
        this.armorBonus = armorBonus;
        this.knockbackResistance = knockbackResistance;
    }

    public String label() {
        return label;
    }

    public NamedTextColor color() {
        return color;
    }

    public double baseDropChance() {
        return baseDropChance;
    }

    public double healthBonus() {
        return healthBonus;
    }

    public double armorBonus() {
        return armorBonus;
    }

    public double knockbackResistance() {
        return knockbackResistance;
    }

    public boolean hasAbility() {
        return this != NONE;
    }
}
