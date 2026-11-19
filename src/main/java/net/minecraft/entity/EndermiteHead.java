package net.minecraft.entity;

/**
 * Typed accessor for the {@code endermite} head, part of the table in {@link Head}.
 */
public final class EndermiteHead {

    public static final String ID = "endermite";

    private EndermiteHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
