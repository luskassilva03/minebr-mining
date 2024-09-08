package net.minebr.functions;

import de.tr7zw.changeme.nbtapi.NBTItem;
import net.minebr.MiningMain;
import net.minebr.database.datamanager.DataManager;
import net.minebr.database.util.Utils;
import net.minebr.object.MinesObject;
import net.minebr.object.PickaxeObject;
import net.minebr.object.PlayerObject;
import net.minebr.pickaxe.ItemBuilderPickaxe;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;

public class TrafficMine {

    private final MiningMain plugin;
    protected DataManager dataManager;

    public TrafficMine(MiningMain plugin, DataManager dataManager) {
        this.plugin = plugin;
        this.dataManager = dataManager;
    }

    public void teleportToMine(Player p, MinesObject minesObject) {
        PlayerObject playerObject = MiningMain.getPlugin().getMainDataManager().USERS.getCached(p.getName());

        if (!dataManager.USERS.exists(p.getName())) {
            String pickaxeInitial = plugin.configAPI.getString("pickaxe.initial");
            PlayerObject playerObjectNew = new PlayerObject(p.getName(), pickaxeInitial);
            Utils.async(() -> this.dataManager.USERS.cache(playerObjectNew));
            playerObject = playerObjectNew;
        }

        if (playerObject == null) {
            p.sendMessage("§cErro ao recuperar o objeto do jogador.");
            return;
        }

        String namePickaxe = playerObject.getPickaxe();
        PickaxeObject pickaxeObject = plugin.getPickaxeConfigMap().get(namePickaxe);

        if (pickaxeObject == null) {
            p.sendMessage("§cPickaxe não encontrada: " + namePickaxe);
            return;
        }

        if (plugin.getPlayerMining().containsKey(p.getName())) {
            p.sendMessage("§cVocê já está em uma área de mineração!");
            return;
        }

        p.teleport(minesObject.getSpawnLocation());
        ItemBuilderPickaxe.pickaxePlayer(p, namePickaxe);
        plugin.getPlayerMining().put(p.getName(), minesObject);
        p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 1, true, false)); // Haste infinito
        p.sendMessage("§aVocê foi teleportado para a mina com sucesso!");

        // Ativar boosters ao entrar na mina
        activateBoosters(p);
        CoinTracker.startTracking(p);
    }

    public void exitMine(Player p, MinesObject minesObject) {
        if (plugin.getPlayerMining().containsKey(p.getName())) {
            CoinTracker.stopTracking(p);
            removePickaxeWithNBT(p);
            plugin.getPlayerMining().remove(p.getName());
            p.removePotionEffect(PotionEffectType.FAST_DIGGING); // Remove o efeito de Haste
            p.teleport(minesObject.getExitLocation());
            p.sendMessage("§aVocê saiu da mina com sucesso!");

            // Pausar boosters ao sair da mina
            pauseBoosters(p);
        } else {
            p.sendMessage("§cVocê não está em uma mina!");
        }
    }

    private void removePickaxeWithNBT(Player p) {
        for (ItemStack item : p.getInventory().getContents()) {
            if (item != null) {
                NBTItem nbtItem = new NBTItem(item);
                if (nbtItem.hasKey("PICKAXE")) {
                    p.getInventory().remove(item);
                }
            }
        }
    }

    private void activateBoosters(Player player) {
        PlayerObject playerObject = MiningMain.getPlugin().getMainDataManager().USERS.getCached(player.getName());

        if (playerObject != null) {
            for (Map.Entry<String, Long> entry : playerObject.getActiveBoosters().entrySet()) {
                String boosterName = entry.getKey();
                long remainingTime = playerObject.getRemainingBoosterTime(boosterName);
                if (remainingTime > 0) {
                    double boosterBonus = playerObject.getBoosterBonus(boosterName);
                    playerObject.activateBooster(boosterName, remainingTime, boosterBonus);
                }
            }
        }
    }

    private void pauseBoosters(Player player) {
        PlayerObject playerObject = MiningMain.getPlugin().getMainDataManager().USERS.getCached(player.getName());

        if (playerObject != null) {
            playerObject.removeExpiredBoosters(); // Remove boosters expirados

            for (Map.Entry<String, Long> entry : playerObject.getActiveBoosters().entrySet()) {
                String boosterName = entry.getKey();
                long remainingTime = playerObject.getRemainingBoosterTime(boosterName);
                if (remainingTime > 0) {
                    playerObject.updateBoosterTime(boosterName, remainingTime);
                }
            }
        }
    }
}
