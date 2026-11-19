package net.minecraft.entity;

/**
 * Typed accessor for the {@code ocelot} head, part of the table in {@link Head}.
 */
public final class OcelotHead {

    public static final String ID = "ocelot";

    private OcelotHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
