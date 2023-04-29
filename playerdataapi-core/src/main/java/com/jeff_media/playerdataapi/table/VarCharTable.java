package com.jeff_media.playerdataapi.table;

import com.jeff_media.playerdataapi.DataProvider;
import com.jeff_media.playerdataapi.util.Const;
import com.jeff_media.playerdataapi.util.UUIDKeyPair;

import java.sql.Connection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

public class VarCharTable extends AbstractTable<String> {

    public VarCharTable(DataProvider provider, String tableName) {
        super(provider, tableName);
    }

    @Override
    public CompletableFuture<Void> createTableIfNotExists(int keyLength) {
        return createTableIfNotExists(keyLength, Const.DEFAULT_VALUE_LENGTH);
    }

    public CompletableFuture<Void> createTableIfNotExists(int keyLength, int valueLength) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection con = getConnection()) {
                var statement = con.prepareStatement("CREATE TABLE IF NOT EXISTS `" + getTableName() + "` (`uuid` VARCHAR(36), `key` VARCHAR(" + keyLength + "), `value` VARCHAR(" + valueLength + "), PRIMARY KEY (`uuid`, `key`))");
                statement.execute();
                return null;
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        });
    }

    @Override
    public CompletableFuture<String> get(UUIDKeyPair keyPair) {
        return CompletableFuture.supplyAsync(() -> getFromSql(keyPair));
    }

    private String getFromSql(UUIDKeyPair keyPair) {
        try (var connection = getConnection()) {
            var statement = connection.prepareStatement("SELECT `value` FROM `" + getTableName() + "` WHERE `uuid`=? AND `key`=?");
            statement.setString(1, keyPair.uuid());
            statement.setString(2, keyPair.key());
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("value");
            }
        } catch (Exception e) {
            throw new CompletionException(e);
        }
        return null;
    }

    @Override
    public CompletableFuture<Void> set(UUIDKeyPair keyPair, String data) {
        return CompletableFuture.supplyAsync(() -> {
            try (var connection = getConnection()) {
                var statement = connection.prepareStatement("INSERT INTO `" + getTableName() + "` (`uuid`, `key`, `value`) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE `value`=?");
                statement.setString(1, keyPair.uuid());
                statement.setString(2, keyPair.key());
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
    public CompletableFuture<Boolean> exists(UUIDKeyPair keyPair) {
        return CompletableFuture.supplyAsync(() -> {
            try (var connection = getConnection()) {
                var statement = connection.prepareStatement("SELECT `value` FROM `" + getTableName() + "` WHERE `uuid`=? AND `key`=?");
                statement.setString(1, keyPair.uuid());
                statement.setString(2, keyPair.key());
                var resultSet = statement.executeQuery();
                return resultSet.next();
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        });
    }

    @Override
    public CompletableFuture<Void> delete(UUIDKeyPair keyPair) {
        return CompletableFuture.supplyAsync(() -> {
            try (var connection = getConnection()) {
                var statement = connection.prepareStatement("DELETE FROM `" + getTableName() + "` WHERE `uuid`=? AND `key`=?");
                statement.setString(1, keyPair.uuid());
                statement.setString(2, keyPair.key());
                statement.execute();
                return null;
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        });
    }
}
