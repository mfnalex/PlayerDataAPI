package com.jeff_media.playerdataapi;

import com.jeff_media.playerdataapi.table.VarCharTable;
import com.jeff_media.playerdataapi.util.Const;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.JedisPubSub;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class DataProvider {

    private HikariConfig config = new HikariConfig();
    private final JedisPooled pool;
    private final HikariDataSource ds;
    private static final String COLORS_KEY = "colors";
    private final VarCharTable colorsTable;

    public DataProvider(String mysqlUrl, String mysqlUsername, String mysqlPassword, String redisHost, int redisPort) {
        config.setJdbcUrl(mysqlUrl);
        config.setUsername(mysqlUsername);
        config.setPassword(mysqlPassword);
        config.addDataSourceProperty( "cachePrepStmts" , "true" );
        config.addDataSourceProperty( "prepStmtCacheSize" , "250" );
        config.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );
        config.setPoolName("PlayerDataAPI");
        ds = new HikariDataSource( config );
//        if(ds.isRunning()) {
//            System.out.println("HikariCP is running");
//        }

        pool = new JedisPooled(redisHost, redisPort);

        try {
            this.colorsTable = getOrCreateVarCharTable("chatcolors");
        } catch (ExecutionException | InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void redisSubscribe(JedisPubSub jedisPubSub, String... channels) {
        pool.subscribe(jedisPubSub, channels);
    }

    public void redisPublish(String channel, String message) {
        pool.publish(channel, message);
    }

    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public VarCharTable getOrCreateVarCharTable(String tableName) throws ExecutionException, InterruptedException {
        return getOrCreateVarCharTable(tableName, Const.DEFAULT_KEY_LENGTH);
    }

    public VarCharTable getOrCreateVarCharTable(String tableName, int keyLength) throws ExecutionException, InterruptedException {
        return getOrCreateVarCharTable(tableName, keyLength, Const.DEFAULT_VALUE_LENGTH);
    }

    public VarCharTable getOrCreateVarCharTable(String tableName, int keyLength, int valueLength) throws ExecutionException, InterruptedException {
        VarCharTable table = new VarCharTable(this, tableName);
        table.createTableIfNotExists(keyLength, valueLength).get();
        return table;
    }

    public CompletableFuture<Void> setChatColors(UUID uuid, String... hexs) {
        String colors = String.join(",", hexs);
        return colorsTable.set(uuid, COLORS_KEY, colors);
    }

    public CompletableFuture<String> getChatColors(UUID uuid) {
        return colorsTable.get(uuid, COLORS_KEY);
    }
}
