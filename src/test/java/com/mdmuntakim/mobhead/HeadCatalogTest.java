package com.mdmuntakim.mobhead;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.BufferedReader;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

class HeadCatalogTest {

    @Test
    void parsesTsvRowsAndIgnoresComments() throws Exception {
        String data = "# key\tvalue\n"
                + "zombie\tZm9v\tZombie\thumanoid\n"
                + "\n"
                + "invalid-line-without-tabs\n"
                + "creeper\tYmFy\tCreeper\tmonsters\n";
        HeadCatalog catalog = HeadCatalog.parse(new BufferedReader(new StringReader(data)));

        assertEquals(2, catalog.size());
        assertNotNull(catalog.get("zombie"));
        assertEquals("Zm9v", catalog.get("zombie").value());
        assertEquals("Creeper", catalog.get("CREEPER").name());
        assertNull(catalog.get("enderman"));
    }

    @Test
    void emptyCatalogHasNoTextures() {
        assertEquals(0, HeadCatalog.empty().size());
    }
}
