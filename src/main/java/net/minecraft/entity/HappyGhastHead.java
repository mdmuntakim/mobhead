package net.minecraft.entity;

/**
 * Typed accessor for the {@code happy_ghast} head, part of the table in {@link Head}.
 */
public final class HappyGhastHead {

    public static final String ID = "happy_ghast";

    private HappyGhastHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
