package net.minecraft.entity;

/**
 * Typed accessor for the {@code villager} head, part of the table in {@link Head}.
 */
public final class VillagerHead {

    public static final String ID = "villager";

    private VillagerHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
