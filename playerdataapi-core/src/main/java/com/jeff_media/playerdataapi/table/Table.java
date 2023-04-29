package com.jeff_media.playerdataapi.table;

import com.jeff_media.playerdataapi.DataProvider;
import com.jeff_media.playerdataapi.util.UUIDKeyPair;
import com.jeff_media.playerdataapi.util.Const;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface Table<T> {

    /*
    UUIDKeyPairs
     */
    CompletableFuture<T> get(UUIDKeyPair keyPair);
    CompletableFuture<Void> set(UUIDKeyPair keyPair, T data);
    CompletableFuture<Boolean> exists(UUIDKeyPair keyPair);
    CompletableFuture<Void> delete(UUIDKeyPair keyPair);

    /*
    String, String pairs
     */
    default CompletableFuture<T> get(String uuid, String key) {
        return get(new UUIDKeyPair(uuid, key));
    };
    default CompletableFuture<Void> set(String uuid, String key, T data) {
        return set(new UUIDKeyPair(uuid, key), data);
    };
    default CompletableFuture<Boolean> exists(String uuid, String key) {
        return exists(new UUIDKeyPair(uuid, key));
    };
    default CompletableFuture<Void> delete(String uuid, String key) {
        return delete(new UUIDKeyPair(uuid, key));
    };

    /*
    UUID, String pairs
     */
    default CompletableFuture<T> get(UUID uuid, String key) {
        return get(uuid.toString(), key);
    }

    default CompletableFuture<Void> set(UUID uuid, String key, T data) {
        return set(uuid.toString(), key, data);
    }

    default CompletableFuture<Boolean> exists(UUID uuid, String key) {
        return exists(uuid.toString(), key);
    }

    default CompletableFuture<Void> delete(UUID uuid, String key) {
        return delete(uuid.toString(), key);
    }

    CompletableFuture<Void> createTableIfNotExists(int keyLength);

    default CompletableFuture<Void> createTableIfNotExists() {
        return createTableIfNotExists(Const.DEFAULT_KEY_LENGTH);
    }

    CompletableFuture<Void> dropTable();

    String getTableName();

    DataProvider getProvider();

    default Connection getConnection() throws SQLException {
        return getProvider().getConnection();
    }

    default CompletableFuture<Void> onJoin(UUID uuid) {
        return CompletableFuture.completedFuture(null);
    }

    default CompletableFuture<Void> onQuit(UUID uuid) {
        return CompletableFuture.completedFuture(null);
    }

    //Map<UUIDKeyPair, T> getCache();

}
