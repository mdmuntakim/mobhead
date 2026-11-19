package net.minecraft.entity;

/**
 * Typed accessor for the {@code spider} head, part of the table in {@link Head}.
 */
public final class SpiderHead {

    public static final String ID = "spider";

    private SpiderHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
