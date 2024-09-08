package net.minebr.object;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import net.minebr.database.Keyable;
import net.minebr.database.datamanager.CachedDataManager;
import net.minebr.database.util.Utils;

import java.util.*;

@Getter
@Setter
public class PlayerObject implements Keyable<String> {

    private final String player;
    private final String pickaxe;
    private Map<String, Long> activeBoosters; // Stores booster name and expiration time in milliseconds
    private Map<String, Double> boosterBonuses; // Stores booster name and bonus percentage

    public PlayerObject(String player, String pickaxe) {
        this.player = player;
        this.pickaxe = pickaxe;
        this.activeBoosters = new HashMap<>();
        this.boosterBonuses = new HashMap<>();
    }

    @Override
    public String getKey() {
        return player;
    }

    public static void loadAll(CachedDataManager<String, PlayerObject> dao) {
        Utils.measureTime(() -> {
            int i = 0;
            for (PlayerObject playerObject : dao.getAll()) {
                if (dao.isCached(playerObject.getPlayer())) continue;
                load(playerObject.getPlayer(), dao);
                i++;
            }
            return "Carregado " + i + " objetos em {time}";
        });
    }

    public static void load(String string, CachedDataManager<String, PlayerObject> dao) {
        if (dao.exists(string)) {
            val account = dao.find(string);
            dao.cache(account);
        }
    }

    // Method to activate a booster for the player
    public void activateBooster(String boosterName, long durationInSeconds, double bonus) {
        if (boosterName == null) {
            throw new IllegalArgumentException("Booster name cannot be null");
        }

        long currentTime = System.currentTimeMillis();
        long expirationTime = currentTime + (durationInSeconds * 1000);

        // Remove any existing booster with the same name if it's expired
        if (activeBoosters.containsKey(boosterName)) {
            if (activeBoosters.get(boosterName) <= currentTime) {
                activeBoosters.remove(boosterName);
                boosterBonuses.remove(boosterName);
            }
        }

        activeBoosters.put(boosterName, expirationTime);
        boosterBonuses.put(boosterName, bonus);
    }

    public List<Booster> getActiveAllBoosters() {
        List<Booster> boosters = new ArrayList<>();
        long currentTime = System.currentTimeMillis();
        for (Map.Entry<String, Long> entry : activeBoosters.entrySet()) {
            if (entry.getValue() > currentTime) {
                String type = entry.getKey();
                double percentage = boosterBonuses.get(type);
                long remainingTime = (entry.getValue() - currentTime) / 1000;
                boosters.add(new Booster(type, percentage, remainingTime));
            }
        }
        return boosters;
    }

    // Method to get the remaining time for a booster
    public long getRemainingBoosterTime(String boosterName) {
        if (boosterName == null) {
            throw new IllegalArgumentException("Booster name cannot be null");
        }

        Long expirationTime = activeBoosters.get(boosterName);
        if (expirationTime == null) {
            return 0;
        }
        long remainingTime = expirationTime - System.currentTimeMillis();
        return remainingTime > 0 ? remainingTime / 1000 : 0; // Return remaining time in seconds
    }

    // Method to get the bonus for a booster
    public double getBoosterBonus(String boosterName) {
        if (boosterName == null) {
            throw new IllegalArgumentException("Booster name cannot be null");
        }

        return boosterBonuses.getOrDefault(boosterName, 0.0);
    }

    // Method to check if a player has an active booster
    public boolean hasActiveBooster(String boosterName) {
        if (boosterName == null) {
            throw new IllegalArgumentException("Booster name cannot be null");
        }

        return getRemainingBoosterTime(boosterName) > 0;
    }

    // Method to update the booster expiration time
    public void updateBoosterTime(String boosterName, long remainingTimeInSeconds) {
        if (boosterName == null) {
            throw new IllegalArgumentException("Booster name cannot be null");
        }

        long expirationTime = System.currentTimeMillis() + (remainingTimeInSeconds * 1000);
        activeBoosters.put(boosterName, expirationTime);
    }

    // Method to remove expired boosters
    public void removeExpiredBoosters() {
        long currentTime = System.currentTimeMillis();
        activeBoosters.entrySet().removeIf(entry -> entry.getValue() <= currentTime);
        boosterBonuses.keySet().removeIf(key -> !activeBoosters.containsKey(key));
    }
}
