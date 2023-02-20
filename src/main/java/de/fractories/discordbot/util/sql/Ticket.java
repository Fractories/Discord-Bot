package de.fractories.discordbot.util.sql;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;

import java.sql.PreparedStatement;
import java.sql.SQLException;

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
                                        "(id MEDIUMINT NOT NULL AUTO_INCREMENT, client_id VARCHAR(255), channel_id VARCHAR(255), audiochannel_id VARCHAR(255), creation DATE, PRIMARY KEY (id))");
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void add(Member member, TextChannel channel, AudioChannel audioChannel) {
        executor.execute(() -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO " + table() + " (client_id, channel_id, audiochannel_id) VALUES (?, ?, ?)");
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
                PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM " + table() + " WHERE channel_id=?");
                preparedStatement.setString(1, textChannel.getId());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
