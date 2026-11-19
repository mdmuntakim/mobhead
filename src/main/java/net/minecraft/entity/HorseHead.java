package net.minecraft.entity;

/**
 * Typed accessor for the {@code horse} head, part of the table in {@link Head}.
 */
public final class HorseHead {

    public static final String ID = "horse";

    private HorseHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
