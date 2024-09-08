package net.minebr.object;

import lombok.Getter;
import org.bukkit.Material;

import java.util.List;

@Getter
public class BoosterObject {

    private final boolean useSkull;
    private final String skullUrl;
    private final Material material;
    private final String name;
    private final List<String> lore;
    private final int time;
    private final double bonus;

    public BoosterObject(boolean useSkull, String skullUrl, Material material, String name, List<String> lore, int time, double bonus) {
        this.useSkull = useSkull;
        this.skullUrl = skullUrl;
        this.material = material;
        this.name = name;
        this.lore = lore;
        this.time = time;
        this.bonus = bonus;
    }
}
