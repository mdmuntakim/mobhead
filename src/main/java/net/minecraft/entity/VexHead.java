package net.minecraft.entity;

/**
 * Typed accessor for the {@code vex} head, part of the table in {@link Head}.
 */
public final class VexHead {

    public static final String ID = "vex";

    private VexHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
