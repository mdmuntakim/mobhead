package net.minecraft.entity;

/**
 * Typed accessor for the {@code cave_spider} head, part of the table in {@link Head}.
 */
public final class CaveSpiderHead {

    public static final String ID = "cave_spider";

    private CaveSpiderHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
