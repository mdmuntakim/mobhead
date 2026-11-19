package net.minecraft.entity;

/**
 * Typed accessor for the {@code mule} head, part of the table in {@link Head}.
 */
public final class MuleHead {

    public static final String ID = "mule";

    private MuleHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
