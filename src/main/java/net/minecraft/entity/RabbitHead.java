package net.minecraft.entity;

/**
 * Typed accessor for the {@code rabbit} head, part of the table in {@link Head}.
 */
public final class RabbitHead {

    public static final String ID = "rabbit";

    private RabbitHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
