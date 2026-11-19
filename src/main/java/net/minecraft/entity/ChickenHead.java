package net.minecraft.entity;

/**
 * Typed accessor for the {@code chicken} head, part of the table in {@link Head}.
 */
public final class ChickenHead {

    public static final String ID = "chicken";

    private ChickenHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
