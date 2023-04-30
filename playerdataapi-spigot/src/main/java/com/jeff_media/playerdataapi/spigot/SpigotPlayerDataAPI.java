package com.jeff_media.playerdataapi.spigot;

import com.jeff_media.playerdataapi.DataProvider;
import com.jeff_media.playerdataapi.PlayerDataAPI;
import com.jeff_media.playerdataapi.events.GlobalEventManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class SpigotPlayerDataAPI extends JavaPlugin implements PlayerDataAPI {

    private DataProvider provider;
    private final GlobalEventManager globalEventManager = new GlobalEventManager(this);

    @Override
    public void onEnable() {
        saveDefaultConfig();
        String dataBase = getConfig().getString("mysql.database", "minecrafttest");
        int port = getConfig().getInt("mysql.port", 3306);
        String host = getConfig().getString("mysql.host", "localhost");
        String user = getConfig().getString("mysql.user", "minecrafttest");
        String password = getConfig().getString("mysql.password", "minecrafttest");

        String jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + dataBase;

        provider = new DataProvider(getConfig().getString("servername"),jdbcUrl, user, password, getConfig().getString("redis.host"), getConfig().getInt("redis.port"));
    }

    @Override
    public DataProvider getProvider() {
        return Objects.requireNonNull(provider);
    }

    @Override
    public GlobalEventManager getGlobalEventManager() {
        return globalEventManager;
    }
}
