package com.jeff_media.playerdataapi.data;

import com.google.gson.Gson;
import com.jeff_media.playerdataapi.PlayerDataAPI;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class GlobalPlayer {

    final @NotNull UUID uuid;
    final @NotNull String name;
    String serverName;

    public GlobalPlayer(@NotNull UUID uuid, @NotNull String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getServerName() {
        return serverName;
    }

    void setServerName(String serverName) {
        this.serverName = serverName;
    }

    @Override
    public String toString() {
        return "GlobalPlayer{" +
                "uuid=" + uuid +
                ", name='" + name + '\'' +
                ", serverName='" + serverName + '\'' +
                '}';
    }
}
