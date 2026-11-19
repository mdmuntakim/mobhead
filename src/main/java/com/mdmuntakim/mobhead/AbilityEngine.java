package com.mdmuntakim.mobhead;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitTask;

/**
 * Applies the passive ability of the head a player wears in the helmet slot.
 * Potion effects are refreshed continuously; attribute bonuses are kept in
 * sync with short lived modifier keys so they disappear the moment the head
 * is taken off.
 */
public final class AbilityEngine {

    private static final List<Attribute> ATTRIBUTES = List.of(
            Attribute.MAX_HEALTH,
            Attribute.ARMOR,
            Attribute.KNOCKBACK_RESISTANCE);

    private final MobHeadPlugin plugin;
    private final MobHeadItem items;
    private final Map<UUID, Long> teleportCooldowns = new HashMap<>();
    private final Map<UUID, Long> retaliationCooldowns = new HashMap<>();
    private BukkitTask task;

    public AbilityEngine(MobHeadPlugin plugin, MobHeadItem items) {
        this.plugin = plugin;
        this.items = items;
    }

    public void start() {
        stop();
        long refresh = Math.max(5, plugin.getConfig().getLong("abilities.refresh-ticks", 20));
        task = plugin.getServer().getScheduler()
                .runTaskTimer(plugin, this::tick, 20L, refresh);
    }

