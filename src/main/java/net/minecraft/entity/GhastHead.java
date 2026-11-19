package net.minecraft.entity;

/**
 * Typed accessor for the {@code ghast} head, part of the table in {@link Head}.
 */
public final class GhastHead {

    public static final String ID = "ghast";

    private GhastHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
