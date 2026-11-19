package net.minecraft.entity;

/**
 * Typed accessor for the {@code allay} head, part of the table in {@link Head}.
 */
public final class AllayHead {

    public static final String ID = "allay";

    private AllayHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
