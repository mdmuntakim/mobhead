package com.mdmuntakim.mobhead;

import java.util.ArrayList;
import java.util.List;

/**
 * The full passive profile of a mob head: potion effects, attribute bonuses
 * and the a few event driven extras (retaliation, explosion/freeze immunity,
 * teleporting away when hit).
 */
public record Ability(
        Tier tier,
        List<AbilityEffect> effects,
        double healthBonus,
        double armorBonus,
        double knockbackResistance,
        Retaliation retaliation,
        boolean explosionImmune,
        boolean freezeImmune,
        boolean teleportOnHit) {

    public Ability {
        effects = List.copyOf(effects);
    }

    /**
     * The baseline profile of a tier: attribute bonuses from the tier and no
     * potion effects. Used for mobs that explicitly have no extra ability.
     */
    public static Ability of(Tier tier) {
        return builder(tier).build();
    }

    public static Builder builder(Tier tier) {
        return new Builder(tier);
    }

    public boolean hasEffects() {
        return !effects.isEmpty();
    }

    public static final class Builder {
        private final Tier tier;
        private final List<AbilityEffect> effects = new ArrayList<>();
        private double healthBonus;
        private double armorBonus;
        private double knockbackResistance;
        private Retaliation retaliation = Retaliation.NONE;
        private boolean explosionImmune;
        private boolean freezeImmune;
        private boolean teleportOnHit;

        private Builder(Tier tier) {
            this.tier = tier;
            this.healthBonus = tier.healthBonus();
            this.armorBonus = tier.armorBonus();
            this.knockbackResistance = tier.knockbackResistance();
        }

        public Builder effect(AbilityEffect effect) {
            this.effects.add(effect);
            return this;
        }

        public Builder health(double value) {
            this.healthBonus = value;
            return this;
        }

        public Builder armor(double value) {
            this.armorBonus = value;
            return this;
        }

        public Builder knockbackResistance(double value) {
            this.knockbackResistance = value;
            return this;
        }

        public Builder retaliation(Retaliation value) {
            this.retaliation = value;
            return this;
        }

        public Builder explosionImmune() {
            this.explosionImmune = true;
            return this;
        }

        public Builder freezeImmune() {
            this.freezeImmune = true;
            return this;
        }

        public Builder teleportOnHit() {
            this.teleportOnHit = true;
            return this;
        }

        public Ability build() {
            return new Ability(tier, effects, healthBonus, armorBonus,
                    knockbackResistance, retaliation, explosionImmune,
                    freezeImmune, teleportOnHit);
        }
    }
}
