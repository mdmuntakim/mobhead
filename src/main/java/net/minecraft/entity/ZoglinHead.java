package net.minecraft.entity;

/**
 * Typed accessor for the {@code zoglin} head, part of the table in {@link Head}.
 */
public final class ZoglinHead {

    public static final String ID = "zoglin";

    private ZoglinHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
