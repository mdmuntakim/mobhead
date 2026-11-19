package net.minecraft.entity;

/**
 * Typed accessor for the {@code parched} head, part of the table in {@link Head}.
 */
public final class ParchedHead {

    public static final String ID = "parched";

    private ParchedHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
