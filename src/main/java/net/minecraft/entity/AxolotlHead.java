package net.minecraft.entity;

/**
 * Typed accessor for the {@code axolotl} head, part of the table in {@link Head}.
 */
public final class AxolotlHead {

    public static final String ID = "axolotl";

    private AxolotlHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
