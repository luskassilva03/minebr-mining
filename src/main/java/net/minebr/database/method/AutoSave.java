package net.minebr.database.method;

import net.minebr.MiningMain;
import net.minebr.database.datamanager.DataManager;
import net.minebr.database.util.Utils;
import lombok.val;
import org.bukkit.scheduler.BukkitRunnable;

public class AutoSave extends BukkitRunnable {

    private final DataManager dataManager;

    public AutoSave(MiningMain main, DataManager dataManager) {
        this.dataManager = dataManager;

        runTaskTimerAsynchronously(main, 20L * 60 * 30, 20L * 60 * 30);
    }

    @Override
    public void run() {
        Utils.debug(Utils.LogType.DEBUG, "Iniciando auto save");
        val before = System.currentTimeMillis();
        val i = dataManager.saveCached(false);
        val now = System.currentTimeMillis();
        val total = now - before;
        Utils.debug(Utils.LogType.INFO, "Auto completo, salvo " + i + " objetos em " + total + "ms.");
    }

}
