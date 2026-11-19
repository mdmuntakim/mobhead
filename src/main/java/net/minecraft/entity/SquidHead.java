package net.minecraft.entity;

/**
 * Typed accessor for the {@code squid} head, part of the table in {@link Head}.
 */
public final class SquidHead {

    public static final String ID = "squid";

    private SquidHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
