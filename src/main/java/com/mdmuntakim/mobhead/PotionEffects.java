package com.mdmuntakim.mobhead;

import java.util.Locale;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.potion.PotionEffectType;

/**
 * Resolves effect ids lazily, so the ability table and the unit tests do not
 * touch the server owned {@link PotionEffectType} registry.
 */
final class PotionEffects {

    private PotionEffects() {
    }

    static PotionEffectType resolve(String id) {
        PotionEffectType type = Registry.MOB_EFFECT.get(NamespacedKey.minecraft(id));
        if (type == null) {
            throw new IllegalArgumentException("Unknown potion effect: " + id);
        }
        return type;
    }

    static String pretty(String id) {
        String[] parts = id.split("_");
        StringBuilder out = new StringBuilder();
        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }
            if (!out.isEmpty()) {
                out.append(' ');
            }
            out.append(part.substring(0, 1).toUpperCase(Locale.ROOT))
                    .append(part.substring(1));
        }
        return out.toString();
    }
}
