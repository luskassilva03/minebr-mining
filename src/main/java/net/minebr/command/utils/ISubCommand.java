package net.minebr.command.utils;

import org.bukkit.command.CommandSender;

public interface ISubCommand {

    public void execute(CommandSender s, String[] a);
}
