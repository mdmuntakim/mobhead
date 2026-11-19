package net.minecraft.entity;

/**
 * Typed accessor for the {@code slime} head, part of the table in {@link Head}.
 */
public final class SlimeHead {

    public static final String ID = "slime";

    private SlimeHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
