package net.minecraft.entity;

/**
 * Typed accessor for the {@code skeleton} head, part of the table in {@link Head}.
 */
public final class SkeletonHead {

    public static final String ID = "skeleton";

    private SkeletonHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
