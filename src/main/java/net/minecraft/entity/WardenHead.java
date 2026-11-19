package net.minecraft.entity;

/**
 * Typed accessor for the {@code warden} head, part of the table in {@link Head}.
 */
public final class WardenHead {

    public static final String ID = "warden";

    private WardenHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
