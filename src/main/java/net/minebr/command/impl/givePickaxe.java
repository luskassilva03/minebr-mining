package net.minebr.command.impl;

import lombok.val;
import net.minebr.MiningMain;
import net.minebr.command.utils.ISubCommand;
import net.minebr.pickaxe.ItemBuilderPickaxe;
import net.minebr.utils.LocationConverter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class givePickaxe implements ISubCommand {
    @Override
    public void execute(CommandSender s, String[] a) {
        if (s.hasPermission("mining.admin")) {
            //Player p = (Player)s;
            if (a.length > 3) {
                s.sendMessage("§cArgumentos maiores que o permitido, tente novamente!");
                return;
            }
            if (a.length < 3) {
                s.sendMessage("§cArgumentos menores que o permitido, tente novamente!");
                return;
            }
            val player = a[1];
            Player targetPlayer = Bukkit.getPlayerExact(player);
            if (targetPlayer == null || !targetPlayer.isOnline()) {
                s.sendMessage("§cO jogador " + targetPlayer + " não está online.");
                return;
            }
            val pickaxe = a[2];
            if (MiningMain.getPlugin().getPickaxeConfigMap().containsKey(pickaxe)) {
                ItemBuilderPickaxe.pickaxePlayer(targetPlayer, pickaxe);
                s.sendMessage("§aVocê setou com sucesso a saída da §f" + pickaxe);
                return;
            }
            s.sendMessage("§cEssa picareta não foi encontrada na pickaxes.yml");
            s.sendMessage("§cTente algumas dessas: " + MiningMain.getPlugin().getPickaxeConfigMap().keySet());
        }
    }
}
