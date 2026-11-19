#!/usr/bin/env python3
"""Generate the net.minecraft.entity.Head table and its per-mob accessors.

The source of truth stays in this repository:

* ``src/main/resources/mobheads.tsv`` carries the head texture of every mob,
* ``AbilityTable.java`` carries the potion effects and the event driven
  extras (retaliation, immunities, teleporting, attribute overrides),
* ``MobHeadRegistry.java`` carries the display names that differ from the
  entity key.

``Head.java`` embeds all three in one Java file, so the table is available
without a running server and without reading a resource.  The per-mob classes
are thin, typed accessors for the same entries.

Run from the repository root:

    python3 tools/generate-entity-package.py
"""

from __future__ import annotations

import re
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
TSV = ROOT / "src/main/resources/mobheads.tsv"
ABILITY = ROOT / "src/main/java/com/mdmuntakim/mobhead/AbilityTable.java"
REGISTRY = ROOT / "src/main/java/com/mdmuntakim/mobhead/MobHeadRegistry.java"
OUT = ROOT / "src/main/java/net/minecraft/entity"

VANILLA_ITEMS = {
    "creeper": "CREEPER_HEAD",
    "skeleton": "SKELETON_SKULL",
    "wither_skeleton": "WITHER_SKELETON_SKULL",
    "zombie": "ZOMBIE_HEAD",
    "piglin": "PIGLIN_HEAD",
    "ender_dragon": "DRAGON_HEAD",
}


def read_catalog():
    rows = []
    for line in TSV.read_text(encoding="utf-8").splitlines():
        if not line.strip() or line.startswith("#"):
            continue
        parts = line.split("\t", -1)
        if len(parts) < 2 or not parts[0].strip() or not parts[1].strip():
            continue
        rows.append(
            {
                "id": parts[0].strip().lower(),
                "texture": parts[1].strip(),
                "catalog_name": parts[2].strip() if len(parts) > 2 else parts[0].strip(),
                "category": parts[3].strip() if len(parts) > 3 else "",
            }
        )
    return rows


def read_abilities():
    source = ABILITY.read_text(encoding="utf-8")
    starts = list(re.finditer(r'define\("([A-Z0-9_]+)",\s*Tier\.([A-Z]+),', source))
    abilities = {}
    for index, match in enumerate(starts):
        end = starts[index + 1].start() if index + 1 < len(starts) else len(source)
        block = source[match.end():end]
        effects = [
            (name, int(amplifier))
            for name, amplifier in re.findall(r'fx\("([a-z_]+)",\s*(\d+)\)', block)
        ]
        if not effects:
            simple = re.search(r"simple\(Tier\.[A-Z]+,\s*([^)]*)\)", block)
            if simple:
                effects = [(name, 0) for name in re.findall(r'"([a-z_]+)"', simple.group(1))]

        externals = []
        retaliation = re.search(r"retaliation\(Retaliation\.([A-Z]+)\)", block)
        if retaliation and retaliation.group(1) != "NONE":
            externals.append("retaliation:" + retaliation.group(1).lower())
        if "explosionImmune()" in block:
            externals.append("explosion_immune")
        if "freezeImmune()" in block:
            externals.append("freeze_immune")
        if "teleportOnHit()" in block:
            externals.append("teleport_on_hit")
        for field in ("health", "armor", "knockbackResistance"):
            value = re.search(field + r"\(([0-9.]+)\)", block)
            if value:
                token = {
                    "health": "health",
                    "armor": "armor",
                    "knockbackResistance": "knockback_resistance",
                }[field]
                externals.append(f"{token}:{number(value.group(1))}")

        abilities[match.group(1).lower()] = {
            "tier": match.group(2),
            "effects": effects,
            "externals": externals,
        }
    return abilities


def read_display_names():
    source = REGISTRY.read_text(encoding="utf-8")
    return {
        key.lower(): name
        for key, name in re.findall(
            r'DISPLAY_NAMES\.put\(EntityType\.([A-Z0-9_]+), "([^"]+)"', source
        )
    }


def number(text: str) -> str:
    value = float(text)
    return str(int(value)) if value == int(value) else repr(value)


def title_case(key: str) -> str:
    return " ".join(part[:1].upper() + part[1:] for part in key.split("_") if part)


