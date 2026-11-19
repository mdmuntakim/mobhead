package net.minecraft.entity;

/**
 * Typed accessor for the {@code skeleton_horse} head, part of the table in {@link Head}.
 */
public final class SkeletonHorseHead {

    public static final String ID = "skeleton_horse";

    private SkeletonHorseHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
