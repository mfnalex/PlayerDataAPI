package com.jeff_media.playerdataapi.table;

import com.jeff_media.playerdataapi.DataProvider;

import java.sql.Connection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

public class VarCharTable extends AbstractTable<String> {

    public VarCharTable(DataProvider provider, String tableName) {
        super(provider, tableName);
    }

    @Override
    public CompletableFuture<Boolean> createTableIfNotExists(int keyLength) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection con = getConnection()) {
                var statement = con.prepareStatement("CREATE TABLE IF NOT EXISTS `" + getTableName() + "` (`uuid` VARCHAR(36), `key` VARCHAR(" + keyLength + "), `value` VARCHAR(255), PRIMARY KEY (`uuid`, `key`))");
                return statement.execute();
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        });
    }

    @Override
    public CompletableFuture<String> get(String uuid, String key) {
        return CompletableFuture.supplyAsync(() -> {
            try (var connection = getConnection()) {
                var statement = connection.prepareStatement("SELECT `value` FROM `" + getTableName() + "` WHERE `uuid`=? AND `key`=?");
                statement.setString(1, uuid);
                statement.setString(2, key);
                var resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getString("value");
                }
            } catch (Exception e) {
                throw new CompletionException(e);
            }
            return null;
        });
    }

    @Override
    public CompletableFuture<Void> set(String uuid, String key, String data) {
        return CompletableFuture.supplyAsync(() -> {
            try (var connection = getConnection()) {
                var statement = connection.prepareStatement("INSERT INTO `" + getTableName() + "` (`uuid`, `key`, `value`) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE `value`=?");
                statement.setString(1, uuid);
                statement.setString(2, key);
                statement.setString(3, data);
                statement.setString(4, data);
                statement.execute();
                return null;
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        });
    }

    @Override
    public CompletableFuture<Boolean> exists(String uuid, String key) {
        return CompletableFuture.supplyAsync(() -> {
            try (var connection = getConnection()) {
                var statement = connection.prepareStatement("SELECT `value` FROM `" + getTableName() + "` WHERE `uuid`=? AND `key`=?");
                statement.setString(1, uuid);
                statement.setString(2, key);
                var resultSet = statement.executeQuery();
                return resultSet.next();
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        });
    }

    @Override
    public CompletableFuture<Void> delete(String uuid, String key) {
        return CompletableFuture.supplyAsync(() -> {
            try (var connection = getConnection()) {
                var statement = connection.prepareStatement("DELETE FROM `" + getTableName() + "` WHERE `uuid`=? AND `key`=?");
                statement.setString(1, uuid);
                statement.setString(2, key);
                statement.execute();
                return null;
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        });
    }
}
