package de.fractories.discordbot.util.sql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class SqlConnection {

    public Connection connection;

    public SqlConnection() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            Properties properties = new Properties();
            properties.load(getClass().getClassLoader().getResourceAsStream("sql.properties"));
            String address = properties.getProperty("address");
            String port = properties.getProperty("port");
            String database = properties.getProperty("database");
            String user = properties.getProperty("user");
            String password = properties.getProperty("password");
            connection = DriverManager.getConnection("jdbc:mariadb://" + address + ":" + port + "/" + database + "?autoReconnect=true", user, password);
            System.out.println("Sql connected successfully!");
        } catch (ClassNotFoundException | SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
