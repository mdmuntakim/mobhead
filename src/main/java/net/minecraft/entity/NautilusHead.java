package net.minecraft.entity;

/**
 * Typed accessor for the {@code nautilus} head, part of the table in {@link Head}.
 */
public final class NautilusHead {

    public static final String ID = "nautilus";

    private NautilusHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
