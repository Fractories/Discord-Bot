package de.fractories.discordbot.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

public class SlashCommandListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String command = event.getName();
        if (!Util.register.isRegistered(command)) return;
        String subCommand = event.getSubcommandName();
        Util.register.getExecutor(command).run(event);
        super.onSlashCommandInteraction(event);
    }
}
