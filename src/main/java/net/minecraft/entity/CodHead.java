package net.minecraft.entity;

/**
 * Typed accessor for the {@code cod} head, part of the table in {@link Head}.
 */
public final class CodHead {

    public static final String ID = "cod";

    private CodHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
