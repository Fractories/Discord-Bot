package de.fractories.discordbot.listener.ticket;

import de.fractories.discordbot.util.Util;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import java.util.EnumSet;

public class TicketButton extends ListenerAdapter {

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (!event.getComponentId().equals("openTicket")) return;
        Guild guild = event.getGuild();
//        Util.ticket.getPlayerEntryTimes(event.getMember().getId()).thenAcceptAsync(times -> {
            ChannelAction<TextChannel> ticket = guild.createTextChannel(Util.formatText
//                    .replace("%ticket_int%", times.toString())
                    .replace("%user%", event.getUser().getName()), event.getGuildChannel().asStandardGuildChannel().getParentCategory());
            ticket.addRolePermissionOverride(guild.getPublicRole().getIdLong(), null, EnumSet.of(Permission.VIEW_CHANNEL));
            ticket.addMemberPermissionOverride(event.getMember().getIdLong(), EnumSet.of(Permission.VIEW_CHANNEL), null);
            for (String role : Util.ticketRoles) {
                ticket.addRolePermissionOverride(Long.parseLong(role.replace(" ", "")), EnumSet.of(Permission.VIEW_CHANNEL), null);
            }
            ChannelAction<VoiceChannel> ticketVoice = guild.createVoiceChannel(Util.formatVoice
//                    .replace("%ticket_int%", times.toString())
                    .replace("%user%", event.getUser().getName()), event.getGuildChannel().asStandardGuildChannel().getParentCategory());
            ticketVoice.addRolePermissionOverride(guild.getPublicRole().getIdLong(), null, EnumSet.of(Permission.VIEW_CHANNEL));
            for (String role : Util.ticketRoles) {
                ticketVoice.addRolePermissionOverride(Long.parseLong(role.replace(" ", "")), EnumSet.of(Permission.VIEW_CHANNEL), null);
            }
            ticketVoice.addMemberPermissionOverride(event.getMember().getIdLong(), EnumSet.of(Permission.VIEW_CHANNEL), null);
            VoiceChannel voiceChannel = ticketVoice.complete();
            TextChannel complete = ticket.complete();
            Util.botSettings.get("logchannel").thenAcceptAsync(logChannel -> {
                if (Util.logChannel == null) Util.logChannel = event.getGuild().getTextChannelById(logChannel);
                new TicketChannel.Interact(event.getMember(), event.getGuild().getTextChannelById(logChannel), complete);
            });
            event.reply(MessageCreateData.fromContent("Die Channel wurden erstellt! #" + complete.getAsMention())).queue();
            Util.ticket.add(event.getMember(), complete, voiceChannel);
            Util.executor.execute(() -> {
                try {
                    Thread.sleep(100 * 60);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                event.getMessageChannel().deleteMessageById(event.getMessageChannel().getLatestMessageIdLong()).queue();
            });
//        });
        super.onButtonInteraction(event);
    }
}
