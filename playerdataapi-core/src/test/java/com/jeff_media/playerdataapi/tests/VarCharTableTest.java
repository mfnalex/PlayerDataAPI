import com.jeff_media.playerdataapi.DataProvider;
import com.jeff_media.playerdataapi.table.VarCharTable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class VarCharTableTest {

    UUID uuid = UUID.randomUUID();

    @Test


    @Test
    public void testSetAndGetValues() {
        DataProvider provider = new DataProvider("jdbc:mysql://localhost:3306/minecrafttest", "minecrafttest","minecrafttest");
        try {
            VarCharTable table = provider.getOrCreateVarCharTable("test");
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
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

    }
}
