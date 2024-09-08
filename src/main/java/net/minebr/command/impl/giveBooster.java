package net.minebr.command.impl;

import lombok.val;
import net.minebr.MiningMain;
import net.minebr.command.utils.ISubCommand;
import net.minebr.pickaxe.ItemBuilderBooster;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class giveBooster implements ISubCommand {
    @Override
    public void execute(CommandSender s, String[] a) {
        if (s.hasPermission("mining.admin")) {
            if (a.length > 3) {
                s.sendMessage("§cArgumentos maiores que o permitido, tente novamente!");
                return;
            }
            if (a.length < 3) {
                s.sendMessage("§cArgumentos menores que o permitido, tente novamente!");
                return;
            }

            String playerName = a[1];
            Player targetPlayer = Bukkit.getPlayerExact(playerName);
            if (targetPlayer == null || !targetPlayer.isOnline()) {
                s.sendMessage("§cO jogador " + playerName + " não está online.");
                return;
            }

            String boosterKey = a[2];
            if (MiningMain.getPlugin().getBoosterMap().containsKey(boosterKey)) {
                ItemBuilderBooster.createBoosterItem(targetPlayer, boosterKey);
                s.sendMessage("§aVocê deu com sucesso o booster " + boosterKey + " para " + playerName);
                return;
            }

            s.sendMessage("§cEsse booster não foi encontrado na boosters.yml");
            s.sendMessage("§cTente alguns desses: " + MiningMain.getPlugin().getBoosterMap().keySet());
        } else {
            s.sendMessage("§cVocê não tem permissão para usar este comando.");
        }
    }
}
