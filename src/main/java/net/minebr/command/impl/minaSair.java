package net.minebr.command.impl;

import net.minebr.MiningMain;
import net.minebr.command.utils.ISubCommand;
import net.minebr.functions.TrafficMine;
import net.minebr.object.MinesObject;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class minaSair implements ISubCommand {
    @Override
    public void execute(CommandSender s, String[] a) {
        if (!(s instanceof Player)) {
            s.sendMessage("§cEste comando só pode ser executado por jogadores.");
            return;
        }

        Player p = (Player) s;

        if (a.length > 1) { // O comando não deve ter argumentos
            p.sendMessage("§cUso incorreto do comando! Utilize /mina sair");
            return;
        }

        if (MiningMain.getPlugin().getPlayerMining().containsKey(p.getName())) {
            MinesObject mine = MiningMain.getPlugin().getPlayerMining().get(p.getName());
            TrafficMine trafficMine = MiningMain.getPlugin().getTrafficMine();
            trafficMine.exitMine(p, mine);
            p.sendMessage("§aVocê saiu da mina " + mine.getKey() + " com sucesso!");
        } else {
            p.sendMessage("§cVocê não está em uma mina.");
        }
    }
}
