package com.jeff_media.playerdataapi.table;

import com.jeff_media.playerdataapi.DataProvider;
import com.jeff_media.playerdataapi.util.Const;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface Table<T> {

    CompletableFuture<T> get(String uuid, String key);
    CompletableFuture<Void> set(String uuid, String key, T data);
    CompletableFuture<Boolean> exists(String uuid, String key);
    CompletableFuture<Void> delete(String uuid, String key);

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

    CompletableFuture<Boolean> createTableIfNotExists(int keyLength);

    default CompletableFuture<Boolean> createTableIfNotExists() {
        return createTableIfNotExists(Const.DEFAULT_KEY_LENGTH);
    }

    CompletableFuture<Void> dropTable();

    String getTableName();

    DataProvider getProvider();

    default Connection getConnection() throws SQLException {
        return getProvider().getConnection();
    }

}
