package com.mdmuntakim.mobhead;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class MobHeadItemTest {

    @Test
    void profileNameIsValidForTheMinecraftProfile() {
        assertEquals("Iron_Golem", MobHeadItem.profileName("Iron Golem"));
        assertEquals("Ender_Dragon", MobHeadItem.profileName("Ender Dragon"));
        assertTrue(MobHeadItem.profileName("A very long mob head name").length() <= 16);
    }

    @Test
    void profileIdsAreStableAndDistinct() {
        UUID zombie = MobHeadItem.profileId("zombie");
        assertEquals(zombie, MobHeadItem.profileId("zombie"));
        assertNotEquals(zombie, MobHeadItem.profileId("creeper"));
    }

    @Test
    void percentFormatting() {
        assertEquals("2%", MobHeadItem.percent(0.02));
        assertEquals("50%", MobHeadItem.percent(0.5));
        assertEquals("100%", MobHeadItem.percent(1.0));
    }
}
