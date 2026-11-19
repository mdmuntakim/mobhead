package net.minecraft.entity;

/**
 * Typed accessor for the {@code zombie} head, part of the table in {@link Head}.
 */
public final class ZombieHead {

    public static final String ID = "zombie";

    private ZombieHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
