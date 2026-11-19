package com.mdmuntakim.mobhead;

import org.bukkit.NamespacedKey;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * MobHead: every living mob in the game drops a head, and wearing that head in
 * the helmet slot grants a tier scaled ability.
 */
public final class MobHeadPlugin extends JavaPlugin {

    private NamespacedKey mobKey;
    private MobHeadRegistry registry;
    private MobHeadItem items;
    private AbilityEngine abilities;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        mobKey = new NamespacedKey(this, "mob");

        registry = new MobHeadRegistry(this);
        registry.reload();

        items = new MobHeadItem(this);
        abilities = new AbilityEngine(this, items);
        abilities.start();

        getServer().getPluginManager().registerEvents(new AbilityListener(this), this);
        getServer().getPluginManager().registerEvents(new HeadDropListener(this), this);

        PluginCommand command = getCommand("mobhead");
        if (command != null) {
            MobHeadCommand executor = new MobHeadCommand(this);
            command.setExecutor(executor);
            command.setTabCompleter(executor);
        }

        logSummary();
    }

    @Override
    public void onDisable() {
        if (abilities != null) {
            abilities.stop();
        }
    }

    public void reloadPlugin() {
        reloadConfig();
        registry.reload();
        abilities.start();
        logSummary();
    }

    private void logSummary() {
        getLogger().info("Registered " + registry.size() + " mob heads: "
                + registry.withEffects() + " with potion effects, "
                + registry.withoutAbility() + " without an extra ability.");
        if (!registry.missingTextures().isEmpty()) {
            getLogger().warning("No custom head texture for: "
                    + String.join(", ", registry.missingTextures()));
        }
        getLogger().info("Head catalog: " + registry.catalog().size() + " textures.");
    }

    public NamespacedKey mobKey() {
        return mobKey;
    }

    public MobHeadRegistry registry() {
        return registry;
    }

    public MobHeadItem items() {
        return items;
    }

    public AbilityEngine abilities() {
        return abilities;
    }
}
