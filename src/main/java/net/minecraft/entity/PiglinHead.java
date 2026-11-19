package net.minecraft.entity;

/**
 * Typed accessor for the {@code piglin} head, part of the table in {@link Head}.
 */
public final class PiglinHead {

    public static final String ID = "piglin";

    private PiglinHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
