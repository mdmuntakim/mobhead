package net.minecraft.entity;

/**
 * Typed accessor for the {@code guardian} head, part of the table in {@link Head}.
 */
public final class GuardianHead {

    public static final String ID = "guardian";

    private GuardianHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
