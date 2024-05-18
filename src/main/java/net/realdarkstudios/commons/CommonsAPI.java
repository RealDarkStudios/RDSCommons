package net.realdarkstudios.commons;

import net.realdarkstudios.commons.misc.BaseAPI;
import net.realdarkstudios.commons.misc.Config;
import net.realdarkstudios.commons.data.PlayerDataObject;
import net.realdarkstudios.commons.data.PlayerStorage;
import net.realdarkstudios.commons.util.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.function.Predicate;

public class CommonsAPI extends BaseAPI {
    /**
     * Defines the expected Config Data Version
     */
    public static final int CONFIG_DATA_VERSION = 12;
    /**
     * Defines the expected Player Data Version
     */
    public static final int PLAYER_DATA_VERSION = 7;

    private boolean hasInitialized = false;
    private static Config config;
    private static RDSLogHelper logHelper;
    private static LocalizationProvider localizationProvider;
    private static Localization apiLocalization;
    private static RDSCommonsUpdater updater;
    private static PlayerStorage playerStorage;

    /**
     * Gets the Config for {@link RDSCommons}
     * @return The {@link Config}
     */
    public static Config getConfig() {
        return config;
    }

    /**
     * Gets the Player Storage for {@link RDSCommons}
     * @return The {@link PlayerStorage}
     */
    public static PlayerStorage getPlayerStorage() {
        return playerStorage;
    }

    /**
     * Gets the Localization Provider for {@link RDSCommons}
     * @return The {@link LocalizationProvider}
     * @since 1.0.0.0
     */
    public LocalizationProvider getLocalizationProvider() {
        return localizationProvider;
    }

    /**
     * Gets the API Localization for {@link RDSCommons}
     * @return The API {@link Localization}
     * @since 1.0.0.0
     */
    public Localization getAPILocalization() {
        return apiLocalization;
    }

    /**
     * Gets the API Logger for {@link RDSCommons}
     * @return The API {@link RDSLogHelper}
     * @since 1.0.0.0
     */
    public RDSLogHelper getAPILogger() {
        return logHelper;
    }

    /**
     * Gets the Auto Updater for {@link RDSCommons}
     * @return The {@link RDSCommonsUpdater}
     * @since 1.0.0.0
     */
    public RDSCommonsUpdater getUpdater() {
        return updater;
    }

    /**
     * Gets the version from {@link RDSCommons}
     * @return The {@link Version}
     * @since 1.0.0.0
     */
    public Version getVersion() {
        return RDSCommons.getInstance().getVersion();
    }

    /**
     * Gets the version string from {@link RDSCommons}
     * @return The version string
     * @since 1.0.0.0
     */
    public String getVersionString() {
        return RDSCommons.getInstance().getVersionString();
    }

    /**
     * Logs all the messages at the INFO level from the Commons Logger.
     * To use your own logger, use {@link Localization#getLogHelper()}
     * @param messages The messages to log
     * @since 1.0.0.0
     */
    public static void info(String... messages) {
        if (!get().hasInitialized()) {
            for (String msg : messages) {
                RDSCommons.getInstance().getLogger().info(msg);
            }
        }
        else get().getAPILogger().info(messages);
    }

    /**
     * Logs the message using the {@link LocalizedMessages} system at the INFO level
     * @param key The {@link LocalizedMessages.Key} to log
     * @param formatArgs The format arguments
     * @see MessageKeys
     * @since 1.0.0.0
     */
    public static void tInfo(LocalizedMessages.Key key, Object... formatArgs) {
        info(key.console(formatArgs));
    }

    /**
     * Logs all the messages at the WARN level
     * @param messages The messages to log
     * @since 1.0.0.0
     */
    public static void warning(String... messages) {
        if (!get().hasInitialized()) {
            for (String msg : messages) {
                RDSCommons.getInstance().getLogger().warning(msg);
            }
        }
        else get().getAPILogger().warning(messages);
    }

    /**
     * Logs the message using the {@link LocalizedMessages} system at the WARNING level
     * @param key The {@link LocalizedMessages.Key} to log
     * @param formatArgs The format arguments
     * @see MessageKeys
     * @since 1.0.0.0
     */
    public static void tWarning(LocalizedMessages.Key key, Object... formatArgs) {
        warning(key.console(formatArgs));
    }

    public Localization registerLocalization(Plugin plugin, Locale locale) {
        if (!hasInitialized) {
            info("Registering localization '" + locale.toLanguageTag() + "' for plugin '" + plugin.getName() + "'");
        } else tInfo(MessageKeys.Api.REGISTERING_LOCALIZATION, locale.toLanguageTag(), plugin.getName());
        return localizationProvider.load(plugin, locale, plugin.getDescription().getPrefix());
    }

    public Localization registerLocalization(Plugin plugin, Locale locale, String prefix) {
        if (!hasInitialized) {
            info("Registering localization '" + locale.toLanguageTag() + "' for plugin '" + plugin.getName() + "'");
        } else tInfo(MessageKeys.Api.REGISTERING_LOCALIZATION, locale.toLanguageTag(), plugin.getName());
        return localizationProvider.load(plugin, locale, prefix);
    }

