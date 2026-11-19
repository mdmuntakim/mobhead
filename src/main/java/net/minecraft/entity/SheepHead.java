package net.minecraft.entity;

/**
 * Typed accessor for the {@code sheep} head, part of the table in {@link Head}.
 */
public final class SheepHead {

    public static final String ID = "sheep";

    private SheepHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
