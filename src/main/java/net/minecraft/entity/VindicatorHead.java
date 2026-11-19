package net.minecraft.entity;

/**
 * Typed accessor for the {@code vindicator} head, part of the table in {@link Head}.
 */
public final class VindicatorHead {

    public static final String ID = "vindicator";

    private VindicatorHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
