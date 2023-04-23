package com.jeff_media.playerdataapi.spigot;

import com.jeff_media.playerdataapi.DataProvider;
import com.jeff_media.playerdataapi.PlayerDataAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class SpigotPlayerDataAPI extends JavaPlugin implements PlayerDataAPI {

    DataProvider provider;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        String dataBase = getConfig().getString("database", "minecrafttest");
        int port = getConfig().getInt("port", 3306);
        String host = getConfig().getString("host", "localhost");
        String user = getConfig().getString("user", "minecrafttest");
        String password = getConfig().getString("password", "minecrafttest");

        String jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + dataBase;

        provider = new DataProvider(jdbcUrl, user, password);

    }

    @Override
    public DataProvider getProvider() {
        return Objects.requireNonNull(provider);
    }
}
