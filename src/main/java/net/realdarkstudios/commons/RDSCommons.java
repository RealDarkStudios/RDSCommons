package net.realdarkstudios.commons;

import net.realdarkstudios.commons.menu.MCMenuHolder;
import net.realdarkstudios.commons.misc.IRDSPlugin;
import net.realdarkstudios.commons.util.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public final class RDSCommons extends JavaPlugin implements IRDSPlugin {
    private static Logger logger;
    private static LocalizationProvider localizationProvider;
    private static Localization apiLocalization;
    private static RDSCommonsUpdater updater;
    private static Version version;

    // For the deop checkc
    private static List<Player> onlineOPPlayerList = List.of();
    private static int deopCheckTaskId;

    public static LocalizationProvider getAPILocalizationProvider() {
        return localizationProvider;
    }

    public static Localization getAPILocalization() {
        return apiLocalization;
    }

    public static Logger getAPILogger() {
        return logger;
    }

    @Override
    public Version getVersion() {
        return version;
    }

    @Override
    public String getVersionString() {
        return version.toString();
    }

    public static RDSCommonsUpdater getUpdater() {
        return updater;
    }

    public static RDSCommons getInstance() {
        return getPlugin(RDSCommons.class);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

        logger = this.getLogger();
        localizationProvider = LocalizationProvider.getInstance();
        apiLocalization = localizationProvider.get(this);
        updater = new RDSCommonsUpdater();
        version = Version.fromString(getDescription().getVersion());

        menuDeopCheck();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        Bukkit.getScheduler().cancelTask(deopCheckTaskId);
    }

    /**
     * Logs all the messages at the INFO level
     * @param messages The messages to log
     * @since 0.2.1.0
     */
    public static void info(String... messages) {
        for (String msg : messages) {
            logger.info(msg);
        }
    }

    /**
     * Logs the message using the {@link LocalizedMessages} system at the INFO level
     * @param key The {@link LocalizedMessages.Key} to log
     * @param formatArgs The format arguments
     * @see MessageKeys
     * @since 0.3.1.0
     */
    public static void tInfo(LocalizedMessages.Key key, Object... formatArgs) {
        info(key.console(formatArgs));
    }

    /**
     * Logs all the messages at the WARN level
     * @param messages The messages to log
     * @since 0.2.1.0
     */
    public static void warning(String... messages) {
        for (String msg : messages) {
            logger.warning(msg);
        }
    }

    /**
     * Logs the message using the {@link LocalizedMessages} system at the WARNING level
     * @param key The {@link LocalizedMessages.Key} to log
     * @param formatArgs The format arguments
     * @see MessageKeys
     * @since 0.3.1.0
     */
    public static void tWarning(LocalizedMessages.Key key, Object... formatArgs) {
        warning(key.console(formatArgs));
    }

    // Handle reloading MCMenus when a player is deopped (to avoid possibly executing something only ops should be able to do)

    void menuDeopCheck() {
        deopCheckTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(RDSCommons.getInstance(), () -> {
            List<Player> opList = new ArrayList<>();

            for (Player p: Bukkit.getOnlinePlayers()) {
                if (p.isOp()) opList.add(p);

                if (!p.isOp() && p.isOnline() && onlineOPPlayerList.contains(p)) {
                    p.getOpenInventory();
                    Inventory inv = p.getOpenInventory().getTopInventory();
                    if (inv.getHolder() instanceof MCMenuHolder) p.closeInventory();
                    LocalizedMessages.send(p, MessageKeys.Menu.PERM_CHANGED);
                }
            }

            onlineOPPlayerList = opList;
        }, 1L, 1L);
    }
}
