package net.minecraft.entity;

/**
 * Typed accessor for the {@code wandering_trader} head, part of the table in {@link Head}.
 */
public final class WanderingTraderHead {

    public static final String ID = "wandering_trader";

    private WanderingTraderHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
