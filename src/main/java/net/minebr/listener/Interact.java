package net.minebr.listener;

import de.tr7zw.changeme.nbtapi.NBTItem;
import lombok.val;
import net.minebr.MiningMain;
import net.minebr.object.PlayerObject;
import net.minebr.registery.ListenerRegistry;
import net.minebr.utils.LocationConverter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class Interact extends ListenerRegistry {
    public Interact(MiningMain main) {
        super(main);
    }

    @EventHandler
    void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (p.getItemInHand() == null || p.getItemInHand().getItemMeta() == null || p.getItemInHand().getItemMeta().getDisplayName() == null)
            return;
        NBTItem nbtItem = new NBTItem(p.getItemInHand());

        // Handle PICKAXE interaction
        if (nbtItem.hasTag("PICKAXE")) {
            if (e.getAction() == Action.RIGHT_CLICK_AIR) {
                p.sendMessage("");
                p.sendMessage("§a§lMINEBR §8➟ §7As §fevoluções §7da picareta são através de compras");
                p.sendMessage("              §7compre picaretas melhores em §f/picaretas §7ouuuu");
                p.sendMessage("              §7adquira picaretas §fOPs §7em nosso site:");
                p.sendMessage("                            §fwww.minebr.net");
                p.sendMessage("");
            }
        }

        // Handle MINAADMIN interaction
        if (nbtItem.hasTag("MINAADMIN")) {
            e.setCancelled(true);
            String mine = nbtItem.getString("MINAADMIN");
            if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
                MiningMain.getPlugin().mines.set("mines." + mine + ".positions.pos1", LocationConverter.convertLocationToString(e.getClickedBlock().getLocation(), false));
                MiningMain.getPlugin().mines.saveConfig();
                p.sendMessage("§aPosição 1 da mina " + mine + " setada com sucesso!");
                return;
            }
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                MiningMain.getPlugin().mines.set("mines." + mine + ".positions.pos2", LocationConverter.convertLocationToString(e.getClickedBlock().getLocation(), false));
                MiningMain.getPlugin().mines.saveConfig();
                p.sendMessage("§aPosição 2 da mina " + mine + " setada com sucesso!");
            }
        }

        // Handle booster interaction
        if (nbtItem.hasTag("BOOSTER")) {
            e.setCancelled(true);
            String boosterKey = nbtItem.getString("BOOSTER");
            val booster = MiningMain.getPlugin().getBoosterMap().get(boosterKey);

            if (booster == null) {
                p.sendMessage("§cBooster não encontrado: " + boosterKey);
                return;
            }

            PlayerObject playerObject = MiningMain.getPlugin().getMainDataManager().USERS.getCached(p.getName());
            if (playerObject == null) {
                p.sendMessage("§cDados do jogador não encontrados.");
                return;
            }

            // Check if the booster name is valid
            if (boosterKey == null || booster.getTime() <= 0 || booster.getBonus() < 0) {
                p.sendMessage("§cDados inválidos para o booster: " + boosterKey);
                return;
            }

            // Check if the player already has an active booster
            if (playerObject.getActiveBoosters().containsKey(boosterKey)) {
                p.sendMessage("§cVocê já tem um booster ativo com a chave: " + boosterKey);
                return;
            }

            playerObject.activateBooster(boosterKey, booster.getTime(), booster.getBonus());

            p.getInventory().remove(p.getItemInHand());
            p.sendMessage("§aVocê ativou o booster: " + boosterKey);
        }
    }
}
