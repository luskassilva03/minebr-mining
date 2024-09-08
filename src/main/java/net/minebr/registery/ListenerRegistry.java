package net.minebr.registery;

import net.minebr.MiningMain;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class ListenerRegistry implements Listener {

    protected MiningMain main;

    public ListenerRegistry(MiningMain main) {
        this.main = main;

        Bukkit.getPluginManager().registerEvents(this, main);
    }

}
