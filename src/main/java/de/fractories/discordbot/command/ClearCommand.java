package de.fractories.discordbot.command;

import de.fractories.discordbot.util.CommandExecutor;
import de.fractories.discordbot.util.Util;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.Objects;

public class ClearCommand implements CommandExecutor {

    @Override
    public void run(SlashCommandInteractionEvent event) {
        for (int i = 0; i < Integer.parseInt(Objects.requireNonNull(event.getSubcommandName())); i++) {
            event.getMessageChannel().deleteMessageById(event.getMessageChannel().getLatestMessageId()).queue();
        }
        event.reply("Done!").queue();
        Util.executor.execute(() -> {
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            event.getMessageChannel().deleteMessageById(event.getMessageChannel().getLatestMessageId()).queue();
        });
    }
}
