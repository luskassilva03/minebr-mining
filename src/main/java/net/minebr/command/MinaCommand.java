package net.minebr.command;

import net.minebr.MiningMain;
import net.minebr.command.impl.*;
import net.minebr.inventory.InventoryMain;
import net.minebr.registery.CommandRegistry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class MinaCommand extends CommandRegistry {

    private minaIr minaIr;
    private minaSair minaSair;

    public MinaCommand(MiningMain main) {
        super(main);

        this.minaIr = new minaIr();
        this.minaSair = new minaSair();
        main.getCommand("mina").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender s, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (s instanceof Player || s instanceof ConsoleCommandSender) {
                if (!s.hasPermission("minaadmin.members")) {
                    s.sendMessage("§cVocê não tem permissão para usar esse comando!");
                    return false;
                }
                Player p = (Player)s;
                InventoryMain invNew = new InventoryMain(MiningMain.getPlugin());
                invNew.openInventory(p);
                return false;
            }
        }
        if (args.length >= 1) {
            switch (args[0].toLowerCase()) {
                case "ir":
                case "go":
                    minaIr.execute(s, args);
                    break;
                case "sair":
                case "exit":
                    minaSair.execute(s, args);
                    break;
                default:
                case "help":
                case "ajuda":
                    s.sendMessage("");
                    s.sendMessage("§a/minaadmin ir (mina)");
                    s.sendMessage("§a/minaadmin sair");
                    s.sendMessage("");
                    break;
            }
        }
        return false;
    }
}
