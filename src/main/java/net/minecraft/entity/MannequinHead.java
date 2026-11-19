package net.minecraft.entity;

/**
 * Typed accessor for the {@code mannequin} head, part of the table in {@link Head}.
 */
public final class MannequinHead {

    public static final String ID = "mannequin";

    private MannequinHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
