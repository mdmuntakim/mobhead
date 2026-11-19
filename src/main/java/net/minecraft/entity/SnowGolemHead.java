package net.minecraft.entity;

/**
 * Typed accessor for the {@code snow_golem} head, part of the table in {@link Head}.
 */
public final class SnowGolemHead {

    public static final String ID = "snow_golem";

    private SnowGolemHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
