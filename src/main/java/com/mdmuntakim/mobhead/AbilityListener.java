package com.mdmuntakim.mobhead;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Wires the worn head into damage events and keeps the player state clean when
 * somebody leaves the server.
 */
public final class AbilityListener implements Listener {

    private final MobHeadPlugin plugin;

    public AbilityListener(MobHeadPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            plugin.abilities().onDamage(player, event);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player player) {
            plugin.abilities().onRetaliation(player, event.getDamager());
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.getServer().getScheduler().runTask(plugin, () -> plugin.abilities().apply(player));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        plugin.abilities().clear(player);
        plugin.abilities().forget(player.getUniqueId());
    }
}
