package net.minecraft.entity;

/**
 * Typed accessor for the {@code stray} head, part of the table in {@link Head}.
 */
public final class StrayHead {

    public static final String ID = "stray";

    private StrayHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
