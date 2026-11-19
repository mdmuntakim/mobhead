package net.minecraft.entity;

/**
 * Typed accessor for the {@code ender_dragon} head, part of the table in {@link Head}.
 */
public final class EnderDragonHead {

    public static final String ID = "ender_dragon";

    private EnderDragonHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
