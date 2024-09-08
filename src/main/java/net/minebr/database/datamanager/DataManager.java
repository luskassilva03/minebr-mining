package net.minebr.database.datamanager;

import net.minebr.database.datasource.AbstractDataSource;
import net.minebr.object.PlayerObject;

import java.util.ArrayList;
import java.util.List;

public class DataManager {

    public final CachedDataManager<String, PlayerObject> USERS;

    @SuppressWarnings("rawtypes")
    private List<CachedDataManager> daos;

    public DataManager(AbstractDataSource abstractDataSource) {
        this.daos = new ArrayList<>();
        daos.add(USERS = new CachedDataManager<>(abstractDataSource, "mines_player", PlayerObject.class));
    }

    public int saveCached(boolean async) {
        daos.forEach(dao -> dao.saveCached(async));
        return daos.stream().mapToInt(dao -> dao.getCached().size()).sum();
    }
}