package de.fractories.discordbot.util.sql;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

public class Ticket extends Table {
    public Ticket() {
        createTable();
    }

    @Override
    String table() {
        return "ticket";
    }

    public void createTable() {
        executor.execute(() -> {
            try {
                PreparedStatement preparedStatement =
                        connection.prepareStatement(
                                "CREATE TABLE IF NOT EXISTS " + table() +
                                        "(id MEDIUMINT NOT NULL AUTO_INCREMENT, client_id VARCHAR(255), textchannel_id VARCHAR(255), voicechannel_id VARCHAR(255), creation DATE, PRIMARY KEY (id))");
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void add(Member member, TextChannel channel, AudioChannel audioChannel) {
        executor.execute(() -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO " + table() + " (client_id, textchannel_id, voicechannel_id) VALUES (?, ?, ?)");
                preparedStatement.setString(1, member.getId());
                preparedStatement.setString(2, channel.getId());
                preparedStatement.setString(3, audioChannel.getId());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void remove(TextChannel textChannel) {
        executor.execute(() -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM " + table() + " WHERE textchannel_id=?");
                preparedStatement.setString(1, textChannel.getId());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void remove(VoiceChannel voiceChannel) {
        executor.execute(() -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM " + table() + " WHERE voicechannel_id=?");
                preparedStatement.setString(1, voiceChannel.getId());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void remove(String userId) {
        executor.execute(() -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM " + table() + " WHERE client_id=?");
                preparedStatement.setString(1, userId);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public CompletableFuture<Integer> getPlayerEntryTimes(String userId) {
        CompletableFuture<Integer> completableFuture = new CompletableFuture<>();
        executor.execute(() -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT client_id FROM " + table() + " WHERE client_id=?");
                preparedStatement.setString(1, userId);
                int i = 1;
                while (preparedStatement.executeQuery().next()) {
                    i++;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        return completableFuture;
    }

    public CompletableFuture<String> getOwner(String channelId) {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        executor.execute(() -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT client_id FROM " + table() + " WHERE textchannel_id=?");
                preparedStatement.setString(1, channelId);
                if (preparedStatement.executeQuery().next()) {
                    completableFuture.complete(preparedStatement.getResultSet().getString(1));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        return completableFuture;
    }

    public CompletableFuture<String> getVoiceChannel(String channelId) {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        executor.execute(() -> {
            try {
              PreparedStatement preparedStatement = connection.prepareStatement("SELECT voicechannel_id FROM " + table() + " WHERE textchannel_id=?");
              preparedStatement.setString(1, channelId);
                if (preparedStatement.executeQuery().next()) {
                    completableFuture.complete(preparedStatement.getResultSet().getString(1));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        return completableFuture;
    }
}