    /**
     * Gets the player data for the given player
     * @param player The {@link Player} to get the data for
     * @return The {@link PlayerDataObject} associated with the player
     * @since 0.2.0.0
     */
    public PlayerDataObject getPlayerData(Player player) {
        return getPlayerData(player.getUniqueId());
    }

    /**
     * Gets the player data for the given UUID
     * @param uuid The {@link UUID} of the player to get the data for
     * @return The {@link PlayerDataObject} associated with the UUID
     * @since 0.2.0.0
     */
    public PlayerDataObject getPlayerData(UUID uuid) {
        return playerStorage.getOrCreatePlayerData(uuid);
    }

    /**
     * Gets the list of {@link PlayerDataObject}s for all players that currently have data
     * @return The list of PlayerDataObjects for all players that have data
     * If you want a list of ALL players, use {@link Bukkit#getOfflinePlayers()} or {@link Bukkit#getOnlinePlayers()}
     * @since 0.2.1.0
     */
    public List<PlayerDataObject> getAllKnownPlayers() {
        return playerStorage.getPlayers();
    }

    /**
     * Gets the list of {@link PlayerDataObject}s for all players that currently have data and filters it by the predicate
     * @param predicate The method by which to filter out player data
     * @return The filtered list of PlayerDataObjects for all players that have data
     * If you want a list of ALL players, use {@link Bukkit#getOfflinePlayers()} or {@link Bukkit#getOnlinePlayers()}
     * @since 0.2.1.0
     */
    public List<PlayerDataObject> getFilteredPlayers(Predicate<PlayerDataObject> predicate) {
        return getAllKnownPlayers().stream().filter(predicate).toList();
    }

    /**
     * Gets the list of {@link PlayerDataObject}s for all players that currently have data and sorts it by the comparator
     * @param comparator The method by which to sort player data
     * @return The sorted list of PlayerDataObjects for all players that have data
     * If you want a list of ALL players, use {@link Bukkit#getOfflinePlayers()} or {@link Bukkit#getOnlinePlayers()}
     * @since 0.3.0.3
     */
    public List<PlayerDataObject> getSortedPlayers(Comparator<PlayerDataObject> comparator) {
        return getAllKnownPlayers().stream().sorted(comparator).toList();
    }

    /**
     * Checks if the player data for the given player exists
     * @param player The player to check for
     * @return true if the player data does exist, false otherwise
     * @since 0.2.0.0
     */
    public boolean hasPlayerData(Player player) {
        return hasPlayerData(player.getUniqueId());
    }

    /**
     * Checks if the player data for the given UUID exists
     * @param uuid The UUID of the player to check for
     * @return true if the player data does exist, false otherwise
     * @since 0.2.0.0
     */
    public boolean hasPlayerData(UUID uuid) {
        return playerStorage.hasPlayerData(uuid);
    }

    /**
     * Deletes player data from the file system
     * @param pdo The {@link PlayerDataObject} associated with the player data you want to delete
     * @return {@code true} if it succeeded, {@code false} if not
     * @since 0.2.1.0
     */
    public boolean deletePlayerData(PlayerDataObject pdo) {
        return deletePlayerData(pdo.getUniqueID());
    }

    /**
     * Deletes player data from the file system
     * @param player The {@link Player} associated with the player data you want to delete
     * @return {@code true} if it succeeded, {@code false} if not
     * @since 0.2.1.0
     */
    public boolean deletePlayerData(Player player) {
        return deletePlayerData(player.getUniqueId());
    }

    /**
     * Deletes player data from the file system
     * @param uuid The {@link UUID} associated with the player data you want to delete
     * @return {@code true} if it succeeded, {@code false} if not
     * @since 0.2.1.0
     */
    public boolean deletePlayerData(UUID uuid) {
        return playerStorage.deletePlayerData(uuid);
    }


    /**
     * Initializes CommonsAPI elements.
     * @since 1.0.0.0
     */
    @ApiStatus.Internal
    void init() {
        if (hasInitialized) return;

        config = Config.getInstance();
        localizationProvider = LocalizationProvider.getInstance();
        playerStorage = PlayerStorage.getInstance();
        apiLocalization = registerLocalization(RDSCommons.getInstance(), Locale.US, "§6Commons");
        logHelper = apiLocalization.getLogHelper();
        updater = new RDSCommonsUpdater();

        MessageKeys.init();

        hasInitialized = true;
    }

    /**
     * Checks if the CommonsAPI has been initialized
     * @return {@code true} if initialized, {@code false} otherwise
     * @since 1.0.0.0
     */
    @Override
    public boolean hasInitialized() {
        return hasInitialized;
    }

    /**
     * Gets the instance of CommonsAPI
     * @return The {@link CommonsAPI} instance
     * @since 1.0.0.0
     */
    public static CommonsAPI get() {
        return RDSCommons.getAPI();
    }
}
