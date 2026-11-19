package net.minecraft.entity;

/**
 * Typed accessor for the {@code camel} head, part of the table in {@link Head}.
 */
public final class CamelHead {

    public static final String ID = "camel";

    private CamelHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
