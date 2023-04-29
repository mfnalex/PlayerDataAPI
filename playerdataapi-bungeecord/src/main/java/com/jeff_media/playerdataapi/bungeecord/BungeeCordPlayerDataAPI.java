package com.jeff_media.playerdataapi.bungeecord;

import com.jeff_media.playerdataapi.DataProvider;
import com.jeff_media.playerdataapi.PlayerDataAPI;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class BungeeCordPlayerDataAPI extends Plugin implements PlayerDataAPI {

    private static ConfigurationProvider yamlConfigurationProvider = ConfigurationProvider.getProvider(YamlConfiguration.class);
    private DataProvider provider;
    private Configuration config;

    public Configuration getConfig() {
        return config;
    }

    @Override
    public void onEnable() {

        config = loadConfig("config.yml");

        String mysqlDatabase = getConfig().getString("mysql.database", "test");
        int mysqlPort = getConfig().getInt("mysql.port", 3306);
        String mysqlHost = getConfig().getString("mysql.host", "localhost");
        String mysqlUser = getConfig().getString("mysql.user", "test");
        String mysqlPassword = getConfig().getString("mysql.password", "test");

        String redisHost = getConfig().getString("redis.host", "localhost");
        int redisPort = getConfig().getInt("redis.port", 6379);

        String jdbcUrl = "jdbc:mysql://" + mysqlHost + ":" + mysqlPort + "/" + mysqlDatabase;

        provider = new DataProvider(getConfig().getString("servername"), jdbcUrl, mysqlUser, mysqlPassword, redisHost, redisPort);

    }

    @Override
    public DataProvider getProvider() {
        return Objects.requireNonNull(provider);
    }

    private Configuration saveDefaultConfig(File file, String fileName) {
        Configuration defaultConfig = yamlConfigurationProvider.load(getResourceAsStream(fileName));
        if(!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                yamlConfigurationProvider.save(defaultConfig, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return defaultConfig;
    }

    public Configuration loadConfig(String fileName) {
        File file = new File(getDataFolder(), fileName);
        Configuration defaultConfig = saveDefaultConfig(file, fileName);
        try {
            Configuration config = yamlConfigurationProvider.load(file);
            for(String key : defaultConfig.getKeys()) {
                if(config.contains(key)) {
                    //System.out.println("Loaded value: " + fileName + ", " + key + ": " + config.get(key));
                    continue;
                }
                //System.out.println(fileName + " doesn't have any value for " + key + ", using default value " + defaultConfig.get(key));
                config.set(key, defaultConfig.get(key));
            }
            return config;
        } catch (IOException e) {
            e.printStackTrace();
            return defaultConfig;
        }
    }

}
