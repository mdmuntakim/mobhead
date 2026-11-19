package net.minecraft.entity;

/**
 * Typed accessor for the {@code evoker} head, part of the table in {@link Head}.
 */
public final class EvokerHead {

    public static final String ID = "evoker";

    private EvokerHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
