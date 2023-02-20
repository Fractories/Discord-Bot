package de.fractories.discordbot.listener;

import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ContextMenuListener extends ListenerAdapter {

    @Override
    public void onUserContextInteraction(UserContextInteractionEvent event) {
        if (event.getName().equals("Get user Avatar"))
            event.reply("Avatar: " + event.getTarget().getEffectiveAvatarUrl()).queue();
    }
}
