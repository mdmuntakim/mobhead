package net.minecraft.entity;

/**
 * Typed accessor for the {@code zombified_piglin} head, part of the table in {@link Head}.
 */
public final class ZombifiedPiglinHead {

    public static final String ID = "zombified_piglin";

    private ZombifiedPiglinHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
