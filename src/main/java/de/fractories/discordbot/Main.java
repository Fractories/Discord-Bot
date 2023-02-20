package de.fractories.discordbot;

import de.fractories.discordbot.util.SlashCommandListener;
import de.fractories.discordbot.util.Util;
import de.fractories.discordbot.listener.ContextMenuListener;
import de.fractories.discordbot.util.sql.SqlConnection;
import de.fractories.discordbot.util.sql.Ticket;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static SqlConnection connection;
    public static ExecutorService executor;

    public static void main(String[] args) throws IOException {
        executor = Executors.newFixedThreadPool(2);
        Properties botProperties = new Properties();
        botProperties.load(new InputStreamReader(Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream("settings.properties"))));
        JDABuilder builder = JDABuilder.createDefault(botProperties.getProperty("token"));
        builder.setEnabledIntents(GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS));
        builder.addEventListeners(new SlashCommandListener(), new ContextMenuListener());
        JDA jda = builder.build();
        jda.updateCommands().addCommands(
                        Commands.context(Command.Type.USER, "Get user Avatar"),
                        Commands.slash("setup", "A command for Admins to setup the bot")
                                .addSubcommands(new SubcommandData("ticket", "Sets the channel for ticket support")))
                .queue();
        Util.register();
        connection = new SqlConnection();
        new Ticket();
    }

}
