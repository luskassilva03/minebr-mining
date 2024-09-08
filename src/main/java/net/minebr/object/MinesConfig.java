package net.minebr.object;

import lombok.Getter;
import org.bukkit.Material;

import java.util.List;

@Getter
public class MinesConfig {

    private final boolean useSkull;
    private final String skullUrl;
    private Material material;
    int slot;
    String display;
    List<String> lore;

    public MinesConfig(boolean useSkull, String skullUrl, Material material, int slot, String display, List<String> lore) {
        this.useSkull = useSkull;
        this.skullUrl = skullUrl;
        this.material = material;
        this.display = display;
        this.slot = slot;
        this.lore = lore;
    }
}
