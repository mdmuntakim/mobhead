package net.minecraft.entity;

/**
 * Typed accessor for the {@code shulker} head, part of the table in {@link Head}.
 */
public final class ShulkerHead {

    public static final String ID = "shulker";

    private ShulkerHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
