package net.minecraft.entity;

/**
 * Typed accessor for the {@code breeze} head, part of the table in {@link Head}.
 */
public final class BreezeHead {

    public static final String ID = "breeze";

    private BreezeHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