def java_string(text: str) -> str:
    return '"' + text.replace("\\", "\\\\").replace('"', '\\"') + '"'


def entry_lines(row, ability, display_name):
    id_ = row["id"]
    effects = ", ".join(
        f"new Potion({java_string(name)}, {amplifier})"
        for name, amplifier in ability["effects"]
    )
    externals = ", ".join(java_string(value) for value in ability["externals"])
    item = VANILLA_ITEMS.get(id_)
    return [
        f'        put(entries, {java_string(id_)}, {java_string(display_name)},',
        f'                {java_string(row["category"])}, {java_string(row["texture"])},',
        f'                {"null" if item is None else java_string(item)}, {java_string(ability["tier"])},',
        f'                List.of({effects}), List.of({externals}));',
    ]


HEAD = '''\
package net.minecraft.entity;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * The complete mob head table of Minecraft 26.3.
 *
 * <p>Every living mob has one {@link Entry}: its entity id and namespaced id,
 * the display name, the catalogue category and the head texture that is
 * already part of the game data.  The entry also carries the potion effects
 * and the event driven externals of the head, so the table can be read without
 * a running Paper server.</p>
 *
 * <p>This file is generated by {@code tools/generate-entity-package.py} from
 * {@code mobheads.tsv}, {@code AbilityTable} and {@code MobHeadRegistry}; edit
 * those sources and regenerate instead of editing this file.  The per-mob
 * classes in this package are typed accessors for the same entries.</p>
 *
 * <p>Paper's plugin class loader refuses to define classes whose name starts
 * with {@code net.minecraft.}, so the packaged jar relocates this package to
 * {@code com.mdmuntakim.mobhead.internal.entity}.  The source tree keeps the
 * requested {@code net.minecraft.entity} package; only the build output is
 * relocated.</p>
 */
public final class Head {

    /** One potion effect: a registry id and its amplifier (0 = level I). */
    public record Potion(String id, int amplifier) {

        public Potion {
            if (id == null || id.isBlank()) {
                throw new IllegalArgumentException("potion id must not be blank");
            }
            if (amplifier < 0) {
                throw new IllegalArgumentException("amplifier must not be negative");
            }
        }

        /** The effect id with the amplifier in roman numerals, e.g. {@code Speed II}. */
        public String describe() {
            return pretty(id) + " " + Roman.of(amplifier + 1);
        }
    }

    /**
     * One mob head.
     *
     * @param id          the entity key, e.g. {@code zombie}
     * @param namespacedId the key with its namespace, e.g. {@code minecraft:zombie}
     * @param name        the display name, e.g. {@code Zombie}
     * @param category    the catalogue category (animals, humanoid, monsters)
     * @param texture     the base64 skin texture value, or {@code null} when none exists
     * @param headItem    the vanilla skull material, or {@code null} for a player head
     * @param tier        the difficulty tier (NONE, COMMON, UNCOMMON, RARE, EPIC, LEGENDARY)
     * @param potions     the potion effects applied while the head is worn
     * @param abilities   event driven extras, e.g. {@code retaliation:fire}, {@code health:16}
     */
    public record Entry(
            String id,
            String namespacedId,
            String name,
            String category,
            String texture,
            String headItem,
            String tier,
            List<Potion> potions,
            List<String> abilities) {

        public Entry {
            if (id == null || id.isBlank()) {
                throw new IllegalArgumentException("head id must not be blank");
            }
            potions = List.copyOf(potions);
            abilities = List.copyOf(abilities);
        }

        /** True when the head is a vanilla skull item instead of a player head. */
        public boolean vanilla() {
            return headItem != null;
        }

        /** True when the entry carries a skin texture. */
        public boolean hasTexture() {
            return texture != null && !texture.isBlank();
        }

        /** True when the head changes something besides its tier attributes. */
        public boolean hasAbility() {
            return !potions.isEmpty() || !abilities.isEmpty();
        }
    }

    private static final Map<String, Entry> BY_ID;

    static {
        Map<String, Entry> entries = new LinkedHashMap<>();
@ENTRIES@
        BY_ID = Collections.unmodifiableMap(entries);
    }

    private Head() {
    }

    /** Every head in declaration order (the order of {@code mobheads.tsv}). */
    public static List<Entry> all() {
        return List.copyOf(BY_ID.values());
    }

    /** The head for an entity key or a namespaced id, or {@code null}. */
    public static Entry of(String id) {
        if (id == null) {
            return null;
        }
        String key = id.trim().toLowerCase(Locale.ROOT)
                .replace(' ', '_')
                .replace("minecraft:", "");
        return BY_ID.get(key);
    }

    /** The head for an entity key, or an exception when the key is unknown. */
    public static Entry require(String id) {
        Entry entry = of(id);
        if (entry == null) {
            throw new IllegalArgumentException("Unknown mob head: " + id);
        }
        return entry;
    }

    /** The head whose display name matches, ignoring case, or {@code null}. */
    public static Entry byName(String name) {
        if (name == null || name.isBlank()) {
            return null;
        }
        String wanted = name.trim();
        for (Entry entry : BY_ID.values()) {
            if (entry.name().equalsIgnoreCase(wanted)) {
                return entry;
            }
        }
        return null;
    }

    public static int size() {
        return BY_ID.size();
    }

    public static List<Entry> vanillaHeads() {
        return BY_ID.values().stream().filter(Entry::vanilla).toList();
    }

    public static List<Entry> withAbility() {
        return BY_ID.values().stream().filter(Entry::hasAbility).toList();
    }

    private static void put(Map<String, Entry> entries, String id, String name,
                            String category, String texture, String headItem,
                            String tier, List<Potion> potions, List<String> abilities) {
        entries.put(id, new Entry(id, "minecraft:" + id, name, category,
                texture, headItem, tier, potions, abilities));
    }

    private static String pretty(String id) {
        StringBuilder out = new StringBuilder();
        for (String part : id.split("_")) {
            if (part.isEmpty()) {
                continue;
            }
            if (!out.isEmpty()) {
                out.append(' ');
            }
            out.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
        }
        return out.toString();
    }

    /** Roman numerals for potion levels, kept here so the table has no dependency. */
    static final class Roman {

        private Roman() {
        }

        static String of(int value) {
            return switch (value) {
                case 1 -> "I";
                case 2 -> "II";
                case 3 -> "III";
                case 4 -> "IV";
                case 5 -> "V";
                case 6 -> "VI";
                case 7 -> "VII";
                case 8 -> "VIII";
                case 9 -> "IX";
                case 10 -> "X";
                default -> Integer.toString(value);
            };
        }
    }
}
'''

