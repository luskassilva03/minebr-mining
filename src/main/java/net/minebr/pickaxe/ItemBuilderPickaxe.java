package net.minebr.pickaxe;

import de.tr7zw.changeme.nbtapi.NBTItem;
import net.minebr.MiningMain;
import net.minebr.api.builder.ItemBuilder;
import net.minebr.object.PickaxeObject;
import net.minebr.utils.NumberFormat;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.stream.Collectors;

public class ItemBuilderPickaxe {

    public static void pickaxePlayer(Player p, String pickaxe) {
        PickaxeObject pickaxeObject = MiningMain.getPlugin().getPickaxeConfigMap().get(pickaxe);

        if (pickaxeObject == null) {
            p.sendMessage("§cPickaxe não encontrada: " + pickaxe);
            return;
        }

        ItemStack item = (new ItemBuilder()).setType(pickaxeObject.getMaterial()).setName(pickaxeObject.getName().replace("{broken}", "0"))
                .setLore(pickaxeObject.getLore().stream().map(line -> line.replace("&", "§")
                        .replace("{efficiency}", NumberFormat.numberFormat(pickaxeObject.getEnchantmentLevel("efficiency")))
                        .replace("{fortune}", NumberFormat.decimalFormat(pickaxeObject.getEnchantmentLevel("fortune")))
                        .replace("{durability}", "∞")
                        .replace("{laser}", NumberFormat.decimalFormat(pickaxeObject.getEnchantmentLevel("laser")))
                        .replace("{destroyer}", NumberFormat.decimalFormat(pickaxeObject.getEnchantmentLevel("destroyer")))
                        .replace("{explosive}", NumberFormat.decimalFormat(pickaxeObject.getEnchantmentLevel("explosive")))
                        .replace("{nuclear}", NumberFormat.decimalFormat(pickaxeObject.getEnchantmentLevel("nuclear")))
                ).collect(Collectors.toList())).addEnchant(Enchantment.DIG_SPEED, pickaxeObject.getEnchantmentLevel("efficiency")).addEnchant(Enchantment.DURABILITY, 100).addGlow()
                .build();


        ItemMeta meta = item.getItemMeta();
        meta.spigot().setUnbreakable(true);

        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setString("PICKAXE", pickaxe);
        nbtItem.setInteger("efficiency", pickaxeObject.getEnchantmentLevel("efficiency"));
        nbtItem.setInteger("fortune", pickaxeObject.getEnchantmentLevel("fortune"));
        nbtItem.setInteger("durability", pickaxeObject.getEnchantmentLevel("durability"));
        nbtItem.setInteger("laser", pickaxeObject.getEnchantmentLevel("laser"));
        nbtItem.setInteger("destroyer", pickaxeObject.getEnchantmentLevel("destroyer"));
        nbtItem.setInteger("explosive", pickaxeObject.getEnchantmentLevel("explosive"));
        nbtItem.setInteger("nuclear", pickaxeObject.getEnchantmentLevel("nuclear"));


        p.getInventory().addItem(new ItemStack(nbtItem.getItem()));
    }
}
