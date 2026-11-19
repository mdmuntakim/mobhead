package net.minecraft.entity;

/**
 * Typed accessor for the {@code salmon} head, part of the table in {@link Head}.
 */
public final class SalmonHead {

    public static final String ID = "salmon";

    private SalmonHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
