package net.minecraft.entity;

/**
 * Typed accessor for the {@code llama} head, part of the table in {@link Head}.
 */
public final class LlamaHead {

    public static final String ID = "llama";

    private LlamaHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
