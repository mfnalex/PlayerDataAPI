package com.jeff_media.playerdataapi;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface Table<T> {

    CompletableFuture<T> get(String uuid, String key);
    CompletableFuture<Void> set(String uuid, String key, T data);
    CompletableFuture<Boolean> exists(String uuid, String key);
    CompletableFuture<Boolean> delete(String uuid, String key);

    default CompletableFuture<T> get(UUID uuid, String key) {
        return get(uuid.toString(), key);
    }

    default CompletableFuture<Void> set(UUID uuid, String key, T data) {
        return set(uuid.toString(), key, data);
    }

    default CompletableFuture<Boolean> exists(UUID uuid, String key) {
        return exists(uuid.toString(), key);
    }

    default CompletableFuture<Boolean> delete(UUID uuid, String key) {
        return delete(uuid.toString(), key);
    }

}
