package com.jeff_media.playerdataapi.table;

import com.jeff_media.playerdataapi.DataProvider;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public abstract class AbstractTable<T> implements Table<T> {

    private DataProvider provider;
    private String tableName;

    public AbstractTable(DataProvider provider, String tableName) {
        this.provider = provider;
        this.tableName = tableName;
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    @Override
    public CompletableFuture<Void> dropTable() {
        return CompletableFuture.supplyAsync(() -> {
            try (var connection = getConnection()) {
                var statement = connection.prepareStatement("DROP TABLE IF EXISTS `" + getTableName() + "`");
                statement.execute();
                return null;
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        });
    }

    @Override
    public DataProvider getProvider() {
        return provider;
    }
}
