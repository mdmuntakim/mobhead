package net.minecraft.entity;

/**
 * Typed accessor for the {@code creeper} head, part of the table in {@link Head}.
 */
public final class CreeperHead {

    public static final String ID = "creeper";

    private CreeperHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
