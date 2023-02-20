package de.fractories.discordbot;

import de.fractories.discordbot.listener.ContextMenuListener;
import de.fractories.discordbot.listener.SlashCommandListener;
import de.fractories.discordbot.util.Util;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Properties;

public class Main {

    public static void main(String[] args) throws IOException {
        Properties botProperties = new Properties();
        botProperties.load(new InputStreamReader(Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream("settings.properties"))));
        JDABuilder builder = JDABuilder.createDefault(botProperties.getProperty("token"));
        builder.setEnabledIntents(GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS));
        builder.addEventListeners(new SlashCommandListener(), new ContextMenuListener());
        Util.register(builder.build());
    }

}
