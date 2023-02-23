package de.fractories.discordbot.listener;

import de.fractories.discordbot.util.Util;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class UserJoinListener extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        super.onGuildMemberJoin(event);
        Util.botSettings.get("defaultRole").thenAcceptAsync(it ->
            event.getGuild()
                    .addRoleToMember(event.getUser(), Objects.requireNonNull(event.getGuild().getRoleById(it)))
                    .queue());
    }
}
