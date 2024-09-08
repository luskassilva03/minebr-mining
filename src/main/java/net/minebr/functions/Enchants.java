package net.minebr.functions;

import net.minebr.object.MinesObject;
import net.minebr.utils.SendTitle;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Enchants {

    private final JavaPlugin plugin;

    public Enchants(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    private boolean isValidBlock(Block block, MinesObject mine) {
        return block.getType() != Material.AIR && mine.isBlockInMine(block.getType());
    }

    public List<Block> applyExplosiveEnchant(Player player, Block block, MinesObject mine) {
        SendTitle.enviarTitulo(player, "§c§lBOOOM", "§fVocê causou uma explosão!", 0,0,0);
        player.playSound(player.getLocation(), Sound.FIREWORK_BLAST2, 1.0F, 1.0F);
        List<Block> blocks = new ArrayList<>();
        int radius = 2; // 5x5 area (2 blocks in each direction)
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Block targetBlock = block.getRelative(x, y, z);
                    if (isValidBlock(targetBlock, mine)) {
                        blocks.add(targetBlock);
                    }
                }
            }
        }
        return blocks;
    }

    public List<Block> applyLaserEnchant(Player player, Block block, MinesObject mine) {
        SendTitle.enviarTitulo(player, "§a§lZIIIP", "§fVocê usou o laser!", 0,0,0);
        player.playSound(player.getLocation(), Sound.FIREWORK_LAUNCH, 1.0F, 3.0F);
        List<Block> blocks = new ArrayList<>();
        int radius = 15; // X shape
        for (int i = -radius; i <= radius; i++) {
            Block targetBlock1 = block.getRelative(i, 0, 0);
            Block targetBlock2 = block.getRelative(0, 0, i);
            if (isValidBlock(targetBlock1, mine)) {
                blocks.add(targetBlock1);
            }
            if (isValidBlock(targetBlock2, mine)) {
                blocks.add(targetBlock2);
            }
        }
        return blocks;
    }

    public List<Block> applyDestruidorEnchant(Player player, Block block, MinesObject mine) {
        SendTitle.enviarTitulo(player, "§b§lTRAAAA", "§fVocê arrebentou uma camada!", 0,0,0);
        player.playSound(player.getLocation(), Sound.ITEM_BREAK, 1.0F, 1.0F);
        List<Block> blocks = new ArrayList<>();
        int radius = 5; // 10x10 area
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                Block targetBlock = block.getRelative(x, 0, z);
                if (isValidBlock(targetBlock, mine)) {
                    blocks.add(targetBlock);
                }
            }
        }
        return blocks;
    }

    public List<Block> applyNuclearEnchant(Player player, Block block, MinesObject mine) {
        SendTitle.enviarTitulo(player, "§4§lKABUMMM", "§fArrebenta tudo!", 0,0,0);
        player.playSound(player.getLocation(), Sound.EXPLODE, 1.0F, 1.0F);
        List<Block> blocks = new ArrayList<>();
        int radius = 6; // Sphere with radius 5
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if (Math.sqrt(x * x + y * y + z * z) <= radius) {
                        Block targetBlock = block.getRelative(x, y, z);
                        if (isValidBlock(targetBlock, mine)) {
                            blocks.add(targetBlock);
                        }
                    }
                }
            }
        }
        return blocks;
    }

    public void showParticleEffect(Player player, List<Block> blocks) {
        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;

        for (Block block : blocks) {
            Location location = block.getLocation();
            float x = (float) (location.getX() + 0.5D);
            float y = (float) (location.getY() + 0.5D);
            float z = (float) (location.getZ() + 0.5D);

            PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(
                    EnumParticle.FIREWORKS_SPARK, // Tipo de partícula
                    true, // Pode se espalhar
                    x, y, z, // Localização
                    0.3F, 0.3F, 0.3F, // Velocidade
                    0.1F, // Probabilidade
                    1, // Número de partículas
                    new int[0] // Dados adicionais, se houver
            );

            connection.sendPacket(packet);
        }
    }

}
