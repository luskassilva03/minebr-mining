package net.minebr.listener;

import net.minebr.MiningMain;
import net.minebr.functions.TrafficMine;
import net.minebr.object.MinesObject;
import net.minebr.registery.ListenerRegistry;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class TrafficPlayer extends ListenerRegistry {

    public TrafficPlayer(MiningMain main) {
        super(main);
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (MiningMain.getPlugin().getPlayerMining().containsKey(p.getName())) {
            MinesObject mine = MiningMain.getPlugin().getPlayerMining().get(p.getName());
            TrafficMine trafficMine = MiningMain.getPlugin().getTrafficMine();
            trafficMine.exitMine(p, mine);
        }
    }
}