PER_MOB = '''\
package net.minecraft.entity;

/**
 * Typed accessor for the {@code {id}} head, part of the table in {@link Head}.
 */
public final class {class} {

    public static final String ID = "{id}";

    private {class}() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
'''

PACKAGE_INFO = '''\
/**
 * The mob head table of the game.
 *
 * <p>{@link Head} owns the entity names, head textures, potion effects and
 * external abilities of every mob; the other classes are one typed accessor
 * per mob.  The table is embedded in the plugin jar and needs no server.</p>
 */
package net.minecraft.entity;
'''


def main() -> None:
    rows = read_catalog()
    abilities = read_abilities()
    display = read_display_names()
    missing = sorted(set(abilities) - {row["id"] for row in rows})
    if missing:
        raise SystemExit(f"ability entries without a head texture: {missing}")

    OUT.mkdir(parents=True, exist_ok=True)
    for old in OUT.glob("*.java"):
        old.unlink()

    generated = []
    for row in rows:
        ability = abilities.get(
            row["id"], {"tier": "NONE", "effects": [], "externals": []}
        )
        name = display.get(row["id"]) or title_case(row["id"])
        generated.extend(entry_lines(row, ability, name))

    (OUT / "Head.java").write_text(HEAD.replace("@ENTRIES@", "\n".join(generated)),
                                   encoding="utf-8")
    (OUT / "package-info.java").write_text(PACKAGE_INFO, encoding="utf-8")
    for row in rows:
        class_name = "".join(part.capitalize() for part in row["id"].split("_")) + "Head"
        text = PER_MOB.replace("{class}", class_name).replace("{id}", row["id"])
        (OUT / f"{class_name}.java").write_text(text, encoding="utf-8")

    print(f"wrote {len(rows) + 2} files to {OUT.relative_to(ROOT)}")
    print(f"{len(rows)} mobs, {sum(1 for row in rows if row['id'] in abilities)} with abilities")


if __name__ == "__main__":
    main()
