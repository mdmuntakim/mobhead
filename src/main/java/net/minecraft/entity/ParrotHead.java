package net.minecraft.entity;

/**
 * Typed accessor for the {@code parrot} head, part of the table in {@link Head}.
 */
public final class ParrotHead {

    public static final String ID = "parrot";

    private ParrotHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
