package net.minecraft.entity;

/**
 * Typed accessor for the {@code bogged} head, part of the table in {@link Head}.
 */
public final class BoggedHead {

    public static final String ID = "bogged";

    private BoggedHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
