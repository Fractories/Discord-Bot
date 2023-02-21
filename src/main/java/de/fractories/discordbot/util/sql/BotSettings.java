package de.fractories.discordbot.util.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

public class BotSettings extends Table {
    @Override
    String table() {
        return "botsettings";
    }

    public void createTable() {
        executor.execute(() -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + table() + "(id MEDIUMINT NOT NULL AUTO_INCREMENT, k VARCHAR(255), value VARCHAR(255), PRIMARY KEY (id))");
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void add(String key, String value) {
        executor.execute(() -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO " + table() + " (k, value) VALUES (?, ?)");
                preparedStatement.setString(1, key);
                preparedStatement.setString(2, value);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void remove(String key) {
        executor.execute(() -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM " + table() + " WHERE k=?");
                preparedStatement.setString(1, key);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public CompletableFuture<String> get(String key) {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        executor.execute(() -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT value FROM " + table() + " WHERE k=?");
                preparedStatement.setString(1, key);
                if (preparedStatement.executeQuery().next()) {
                    completableFuture.complete(preparedStatement.getResultSet().getString(1));
                }
            } catch (SQLException ignored) {}
        });
        return completableFuture;
    }
}
