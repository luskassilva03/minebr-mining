package net.minebr.command;

import net.minebr.MiningMain;
import net.minebr.command.impl.*;
import net.minebr.registery.CommandRegistry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class MinaAdminCommand extends CommandRegistry {

    private setSpawn setSpawn;
    private setExit setExit;
    private setPositions setPositions;
    private resetMine resetMine;
    private givePickaxe givePickaxe;
    private giveBooster giveBooster;

    public MinaAdminCommand(MiningMain main) {
        super(main);

        this.setSpawn = new setSpawn();
        this.setExit = new setExit();
        this.resetMine = new resetMine();
        this.setPositions = new setPositions();
        this.givePickaxe = new givePickaxe();
        this.giveBooster = new giveBooster();
        main.getCommand("minaadmin").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender s, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (s instanceof Player || s instanceof ConsoleCommandSender) {
                if (!s.hasPermission("minaadmin.admin")) {
                    s.sendMessage("§cVocê precisa ser Gerente ou superior para poder usar este comando!");
                    return false;
                }
                s.sendMessage("");
                s.sendMessage("§a/minaadmin resetar (mina)");
                s.sendMessage("§a/minaadmin setpicareta (player) (picareta)");
                s.sendMessage("§a/minaadmin setpositions (mina)");
                s.sendMessage("§a/minaadmin setspawn (mina)");
                s.sendMessage("§a/minaadmin setexit (mina)");
                s.sendMessage("§a/minaadmin givebooster (player) (booster)");
                s.sendMessage("");
                return false;
            }
        }
        if (args.length >= 1) {
            switch (args[0].toLowerCase()) {
                case "setpositions":
                case "setarposicoes":
                case "setposicao":
                    setPositions.execute(s, args);
                    break;
                case "resetar":
                case "reset":
                    resetMine.execute(s, args);
                    break;
                case "setsaida":
                case "setexit":
                    setExit.execute(s, args);
                    break;
                case "setentrada":
                case "setspawn":
                    setSpawn.execute(s, args);
                    break;
                case "setpicareta":
                case "setarpicareta":
                    givePickaxe.execute(s, args);
                    break;
                case "givebooster":
                case "darbooster":
                    giveBooster.execute(s, args);
                    break;
                default:
                case "help":
                case "ajuda":
                    s.sendMessage("");
                    s.sendMessage("§a/minaadmin resetar (mina)");
                    s.sendMessage("§a/minaadmin setpicareta (player) (picareta)");
                    s.sendMessage("§a/minaadmin setpositions (mina)");
                    s.sendMessage("§a/minaadmin setspawn (mina)");
                    s.sendMessage("§a/minaadmin setexit (mina)");
                    s.sendMessage("§a/minaadmin givebooster (player) (booster)");
                    s.sendMessage("");
                    break;
            }
        }
        return false;
    }
}
