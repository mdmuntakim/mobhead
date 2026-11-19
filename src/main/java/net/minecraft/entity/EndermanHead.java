package net.minecraft.entity;

/**
 * Typed accessor for the {@code enderman} head, part of the table in {@link Head}.
 */
public final class EndermanHead {

    public static final String ID = "enderman";

    private EndermanHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
