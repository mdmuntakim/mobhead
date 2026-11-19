package net.minecraft.entity;

/**
 * Typed accessor for the {@code trader_llama} head, part of the table in {@link Head}.
 */
public final class TraderLlamaHead {

    public static final String ID = "trader_llama";

    private TraderLlamaHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
