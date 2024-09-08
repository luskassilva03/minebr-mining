package net.minebr.object;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class MinesObject {
    private final String key;
    private final String coloredName;
    private final String permission;
    @Getter
    private final Map<Material, Integer> blockPercentages;
    @Getter
    private final Map<Material, Double> blockCosts;
    private final Location spawnLocation;
    private final Location exitLocation;
    private final Location pos1;
    private final Location pos2;
    private final World world;

    public MinesObject(String key, String coloredName, String permission, List<String> blockPercentageList,
                       List<String> costs, Location spawnLocation, Location exitLocation,
                       Location pos1, Location pos2, World world) {
        this.key = key;
        this.coloredName = coloredName;
        this.permission = permission;
        this.blockPercentages = parseBlocksWithPercentages(blockPercentageList);
        this.blockCosts = parseCosts(costs);
        this.spawnLocation = spawnLocation;
        this.exitLocation = exitLocation;
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.world = world;
    }

    private Map<Material, Integer> parseBlocksWithPercentages(List<String> blockPercentageList) {
        Map<Material, Integer> blockPercentages = new HashMap<>();
        int totalPercentage = 0;

        for (String blockPercentage : blockPercentageList) {
            String[] parts = blockPercentage.split(";");
            if (parts.length == 2) {
                String[] blockParts = parts[0].split(":");
                if (blockParts.length == 2) {
                    Material material = Material.getMaterial(blockParts[0]);
                    int data = Integer.parseInt(blockParts[1]);
                    int percentage = Integer.parseInt(parts[1]);
                    if (material != null) {
                        blockPercentages.put(material, percentage);
                        totalPercentage += percentage;
                    }
                }
            }
        }

        if (totalPercentage <= 0) {
            throw new IllegalArgumentException("A soma das porcentagens deve ser maior que zero.");
        }

        return blockPercentages;
    }

    private Map<Material, Double> parseCosts(List<String> costs) {
        Map<Material, Double> costMap = new HashMap<>();
        for (String cost : costs) {
            String[] parts = cost.split(";");
            if (parts.length == 2) {
                Material material = Material.getMaterial(parts[0]);
                if (material != null) {
                    try {
                        double value = Double.parseDouble(parts[1]);
                        costMap.put(material, value);
                    } catch (NumberFormatException e) {
                        if (parts[1].equalsIgnoreCase("diamond")) {
                            costMap.put(material, -1.0);
                        }
                    }
                }
            }
        }
        return costMap;
    }

    public boolean isBlockInMine(Material material) {
        return blockCosts.containsKey(material);
    }

    public double getBlockCost(Material material) {
        return blockCosts.getOrDefault(material, 0.0);
    }
}
