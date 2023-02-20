package de.fractories.discordbot.listener;

import de.fractories.discordbot.util.Util;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SlashCommandListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String command = event.getName();
        if (!Util.register.isRegistered(command)) return;
        Util.register.getExecutor(command).run(event);
        super.onSlashCommandInteraction(event);
    }
}
