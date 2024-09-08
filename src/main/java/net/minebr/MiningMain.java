package net.minebr;

import com.henryfabio.minecraft.inventoryapi.manager.InventoryManager;
import lombok.Getter;
import lombok.Setter;
import net.minebr.api.ConfigAPI;
import net.minebr.api.VaultAPI;
import net.minebr.command.MinaAdminCommand;
import net.minebr.command.MinaCommand;
import net.minebr.configuration.*;
import net.minebr.database.datamanager.DataManager;
import net.minebr.database.datasource.AbstractDataSource;
import net.minebr.database.method.AutoSave;
import net.minebr.database.method.SaveAndLoad;
import net.minebr.database.util.Utils;
import net.minebr.functions.TrafficMine;
import net.minebr.listener.Break;
import net.minebr.listener.Interact;
import net.minebr.listener.TrafficPlayer;
import net.minebr.manager.resetarMine;
import net.minebr.object.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

@Getter
public class MiningMain extends JavaPlugin {

    @Getter
    private static MiningMain plugin;

    @Setter
    private AbstractDataSource abstractDataSource;
    @Setter private DataManager mainDataManager;

    public ConfigAPI configAPI;
    public ConfigAPI mines;
    public ConfigAPI blocks;
    public ConfigAPI pickaxes;
    public ConfigAPI bonus;
    public ConfigAPI boosters;
    public ConfigAPI menumines;

    public HashMap<String, MinesObject> minesObjectHashMap = new HashMap<>();
    private final Map<String, PickaxeObject> pickaxeConfigMap = new HashMap<>();
    private final Map<String, PermissionValues> bonusValuesMap = new HashMap<>();
    private final Map<String, BoosterObject> boosterMap = new HashMap<>();

    private final Map<String, MinesObject> playerMining = new HashMap<>();

    private final Map<String, MinesConfig> menuMinesHashMap = new HashMap<>();

    private TrafficMine trafficMine;

    @Override
    public void onEnable() {
        plugin = this;
        VaultAPI.hook();

        configAPI = new ConfigAPI(null, "config.yml", false);
        mines = new ConfigAPI(null, "mines.yml", false);
        blocks = new ConfigAPI(null, "blocks.yml", false);
        pickaxes = new ConfigAPI(null, "pickaxes.yml", false);
        bonus = new ConfigAPI(null, "bonus.yml", false);
        boosters = new ConfigAPI(null, "boosters.yml", false);
        menumines = new ConfigAPI("menus", "mines.yml", false);


        Bukkit.getScheduler().runTaskLater(this, new Runnable() {
            @Override
            public void run() {
                (new LoadMines()).loadMines();
                (new LoadPickaxes()).loadPickaxes();
                (new LoadBonus()).loadBonuses();
                (new LoadBoosters()).loadBoosters();
                (new LoadMenus()).loadMenus();
            }
        }, 30L); // 100 ticks = 5 segundos (20 ticks por segundo)

        registerInventorys();

        new MinaAdminCommand(this);
        new MinaCommand(this);

        new Interact(this);
        new Break(this);
        new TrafficPlayer(this);

        Utils.DEBUGGING = configAPI.getBoolean("Database.Debug");
        if (!SaveAndLoad.prepareDatabase(this)) return;
        PlayerObject.loadAll(this.mainDataManager.USERS);

        trafficMine = new TrafficMine(this, mainDataManager);

        new AutoSave(this, mainDataManager);

        // Agendar a tarefa de resetar as minas a cada 5 minutos
        scheduleMineResetTask();


        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage("§a(minebr-mining) §ffoi inicializado com sucesso!");
        Bukkit.getConsoleSender().sendMessage("");
    }

    @Override
    public void onDisable() {
        removeAllPlayersFromMines();
        SaveAndLoad.saveAll(this);
    }

    public void registerInventorys() {
        InventoryManager.enable(this);
    }

    private void removeAllPlayersFromMines() {
        for (Player player : getServer().getOnlinePlayers()) {
            if (getPlayerMining().containsKey(player.getName())) {
                trafficMine.exitMine(player, getPlayerMining().get(player.getName()));
            }
        }
    }
    public int getPlayersInMine(String mineKey) {
        long count = playerMining.values().stream()
                .filter(mine -> mine.getKey().equals(mineKey)) // Compara o key do MinesObject com mineKey
                .count();
        return (int) count;
    }

    // Método para agendar a tarefa de resetar as minas a cada 5 minutos
    private void scheduleMineResetTask() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                for (MinesObject mine : minesObjectHashMap.values()) {
                    resetarMine resetManager = new resetarMine(mine);
                    resetManager.resetMine();
                }
                Bukkit.getConsoleSender().sendMessage("§aTodas as minas foram resetadas!");
            }
        }, 0L, 300L * 20L); // 300 segundos * 20 ticks/segundo = 6000 ticks (5 minutos)
    }

}
