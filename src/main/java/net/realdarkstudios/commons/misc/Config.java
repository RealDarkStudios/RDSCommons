package net.realdarkstudios.commons.misc;

import net.realdarkstudios.commons.CommonsAPI;
import net.realdarkstudios.commons.RDSCommons;
import net.realdarkstudios.commons.util.MessageKeys;
import org.bukkit.configuration.file.YamlConfiguration;
import org.checkerframework.common.value.qual.IntRange;

import java.io.File;
import java.util.Locale;
import org.joml.Math;

public class Config {
    private File file;
    private YamlConfiguration yaml;

    Config() {
    }

    public void load() {
        file = new File(RDSCommons.getInstance().getDataFolder(), "config.yml");

        if (!file.exists()) {
            RDSCommons.getInstance().saveResource("config.yml", false);
        }

        yaml = new YamlConfiguration();
        yaml.options().parseComments(true);

        try {
            yaml.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            yaml.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getConfigVersion() {
        return yaml.getInt("CONFIG_VERSION");
    }

    public int getPlayerDataVersion() {
        return yaml.getInt("PLAYER_DATA_VERSION");
    }

    public void setPlayerDataVersion(int version) {
        yaml.set("PLAYER_DATA_VERSION", version);
    }

    public Locale getServerLocale() {
        return Locale.forLanguageTag(yaml.getString("LOCALE", "en-US"));
    }

    public boolean autoUpdate() {
        return yaml.getBoolean("AUTO_UPDATE", false);
    }

    public void setAutoUpdate(boolean autoUpdate) {
        yaml.set("AUTO_UPDATE", autoUpdate);
        save();
    }

    public String getUpdateBranch() {
        return yaml.getString("UPDATE_BRANCH", "release").equals("snapshot") ? "snapshot" : "release";
    }

    public void setUpdateBranch(String branch) {
        yaml.set("UPDATE_BRANCH", branch.equals("snapshot") ? "snapshot" : branch.equals("release") ? "release" : getUpdateBranch());
        save();
    }

    public boolean experimentalFeatures() {
        return yaml.getBoolean("EXPERIMENTAL_FEATURES", false);
    }

    public boolean debugEvents() {
        return yaml.getBoolean("DEBUG_EVENTS", false);
    }

    public void setDebugEvents(boolean debugEvents) {
        yaml.set("DEBUG_EVENTS", debugEvents);
        save();
    }

    public int getDebugEventsLevel() {
        return Math.clamp(0, 2, yaml.getInt("DEBUG_EVENTS_LEVEL", 0));
    }

    public void setDebugEventsLevel(@IntRange(from = 0, to = 2) int level) {
        yaml.set("DEBUG_EVENTS_LEVEL", level);
        save();
    }

    public void updateData() {
        try {
            int configVersion = getConfigVersion();
            int playerDataVersion = getPlayerDataVersion();

            Locale serverLocale = getServerLocale();

            boolean autoUpdate = autoUpdate();
            String updateBranch = getUpdateBranch();
            boolean experimentalFeatures = experimentalFeatures();
            boolean debugEvents = debugEvents();
            int debugEventsLevel = getDebugEventsLevel();


            yaml.set("PLAYER_DATA_VERSION", playerDataVersion);
            yaml.set("LOCALE", serverLocale.toLanguageTag());
            yaml.set("AUTO_UPDATE", autoUpdate);
            yaml.set("UPDATE_BRANCH", updateBranch);
            yaml.set("EXPERIMENTAL_FEATURES", experimentalFeatures);
            yaml.set("DEBUG_EVENTS", debugEvents);
            yaml.set("DEBUG_EVENTS_LEVEL", debugEventsLevel);
            yaml.set("CONFIG_VERSION", CommonsAPI.CONFIG_DATA_VERSION);
            CommonsAPI.tInfo(MessageKeys.Api.UPDATE_SUCCEEDED, "Config", configVersion, CommonsAPI.CONFIG_DATA_VERSION);
            save();
        } catch (Exception e) {
            CommonsAPI.tInfo(MessageKeys.Api.UPDATE_FAILED, "Config", getConfigVersion(), CommonsAPI.CONFIG_DATA_VERSION);
        }
    }

    public static Config getInstance() {
        if (CommonsAPI.get().hasInitialized()) return CommonsAPI.getConfig();
        else return new Config();
    }
}
