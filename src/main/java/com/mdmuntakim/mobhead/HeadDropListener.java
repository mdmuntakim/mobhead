package com.mdmuntakim.mobhead;

import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Drops the matching head when a player kills a mob. The chance comes from the
 * mob's tier (2% for a head without an ability, 100% for a legendary boss) and
 * is increased by Looting.
 */
public final class HeadDropListener implements Listener {

    private final MobHeadPlugin plugin;

    public HeadDropListener(MobHeadPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player) {
            return;
        }
        MobHead head = plugin.registry().byType(entity.getType());
        if (head == null) {
            return;
        }
        Player killer = entity.getKiller();
        if (killer == null) {
            return;
        }

        double multiplier = plugin.getConfig().getDouble("drop-chance-multiplier", 1.0);
        double lootingBonus = plugin.getConfig().getDouble("looting-bonus-per-level", 0.01);
        int looting = killer.getInventory().getItemInMainHand()
                .getEnchantmentLevel(Enchantment.LOOTING);
        double chance = Math.min(1.0, head.dropChance() * multiplier + looting * lootingBonus);

        if (ThreadLocalRandom.current().nextDouble() < chance) {
            event.getDrops().add(plugin.items().create(head));
        }

        if (plugin.getConfig().getBoolean("suppress-vanilla-skull-drops", true)
                && head.material() != Material.PLAYER_HEAD) {
            event.getDrops().removeIf(stack -> stack != null && stack.getType() == head.material());
        }
    }
}
