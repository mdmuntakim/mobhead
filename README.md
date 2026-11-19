# MobHead

Every living mob in Minecraft drops its own head, and wearing that head in the
helmet slot grants a themed ability. Built for **Paper 26.3** (Minecraft 26.3)
and Java 25.

```
plugins/mobhead-26.3.jar
```

## What it does

* Registers a head for **every living entity the server knows about** by reading
  the entity registry at startup, so new mobs are not forgotten when Minecraft
  adds them.
* Drops the head when a player kills the mob. The chance scales with how hard
  the mob is to defeat and how rare it is:

  | Tier | Drop chance | Max health | Armor |
  | --- | ---: | ---: | ---: |
  | No ability | 2% | +0 | +0 |
  | Common | 5% | +2 | +0 |
  | Uncommon | 10% | +4 | +1 |
  | Rare | 25% | +6 | +2 |
  | Epic | 50% | +10 | +3 |
  | Legendary | 100% | +16 | +5 |

  Looting adds 1 percentage point per level by default.
* Applies the head's ability while it is worn in the helmet slot. The effect is
  refreshed in the background and the attribute bonuses disappear the moment
  the head is removed.

### Examples

| Head | Ability |
| --- | --- |
| Iron Golem | Strength II, Resistance I, +16 max health, +6 armor, 75% knockback resistance |
| Warden | Strength III, Resistance III, Night Vision, +20 max health, +6 armor |
| Ender Dragon | Strength III, Resistance III, Regeneration II, Slow Falling, +24 max health |
| Blaze | Fire Resistance, Strength, sets attackers on fire |
| Enderman | Speed III, Night Vision, teleports away when hit |
| Creeper | Resistance, Speed II, immune to explosions |
| Snow Golem | Slow Falling, immune to freezing |
| Squid | Water Breathing |
| Cow, Pig, Sheep, fish | No extra ability, only the tier health bonus |

Use `/mobhead info <mob>` to see the exact profile of any head.

## Commands

| Command | Permission | What it does |
| --- | --- | --- |
| `/mobhead give <player> <mob\|all> [amount]` | `mobhead.give` | Give one head, or every head, to a player |
| `/mobhead list [tier] [page]` | `mobhead.command` | Browse the catalogue |
| `/mobhead info <mob>` | `mobhead.command` | Tier, drop chance and ability of a mob |
| `/mobhead selftest` | `mobhead.command` | Build every head and verify the catalogue |
| `/mobhead reload` | `mobhead.reload` | Reload config and rebuild the registry |

`/mh` is an alias. All permissions default to operators.

## Configuration

`plugins/MobHead/config.yml`:

```yaml
abilities:
  enabled: true
  refresh-ticks: 20
  effect-duration-ticks: 100

drop-chance-multiplier: 1.0
looting-bonus-per-level: 0.01
suppress-vanilla-skull-drops: true
show-ability-lore: true
```

`suppress-vanilla-skull-drops` removes the vanilla zombie/skeleton/wither
skeleton/creeper/piglin/dragon head from the drops of the same mob, so one kill
never produces two heads. Set it to `false` to keep the vanilla drop as well.

## The `net.minecraft.entity.Head` table

The complete table also lives in code, in the `net.minecraft.entity` package:

```
src/main/java/net/minecraft/entity/Head.java          # every mob, texture, potions, abilities
src/main/java/net/minecraft/entity/ZombieHead.java    # typed accessor, one per mob
...                                                   # 90 accessors + package-info
```

`Head` is generated from `mobheads.tsv`, `AbilityTable` and `MobHeadRegistry`:

* `Head.all()` returns all 90 mobs in catalogue order.
* `Head.of("zombie")`, `Head.of("minecraft:zombie")` and
  `Head.byName("Zombie")` look an entry up.
* An entry exposes the entity id, the display name, the category, the head
  texture, the vanilla skull item (for the six vanilla heads), the tier, the
  potion effects and the event driven extras (`retaliation:fire`,
  `explosion_immune`, `health:16`, ...).

The plugin merges this embedded table into its runtime catalogue, so a jar
built without `mobheads.tsv` still has every texture. The TSV stays the
editable copy and still wins when both are present.

After editing the TSV or the ability table, regenerate the package:

```bash
python3 tools/generate-entity-package.py
mvn -B verify
```

`HeadTableConsistencyTest` fails when the generated table and `AbilityTable`
disagree on a tier, a potion effect or an external ability.

## How the heads are built

* The six heads that exist as vanilla items (zombie, skeleton, wither skeleton,
  creeper, piglin, dragon) use their native material, so they always render.
* Every other head is a player head with a texture property embedded in the
  jar (`src/main/resources/mobheads.tsv`). The client fetches the skin from
  `textures.minecraft.net`; no server side network access is needed. If a
  client is offline it shows the default skin, the ability still works.
* `tools/generate-head-catalog.py` regenerates the catalogue from the free
  Minecraft-Heads.com API. The catalogue is committed, so a normal build is
  fully offline and reproducible.

## Build

```bash
mvn -B verify
# target/mobhead-26.3.jar
```

or `./build.sh`.

Tests cover the tier scaling, ability formatting, catalogue parsing and item
identity helpers. The live server check is done with Paper 26.3 build 256 on
Java 25: start the server with the jar in `plugins/`, run `mobhead selftest`
from the console, then `mobhead list` and `mobhead info iron_golem`.

## Notes

* Heads keep their identity through chests, shulker boxes, item frames and
  renaming because the mob key lives in the item's persistent data container.
* Mob heads are not craftable; they are drops and `/mobhead give` rewards.
* Custom head textures are the property of their authors and are used under
  the Minecraft-Heads.com free-plugin terms. They are not sold with this
  plugin.

Paper 26.3 / Minecraft 26.3, Java 25, API version `26.3`.
