package net.minecraft.entity;

/**
 * Typed accessor for the {@code strider} head, part of the table in {@link Head}.
 */
public final class StriderHead {

    public static final String ID = "strider";

    private StriderHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
