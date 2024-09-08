package net.minebr.configuration;

import net.minebr.MiningMain;
import net.minebr.object.PickaxeObject;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadPickaxes {

    private final MiningMain main = MiningMain.getPlugin();

    public void loadPickaxes() {
        try {
            ConfigurationSection pickaxesSection = main.pickaxes.getConfigurationSection("pickaxes");
            if (pickaxesSection != null) {
                for (String pickaxeKey : pickaxesSection.getKeys(false)) {
                    ConfigurationSection pickaxeSection = pickaxesSection.getConfigurationSection(pickaxeKey);

                    String materialString = pickaxeSection.getString("material");
                    Material material = Material.matchMaterial(materialString.split(":")[0]);

                    String name = pickaxeSection.getString("name");
                    List<String> lore = pickaxeSection.getStringList("lore");

                    Map<String, Integer> enchants = new HashMap<>();
                    ConfigurationSection enchantsSection = pickaxeSection.getConfigurationSection("enchants");
                    if (enchantsSection != null) {
                        for (String enchantKey : enchantsSection.getKeys(false)) {
                            enchants.put(enchantKey, enchantsSection.getInt(enchantKey));
                        }
                    }

                    PickaxeObject pickaxeObject = new PickaxeObject(material, name.replace("&", "§"), lore, enchants);
                    main.getPickaxeConfigMap().put(pickaxeKey, pickaxeObject);
                }
                Bukkit.getConsoleSender().sendMessage("§a(minebr-mining) §fforam carregadas §a" + main.getPickaxeConfigMap().size() + " §fpicareta(s) em pickaxes.yml");
            }
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("§cWarn! há um erro em sua pickaxes.yml");
            throw new RuntimeException(e);
        }
    }
}
