package net.minecraft.entity;

/**
 * Typed accessor for the {@code donkey} head, part of the table in {@link Head}.
 */
public final class DonkeyHead {

    public static final String ID = "donkey";

    private DonkeyHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
