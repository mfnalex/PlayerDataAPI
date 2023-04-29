package com.jeff_media.playerdataapi.util;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record UUIDKeyPair (@NotNull String uuid, @NotNull String key) {
    public UUIDKeyPair {
        Objects.requireNonNull(uuid);
        Objects.requireNonNull(key);
        if(key.contains("|")) {
            throw new IllegalArgumentException("Key cannot contain |");
        }
    }
}
