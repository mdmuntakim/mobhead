package net.minecraft.entity;

/**
 * Typed accessor for the {@code turtle} head, part of the table in {@link Head}.
 */
public final class TurtleHead {

    public static final String ID = "turtle";

    private TurtleHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
