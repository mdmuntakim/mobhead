package net.minecraft.entity;

/**
 * Typed accessor for the {@code cow} head, part of the table in {@link Head}.
 */
public final class CowHead {

    public static final String ID = "cow";

    private CowHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
