#!/usr/bin/env python3
"""Build src/main/resources/mobheads.tsv from the Minecraft-Heads.com API.

The API is used for free plugins. The values are ordinary self contained
texture properties that point at textures.minecraft.net, so the server does
not need network access at runtime: the client fetches the skin it already
knows how to fetch.

Usage:
    python3 tools/generate-head-catalog.py          # use the HTTP cache
    python3 tools/generate-head-catalog.py --refresh # fetch the API again
"""

from __future__ import annotations

import base64
import json
import pathlib
import sys
import urllib.request

ROOT = pathlib.Path(__file__).resolve().parent.parent
CACHE = ROOT / ".cache" / "minecraft-heads"
OUTPUT = ROOT / "src" / "main" / "resources" / "mobheads.tsv"
ENTITIES = ROOT / "tools" / "mobs.txt"
CATEGORIES = ("monsters", "animals", "humanoid")
API = "https://minecraft-heads.com/scripts/api.php?cat={category}&tags=true"

# The API names a few heads differently from the entity name.
ALIASES = {
    "TRADER_LLAMA": ["Llama"],
    "TROPICAL_FISH": ["Tropical Fish (clownfish)"],
    "SULFUR_CUBE": ["Magma Cube"],
    "MANNEQUIN": ["Player"],
}

# Display names that cannot be derived from the enum key.
DISPLAY = {
    "MOOSHROOM": "Mooshroom",
    "SNOW_GOLEM": "Snow Golem",
    "TRADER_LLAMA": "Trader Llama",
    "WITHER": "Wither",
    "ENDER_DRAGON": "Ender Dragon",
    "ZOMBIFIED_PIGLIN": "Zombified Piglin",
    "PIGLIN_BRUTE": "Piglin Brute",
    "MAGMA_CUBE": "Magma Cube",
    "CAVE_SPIDER": "Cave Spider",
    "ELDER_GUARDIAN": "Elder Guardian",
    "WITHER_SKELETON": "Wither Skeleton",
    "SKELETON_HORSE": "Skeleton Horse",
    "ZOMBIE_HORSE": "Zombie Horse",
    "ZOMBIE_VILLAGER": "Zombie Villager",
    "WANDERING_TRADER": "Wandering Trader",
    "TROPICAL_FISH": "Tropical Fish",
    "GLOW_SQUID": "Glow Squid",
    "HAPPY_GHAST": "Happy Ghast",
    "CAMEL_HUSK": "Camel Husk",
    "ZOMBIE_NAUTILUS": "Zombie Nautilus",
    "SULFUR_CUBE": "Sulfur Cube",
    "COPPER_GOLEM": "Copper Golem",
}

DECORATIVE_TAGS = (
    "hat", "cap", "helmet", "mask", "sunglasses", "glasses", "horns",
    "headphones", "flower", "hood", "crown", "8-bit", "pixel", "glowing",
    "dead", "angry", "sad", "crying", "baby", "old", "grayed", "programmer",
    "figurine", "plush", "dungeons", "hypixel", "inspired", "skeleton",
)


def fetch(category: str, refresh: bool) -> list[dict]:
    CACHE.mkdir(parents=True, exist_ok=True)
    path = CACHE / f"{category}.json"
    if path.exists() and not refresh:
        return json.loads(path.read_text(encoding="utf-8"))
    print(f"fetching {category} ...", file=sys.stderr)
    request = urllib.request.Request(API.format(category=category),
                                     headers={"User-Agent": "mobhead-catalog/1.0"})
    with urllib.request.urlopen(request, timeout=120) as response:
        data = response.read()
    path.write_bytes(data)
    return json.loads(data)


def display_name(key: str) -> str:
    if key in DISPLAY:
        return DISPLAY[key]
    return " ".join(part.capitalize() for part in key.split("_"))


def score(entry: dict, names: list[str]) -> int:
    name = entry.get("name", "")
    tags = [tag.strip().lower() for tag in entry.get("tags", "").split(",") if tag.strip()]
    value = 0
    if name.lower() in (candidate.lower() for candidate in names):
        value += 60
    else:
        value -= 20
    if "vanilla mob" in tags:
        value += 100
    if any(tag in (candidate.lower() for candidate in names) for tag in tags):
        value += 15
    for tag in tags:
        if any(bad in tag for bad in DECORATIVE_TAGS):
            value -= 20
    if "(" in name or "[" in name:
        value -= 5
    if "vanilla" in tags:
        value += 10
    value -= max(0, len(tags) - 1) * 2
    return value


def texture_url(value: str) -> str | None:
    try:
        decoded = base64.b64decode(value).decode("utf-8")
        return json.loads(decoded)["textures"]["SKIN"]["url"]
    except (ValueError, KeyError, UnicodeDecodeError):
        return None


def main() -> int:
    refresh = "--refresh" in sys.argv
    entries: list[tuple[str, dict]] = []
    for category in CATEGORIES:
        for entry in fetch(category, refresh):
            entries.append((category, entry))

    keys = [line.strip() for line in ENTITIES.read_text(encoding="utf-8").splitlines()
            if line.strip() and not line.startswith("#")]

    rows: list[tuple[str, str, str, str]] = []
    missing: list[str] = []
    for key in keys:
        display = display_name(key)
        names = [display, *ALIASES.get(key, [])]
        candidates = [entry for _, entry in entries
                      if entry.get("name", "").lower() in (name.lower() for name in names)]
        if not candidates:
            missing.append(key)
            continue
        best = max(candidates, key=lambda entry: score(entry, names))
        url = texture_url(best["value"])
        if url is None:
            missing.append(key + " (bad texture)")
            continue
        # The client is happiest with HTTPS; the hash is the same.
        best["value"] = base64.b64encode(
            json.dumps({"textures": {"SKIN": {"url": url.replace("http://", "https://")}}},
                       separators=(",", ":")).encode("utf-8")
        ).decode("ascii")
        category = next(cat for cat, entry in entries if entry is best)
        rows.append((key.lower(), best["value"], best["name"], category))

    OUTPUT.parent.mkdir(parents=True, exist_ok=True)
    with OUTPUT.open("w", encoding="utf-8", newline="\n") as handle:
        handle.write("# entity_key\ttextures_value\tname\tcategory\n")
        handle.write("# Generated by tools/generate-head-catalog.py from minecraft-heads.com (free plugin use).\n")
        for row in rows:
            handle.write("\t".join(row) + "\n")

    print(f"wrote {len(rows)} heads to {OUTPUT.relative_to(ROOT)}", file=sys.stderr)
    if missing:
        print("no head found for: " + ", ".join(missing), file=sys.stderr)
        return 1
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
