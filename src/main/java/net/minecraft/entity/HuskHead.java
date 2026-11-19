package net.minecraft.entity;

/**
 * Typed accessor for the {@code husk} head, part of the table in {@link Head}.
 */
public final class HuskHead {

    public static final String ID = "husk";

    private HuskHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
