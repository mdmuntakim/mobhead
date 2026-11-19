package net.minecraft.entity;

/**
 * Typed accessor for the {@code hoglin} head, part of the table in {@link Head}.
 */
public final class HoglinHead {

    public static final String ID = "hoglin";

    private HoglinHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
