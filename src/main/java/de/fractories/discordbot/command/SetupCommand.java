package de.fractories.discordbot.command;

import de.fractories.discordbot.util.CommandExecutor;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

public class SetupCommand extends ListenerAdapter implements CommandExecutor {
    @Override
    public void run(SlashCommandInteractionEvent event) {
        event.replyModal(Modal.create("02", "Fülle folgende Felder aus!")
                .addComponents(
                        ActionRow.of(TextInput.create("I1", "Name der Kategorie", TextInputStyle.SHORT).build()),
                        ActionRow.of(TextInput.create("I2", "Name des StandardChannel", TextInputStyle.SHORT).build()),
                        ActionRow.of(TextInput.create("I3", "Format der TextChannel %id%, %user%", TextInputStyle.SHORT).build()),
                        ActionRow.of(TextInput.create("I4", "Format der AudioChannel %id%, %user%", TextInputStyle.SHORT).build())).build()).queue();
    }
}
