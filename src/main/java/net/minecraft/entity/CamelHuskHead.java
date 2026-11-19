package net.minecraft.entity;

/**
 * Typed accessor for the {@code camel_husk} head, part of the table in {@link Head}.
 */
public final class CamelHuskHead {

    public static final String ID = "camel_husk";

    private CamelHuskHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
