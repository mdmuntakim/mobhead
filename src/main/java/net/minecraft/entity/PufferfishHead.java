package net.minecraft.entity;

/**
 * Typed accessor for the {@code pufferfish} head, part of the table in {@link Head}.
 */
public final class PufferfishHead {

    public static final String ID = "pufferfish";

    private PufferfishHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
