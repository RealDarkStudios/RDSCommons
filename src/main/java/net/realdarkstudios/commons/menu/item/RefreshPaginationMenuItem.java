package net.realdarkstudios.commons.menu.item;

import net.realdarkstudios.commons.event.MenuItemClickEvent;
import net.realdarkstudios.commons.menu.PaginationMenu;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class RefreshPaginationMenuItem extends MenuItem {
    private final PaginationMenu menu;

    public RefreshPaginationMenuItem(PaginationMenu menu, String name, ItemStack item, List<String> lore) {
        super(name, item, lore);
        this.menu = menu;
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        menu.update(event.getPlayer());
        event.setUpdate(true);
    }
}
