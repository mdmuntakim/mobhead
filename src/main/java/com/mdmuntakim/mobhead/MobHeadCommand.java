package com.mdmuntakim.mobhead;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * /mobhead give|list|info|selftest|reload
 */
public final class MobHeadCommand implements CommandExecutor, TabCompleter {

    private static final int PAGE_SIZE = 10;
    private static final NamedTextColor ACCENT = NamedTextColor.GOLD;
    private static final NamedTextColor MUTED = NamedTextColor.GRAY;

    private final MobHeadPlugin plugin;

    public MobHeadCommand(MobHeadPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            usage(sender, label);
            return true;
        }
        switch (args[0].toLowerCase(Locale.ROOT)) {
            case "give" -> give(sender, label, args);
            case "list" -> list(sender, label, args);
            case "info" -> info(sender, label, args);
            case "selftest" -> selftest(sender);
            case "reload" -> reload(sender, label);
            default -> usage(sender, label);
        }
        return true;
    }

    private void give(CommandSender sender, String label, String[] args) {
        if (!sender.hasPermission("mobhead.give")) {
            send(sender, Component.text("You do not have permission to give mob heads.", NamedTextColor.RED));
            return;
        }
        if (args.length < 3) {
            send(sender, Component.text("Usage: /" + label + " give <player> <mob|all> [amount]", NamedTextColor.RED));
            return;
        }
        Player target = Bukkit.getPlayerExact(args[1]);
        if (target == null) {
            send(sender, Component.text("Player not found: " + args[1], NamedTextColor.RED));
            return;
        }
        int amount = args.length > 3 ? parseAmount(args[3]) : 1;
        if (amount <= 0) {
            send(sender, Component.text("Amount must be between 1 and 64.", NamedTextColor.RED));
            return;
        }

        if (args[2].equalsIgnoreCase("all")) {
            int given = 0;
            for (MobHead head : sorted(plugin.registry().all())) {
                given += give(target, plugin.items().create(head), amount);
            }
            send(sender, Component.text("Gave " + given + " mob heads to " + target.getName() + ".", NamedTextColor.GREEN));
            return;
        }

        MobHead head = plugin.registry().byKey(args[2]);
        if (head == null) {
            send(sender, Component.text("Unknown mob: " + args[2] + ". Use /" + label + " list.", NamedTextColor.RED));
            return;
        }
        int given = give(target, plugin.items().create(head), amount);
        send(sender, Component.text("Gave " + given + "x " + head.displayName() + " Head ("
                + head.tier().label() + ") to " + target.getName() + ".", NamedTextColor.GREEN));
    }

    private int give(Player target, ItemStack template, int amount) {
        int given = 0;
        for (int i = 0; i < amount; i++) {
            ItemStack stack = template.clone();
            Map<Integer, ItemStack> leftover = target.getInventory().addItem(stack);
            for (ItemStack drop : leftover.values()) {
                target.getWorld().dropItemNaturally(target.getLocation(), drop);
            }
            given++;
        }
        return given;
    }

    private void list(CommandSender sender, String label, String[] args) {
        Tier filter = null;
        int page = 1;
        for (int i = 1; i < args.length; i++) {
            String value = args[i];
            Tier parsed = parseTier(value);
            if (parsed != null) {
                filter = parsed;
                continue;
            }
            try {
                page = Math.max(1, Integer.parseInt(value));
            } catch (NumberFormatException exception) {
                send(sender, Component.text("Unknown tier or page: " + value, NamedTextColor.RED));
                return;
            }
        }

        List<MobHead> heads = new ArrayList<>();
        for (MobHead head : sorted(plugin.registry().all())) {
            if (filter == null || head.tier() == filter) {
                heads.add(head);
            }
        }
        int pages = Math.max(1, (heads.size() + PAGE_SIZE - 1) / PAGE_SIZE);
        if (page > pages) {
            send(sender, Component.text("Page " + page + " does not exist; there are " + pages + " pages.", NamedTextColor.RED));
            return;
        }
        int from = (page - 1) * PAGE_SIZE;
        int to = Math.min(heads.size(), from + PAGE_SIZE);

        String title = filter == null ? "All mob heads" : filter.label() + " mob heads";
        send(sender, Component.text("MobHead - " + title + " (" + heads.size() + ")", ACCENT));
        for (MobHead head : heads.subList(from, to)) {
            send(sender, Component.text("  " + head.displayName(), head.tier().color())
                    .append(Component.text("  " + head.key() + "  " + head.dropChanceText(), MUTED)));
        }
        send(sender, Component.text("Page " + page + "/" + pages + " - /" + label
                + " info <mob> for details", MUTED));
    }

    private void info(CommandSender sender, String label, String[] args) {
        if (args.length < 2) {
            send(sender, Component.text("Usage: /" + label + " info <mob>", NamedTextColor.RED));
            return;
        }
        MobHead head = plugin.registry().byKey(args[1]);
        if (head == null) {
            send(sender, Component.text("Unknown mob: " + args[1], NamedTextColor.RED));
            return;
        }
        send(sender, Component.text(head.displayName() + " Head", head.tier().color())
                .append(Component.text(" - " + head.tier().label(), MUTED)));
        send(sender, Component.text("  Mob: " + head.key(), MUTED));
        send(sender, Component.text("  Drop chance: " + head.dropChanceText(), MUTED));
        List<String> abilities = head.abilityLines();
        if (abilities.isEmpty()) {
            send(sender, Component.text("  Ability: none", MUTED));
        } else {
            send(sender, Component.text("  Ability: " + String.join(", ", abilities), NamedTextColor.AQUA));
        }
        String attributes = head.attributeSummary();
        if (!attributes.isEmpty()) {
            send(sender, Component.text("  Attributes: " + attributes, NamedTextColor.GREEN));
        }
        send(sender, Component.text("  Item: " + head.material().name().toLowerCase(Locale.ROOT)
                + (head.hasCustomTexture() ? " (custom texture)" : ""), MUTED));
        send(sender, Component.text("  Give: /" + label + " give <player> " + head.key(), MUTED));
    }

    private void reload(CommandSender sender, String label) {
        if (!sender.hasPermission("mobhead.reload")) {
            send(sender, Component.text("You do not have permission to reload MobHead.", NamedTextColor.RED));
            return;
        }
        plugin.reloadPlugin();
        send(sender, Component.text("MobHead reloaded: " + plugin.registry().size()
                + " heads, " + plugin.registry().catalog().size() + " textures.", NamedTextColor.GREEN));
    }

    private void selftest(CommandSender sender) {
        if (!sender.hasPermission("mobhead.command")) {
            send(sender, Component.text("You do not have permission to run the self test.", NamedTextColor.RED));
            return;
        }
        int created = 0;
        List<String> failures = new ArrayList<>();
        for (MobHead head : plugin.registry().all()) {
            try {
                ItemStack item = plugin.items().create(head);
                MobHead roundTrip = plugin.items().read(item);
                if (roundTrip == null || !roundTrip.key().equals(head.key())) {
                    failures.add(head.key());
                } else {
                    created++;
                }
            } catch (RuntimeException exception) {
                failures.add(head.key() + " (" + exception.getClass().getSimpleName() + ")");
            }
        }
        long unregisteredAbilities = AbilityTable.all().keySet().stream()
                .filter(type -> plugin.registry().byType(type) == null)
                .count();

        plugin.getLogger().info("[selftest] living mobs=" + plugin.registry().size()
                + " items=" + created + "/" + plugin.registry().all().size()
                + " textures=" + plugin.registry().catalog().size()
                + " missingTextures=" + plugin.registry().missingTextures().size()
                + " withEffects=" + plugin.registry().withEffects()
                + " noAbility=" + plugin.registry().withoutAbility()
                + " abilityTableUnregistered=" + unregisteredAbilities);
        if (!plugin.registry().missingTextures().isEmpty()) {
            plugin.getLogger().info("[selftest] fallback textures: "
                    + String.join(", ", plugin.registry().missingTextures()));
        }
        if (!failures.isEmpty()) {
            plugin.getLogger().warning("[selftest] item round trip failed for: " + String.join(", ", failures));
        }

        boolean ok = failures.isEmpty() && unregisteredAbilities == 0;
        send(sender, Component.text("MobHead self test: "
                        + (ok ? "PASS" : "FAIL")
                        + " - " + created + "/" + plugin.registry().all().size() + " heads built",
                        ok ? NamedTextColor.GREEN : NamedTextColor.RED)
                .decoration(TextDecoration.BOLD, true));
        send(sender, Component.text("  textures " + plugin.registry().catalog().size()
                + ", fallback " + plugin.registry().missingTextures().size()
                + ", with effects " + plugin.registry().withEffects()
                + ", no ability " + plugin.registry().withoutAbility(), MUTED));
        if (!failures.isEmpty()) {
            send(sender, Component.text("  Failed: " + String.join(", ", failures), NamedTextColor.RED));
        }
    }

    private void usage(CommandSender sender, String label) {
        send(sender, Component.text("MobHead commands", ACCENT));
        send(sender, Component.text("  /" + label + " give <player> <mob|all> [amount]", MUTED));
        send(sender, Component.text("  /" + label + " list [tier] [page]", MUTED));
        send(sender, Component.text("  /" + label + " info <mob>", MUTED));
        send(sender, Component.text("  /" + label + " selftest", MUTED));
        send(sender, Component.text("  /" + label + " reload", MUTED));
    }

    private static List<MobHead> sorted(List<MobHead> heads) {
        List<MobHead> copy = new ArrayList<>(heads);
        copy.sort(Comparator.comparingInt((MobHead head) -> head.tier().ordinal())
                .thenComparing(MobHead::displayName));
        return copy;
    }

    private static int parseAmount(String value) {
        try {
            return Math.max(0, Math.min(64, Integer.parseInt(value)));
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

    private static Tier parseTier(String value) {
        try {
            return Tier.valueOf(value.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }

    private static void send(CommandSender sender, Component component) {
        sender.sendMessage(component);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return filter(List.of("give", "list", "info", "selftest", "reload"), args[0]);
        }
        String sub = args[0].toLowerCase(Locale.ROOT);
        if (args.length == 2) {
            return switch (sub) {
                case "give" -> filter(Bukkit.getOnlinePlayers().stream().map(Player::getName).toList(), args[1]);
                case "list" -> filter(Arrays.stream(Tier.values()).map(Tier::name).toList(), args[1]);
                case "info" -> filter(plugin.registry().all().stream().map(MobHead::key).toList(), args[1]);
                default -> List.of();
            };
        }
        if (args.length == 3 && sub.equals("give")) {
            List<String> options = new ArrayList<>();
            options.add("all");
            options.addAll(plugin.registry().all().stream().map(MobHead::key).toList());
            return filter(options, args[2]);
        }
        if (args.length == 4 && sub.equals("give")) {
            return filter(List.of("1", "2", "8", "16", "32", "64"), args[3]);
        }
        return List.of();
    }

    private static List<String> filter(List<String> options, String prefix) {
        String lower = prefix.toLowerCase(Locale.ROOT);
        return options.stream()
                .filter(option -> option.toLowerCase(Locale.ROOT).startsWith(lower))
                .sorted()
                .toList();
    }
}
