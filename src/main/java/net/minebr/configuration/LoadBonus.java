package net.minebr.configuration;

import net.minebr.MiningMain;
import net.minebr.object.PermissionValues;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;


public class LoadBonus {

    private final MiningMain main = MiningMain.getPlugin();

    public void loadBonuses() {
        try {
            ConfigurationSection bonusesSection = main.bonus.getConfigurationSection("bonus");
            if (bonusesSection != null) {
                for (String groupKey : bonusesSection.getKeys(false)) {
                    ConfigurationSection groupSection = bonusesSection.getConfigurationSection(groupKey);

                    String display = groupSection.getString("display");
                    String permission = groupSection.getString("permission");
                    double bonus = groupSection.getDouble("bonus");

                    PermissionValues permissionValues = new PermissionValues(groupKey, display, permission, bonus);
                    main.getBonusValuesMap().put(groupKey, permissionValues);
                }
                Bukkit.getConsoleSender().sendMessage("§a(minebr-mining) §fForam carregados §a" + main.getBonusValuesMap().size() + " §fgrupos de bonus.yml.");
            }
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("§cWarn! Há um erro em sua configuração de bonus.yml.");
            throw new RuntimeException(e);
        }
    }
}
