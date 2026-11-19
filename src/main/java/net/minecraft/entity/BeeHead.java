package net.minecraft.entity;

/**
 * Typed accessor for the {@code bee} head, part of the table in {@link Head}.
 */
public final class BeeHead {

    public static final String ID = "bee";

    private BeeHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
