package net.minebr.command.impl;

import lombok.val;
import net.minebr.MiningMain;
import net.minebr.command.utils.ISubCommand;
import net.minebr.utils.LocationConverter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class setSpawn implements ISubCommand {
    @Override
    public void execute(CommandSender s, String[] a) {
        if (s.hasPermission("mining.admin")) {
            Player p = (Player)s;
            if (a.length > 2) {
                p.sendMessage("§cArgumentos maiores que o permitido, tente novamente!");
                return;
            }
            if (a.length < 2) {
                p.sendMessage("§cArgumentos menores que o permitido, tente novamente!");
                return;
            }
            val mine = a[1];
            if (MiningMain.getPlugin().minesObjectHashMap.containsKey(mine)) {
                MiningMain.getPlugin().mines.set("mines." + mine + ".locations.spawn", LocationConverter.convertLocationToString(p.getLocation(), true));
                MiningMain.getPlugin().mines.saveConfig();
                p.sendMessage("§aVocê setou com sucesso o spawn da §f" + mine);
                return;
            }
            p.sendMessage("§cEssa área de mineração não foi encontrada na mines.yml");
            p.sendMessage("§cTente algumas dessas: " + MiningMain.getPlugin().minesObjectHashMap.keySet());
        }
    }
}
