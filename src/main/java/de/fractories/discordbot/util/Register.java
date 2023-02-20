package de.fractories.discordbot.util;

import java.util.ArrayList;
import java.util.HashMap;

public class Register {

    private final HashMap<String, CommandExecutor> commandsRegistered = new HashMap<>();
    public void addCommand(String name, CommandExecutor commandExecutor) {
        commandsRegistered.put(name, commandExecutor);
    }

    public boolean isRegistered(String name) {
        return commandsRegistered.containsKey(name);
    }

    public boolean isRegistered(CommandExecutor commandExecutor) {
        return commandsRegistered.containsValue(commandExecutor);
    }

    public void removeCommand(String name) {
        commandsRegistered.remove(name);
    }

    public CommandExecutor getExecutor(String name) {
        return commandsRegistered.get(name);
    }

}
