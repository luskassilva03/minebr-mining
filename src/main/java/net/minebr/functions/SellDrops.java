package net.minebr.functions;

import de.tr7zw.changeme.nbtapi.NBTItem;
import net.minebr.MiningMain;
import net.minebr.object.MinesObject;
import net.minebr.object.PermissionValues;
import net.minebr.object.PickaxeObject;
import net.minebr.object.PlayerObject;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class SellDrops {

    public static void sellDrops(Player p, MinesObject minesObject, List<Block> blocks) {
        double totalAmount = 0.0;
        double fortuneMultiplier = getFortuneMultiplier(p);
        double bonusMultiplier = getMaxBonusMultiplier(p);
        String bonusGroup = getBonusGroup(p, bonusMultiplier);

        PlayerObject playerObject = MiningMain.getPlugin().getMainDataManager().USERS.getCached(p.getName());
        if (playerObject != null) {
            // Remove boosters expirados
            playerObject.removeExpiredBoosters();

            Result result = applyActiveBoosters(playerObject, bonusGroup);
            bonusMultiplier += result.getTotalBonus();
            bonusGroup = result.getBonusGroup();
        }

        for (Block block : blocks) {
            double blockCost = minesObject.getBlockCost(block.getType());
            totalAmount += blockCost * fortuneMultiplier * (1 + (bonusMultiplier / 100));
            //block.setType(Material.AIR);
        }

        CoinTracker.addCoins(p, totalAmount, bonusGroup, bonusMultiplier);
    }

    private static double getFortuneMultiplier(Player p) {
        ItemStack itemInHand = p.getInventory().getItemInHand();
        if (itemInHand != null && itemInHand.hasItemMeta()) {
            NBTItem nbtItem = new NBTItem(itemInHand);
            PickaxeObject pickaxeObject = MiningMain.getPlugin().getPickaxeConfigMap().get(nbtItem.getString("PICKAXE"));
            if (pickaxeObject != null) {
                return pickaxeObject.getEnchantmentLevel("fortune");
            }
        }
        p.sendMessage("Item em mão não é uma picareta válida.");
        return 1.0;
    }

    private static double getMaxBonusMultiplier(Player p) {
        double maxBonus = 0.0;
        for (PermissionValues permissionValues : MiningMain.getPlugin().getBonusValuesMap().values()) {
            if (p.hasPermission(permissionValues.getPermission())) {
                maxBonus = Math.max(maxBonus, permissionValues.getBonus());
            }
        }
        return maxBonus;
    }

    private static String getBonusGroup(Player p, double maxBonus) {
        for (PermissionValues permissionValues : MiningMain.getPlugin().getBonusValuesMap().values()) {
            if (p.hasPermission(permissionValues.getPermission()) && permissionValues.getBonus() == maxBonus) {
                return permissionValues.getGroup();
            }
        }
        return "";
    }

    private static Result applyActiveBoosters(PlayerObject playerObject, String initialBonusGroup) {
        StringBuilder bonusInfo = new StringBuilder(initialBonusGroup);
        double totalBonus = 0.0;

        for (Map.Entry<String, Long> entry : playerObject.getActiveBoosters().entrySet()) {
            String boosterName = entry.getKey();
            long remainingTime = playerObject.getRemainingBoosterTime(boosterName);
            if (remainingTime > 0) {
                double boosterBonus = playerObject.getBoosterBonus(boosterName);
                totalBonus += boosterBonus;
                if (bonusInfo.length() > 0) bonusInfo.append("§7, §f");
                bonusInfo.append(boosterName).append(" §7(§a").append(remainingTime).append("s§7)§f");
            }
        }

        return new Result(totalBonus, bonusInfo.toString());
    }

    private static class Result {
        private final double totalBonus;
        private final String bonusGroup;

        public Result(double totalBonus, String bonusGroup) {
            this.totalBonus = totalBonus;
            this.bonusGroup = bonusGroup;
        }

        public double getTotalBonus() {
            return totalBonus;
        }

        public String getBonusGroup() {
            return bonusGroup;
        }
    }
}
