package net.realdarkstudios.commons.menu.item;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ErrorMenuItem extends MenuItem {
    public ErrorMenuItem(String name, ItemStack item, List<String> lore) {
        super(name, item, lore);
        setClickSound(Sound.ENTITY_VILLAGER_TRADE);
    }

    @Override
    public String getName() {
        return ChatColor.RED + super.getName();
    }
}
