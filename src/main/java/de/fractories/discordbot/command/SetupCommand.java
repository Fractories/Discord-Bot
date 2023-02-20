package de.fractories.discordbot.command;

import de.fractories.discordbot.util.CommandExecutor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionComponent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import java.awt.*;

public class SetupCommand implements CommandExecutor {
    @Override
    public void run(SlashCommandInteractionEvent event) {
//        event.getMessageChannel().sendMessageEmbeds(
//                new EmbedBuilder()
//                        .setTitle("Ticket Interaktionsmenü")
//                        .build()
//        ).addActionRow(Button.primary("01", "Öffne ein Ticket!")).queue();
        event.replyModal(Modal.create("02", "Fülle folgende Felder aus!")
                .addComponents(
                        ActionRow.of(TextInput.create("I1", "Name der Kategorie", TextInputStyle.SHORT).build()),
                        ActionRow.of(TextInput.create("I2", "Name des StandardChannel", TextInputStyle.SHORT).build()),
                        ActionRow.of(TextInput.create("I3", "Format der TextChannel %id%, %user%", TextInputStyle.SHORT).build()),
                        ActionRow.of(TextInput.create("I4", "Format der AudioChannel %id%, %user%", TextInputStyle.SHORT).build())).build()).queue();

//        event.replyEmbeds(new EmbedBuilder()
//                .setColor(Color.RED)
//                .addField("Setup completed!", "This is now the channel to open tickets!", false)
//                .build()).queue();
    }
}
