package com.jeff_media.playerdataapi;

import com.jeff_media.playerdataapi.table.VarCharTable;
import com.jeff_media.playerdataapi.util.Const;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.ApiStatus;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.JedisPubSub;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Provides access to the database and Redis
 */
public class DataProvider {

    private final String serverName;

    public String getServerName() {
        return serverName;
    }

    private HikariConfig config = new HikariConfig();
    private final JedisPooled pool;
    private final HikariDataSource ds;
    private static final String COLORS_KEY = "colors";
    private final VarCharTable colorsTable;

    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    public DataProvider(String serverName, String mysqlUrl, String mysqlUsername, String mysqlPassword, String redisHost, int redisPort) {
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

        this.serverName = serverName;

        executor.execute(() -> {
            pool.subscribe(new RedisDebugPubSub(), "debug");
            //System.out.println("Listening to incoming Redis messages on channel=debug");
        });

        executor.execute(() -> {
            pool.publish("debug", "Server " + serverName + " started");
        });

    }

    /**
     * Subscibes a JedisPubSub to the given channels
     * @param jedisPubSub the JedisPubSub to subscribe
     * @param channels the channels to subscribe to
     */
    public void redisSubscribe(JedisPubSub jedisPubSub, String... channels) {
        executor.execute(() -> pool.subscribe(jedisPubSub, channels));
    }

    /**
     * Publishes a message to the given channel
     * @param channel the channel to publish to
     * @param message the message to publish
     */
    public void redisPublish(String channel, String message) {
        executor.execute(() -> pool.publish(channel, message));
    }

    /**
     * Gets a connection from the database. Use with try-with-resources only!
     * @return a connection from the database
     * @throws SQLException if an error occurs while getting the connection
     */
    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    /**
     * Gets a VarCharTable with the given name. If the table does not exist, it will be created.
     * @param tableName the name of the table
     * @return the VarCharTable
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public VarCharTable getOrCreateVarCharTable(String tableName) throws ExecutionException, InterruptedException {
        return getOrCreateVarCharTable(tableName, Const.DEFAULT_KEY_LENGTH);
    }

    /**
     * Gets a VarCharTable with the given name and max keyLength. If the table does not exist, it will be created.
     * @param tableName the name of the table
     * @param keyLength the max keyLength
     * @return the VarCharTable
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public VarCharTable getOrCreateVarCharTable(String tableName, int keyLength) throws ExecutionException, InterruptedException {
        return getOrCreateVarCharTable(tableName, keyLength, Const.DEFAULT_VALUE_LENGTH);
    }

    /**
     * Gets a VarCharTable with the given name, max keyLength and max valueLength. If the table does not exist, it will be created.
     * @param tableName the name of the table
     * @param keyLength the max keyLength
     * @param valueLength the max valueLength
     * @return the VarCharTable
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public VarCharTable getOrCreateVarCharTable(String tableName, int keyLength, int valueLength) throws ExecutionException, InterruptedException {
        VarCharTable table = new VarCharTable(this, tableName);
        table.createTableIfNotExists(keyLength, valueLength).get();
        return table;
    }

    @ApiStatus.Experimental
    public CompletableFuture<Void> setChatColors(UUID uuid, String... hexs) {
        String colors = String.join(",", hexs);
        return colorsTable.set(uuid, COLORS_KEY, colors);
    }

    @ApiStatus.Experimental
    public CompletableFuture<String> getChatColors(UUID uuid) {
        return colorsTable.get(uuid, COLORS_KEY);
    }
}
