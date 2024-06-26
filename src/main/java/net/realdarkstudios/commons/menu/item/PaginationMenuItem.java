package net.realdarkstudios.commons.menu.item;

import net.realdarkstudios.commons.event.MenuItemClickEvent;
import net.realdarkstudios.commons.menu.MCMenu;
import net.realdarkstudios.commons.menu.PaginationMenu;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PaginationMenuItem extends MenuItem {
    private final PaginationMenu menu;
    private final int index;

    public PaginationMenuItem(PaginationMenu menu, int index) {
        super(MCMenu.EMPTY_SLOT_ITEM.getName(), MCMenu.EMPTY_SLOT_ITEM.getItem(), MCMenu.EMPTY_SLOT_ITEM.getLore());
        this.menu = menu;
        this.index = index;
    }

    public MenuItem getCurrentItem(Player player) {
        List<MenuItem> items = menu.getItems(player);
        int target = (menu.getPage(player) * 45) + index;
        return items.size() - 1 >= target ? items.get(target) : MCMenu.EMPTY_SLOT_ITEM;
    }

    @Override
    public ItemStack getIcon(Player player) {
        return getCurrentItem(player).getIcon(player);
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        getCurrentItem(event.getPlayer()).onItemClick(event);
    }
}
