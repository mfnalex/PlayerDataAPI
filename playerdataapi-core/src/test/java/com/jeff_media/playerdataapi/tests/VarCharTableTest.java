package com.jeff_media.playerdataapi.tests;

import com.jeff_media.playerdataapi.UnitTest;
import com.jeff_media.playerdataapi.table.VarCharTable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

public class VarCharTableTest extends UnitTest {

    UUID uuid = UUID.randomUUID();

    @Test
    public void testCreateAndDeleteTable() {
        try {
            VarCharTable table = getProvider().getOrCreateVarCharTable("test");
            table.dropTable().get();
            Assertions.assertThrows(ExecutionException.class, () -> table.get(uuid, "key1").get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSetAndGetValues() {

        try {
            VarCharTable table = getProvider().getOrCreateVarCharTable("test");
            boolean key1Exists = table.exists(uuid, "key1").get();
            Assertions.assertNull(table.get(uuid, "key1").get());
            Assertions.assertFalse(key1Exists);

            table.set(uuid, "key1", "value1").get();

            key1Exists = table.exists(uuid, "key1").get();
            Assertions.assertTrue(key1Exists);

            String result = table.get(uuid, "key1").get();
            Assertions.assertEquals("value1", result);
            table.delete(uuid, "key1").get();

            Assertions.assertFalse(table.exists(uuid, "key1").get());
            Assertions.assertNull(table.get(uuid, "key1").get());

            table.dropTable().get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

    }
}
