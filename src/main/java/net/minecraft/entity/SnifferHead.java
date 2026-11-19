package net.minecraft.entity;

/**
 * Typed accessor for the {@code sniffer} head, part of the table in {@link Head}.
 */
public final class SnifferHead {

    public static final String ID = "sniffer";

    private SnifferHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
