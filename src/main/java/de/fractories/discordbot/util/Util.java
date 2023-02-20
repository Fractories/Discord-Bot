package de.fractories.discordbot.util;

import de.fractories.discordbot.command.SetupCommand;

import java.util.ArrayList;
import java.util.HashMap;

public class Util {

    public static Register register = new Register();

    public static void register() {
        register.addCommand("setup", new SetupCommand());
    }

}
