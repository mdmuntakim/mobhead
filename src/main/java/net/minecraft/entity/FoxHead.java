package net.minecraft.entity;

/**
 * Typed accessor for the {@code fox} head, part of the table in {@link Head}.
 */
public final class FoxHead {

    public static final String ID = "fox";

    private FoxHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
