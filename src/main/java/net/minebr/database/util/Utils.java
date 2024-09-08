package net.minebr.database.util;

import lombok.val;
import net.minebr.MiningMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.Supplier;

public class Utils {

    private static final MiningMain main = MiningMain.getPlugin();

    private static final String PREFIX = ChatColor.GREEN + "[" + main.getName() + "] " + ChatColor.WHITE;

    public static boolean DEBUGGING = main.getConfig().getBoolean("Debug.enable");

    public static void async(Runnable runnable) {
        new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        }.runTaskAsynchronously(main);
    }

    public static void sync(Runnable runnable) {
        new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        }.runTask(main);
    }

    public static void yes(Player player) {
        player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1, 1);
    }

    public static void no(Player player) {
        player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1, 1);
    }

    public static void measureTime(Supplier<String> runnable) {
        val before = System.currentTimeMillis();
        val task = runnable.get();
        val passed = System.currentTimeMillis() - before;
        debug(LogType.INFO, task.replace("{time}", passed + "ms"));
    }

    public static long measureTime(Runnable runnable) {
        val before = System.currentTimeMillis();
        runnable.run();
        return System.currentTimeMillis() - before;
    }

    public static void removeItemFromHand(Player player, int amount) {
        val item = player.getItemInHand();
        if (item.getAmount() > amount) {
            item.setAmount(item.getAmount() - amount);
        } else {
            player.setItemInHand(new ItemStack(Material.AIR));
        }
    }

    public static void removeItemFromHand(Player player) {
        removeItemFromHand(player, 1);
    }

    public static void debug(LogType type, String message) {
        if (type == LogType.DEBUG && !DEBUGGING) return;
        Bukkit.getConsoleSender().sendMessage("[" + type.name() + "] " + PREFIX + message);
    }

    public enum LogType {
        INFO, DEBUG;
    }

}
