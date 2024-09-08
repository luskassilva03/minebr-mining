package net.minebr.command.impl;

import lombok.val;
import net.minebr.MiningMain;
import net.minebr.command.utils.ISubCommand;
import net.minebr.functions.TrafficMine;
import net.minebr.object.MinesObject;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class minaIr implements ISubCommand {
    @Override
    public void execute(CommandSender s, String[] a) {
        if (!(s instanceof Player)) {
            s.sendMessage("§cEste comando só pode ser executado por jogadores.");
            return;
        }

        Player p = (Player) s;

        if (a.length > 2) {
            p.sendMessage("§cArgumentos maiores que o permitido, tente novamente!");
            return;
        }
        if (a.length < 2) {
            p.sendMessage("§cArgumentos menores que o permitido, tente novamente!");
            return;
        }

        val mineName = a[1];
        MinesObject mine = MiningMain.getPlugin().getMinesObjectHashMap().get(mineName);
        if (mine == null) {
            p.sendMessage("§cEssa mina não foi encontrada");
            p.sendMessage("§cTente algumas dessas: " + MiningMain.getPlugin().getMinesObjectHashMap().keySet());
            return;
        }

        if (mine.getPos1() == null || mine.getPos2() == null || mine.getSpawnLocation() == null || mine.getExitLocation() == null) {
            p.sendMessage("§cPosições da mina não definidas!");
            return;
        }

        // Crie uma instância de TrafficMine e chame o método teleportToMine
        TrafficMine teleportToMine = new TrafficMine(MiningMain.getPlugin(), MiningMain.getPlugin().getMainDataManager());
        teleportToMine.teleportToMine(p, mine);
    }
}
