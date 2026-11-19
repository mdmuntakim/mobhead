package net.minecraft.entity;

/**
 * Typed accessor for the {@code ravager} head, part of the table in {@link Head}.
 */
public final class RavagerHead {

    public static final String ID = "ravager";

    private RavagerHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
