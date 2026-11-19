package com.mdmuntakim.mobhead;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.entity.Player;

/**
 * Creates and recognises MobHead items. The mob key is stored in the item's
 * persistent data container, so a head keeps its ability after being dropped,
 * stored in a chest, enchanted or renamed.
 */
public final class MobHeadItem {

    private final MobHeadPlugin plugin;

    public MobHeadItem(MobHeadPlugin plugin) {
        this.plugin = plugin;
    }

    public ItemStack create(MobHead head) {
        ItemStack item = new ItemStack(head.material());
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return item;
        }

        meta.displayName(Component.text(head.displayName() + " Head", head.tier().color())
                .decoration(TextDecoration.ITALIC, false));

        if (plugin.getConfig().getBoolean("show-ability-lore", true)) {
            meta.lore(lore(head));
        }
        meta.setMaxStackSize(1);
        meta.getPersistentDataContainer().set(plugin.mobKey(), PersistentDataType.STRING, head.key());
        if (head.tier() == Tier.LEGENDARY) {
            meta.setEnchantmentGlintOverride(true);
        }

        if (meta instanceof SkullMeta skull
                && head.material() == Material.PLAYER_HEAD
                && head.hasCustomTexture()) {
            PlayerProfile profile = Bukkit.createProfile(profileId(head.key()), profileName(head.displayName()));
            profile.setProperty(new ProfileProperty("textures", head.texture()));
            skull.setPlayerProfile(profile);
        }

        item.setItemMeta(meta);
        return item;
    }

    public MobHead read(ItemStack item) {
        if (item == null || item.getType().isAir() || !item.hasItemMeta()) {
            return null;
        }
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return null;
        }
        String key = meta.getPersistentDataContainer().get(plugin.mobKey(), PersistentDataType.STRING);
        if (key == null) {
            return null;
        }
        return plugin.registry().byKey(key);
    }

    public MobHead worn(Player player) {
        return read(player.getInventory().getHelmet());
    }

    private List<Component> lore(MobHead head) {
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("Tier: ", NamedTextColor.GRAY)
                .append(Component.text(head.tier().label(), head.tier().color()))
                .decoration(TextDecoration.ITALIC, false));

        List<String> abilities = head.abilityLines();
        if (abilities.isEmpty()) {
            lore.add(Component.text("No special ability", NamedTextColor.DARK_GRAY)
                    .decoration(TextDecoration.ITALIC, true));
        } else {
            for (String line : abilities) {
                lore.add(Component.text("  " + line, NamedTextColor.AQUA)
                        .decoration(TextDecoration.ITALIC, false));
            }
        }

        String attributes = head.attributeSummary();
        if (!attributes.isEmpty()) {
            lore.add(Component.text("  " + attributes, NamedTextColor.GREEN)
                    .decoration(TextDecoration.ITALIC, false));
        }

        lore.add(Component.empty());
        lore.add(Component.text("Wear in the helmet slot", NamedTextColor.DARK_GRAY)
                .decoration(TextDecoration.ITALIC, true));
        lore.add(Component.text("Drop chance: " + head.dropChanceText(), NamedTextColor.DARK_GRAY)
                .decoration(TextDecoration.ITALIC, true));
        return lore;
    }

    static UUID profileId(String key) {
        return UUID.nameUUIDFromBytes(("mobhead:" + key).getBytes(StandardCharsets.UTF_8));
    }

    static String profileName(String displayName) {
        String name = displayName.replaceAll("[^A-Za-z0-9_]", "_");
        if (name.isEmpty()) {
            name = "MobHead";
        }
        return name.length() > 16 ? name.substring(0, 16) : name;
    }

    static String percent(double chance) {
        return String.format(Locale.ROOT, "%.0f%%", chance * 100.0);
    }
}
