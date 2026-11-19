package net.minecraft.entity;

/**
 * Typed accessor for the {@code frog} head, part of the table in {@link Head}.
 */
public final class FrogHead {

    public static final String ID = "frog";

    private FrogHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
