package net.minecraft.entity;

/**
 * Typed accessor for the {@code wither_skeleton} head, part of the table in {@link Head}.
 */
public final class WitherSkeletonHead {

    public static final String ID = "wither_skeleton";

    private WitherSkeletonHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
