package net.minecraft.entity;

/**
 * Typed accessor for the {@code copper_golem} head, part of the table in {@link Head}.
 */
public final class CopperGolemHead {

    public static final String ID = "copper_golem";

    private CopperGolemHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
