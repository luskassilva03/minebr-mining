package net.minebr.inventory;

import com.henryfabio.minecraft.inventoryapi.editor.InventoryEditor;
import com.henryfabio.minecraft.inventoryapi.inventory.impl.simple.SimpleInventory;
import com.henryfabio.minecraft.inventoryapi.item.InventoryItem;
import com.henryfabio.minecraft.inventoryapi.viewer.Viewer;
import net.minebr.MiningMain;
import net.minebr.api.builder.ItemBuilder;
import net.minebr.api.builder.SkullBuilder;
import net.minebr.functions.TrafficMine;
import net.minebr.object.MinesConfig;
import net.minebr.object.MinesObject;
import net.minebr.utils.NumberFormat;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class InventoryMinesList extends SimpleInventory {
    private final MiningMain main;

    public InventoryMinesList(MiningMain main) {
        super("minebr.mining.list", "&8Minas", 9 * 5);
        this.main = main;
        configuration(configuration -> configuration.secondUpdate(1));
    }

    @Override
    protected void update(Viewer viewer, InventoryEditor editor) {
        Player viewerPlayer = viewer.getPlayer();
        setGlassPanels(editor);

        ItemBuilder itemBuilder = new ItemBuilder();
        SkullBuilder skullBuilder = new SkullBuilder();

        main.getMinesObjectHashMap().values().stream()
                .filter(Objects::nonNull)
                .forEach(mines -> {
                    MinesObject minesObject = main.getMinesObjectHashMap().get(mines.getKey());
                    MinesConfig minesConfig = main.getMenuMinesHashMap().get(mines.getKey());

                    if (minesObject == null || minesConfig == null) {
                        return;
                    }

                    int playersInMine = main.getPlayersInMine(minesObject.getKey());

                    List<String> lore = minesConfig.getLore().stream().map(line -> line
                                    .replace("&", "§")
                                    .replace("{connect}", NumberFormat.numberFormat(playersInMine)))
                            .collect(Collectors.toList());

                    // Adiciona informações dos blocos à lore
                    lore.add(" §8§l▸ §fBlocos da Mina§7:");
                    minesObject.getBlockPercentages().forEach((material, percentage) -> {
                        double cost = minesObject.getBlockCost(material);
                        String costStr = cost == -1.0 ? "§cN/A" : String.format("§2$§f%,.2f", cost);
                        lore.add(String.format("    §8§l↳ §7%s§7: %s §7(§a%d%%§7)", material.name().toLowerCase(), costStr, percentage));
                    });
                    lore.add("");
                    lore.add("§aClique aqui para entrar.");

                    if (minesConfig.isUseSkull() && !minesConfig.getSkullUrl().isEmpty()) {
                        skullBuilder.setTexture(minesConfig.getSkullUrl());
                        skullBuilder.setName(minesConfig.getDisplay());
                        skullBuilder.setLore(lore);
                        ItemStack itemStack = skullBuilder.build();
                        InventoryItem inventoryItem = InventoryItem.of(itemStack);
                        editor.setItem(minesConfig.getSlot(), inventoryItem.defaultCallback(event -> {
                            viewerPlayer.closeInventory();
                            TrafficMine teleportToMine = new TrafficMine(MiningMain.getPlugin(), MiningMain.getPlugin().getMainDataManager());
                            teleportToMine.teleportToMine(viewerPlayer, minesObject);
                        }));
                    } else {
                        itemBuilder.setType(minesConfig.getMaterial());
                        itemBuilder.setName(minesConfig.getDisplay());
                        itemBuilder.setLore(lore);
                        ItemStack itemStack = itemBuilder.build();
                        InventoryItem inventoryItem = InventoryItem.of(itemStack);
                        editor.setItem(minesConfig.getSlot(), inventoryItem.defaultCallback(event -> {
                            viewerPlayer.closeInventory();
                            TrafficMine teleportToMine = new TrafficMine(MiningMain.getPlugin(), MiningMain.getPlugin().getMainDataManager());
                            teleportToMine.teleportToMine(viewerPlayer, minesObject);
                        }));
                    }
                });
    }

    private void setGlassPanels(InventoryEditor editor) {
        int[] borderSlots = {
                0, 1, 2, 3, 4, 5, 6, 7, 8,
                9, 18, 27, 36,
                17, 26, 35, 44,
                37, 38, 39, 40, 41, 42, 43
        };

        ItemStack glassPanel = createGlassPanel("§ewww.minebr.net");

        for (int slot : borderSlots) {
            if (slot < getSize() && (editor.getInventory().getItem(slot) == null || editor.getInventory().getItem(slot).getType() == Material.AIR)) {
                editor.setItem(slot, InventoryItem.of(glassPanel));
            }
        }
    }

    private ItemStack createGlassPanel(String name) {
        return new ItemBuilder()
                .setType(Material.STAINED_GLASS_PANE)
                .setData((byte) 7)
                .setName(name)
                .build();
    }
}
