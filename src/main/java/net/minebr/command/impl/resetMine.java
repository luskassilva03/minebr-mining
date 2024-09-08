package net.minebr.command.impl;

import net.minebr.MiningMain;
import net.minebr.command.utils.ISubCommand;
import net.minebr.object.MinesObject;
import net.minebr.manager.resetarMine; // Importar a classe resetarMine
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class resetMine implements ISubCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cEste comando só pode ser executado por jogadores.");
            return;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("mining.admin")) {
            player.sendMessage("§cVocê não tem permissão para usar este comando.");
            return;
        }

        if (args.length != 2) {
            player.sendMessage("§cUso correto: /resetmine <nome_da_mina>");
            return;
        }

        String mineName = args[1];
        MinesObject mine = MiningMain.getPlugin().getMinesObjectHashMap().get(mineName);
        if (mine == null) {
            player.sendMessage("§cMina não encontrada!");
            return;
        }

        if (mine.getPos1() == null || mine.getPos2() == null) {
            player.sendMessage("§cPosições da mina não definidas!");
            return;
        }

        // Cria uma instância de resetarMine e chama o método resetMine
        resetarMine resetManager = new resetarMine(mine);
        resetManager.resetMine();

        player.sendMessage("§aMina " + mineName + " está sendo resetada.");
    }
}
