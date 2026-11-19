package net.minecraft.entity;

/**
 * Typed accessor for the {@code magma_cube} head, part of the table in {@link Head}.
 */
public final class MagmaCubeHead {

    public static final String ID = "magma_cube";

    private MagmaCubeHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
