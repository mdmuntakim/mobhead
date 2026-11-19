package net.minecraft.entity;

/**
 * Typed accessor for the {@code sulfur_cube} head, part of the table in {@link Head}.
 */
public final class SulfurCubeHead {

    public static final String ID = "sulfur_cube";

    private SulfurCubeHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
