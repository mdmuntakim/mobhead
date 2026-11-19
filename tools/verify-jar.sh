#!/usr/bin/env bash
# Checks that the packaged jar does not contain net.minecraft.* classes and
# that the relocated head table is present.  Paper's PluginClassLoader refuses
# to define net.minecraft.* classes, so an unrelocated jar cannot enable.
set -euo pipefail
cd "$(dirname "$0")/.."

jar="${1:-target/mobhead-26.3.jar}"
[ -f "$jar" ] || { printf 'error: %s not found, run mvn package first\n' "$jar" >&2; exit 1; }

if jar tf "$jar" | grep -q '^net/minecraft/'; then
    printf 'error: %s contains net.minecraft.* classes, Paper will not load them\n' "$jar" >&2
    jar tf "$jar" | grep '^net/minecraft/' >&2 || true
    exit 1
fi

listing="$(jar tf "$jar")"
grep -q '^com/mdmuntakim/mobhead/internal/entity/Head\.class$' <<<"$listing" \
    || { printf 'error: relocated Head.class is missing from %s\n' "$jar" >&2; exit 1; }

count="$(grep -c '^com/mdmuntakim/mobhead/internal/entity/.*\.class$' <<<"$listing")"
printf 'shaded package ok: %s relocated entity classes, no net.minecraft.*\n' "$count"