    public void stop() {
        if (task != null) {
            task.cancel();
            task = null;
        }
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            clear(player);
        }
        teleportCooldowns.clear();
        retaliationCooldowns.clear();
    }

    private void tick() {
        boolean enabled = plugin.getConfig().getBoolean("abilities.enabled", true);
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (!enabled) {
                clear(player);
                continue;
            }
            apply(player);
        }
    }

    public void apply(Player player) {
        MobHead head = items.worn(player);
        if (head == null) {
            clear(player);
            return;
        }
        int duration = Math.max(20, plugin.getConfig().getInt("abilities.effect-duration-ticks", 100));
        for (AbilityEffect effect : head.ability().effects()) {
            applyEffect(player, effect, duration);
        }
        syncAttributes(player, head.ability());
    }

    private void applyEffect(Player player, AbilityEffect effect, int duration) {
        PotionEffect existing = player.getPotionEffect(effect.type());
        if (existing != null) {
            boolean stronger = existing.getAmplifier() > effect.amplifier();
            boolean longEnough = existing.getAmplifier() == effect.amplifier()
                    && existing.getDuration() > duration / 2;
            if (stronger || longEnough) {
                return;
            }
        }
        player.addPotionEffect(new PotionEffect(effect.type(), duration,
                effect.amplifier(), true, false, true));
    }

    private void syncAttributes(Player player, Ability ability) {
        Map<Attribute, AttributeModifier> desired = new HashMap<>();
        if (ability.healthBonus() > 0) {
            desired.put(Attribute.MAX_HEALTH, modifier("health", ability.healthBonus()));
        }
        if (ability.armorBonus() > 0) {
            desired.put(Attribute.ARMOR, modifier("armor", ability.armorBonus()));
        }
        if (ability.knockbackResistance() > 0) {
            desired.put(Attribute.KNOCKBACK_RESISTANCE,
                    modifier("knockback", ability.knockbackResistance()));
        }

        for (Attribute attribute : ATTRIBUTES) {
            AttributeInstance instance = player.getAttribute(attribute);
            if (instance == null) {
                continue;
            }
            AttributeModifier wanted = desired.get(attribute);
            for (AttributeModifier current : List.copyOf(instance.getModifiers())) {
                if (!current.getKey().getNamespace().equals(plugin.getName().toLowerCase())) {
                    continue;
                }
                boolean keep = wanted != null
                        && current.getKey().equals(wanted.getKey())
                        && Math.abs(current.getAmount() - wanted.getAmount()) < 1.0E-6;
                if (!keep) {
                    instance.removeModifier(current.getKey());
                }
            }
            if (wanted != null && instance.getModifier(wanted.getKey()) == null) {
                instance.addTransientModifier(wanted);
            }
        }
    }

    private AttributeModifier modifier(String id, double amount) {
        return new AttributeModifier(new NamespacedKey(plugin, id), amount,
                AttributeModifier.Operation.ADD_NUMBER);
    }

    public void clear(Player player) {
        for (Attribute attribute : ATTRIBUTES) {
            AttributeInstance instance = player.getAttribute(attribute);
            if (instance == null) {
                continue;
            }
            for (AttributeModifier modifier : List.copyOf(instance.getModifiers())) {
                if (modifier.getKey().getNamespace().equals(plugin.getName().toLowerCase())) {
                    instance.removeModifier(modifier.getKey());
                }
            }
        }
    }

    /**
     * Extras that fire when a wearer takes damage: immunities and the teleport
     * of the enderman head.
     */
    public void onDamage(Player player, org.bukkit.event.entity.EntityDamageEvent event) {
        MobHead head = items.worn(player);
        if (head == null) {
            return;
        }
        switch (event.getCause()) {
            case FREEZE -> {
                if (head.ability().freezeImmune()) {
                    event.setCancelled(true);
                }
            }
            case BLOCK_EXPLOSION, ENTITY_EXPLOSION -> {
                if (head.ability().explosionImmune()) {
                    event.setCancelled(true);
                }
            }
            default -> {
            }
        }
        if (head.ability().teleportOnHit() && !event.isCancelled()) {
            long now = System.currentTimeMillis();
            long last = teleportCooldowns.getOrDefault(player.getUniqueId(), 0L);
            if (now - last >= 5_000L && teleport(player)) {
                teleportCooldowns.put(player.getUniqueId(), now);
            }
        }
    }

    /**
     * Extras that fire when a wearer damages something: retaliation effects.
     */
    public void onRetaliation(Player victim, org.bukkit.entity.Entity damager) {
        MobHead head = items.worn(victim);
        if (head == null || head.ability().retaliation() == Retaliation.NONE) {
            return;
        }
        LivingEntity target = null;
        if (damager instanceof LivingEntity living) {
            target = living;
        } else if (damager instanceof org.bukkit.entity.Projectile projectile
                && projectile.getShooter() instanceof LivingEntity shooter) {
            target = shooter;
        }
        if (target == null || target.getUniqueId().equals(victim.getUniqueId())) {
            return;
        }
        long now = System.currentTimeMillis();
        long last = retaliationCooldowns.getOrDefault(victim.getUniqueId(), 0L);
        if (now - last < 1_000L) {
            return;
        }
        retaliationCooldowns.put(victim.getUniqueId(), now);

        Retaliation retaliation = head.ability().retaliation();
        if (retaliation == Retaliation.FIRE) {
            target.setFireTicks(80);
        } else {
            target.addPotionEffect(new PotionEffect(retaliation.effect(), retaliation.duration(),
                    retaliation.amplifier(), true, true, true));
        }
    }

    private boolean teleport(Player player) {
        Location origin = player.getLocation();
        World world = player.getWorld();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int attempt = 0; attempt < 24; attempt++) {
            double dx = random.nextDouble(-8.0, 9.0);
            double dz = random.nextDouble(-8.0, 9.0);
            int dy = random.nextInt(-3, 4);
            Location target = origin.clone().add(dx, dy, dz);
            Block feet = target.getBlock();
            Block head = target.clone().add(0, 1, 0).getBlock();
            Block below = target.clone().subtract(0, 1, 0).getBlock();
            if (feet.isPassable() && head.isPassable() && !below.isPassable() && !below.isLiquid()) {
                world.spawnParticle(Particle.PORTAL, origin.add(0, 1, 0), 32, 0.3, 0.5, 0.3, 0.1);
                return player.teleport(target);
            }
        }
        return false;
    }

    public void forget(UUID playerId) {
        teleportCooldowns.remove(playerId);
        retaliationCooldowns.remove(playerId);
    }
}
