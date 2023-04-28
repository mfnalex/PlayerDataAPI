package com.jeff_media.playerdataapi;

import com.jeff_media.playerdataapi.table.VarCharTable;
import com.jeff_media.playerdataapi.util.Const;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

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
    private HikariDataSource ds;
    private static final String COLORS_KEY = "colors";

    public DataProvider(String url, String username, String password) {
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.addDataSourceProperty( "cachePrepStmts" , "true" );
        config.addDataSourceProperty( "prepStmtCacheSize" , "250" );
        config.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );
        config.setPoolName("PlayerDataAPI");
        ds = new HikariDataSource( config );
//        if(ds.isRunning()) {
//            System.out.println("HikariCP is running");
//        }
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

    public void setChatColors(UUID uuid, String... hexs) {
        try {
            VarCharTable charTable = getOrCreateVarCharTable("chatcolors");
            String colors = String.join(",", hexs);
            charTable.set(uuid, COLORS_KEY, colors);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<String> getChatColors(UUID uuid) {
        try {
            VarCharTable charTable = getOrCreateVarCharTable("chatcolors");
            return charTable.get(uuid, COLORS_KEY);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
