package de.fractories.discordbot.command;

import de.fractories.discordbot.util.CommandExecutor;
import de.fractories.discordbot.util.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.GenericSelectMenuInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;

import java.util.Objects;

public class SetupCommand extends ListenerAdapter implements CommandExecutor {
    TextChannel open, message;

    @Override
    public void run(SlashCommandInteractionEvent event) {
        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR)) return;
        if (Objects.requireNonNull(event.getSubcommandName()).equalsIgnoreCase("tickets"))
            event.reply("Wähle ein paar Rollen aus, welche zugriff auf die Channel haben sollen").addActionRow(EntitySelectMenu.create("S1", EntitySelectMenu.SelectTarget.ROLE).setMaxValues(10).build()).queue();
        else if (event.getSubcommandName().equals("defaultrole"))
            event.reply("Wähle nun die Rolle aus.").addActionRow(EntitySelectMenu.create("S4", EntitySelectMenu.SelectTarget.ROLE).build()).queue();
    }

    @Override
    public void onGenericSelectMenuInteraction(GenericSelectMenuInteractionEvent event) {
        if (event.getComponentId().equals("S1")) {
            StringBuilder roleBuilder = new StringBuilder();
            event.getValues().forEach(role -> {
                Util.ticketRoles.add(((Role) role).getId());
                roleBuilder.append(((Role) role).getId()).append(", ");
            });
            roleBuilder.deleteCharAt(roleBuilder.length() - 2);
            Util.botSettings.add("roles", roleBuilder.toString());

            event.getMessage().delete().queue();
            event.reply("Wähle nun einen Channel zur Ticketeröffnung aus!").addActionRow(EntitySelectMenu.create("S2", EntitySelectMenu.SelectTarget.CHANNEL).setMaxValues(1).setMinValues(1).setChannelTypes(ChannelType.TEXT).build()).queue();
        } else if (event.getComponentId().equals("S2")) {
            open = (TextChannel) event.getValues().get(0);
            Util.botSettings.add("openchannel", open.getId());

            event.getMessage().delete().queue();
            event.reply("Wähle nun einen Channel zur Benachrichtigung aus!").addActionRow(EntitySelectMenu.create("S3", EntitySelectMenu.SelectTarget.CHANNEL).setMaxValues(1).setMinValues(1).setChannelTypes(ChannelType.TEXT).build()).queue();
        } else if (event.getComponentId().equals("S3")) {
            message = (TextChannel) event.getValues().get(0);
            Util.botSettings.add("logchannel", message.getId());
            event.getMessage().delete().queue();
            event.replyModal(Modal.create("01", "Fülle folgende Felder aus!").addComponents(ActionRow.of(TextInput.create("I1", "Format der TextChannel %id%, %user%", TextInputStyle.SHORT).build()), ActionRow.of(TextInput.create("I2", "Format der AudioChannel %id%, %user%", TextInputStyle.SHORT).build())).build()).queue();
        } else if (event.getComponentId().equals("S4")) {
            Util.botSettings.add("defaultRole", ((Role) event.getValues().get(0)).getId());
            event.reply("Die Rolle wurde zu " + ((Role) event.getValues().get(0)).getName() + "gesetzt").queue();
        }
        super.onGenericSelectMenuInteraction(event);
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        for (ModalMapping value : event.getValues()) {
            if (value.getId().equals("I1")) {
                Util.botSettings.add("textchannelformat", value.getAsString());
                Util.formatText = value.getAsString();
            } else if (value.getId().equals("I2")) {
                Util.botSettings.add("voicechannelformat", value.getAsString());
                Util.formatVoice = value.getAsString();
            }
        }
        if (!event.getModalId().equals("01")) return;
        open.sendMessageEmbeds(new EmbedBuilder().setTitle("Ticket Interaktionsmenü").build()).addActionRow(Button.primary("openTicket", "Eröffne ein Ticket!")).queue();
        message.sendMessageEmbeds(new EmbedBuilder().setTitle("Dies ist nun der Channel für Ticketbenachrichtigungen").build()).queue();
        event.reply("Das Setup wurde erfolgreich beendet!").queue();
        super.onModalInteraction(event);
    }
}
