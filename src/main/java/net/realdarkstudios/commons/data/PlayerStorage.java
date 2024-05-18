package net.realdarkstudios.commons.data;

import net.realdarkstudios.commons.CommonsAPI;
import net.realdarkstudios.commons.RDSCommons;
import net.realdarkstudios.commons.misc.Config;
import net.realdarkstudios.commons.util.MessageKeys;
import org.bukkit.OfflinePlayer;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerStorage {
    private HashMap<UUID, PlayerDataObject> playerStorage = new HashMap<>();

    private PlayerStorage() {
    }

    public void load() {
        File file = new File(RDSCommons.getInstance().getDataFolder() + "/player/");
        if (!file.exists()) file.mkdirs();

        File[] plrFiles = file.listFiles(File::isFile);
        if (plrFiles == null || plrFiles.length == 0) CommonsAPI.tWarning(MessageKeys.Error.PLAYER_LIST_EMPTY);

        HashMap<UUID, PlayerDataObject> players = new HashMap<>();
        for (File plrFile: plrFiles) {
            String key = plrFile.getName().replace(".yml", "");
            UUID uuid;
            try {
                uuid = UUID.fromString(key);
            } catch (Exception e) {
                CommonsAPI.tWarning(MessageKeys.Error.PARSE_UUID, key);
                continue;
            }

            PlayerDataObject plr = PlayerDataObject.get(uuid);

            plr.load();
            players.put(uuid, plr);
        }

        this.playerStorage = players;

        for (PlayerDataObject plr : playerStorage.values()) {
            plr.load();
        }

        CommonsAPI.tInfo(MessageKeys.Api.LOADED_PLAYERS, playerStorage.size());
    }

    public void save() {
        if (playerStorage != null) {
            for (PlayerDataObject plr : playerStorage.values()) {
                plr.save();
            }
        }
    }

    public void updateMaps() {
        HashMap<UUID, PlayerDataObject> players = new HashMap<>();
        File file = new File(RDSCommons.getInstance().getDataFolder() + "/player/");
        if (!file.exists()) file.mkdirs();

        File[] plrFiles = file.listFiles(File::isFile);
        if (plrFiles == null || plrFiles.length == 0) CommonsAPI.tWarning(MessageKeys.Error.PLAYER_LIST_EMPTY);

        for (File plrFile: plrFiles) {
            String key = plrFile.getName().replace(".yml", "");
            UUID uuid;
            try {
                uuid = UUID.fromString(key);
            } catch (Exception e) {
                CommonsAPI.tWarning(MessageKeys.Error.PARSE_UUID, key);
                continue;
            }

            PlayerDataObject plr = PlayerDataObject.get(uuid);

            plr.load();
            players.put(uuid, plr);
        }

        this.playerStorage = players;
    }

    public boolean deletePlayerData(PlayerDataObject plr) {
        return deletePlayerData(plr.getUniqueID());
    }

    public boolean deletePlayerData(OfflinePlayer plr) {
        return deletePlayerData(plr.getUniqueId());
    }

    public boolean deletePlayerData(UUID uuid) {
        try {

            playerStorage.get(uuid).delete();

            save();
            updateMaps();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public PlayerDataObject createPlayerData(OfflinePlayer plr) {
        return createPlayerData(plr.getUniqueId());
    }

    public PlayerDataObject createPlayerData(UUID uuid) {
        PlayerDataObject pdo = PlayerDataObject.get(uuid);
        CommonsAPI.tInfo(MessageKeys.Api.NEW_PLAYER_DATA, uuid);

        save();
        updateMaps();

        return pdo;
    }

    public boolean hasPlayerData(OfflinePlayer plr) {
        return hasPlayerData(plr.getUniqueId());
    }

    public boolean hasPlayerData(UUID uuid) {
        return playerStorage.containsKey(uuid);
    }

    public PlayerDataObject getPlayerData(OfflinePlayer plr) {
        return getPlayerData(plr.getUniqueId());
    }

    public PlayerDataObject getPlayerData(UUID uuid) {
        return playerStorage.get(uuid);
    }

    public PlayerDataObject getOrCreatePlayerData(OfflinePlayer plr) {
        return getOrCreatePlayerData(plr.getUniqueId());
    }

    public PlayerDataObject getOrCreatePlayerData(UUID uuid) {
        if (hasPlayerData(uuid)) return getPlayerData(uuid);
        else return createPlayerData(uuid);
    }

    public List<PlayerDataObject> getPlayers() {
        return playerStorage.values().stream().toList();
    }

    public static PlayerStorage getInstance() {
        if (CommonsAPI.get().hasInitialized()) return CommonsAPI.getPlayerStorage();
        else return new PlayerStorage();
    }

    public void updateData() {
        try {
            if (playerStorage != null) {
                for (PlayerDataObject plr : playerStorage.values()) {
                    plr.updateData();
                }
            }

            CommonsAPI.tWarning(MessageKeys.Api.UPDATE_SUCCEEDED, "Player Data", Config.getInstance().getPlayerDataVersion(), CommonsAPI.PLAYER_DATA_VERSION);

            Config.getInstance().setPlayerDataVersion(CommonsAPI.PLAYER_DATA_VERSION);
            if (playerStorage != null) {
                for (PlayerDataObject plr : playerStorage.values()) {
                    plr.save();
                }
            }
            Config.getInstance().save();
        } catch (Exception e) {
            CommonsAPI.tWarning(MessageKeys.Api.UPDATE_FAILED, "Player Data", Config.getInstance().getPlayerDataVersion(), CommonsAPI.PLAYER_DATA_VERSION);
        }
    }
}
