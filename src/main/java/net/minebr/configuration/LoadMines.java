package net.minebr.configuration;

import net.minebr.MiningMain;
import net.minebr.manager.resetarMine;
import net.minebr.object.MinesObject;
import net.minebr.utils.LocationConverter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class LoadMines {

    MiningMain main = MiningMain.getPlugin();

    public void loadMines() {
        try {
            ConfigurationSection minesSection = main.mines.getConfigurationSection("mines");
            if (minesSection != null) {
                for (String mineKey : minesSection.getKeys(false)) {
                    ConfigurationSection mineSection = minesSection.getConfigurationSection(mineKey);
                    if (mineSection != null) {
                        String mineColoredName = mineSection.getString("name");
                        String minePermission = mineSection.getString("permission");

                        // Carregar blocos e porcentagens como List<String>
                        List<String> blockPercentagesList = mineSection.getStringList("blocks.percentage");
                        List<String> costs = mineSection.getStringList("blocks.cost");

                        // Carregar localizações
                        ConfigurationSection locationsSection = mineSection.getConfigurationSection("locations");
                        Location spawnLocation = null;
                        Location exitLocation = null;
                        if (locationsSection != null) {
                            String spawnStr = locationsSection.getString("spawn");
                            String exitStr = locationsSection.getString("exit");

                            // Converter strings para Location
                            spawnLocation = LocationConverter.convertStringToLocation(spawnStr);
                            exitLocation = LocationConverter.convertStringToLocation(exitStr);
                        }

                        // Carregar posições
                        ConfigurationSection positionsSection = mineSection.getConfigurationSection("positions");
                        Location pos1 = null;
                        Location pos2 = null;
                        if (positionsSection != null) {
                            String pos1Str = positionsSection.getString("pos1");
                            String pos2Str = positionsSection.getString("pos2");
                            pos1 = LocationConverter.convertStringToLocation(pos1Str);
                            pos2 = LocationConverter.convertStringToLocation(pos2Str);
                        }

                        // Obter o mundo
                        String worldName = mineSection.getString("world");
                        World mineWorld = Bukkit.getWorld(worldName);
                        if (mineWorld == null) {
                            Bukkit.getConsoleSender().sendMessage("§cMundo " + worldName + " não encontrado para a mina " + mineKey);
                            continue;
                        }

                        // Criar objeto MinesObject com o mundo
                        MinesObject mineObject = new MinesObject(
                                mineKey, mineColoredName, minePermission, blockPercentagesList, costs, spawnLocation, exitLocation,
                                pos1, pos2, mineWorld);

                        // Adicionar à lista
                        main.minesObjectHashMap.put(mineKey, mineObject);

                    }
                }
                Bukkit.getConsoleSender().sendMessage("§a(minebr-mining) §fforam carregados §a" + main.minesObjectHashMap.size() + " §fmina(s) em mines.yml ");
            }
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("§cWarn! há um erro em sua mines.yml");
            throw new RuntimeException(e);
        }
    }
}
