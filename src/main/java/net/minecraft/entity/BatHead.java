package net.minecraft.entity;

/**
 * Typed accessor for the {@code bat} head, part of the table in {@link Head}.
 */
public final class BatHead {

    public static final String ID = "bat";

    private BatHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
