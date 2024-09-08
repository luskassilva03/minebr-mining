package net.minebr.manager;

import net.minebr.MiningMain;
import net.minebr.object.MinesObject;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.World;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class resetarMine {
    private final MinesObject mine;
    private final Random random = new Random(); // Use uma única instância de Random

    public resetarMine(MinesObject mine) {
        this.mine = mine;
    }

    public void resetMine() {
        notifyPlayer();

        // Divida a área em partes menores para melhorar o desempenho
        List<Block> blocosOres = pegarBlocos(mine.getPos1(), mine.getPos2(), mine.getWorld());

        new BukkitRunnable() {
            AtomicInteger index = new AtomicInteger(0);

            @Override
            public void run() {
                int startIndex = index.get();
                int endIndex = Math.min(startIndex + 100, blocosOres.size());

                if (startIndex >= blocosOres.size()) {
                    cancel();
                    return;
                }

                List<Block> toChange = blocosOres.subList(startIndex, endIndex);

                for (Block block : toChange) {
                    // Verifique se o bloco é AIR antes de substituir
                    if (block.getType() == Material.AIR) {
                        Material materialBlock = getRandomBlock();
                        block.setType(materialBlock);
                        // Removed FixedMetadataValue
                    }
                }

                index.addAndGet(100);

                if (endIndex >= blocosOres.size()) {
                    cancel();
                }
            }
        }.runTaskTimer(MiningMain.getPlugin(), 0L, 2L); // Ajuste o intervalo conforme necessário
    }

    private List<Block> pegarBlocos(Location pos1, Location pos2, World world) {
        List<Block> blocks = new ArrayList<>();
        int x1 = Math.min(pos1.getBlockX(), pos2.getBlockX());
        int x2 = Math.max(pos1.getBlockX(), pos2.getBlockX());
        int y1 = Math.min(pos1.getBlockY(), pos2.getBlockY());
        int y2 = Math.max(pos1.getBlockY(), pos2.getBlockY());
        int z1 = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        int z2 = Math.max(pos1.getBlockZ(), pos2.getBlockZ());

        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y++) {
                for (int z = z1; z <= z2; z++) {
                    blocks.add(world.getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }

    private Material getRandomBlock() {
        int totalPercentage = mine.getBlockPercentages().values().stream().mapToInt(Integer::intValue).sum();
        int randomValue = random.nextInt(totalPercentage);
        int cumulative = 0;

        for (Map.Entry<Material, Integer> entry : mine.getBlockPercentages().entrySet()) {
            cumulative += entry.getValue();
            if (randomValue < cumulative) {
                return entry.getKey();
            }
        }

        return Material.AIR; // Retorno padrão caso não encontre um bloco
    }

    private void notifyPlayer() {
        // Implementar notificação para o jogador, se necessário
    }
}
