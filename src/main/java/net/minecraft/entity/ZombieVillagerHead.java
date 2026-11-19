package net.minecraft.entity;

/**
 * Typed accessor for the {@code zombie_villager} head, part of the table in {@link Head}.
 */
public final class ZombieVillagerHead {

    public static final String ID = "zombie_villager";

    private ZombieVillagerHead() {
    }

    public static Head.Entry entry() {
        return Head.require(ID);
    }
}
