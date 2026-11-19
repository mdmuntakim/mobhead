package net.minecraft.entity;

/**
 * Typed accessor for the {@code elder_guardian} head, part of the table in {@link Head}.
 */
public final class ElderGuardianHead {

    public static final String ID = "elder_guardian";

    private ElderGuardianHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
