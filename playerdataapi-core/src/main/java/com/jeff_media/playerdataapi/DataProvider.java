package com.jeff_media.playerdataapi;

import com.jeff_media.playerdataapi.table.VarCharTable;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

public class DataProvider {

    private HikariConfig config = new HikariConfig();
    private HikariDataSource ds;

    public DataProvider(String url, String username, String password) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.addDataSourceProperty( "cachePrepStmts" , "true" );
        config.addDataSourceProperty( "prepStmtCacheSize" , "250" );
        config.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );
        ds = new HikariDataSource( config );
    }

    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public VarCharTable getOrCreateVarCharTable(String tableName) throws ExecutionException, InterruptedException {
        return getOrCreateVarCharTable(tableName);
    }

    public VarCharTable getOrCreateVarCharTable(String tableName, int keyLength) throws ExecutionException, InterruptedException {
        return getOrCreateVarCharTable(tableName, keyLength);
    }

    public VarCharTable getOrCreateVarCharTable(String tableName, int keyLength, int valueLength) throws ExecutionException, InterruptedException {
        VarCharTable table = new VarCharTable(this, tableName);
        table.createTableIfNotExists(keyLength, valueLength).get();
        return table;
    }

}
