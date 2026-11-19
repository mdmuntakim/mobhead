package net.minecraft.entity;

/**
 * Typed accessor for the {@code pig} head, part of the table in {@link Head}.
 */
public final class PigHead {

    public static final String ID = "pig";

    private PigHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
