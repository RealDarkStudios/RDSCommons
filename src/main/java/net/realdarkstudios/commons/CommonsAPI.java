package net.realdarkstudios.commons;

import net.realdarkstudios.commons.misc.BaseAPI;
import net.realdarkstudios.commons.util.*;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;

import java.util.Locale;

public class CommonsAPI extends BaseAPI {
    private boolean hasInitialized = false;
    private static RDSLogHelper logHelper;
    private static LocalizationProvider localizationProvider;
    private static Localization apiLocalization;
    private static RDSCommonsUpdater updater;

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
     * Initializes CommonsAPI elements.
     * @since 1.0.0.0
     */
    @ApiStatus.Internal
    void init() {
        if (hasInitialized) return;

        localizationProvider = LocalizationProvider.getInstance();
        apiLocalization = registerLocalization(RDSCommons.getInstance(), Locale.US, "§6Commons");
        logHelper = apiLocalization.getLogHelper();
        updater = new RDSCommonsUpdater();

        MessageKeys.init();

        logHelper.tInfoPrefix(MessageKeys.Update.CHECKING, "RDSCommons", "TROLOLOLOLOLOLOLO");

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
