package com.mdmuntakim.mobhead;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

/**
 * One generated head: the entity it belongs to, its tier, the ability that is
 * granted while it is worn and the texture it uses.
 */
public record MobHead(
        EntityType type,
        String key,
        String displayName,
        Tier tier,
        Ability ability,
        Material material,
        String texture,
        double dropChance) {

    public boolean hasCustomTexture() {
        return texture != null && !texture.isBlank();
    }

    public String dropChanceText() {
        return String.format(Locale.ROOT, "%.0f%%", dropChance * 100.0);
    }

    public List<String> abilityLines() {
        List<String> lines = new ArrayList<>();
        for (AbilityEffect effect : ability.effects()) {
            lines.add(effect.describe());
        }
        if (ability.retaliation() != Retaliation.NONE) {
            lines.add("Retaliation: " + switch (ability.retaliation()) {
                case FIRE -> "sets attackers on fire";
                case POISON -> "poisons attackers";
                case WITHER -> "withers attackers";
                case SLOWNESS -> "slows attackers";
                case LEVITATION -> "levitates attackers";
                case NONE -> "none";
            });
        }
        if (ability.explosionImmune()) {
            lines.add("Immunity: explosions");
        }
        if (ability.freezeImmune()) {
            lines.add("Immunity: freezing");
        }
        if (ability.teleportOnHit()) {
            lines.add("Teleports away when hit");
        }
        return lines;
    }

    public String attributeSummary() {
        StringBuilder out = new StringBuilder();
        if (ability.healthBonus() > 0) {
            out.append('+').append(format(ability.healthBonus())).append(" max health");
        }
        if (ability.armorBonus() > 0) {
            if (!out.isEmpty()) {
                out.append(", ");
            }
            out.append('+').append(format(ability.armorBonus())).append(" armor");
        }
        if (ability.knockbackResistance() > 0) {
            if (!out.isEmpty()) {
                out.append(", ");
            }
            out.append('+').append(Math.round(ability.knockbackResistance() * 100))
                    .append("% knockback resistance");
        }
        return out.toString();
    }

    public boolean hasAnyAbility() {
        return ability.hasEffects()
                || ability.retaliation() != Retaliation.NONE
                || ability.explosionImmune()
                || ability.freezeImmune()
                || ability.teleportOnHit()
                || ability.healthBonus() > 0
                || ability.armorBonus() > 0
                || ability.knockbackResistance() > 0;
    }

    private static String format(double value) {
        return value == Math.rint(value) ? Long.toString((long) value) : Double.toString(value);
    }
}
