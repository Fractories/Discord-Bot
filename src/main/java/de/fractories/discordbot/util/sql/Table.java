package de.fractories.discordbot.util.sql;

import de.fractories.discordbot.Main;
import javafx.util.Pair;

import java.sql.Connection;
import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;

public abstract class Table {
    abstract String table();
    Connection connection = Main.connection.connection;
    ExecutorService executor = Main.executor;
}
