package net.minecraft.entity;

/**
 * Typed accessor for the {@code piglin_brute} head, part of the table in {@link Head}.
 */
public final class PiglinBruteHead {

    public static final String ID = "piglin_brute";

    private PiglinBruteHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
