package net.minecraft.entity;

/**
 * Typed accessor for the {@code glow_squid} head, part of the table in {@link Head}.
 */
public final class GlowSquidHead {

    public static final String ID = "glow_squid";

    private GlowSquidHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
