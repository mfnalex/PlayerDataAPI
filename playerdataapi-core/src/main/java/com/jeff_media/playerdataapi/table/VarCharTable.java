package com.jeff_media.playerdataapi;

import java.sql.Connection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class VarCharTable implements Table<String> {

    private final String tableName;
    private final DataProvider provider;

    public VarCharTable(DataProvider provider, String tableName) {
        this.provider = provider;
        this.tableName = tableName;

        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        try(Connection con = provider.getConnection()) {
            var statement = con.prepareStatement("CREATE TABLE IF NOT EXISTS `" + tableName + "` (`uuid` VARCHAR(36), `key` VARCHAR(255), `value` VARCHAR(255), PRIMARY KEY (`uuid`, `key`))");
            statement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public CompletableFuture<String> get(String uuid, String key) {
        return CompletableFuture.supplyAsync(() -> {
            try (var connection = provider.getConnection()) {
                var statement = connection.prepareStatement("SELECT `value` FROM `" + tableName + "` WHERE `uuid`=? AND `key`=?");
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
            try (var connection = provider.getConnection()) {
                var statement = connection.prepareStatement("INSERT INTO `" + tableName + "` (`uuid`, `key`, `value`) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE `value`=?");
                statement.setString(1, uuid);
                statement.setString(2, key);
                statement.setString(3, data);
                statement.setString(4, data);
                statement.execute();
            } catch (Exception e) {
                throw new CompletionException(e);
            }
            return null;
        });
    }

    @Override
    public CompletableFuture<Boolean> exists(String uuid, String key) {
        return CompletableFuture.supplyAsync(() -> {
            try (var connection = provider.getConnection()) {
                var statement = connection.prepareStatement("SELECT `value` FROM `" + tableName + "` WHERE `uuid`=? AND `key`=?");
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
    public CompletableFuture<Boolean> delete(String uuid, String key) {
        return CompletableFuture.supplyAsync(() -> {
            try (var connection = provider.getConnection()) {
                var statement = connection.prepareStatement("DELETE FROM `" + tableName + "` WHERE `uuid`=? AND `key`=?");
                statement.setString(1, uuid);
                statement.setString(2, key);
                return statement.execute();
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        });
    }
}
