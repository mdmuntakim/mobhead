package net.minecraft.entity;

/**
 * Typed accessor for the {@code silverfish} head, part of the table in {@link Head}.
 */
public final class SilverfishHead {

    public static final String ID = "silverfish";

    private SilverfishHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
