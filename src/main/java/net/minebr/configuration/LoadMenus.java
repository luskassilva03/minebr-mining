package net.minebr.configuration;

import net.minebr.MiningMain;
import net.minebr.object.MinesConfig;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class LoadMenus {

    private final MiningMain main = MiningMain.getPlugin();

    public void loadMenus() {
        try {
            ConfigurationSection menuminesConfigurationSection = main.menumines.getConfigurationSection("mines");
            if (menuminesConfigurationSection != null) {
                Bukkit.getConsoleSender().sendMessage("§a(minebr-mining) Carregando inventários...");
                for (String mineKey : menuminesConfigurationSection.getKeys(false)) {
                    ConfigurationSection boosterSection = menuminesConfigurationSection.getConfigurationSection(mineKey);

                    boolean useSkull = boosterSection.getBoolean("useSkull");
                    String skullUrl = boosterSection.getString("skullUrl");
                    String materialString = boosterSection.getString("Material");
                    Material material = Material.matchMaterial(materialString.split(":")[0]);
                    int slot = boosterSection.getInt("slot");

                    String name = boosterSection.getString("name").replace("&", "§");
                    List<String> lore = boosterSection.getStringList("lore");


                    MinesConfig minesConfig = new MinesConfig(useSkull, skullUrl, material, slot, name, lore);
                    main.getMenuMinesHashMap().put(mineKey, minesConfig);

                    Bukkit.getConsoleSender().sendMessage("§a(minebr-mining) Inventário carregado: " + mineKey);
                }
            }
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("§c(minebr-mining) Não foi possível encontrar a seção 'mines' no arquivo de configuração (menus/mines.yml).");
            throw new RuntimeException(e);
        }
    }

}
