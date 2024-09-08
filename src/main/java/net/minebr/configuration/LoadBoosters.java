package net.minebr.configuration;

import net.minebr.MiningMain;
import net.minebr.object.BoosterObject;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadBoosters {

    private final MiningMain main = MiningMain.getPlugin();

    public void loadBoosters() {
        try {
            ConfigurationSection boostersSection = main.boosters.getConfigurationSection("boosters");
            if (boostersSection != null) {
                for (String boosterKey : boostersSection.getKeys(false)) {
                    ConfigurationSection boosterSection = boostersSection.getConfigurationSection(boosterKey);

                    boolean useSkull = boosterSection.getBoolean("useSkull");
                    String skullUrl = boosterSection.getString("skullUrl");
                    String materialString = boosterSection.getString("Material");
                    Material material = Material.matchMaterial(materialString.split(":")[0]);

                    String name = boosterSection.getString("name").replace("&", "§");
                    List<String> lore = boosterSection.getStringList("lore");

                    ConfigurationSection boosterDetails = boosterSection.getConfigurationSection("booster");
                    int time = boosterDetails.getInt("time");
                    double bonus = boosterDetails.getDouble("bonus");

                    BoosterObject boosterObject = new BoosterObject(useSkull, skullUrl, material, name, lore, time, bonus);
                    main.getBoosterMap().put(boosterKey, boosterObject);
                }
                Bukkit.getConsoleSender().sendMessage("§a(minebr-mining) §fForam carregados §a" + main.getBoosterMap().size() + " §fbooster(s) em boosters.yml.");
            }
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("§cWarn! há um erro em sua boosters.yml");
            throw new RuntimeException(e);
        }
    }
}
