package net.minebr.pickaxe;

import de.tr7zw.changeme.nbtapi.NBTItem;
import net.minebr.MiningMain;
import net.minebr.api.builder.ItemBuilder;
import net.minebr.api.builder.SkullBuilder;
import net.minebr.object.BoosterObject;
import net.minebr.utils.NumberFormat;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.stream.Collectors;

public class ItemBuilderBooster {

    public static void createBoosterItem(Player player, String boosterKey) {
        BoosterObject boosterObject = MiningMain.getPlugin().getBoosterMap().get(boosterKey);

        if (boosterObject == null) {
            player.sendMessage("§cBooster não encontrado: " + boosterKey);
            return;
        }

        ItemBuilder itemBuilder = new ItemBuilder();
        SkullBuilder skullBuilder = new SkullBuilder();

        if (boosterObject.isUseSkull() && !boosterObject.getSkullUrl().isEmpty()) {
            skullBuilder.setTexture(boosterObject.getSkullUrl());
        } else {
            itemBuilder.setType(boosterObject.getMaterial());
        }

        ItemStack item = itemBuilder
                .setName(boosterObject.getName().replace("&", "§"))
                .setLore(boosterObject.getLore().stream()
                        .map(line -> line.replace("&", "§")
                                .replace("{timer}", NumberFormat.numberFormat(boosterObject.getTime()))
                                .replace("{bonus}", NumberFormat.decimalFormat(boosterObject.getBonus())))
                        .collect(Collectors.toList()))
                .addGlow()
                .build();

        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setString("BOOSTER", boosterKey);
        nbtItem.setInteger("time", boosterObject.getTime());
        nbtItem.setDouble("bonus", boosterObject.getBonus());

        player.getInventory().addItem(nbtItem.getItem());
    }
}
