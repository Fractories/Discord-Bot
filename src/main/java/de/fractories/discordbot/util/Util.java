package de.fractories.discordbot.util;

import de.fractories.discordbot.command.SetupCommand;
import de.fractories.discordbot.listener.ContextMenuListener;
import de.fractories.discordbot.util.sql.SqlConnection;
import de.fractories.discordbot.util.sql.Ticket;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Util {

    public static SqlConnection connection;
    public static ExecutorService executor;
    public static Register register = new Register();

    public static void register(JDA jda) {
        executor = Executors.newFixedThreadPool(2);
        connection = new SqlConnection();
        jda.updateCommands().addCommands(
                        Commands.context(Command.Type.USER, "Get user Avatar"),
                        Commands.slash("setup", "A command for Admins to setup the bot")
                                .addSubcommands(new SubcommandData("ticket", "Sets the channel for ticket support"))).queue();
        register.addCommand("setup", new SetupCommand());
        new Ticket();
    }

}
