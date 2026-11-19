package net.minecraft.entity;

/**
 * Typed accessor for the {@code tadpole} head, part of the table in {@link Head}.
 */
public final class TadpoleHead {

    public static final String ID = "tadpole";

    private TadpoleHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
