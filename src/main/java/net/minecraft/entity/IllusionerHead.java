package net.minecraft.entity;

/**
 * Typed accessor for the {@code illusioner} head, part of the table in {@link Head}.
 */
public final class IllusionerHead {

    public static final String ID = "illusioner";

    private IllusionerHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
