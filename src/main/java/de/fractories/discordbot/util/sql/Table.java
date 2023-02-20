package de.fractories.discordbot.util.sql;

import de.fractories.discordbot.util.Util;

import java.sql.Connection;
import java.util.concurrent.ExecutorService;

public abstract class Table {
    abstract String table();
    Connection connection = Util.connection.connection;
    ExecutorService executor = Util.executor;
}
