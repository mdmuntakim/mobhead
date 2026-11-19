package net.minecraft.entity;

/**
 * Typed accessor for the {@code creaking} head, part of the table in {@link Head}.
 */
public final class CreakingHead {

    public static final String ID = "creaking";

    private CreakingHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
