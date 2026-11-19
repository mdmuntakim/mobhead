package net.minecraft.entity;

/**
 * Typed accessor for the {@code polar_bear} head, part of the table in {@link Head}.
 */
public final class PolarBearHead {

    public static final String ID = "polar_bear";

    private PolarBearHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
