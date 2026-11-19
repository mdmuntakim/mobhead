package net.minecraft.entity;

/**
 * Typed accessor for the {@code pillager} head, part of the table in {@link Head}.
 */
public final class PillagerHead {

    public static final String ID = "pillager";

    private PillagerHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
