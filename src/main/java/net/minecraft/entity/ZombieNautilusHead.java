package net.minecraft.entity;

/**
 * Typed accessor for the {@code zombie_nautilus} head, part of the table in {@link Head}.
 */
public final class ZombieNautilusHead {

    public static final String ID = "zombie_nautilus";

    private ZombieNautilusHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
