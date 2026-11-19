package net.minecraft.entity;

/**
 * Typed accessor for the {@code armadillo} head, part of the table in {@link Head}.
 */
public final class ArmadilloHead {

    public static final String ID = "armadillo";

    private ArmadilloHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
