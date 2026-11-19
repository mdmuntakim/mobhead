package net.minecraft.entity;

/**
 * Typed accessor for the {@code phantom} head, part of the table in {@link Head}.
 */
public final class PhantomHead {

    public static final String ID = "phantom";

    private PhantomHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
