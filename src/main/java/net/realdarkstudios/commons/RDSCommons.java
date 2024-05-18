package net.realdarkstudios.commons;

import net.realdarkstudios.commons.menu.MCMenuHolder;
import net.realdarkstudios.commons.misc.IRDSPlugin;
import net.realdarkstudios.commons.util.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class RDSCommons extends JavaPlugin implements IRDSPlugin {
    private static RDSCommons plugin;
    private static Version version;
    private static CommonsAPI api;

    // For the deop check
    private static List<Player> onlineOPPlayerList = List.of();
    private static int deopCheckTaskId;

    @Override
    public void onEnable() {
        plugin = this;
        api = new CommonsAPI();
        version = Version.fromString(getDescription().getVersion());

        api.init();

        menuDeopCheck();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        Bukkit.getScheduler().cancelTask(deopCheckTaskId);
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
        }, 1L, 2L);
    }

    public static RDSCommons getInstance() {
        return plugin;
    }

    @Override
    @NotNull
    public Version getVersion() {
        return version;
    }

    @Override
    @NotNull
    public String getVersionString() {
        return version.toString();
    }

    public static CommonsAPI getAPI() {
        return api;
    }
}
