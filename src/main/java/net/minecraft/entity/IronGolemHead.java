package net.minecraft.entity;

/**
 * Typed accessor for the {@code iron_golem} head, part of the table in {@link Head}.
 */
public final class IronGolemHead {

    public static final String ID = "iron_golem";

    private IronGolemHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
