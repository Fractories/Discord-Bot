package de.fractories.discordbot.util;

import de.fractories.discordbot.command.SetupCommand;
import de.fractories.discordbot.listener.ContextMenuListener;
import de.fractories.discordbot.listener.SlashCommandListener;
import de.fractories.discordbot.listener.UserJoinListener;
import de.fractories.discordbot.listener.ticket.TicketButton;
import de.fractories.discordbot.listener.ticket.TicketChannel;
import de.fractories.discordbot.util.sql.SqlConnection;
import de.fractories.discordbot.util.sql.Ticket;
import de.fractories.discordbot.util.sql.BotSettings;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Util {

    public static SqlConnection connection;
    public static ExecutorService executor;
    public static Register register = new Register();
    public static Ticket ticket;
    public static BotSettings botSettings;
    public static TextChannel logChannel;
    public static ArrayList<String> ticketRoles = new ArrayList<>();
    public static String formatVoice, formatText;

    public static void register(JDABuilder jdaBuilder) {
        jdaBuilder.addEventListeners(
                new ContextMenuListener(),
                new SlashCommandListener(),
                new SetupCommand(),
                new UserJoinListener(),
                new TicketButton(),
                new TicketChannel());
        JDA jda = jdaBuilder.build();
        executor = Executors.newFixedThreadPool(2);
        connection = new SqlConnection();
        jda.updateCommands().addCommands(
                Commands.context(Command.Type.USER, "Get user Avatar"),
                Commands.slash("setup", "A command for Admins to setup the bot")
                        .addSubcommands(new SubcommandData("ticket", "Sets the channel for ticket support"), new SubcommandData("defaultrole", "Rang der Standardrolle"))).queue();
        register.addCommand("setup", new SetupCommand());
        ticket = new Ticket();
        ticket.createTable();
        botSettings = new BotSettings();
        botSettings.createTable();
        botSettings.get("roles").thenAccept(roles -> {
            ticketRoles.addAll(Arrays.asList(roles.split(",")));
        });
        botSettings.get("voicechannelformat").thenAccept(it -> formatVoice = it);
        botSettings.get("textchannelformat").thenAccept(it -> formatText = it);
    }

}
