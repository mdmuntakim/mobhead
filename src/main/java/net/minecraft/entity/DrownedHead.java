package net.minecraft.entity;

/**
 * Typed accessor for the {@code drowned} head, part of the table in {@link Head}.
 */
public final class DrownedHead {

    public static final String ID = "drowned";

    private DrownedHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
