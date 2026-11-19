package net.minecraft.entity;

/**
 * Typed accessor for the {@code tropical_fish} head, part of the table in {@link Head}.
 */
public final class TropicalFishHead {

    public static final String ID = "tropical_fish";

    private TropicalFishHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
