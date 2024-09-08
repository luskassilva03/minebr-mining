package net.minebr.functions;

import net.minebr.MiningMain;
import net.minebr.api.VaultAPI;
import net.minebr.utils.ActionBar;
import net.minebr.utils.NumberFormat;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class CoinTracker {

    private static final Map<Player, PlayerData> playerDataMap = new HashMap<>();
    private static final MiningMain plugin = MiningMain.getPlugin();

    public static void startTracking(Player player) {
        // Inicializa o valor se ainda não estiver presente
        playerDataMap.putIfAbsent(player, new PlayerData());

        // Se já estiver rastreando, cancela a tarefa existente
        stopTracking(player);

        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                PlayerData data = playerDataMap.get(player);
                if (data != null) {
                    double coins = data.getCoins();
                    if (coins > 0.1) {
                        String bonusGroup = data.getBonusGroup();
                        double bonusAmount = data.getBonusAmount();
                        ActionBar.sendActionText(player, "§aVocê ganhou: §2$§f" + NumberFormat.numberFormat(coins) + " §ade bônus: §f" + bonusGroup + " §8[§e" + bonusAmount + "%§8]");


                        VaultAPI.econ.depositPlayer(player, coins);
                        // Reseta o valor de coins e bônus
                        data.reset();
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 60L); // 60L é 3 segundos em ticks

        playerDataMap.put(player, new PlayerData(task));
    }

    public static void addCoins(Player player, double amount, String bonusGroup, double bonusAmount) {
        PlayerData data = playerDataMap.computeIfAbsent(player, k -> new PlayerData());
        data.addCoins(amount);
        data.setBonusGroup(bonusGroup);
        data.setBonusAmount(bonusAmount);
    }

    public static void stopTracking(Player player) {
        PlayerData data = playerDataMap.remove(player);
        if (data != null) {
            BukkitTask task = data.getTask();
            if (task != null) {
                task.cancel();
            }
        }
    }

    private static class PlayerData {
        private double coins;
        private String bonusGroup = "Nenhum";
        private double bonusAmount;
        private BukkitTask task;

        public PlayerData() {
        }

        public PlayerData(BukkitTask task) {
            this.task = task;
        }

        public double getCoins() {
            return coins;
        }

        public void addCoins(double amount) {
            this.coins += amount;
        }

        public String getBonusGroup() {
            return bonusGroup;
        }

        public void setBonusGroup(String bonusGroup) {
            this.bonusGroup = bonusGroup;
        }

        public double getBonusAmount() {
            return bonusAmount;
        }

        public void setBonusAmount(double bonusAmount) {
            this.bonusAmount = bonusAmount;
        }

        public BukkitTask getTask() {
            return task;
        }

        public void reset() {
            this.coins = 0;
            this.bonusGroup = "Nenhum";
            this.bonusAmount = 0;
        }
    }
}
