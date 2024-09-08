package net.minebr.listener;

import de.tr7zw.changeme.nbtapi.NBTItem;
import net.minebr.MiningMain;
import net.minebr.functions.Enchants;
import net.minebr.functions.SellDrops;
import net.minebr.object.MinesObject;
import net.minebr.object.PickaxeObject;
import net.minebr.object.PlayerObject;
import net.minebr.registery.ListenerRegistry;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Break extends ListenerRegistry {
    private final Enchants enchants;

    public Break(MiningMain main) {
        super(main);
        this.enchants = new Enchants(main);
    }

    private final Set<Block> brokenLapisOreBlocks = new HashSet<>();

    @EventHandler
    void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Block block = e.getBlock();
        if (block.getWorld().getName().equalsIgnoreCase("Mina") || block.getWorld().getName().equalsIgnoreCase("Arenas")) {

            if (MiningMain.getPlugin().getPlayerMining().containsKey(p.getName())) {
                if (p.getGameMode() == GameMode.CREATIVE) return;
                if (p.getItemInHand() == null || !p.getItemInHand().hasItemMeta()) {
                    e.setCancelled(true);
                    return;
                }

                String mineName = MiningMain.getPlugin().getPlayerMining().get(p.getName()).getKey();
                MinesObject mine = main.getMinesObjectHashMap().get(mineName);

                if (mine == null) {
                    p.sendMessage("§cMina não encontrada!");
                    return;
                }

                if (!mine.isBlockInMine(block.getType())) {
                    e.setCancelled(true);
                    return;
                }

                brokenLapisOreBlocks.add(block);

                List<Block> blocksToSell = new ArrayList<>();
                blocksToSell.add(block); // Add the broken block

                PlayerObject playerObject = main.getMainDataManager().USERS.getCached(p.getName());
                PickaxeObject pickaxe = MiningMain.getPlugin().getPickaxeConfigMap().get(playerObject.getPickaxe());

                // Apply enchants with respective chances
                boolean explosiveApplied = false;
                boolean laserApplied = false;
                boolean destroyerApplied = false;
                boolean nuclearApplied = false;

                double explosiveChance = (double) pickaxe.getEnchantmentLevel("explosive") / 12; // Adjusted chance
                double laserChance = (double) pickaxe.getEnchantmentLevel("laser") / 8; // Adjusted chance
                double destroyerChance = (double) pickaxe.getEnchantmentLevel("destroyer") / 16; // Adjusted chance
                double nuclearChance = (double) pickaxe.getEnchantmentLevel("nuclear") / 21; // Adjusted chance


                if (explosiveChance > 0.00 && ThreadLocalRandom.current().nextDouble() * 100 < explosiveChance) {
                    blocksToSell.addAll(enchants.applyExplosiveEnchant(p, block, mine));
                    enchants.showParticleEffect(p, blocksToSell);
                    explosiveApplied = true;
                }

                if (laserChance > 0.00 && !explosiveApplied && ThreadLocalRandom.current().nextDouble() * 100 < laserChance) {
                    blocksToSell.addAll(enchants.applyLaserEnchant(p, block, mine));
                    enchants.showParticleEffect(p, blocksToSell);
                    laserApplied = true;
                }

                if (destroyerChance > 0.00 && !explosiveApplied && !laserApplied && ThreadLocalRandom.current().nextDouble() * 100 < destroyerChance) {
                    blocksToSell.addAll(enchants.applyDestruidorEnchant(p, block, mine));
                    enchants.showParticleEffect(p, blocksToSell);
                    destroyerApplied = true;
                }

                if (nuclearChance > 0.0 && !explosiveApplied && !laserApplied && !destroyerApplied && ThreadLocalRandom.current().nextDouble() * 100 < nuclearChance) {
                    blocksToSell.addAll(enchants.applyNuclearEnchant(p, block, mine));
                    enchants.showParticleEffect(p, blocksToSell);
                    nuclearApplied = true;
                }

                double kyRewardBasico = 0.2;
                String commandBasico = "crates givekey " + p.getName() + " basico 1"; // Substitua pelo comando desejado

                if (kyRewardBasico > 0.00 && ThreadLocalRandom.current().nextDouble() * 100 < kyRewardBasico) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandBasico);
                }
                double kyRewardComum = 0.09;
                String commandComum = "crates givekey " + p.getName() + " comum 1"; // Substitua pelo comando desejado

                if (kyRewardComum > 0.00 && ThreadLocalRandom.current().nextDouble() * 100 < kyRewardComum) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandComum);
                }



                // Handle selling drops and setting blocks to AIR
                SellDrops.sellDrops(p, mine, blocksToSell);
                blocksToSell.forEach(b -> b.setType(Material.AIR));
            }
        }
    }

}
