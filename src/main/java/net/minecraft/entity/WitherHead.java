package net.minecraft.entity;

/**
 * Typed accessor for the {@code wither} head, part of the table in {@link Head}.
 */
public final class WitherHead {

    public static final String ID = "wither";

    private WitherHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
