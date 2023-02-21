package de.fractories.discordbot.listener.ticket;

import de.fractories.discordbot.util.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.GenericSelectMenuInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.internal.entities.MemberImpl;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

public class TicketChannel extends ListenerAdapter {

    public boolean listContainsOneOf(List<Role> entity, List<String> needed) {
        for (Role role : entity) {
            if (needed.contains(role.getId()))
                return true;
        }
        return false;
    }
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getComponentId().equals("closeTicket")) {
            Util.ticket.getOwner(event.getChannel().getId()).thenAcceptAsync(owner -> {
                if (listContainsOneOf(event.getMember().getRoles(), Util.ticketRoles) || event.getMember().getId().equals(owner)) {
                    event.replyModal(Modal.create("closeModal", "Ticket schließen!").addActionRow(
                            TextInput.create("closeReason", "Gebe einen Grund ein. Halte dich kurz.", TextInputStyle.PARAGRAPH).build()).build()).queue();
                } else event.reply("Mit diesem Button darfst du nicht interagieren").queue();
            });
        } else if (event.getComponentId().equals("addUser")) {
            System.out.println(Util.ticketRoles);
            Util.ticket.getOwner(event.getChannel().getId()).thenAcceptAsync(owner -> {
                if (listContainsOneOf(event.getMember().getRoles(), Util.ticketRoles)  || event.getMember().getId().equals(owner)) {
                    event.reply("Wähle einen Benutzer zum Hinzufügen aus!").addActionRow(
                            EntitySelectMenu.create("A1", EntitySelectMenu.SelectTarget.USER).build()
                    ).queue();
                } else event.reply("Mit diesem Button darfst du nicht interagieren").queue();
            });
        } else if (event.getComponentId().equals("removeUser")) {
            Util.ticket.getOwner(event.getChannel().getId()).thenAcceptAsync(owner -> {
                if (listContainsOneOf(event.getMember().getRoles(), Util.ticketRoles)  || event.getMember().getId().equals(owner)) {
                    event.reply("Wähle einen Benutzer zum entfernen aus!").addActionRow(
                            EntitySelectMenu.create("A2", EntitySelectMenu.SelectTarget.USER).build()
                    ).queue();
                } else event.reply("Mit diesem Button darfst du nicht interagieren").queue();
            });
        }
        super.onButtonInteraction(event);
    }

    @Override
    public void onGenericSelectMenuInteraction(@NotNull GenericSelectMenuInteractionEvent event) {
        if (event.getComponentId().equals("A1")) {
            event.getMessageChannel().deleteMessageById(event.getMessageChannel().getLatestMessageId()).queue();
            event.getChannel().asTextChannel()
                    .upsertPermissionOverride((MemberImpl) event.getValues().get(0))
                    .grant(Permission.VIEW_CHANNEL).queue();
            Util.ticket.getVoiceChannel(event.getChannel().getId()).thenAcceptAsync(it -> {
                event.getGuild().getVoiceChannelById(it)
                        .upsertPermissionOverride((MemberImpl) event.getValues().get(0))
                        .grant(Permission.VIEW_CHANNEL).queue();
            });
            event.reply("Du hast den Benutzer " + ((Member) event.getValues().get(0)).getEffectiveName() + " hinzugefügt").queue();
        } else if (event.getComponentId().equals("A2")) {
            if (!((MemberImpl) event.getValues().get(0)).getRoles().containsAll(Util.ticketRoles) && ((Member) event.getValues().get(0)).getId() != event.getMember().getId()) {
                event.reply("Den Benutzer" + ((Member) event.getValues().get(0)).getEffectiveName() + " kannst du nicht entfernen").queue();
                return;
            }
            event.getMessageChannel().deleteMessageById(event.getMessageChannel().getLatestMessageId()).queue();
            event.getChannel().asTextChannel()
                    .upsertPermissionOverride((MemberImpl) event.getValues().get(0))
                    .clear(Permission.VIEW_CHANNEL).queue();
            Util.ticket.getVoiceChannel(event.getChannel().getId()).thenAcceptAsync(it -> {
                event.getGuild().getVoiceChannelById(it)
                        .upsertPermissionOverride((MemberImpl) event.getValues().get(0))
                        .clear(Permission.VIEW_CHANNEL).queue();
            });
            event.reply("Du hast den Benutzer " + ((Member) event.getValues().get(0)).getEffectiveName() + " entfernt").queue();
        }
        super.onGenericSelectMenuInteraction(event);
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        if (!event.getModalId().equals("closeModal"))
            return;
        event.reply("Das Ticket wird geschlossen").queue();
        Util.botSettings.get("logchannel").thenAcceptAsync(logChannel -> {
            event.getGuild().getTextChannelById(logChannel).sendMessageEmbeds(new EmbedBuilder().setTitle("Ticket geschlossen!").addField("Das Ticket von " + event.getMember().getEffectiveName() + " wurde geschlossen!", "Grund dafür ist:\n" + event.getValue("closeReason").getAsString(), false).setColor(Color.RED).build()).queue();
        });
        Util.ticket.getVoiceChannel(event.getMessageChannel().getId()).thenAccept(channel -> {
            event.getGuild().getVoiceChannelById(channel).delete().queue();
            Util.ticket.remove(event.getChannel().asTextChannel());
            event.getChannel().delete().queue();
        });
        super.onModalInteraction(event);
    }

    static class Interact {
        public TextChannel textChannel;
        public TextChannel info;
        public Member member;

        public Interact(Member member, TextChannel info, TextChannel textChannel) {
            this.member = member;
            this.info = info;
            this.textChannel = textChannel;
            info.sendMessage("@here, " + member.getAsMention() + " braucht deine Hilfe!").queue();
            textChannel.sendMessageEmbeds(new EmbedBuilder().setTitle("Ticketinteraktion")
                            .addField("Hiermit kannst du das Ticket verwalten!", "Reagiere mit den Buttons", false)
                            .addField("Interaktionen:", "\uD83D\uDD12 Schließen\n\uD83D\uDC65 Benutzer Hinzufügen\n\uD83D\uDEB7 Benutzer Entfernen\n", false).build())
                    .addActionRow(Button.danger("closeTicket", Emoji.fromUnicode("U+1F512")), Button.success("addUser", Emoji.fromUnicode("U+1F465")), Button.secondary("removeUser", Emoji.fromUnicode("U+1F6B7"))).queue();
        }
    }
}
