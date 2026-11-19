package net.minecraft.entity;

/**
 * Typed accessor for the {@code panda} head, part of the table in {@link Head}.
 */
public final class PandaHead {

    public static final String ID = "panda";

    private PandaHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
