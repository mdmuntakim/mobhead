#!/usr/bin/env bash
# Build MobHead in a scratch friendly way; the jar ends up in target/.
set -euo pipefail
cd "$(dirname "$0")"

mvn -B verify
printf 'built %s\n' "$(pwd)/target/mobhead-26.3.jar"
