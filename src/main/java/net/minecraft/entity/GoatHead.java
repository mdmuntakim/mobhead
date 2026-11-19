package net.minecraft.entity;

/**
 * Typed accessor for the {@code goat} head, part of the table in {@link Head}.
 */
public final class GoatHead {

    public static final String ID = "goat";

    private GoatHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
