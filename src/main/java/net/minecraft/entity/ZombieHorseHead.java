package net.minecraft.entity;

/**
 * Typed accessor for the {@code zombie_horse} head, part of the table in {@link Head}.
 */
public final class ZombieHorseHead {

    public static final String ID = "zombie_horse";

    private ZombieHorseHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
