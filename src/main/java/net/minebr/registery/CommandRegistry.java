package net.minebr.registery;

import net.minebr.MiningMain;
import org.bukkit.command.CommandExecutor;

public abstract class CommandRegistry implements CommandExecutor {

    protected MiningMain main;

    public CommandRegistry(MiningMain main) {
        this.main = main;
    }
}
