package net.realdarkstudios.commons.data;

import me.scarsz.mojang.Mojang;
import me.scarsz.mojang.exception.ProfileFetchException;
import net.realdarkstudios.commons.CommonsAPI;
import net.realdarkstudios.commons.RDSCommons;
import net.realdarkstudios.commons.menu.item.SkullMenuItem;
import net.realdarkstudios.commons.misc.Config;
import net.realdarkstudios.commons.util.CommonsUtils;
import net.realdarkstudios.commons.util.MessageKeys;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nullable;
import java.io.File;
import java.util.*;

public class PlayerDataObject extends DataObject {
    private final UUID uniqueID;
    private boolean banned;
    private ItemStack skullItemStack;

    public PlayerDataObject(UUID uniqueID, YamlConfiguration yaml, File file) {
        super(yaml, file);
        this.uniqueID = uniqueID;
    }

    /* DEFAULT INFO */

    public String getUsername() {
        if (getUniqueID().equals(CommonsUtils.EMPTY_UUID)) return "[CONSOLE]";

        try {
            if (getOfflinePlayer().hasPlayedBefore() && getOfflinePlayer().getName() != null) return getOfflinePlayer().getName();
            else return getGameProfile().getName();
        } catch (Exception e) {
            return getGameProfile().getName();
        }
    }

    public boolean isOnline() {
        return getOfflinePlayer().isOnline();
    }

    public OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(uniqueID);
    }

    private Mojang.GameProfile getGameProfile() {
        try {
            return Mojang.fetch(uniqueID);
        } catch (ProfileFetchException e) {
            return new Mojang.GameProfile(uniqueID, getOfflinePlayer().getName(), List.of(), "");
        }
    }

    @Nullable
    public Player getPlayer() {
        return getOfflinePlayer().getPlayer();
    }

    public UUID getUniqueID() {
        return uniqueID;
    }

    public ItemStack getSkullItemStack() {
        return skullItemStack;
    }

    @Override
    protected void preload() {
        get(uniqueID);
    }

    @Override
    protected void updateFields() {
        try {
            if (uniqueID.equals(CommonsUtils.EMPTY_UUID)) {
                this.skullItemStack = new ItemStack(Material.PLAYER_HEAD);
            } else if (Mojang.fetch(uniqueID) == null) throw new ProfileFetchException(uniqueID.toString(), new Exception());
            else this.skullItemStack = new SkullMenuItem(getUsername(), getUniqueID(), List.of()).getSkull(getOfflinePlayer());
        } catch (ProfileFetchException e) {
            this.skullItemStack = new ItemStack(Material.PLAYER_HEAD);
        }
    }

    @Override
    protected void saveFields() {
    }

    public static PlayerDataObject get(UUID uuid) {
        File plrFile = new File(RDSCommons.getInstance().getDataFolder() + "/player/" + uuid + ".yml");

        if (!plrFile.exists()) {
            try {
                plrFile.createNewFile();
            } catch (Exception e) {
                CommonsAPI.tWarning(MessageKeys.Error.CREATE_FILE, uuid + ".yml");
            }
        } else if (Config.getInstance().getPlayerDataVersion() < CommonsAPI.PLAYER_DATA_VERSION) {
            try {
                if (!plrFile.canWrite()) throw new Exception();
                plrFile.createNewFile();
            } catch (Exception e) {
                CommonsAPI.tWarning(MessageKeys.Error.UPDATE_FILE, uuid + ".yml");
            }
        }

        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(plrFile);

        return new PlayerDataObject(uuid, yaml, plrFile);
    }

    boolean delete() {
        return getFile().delete();
    }
}
