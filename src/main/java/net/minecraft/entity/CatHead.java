package net.minecraft.entity;

/**
 * Typed accessor for the {@code cat} head, part of the table in {@link Head}.
 */
public final class CatHead {

    public static final String ID = "cat";

    private CatHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
