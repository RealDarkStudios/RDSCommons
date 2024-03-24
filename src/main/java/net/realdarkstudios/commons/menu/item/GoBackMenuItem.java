package net.realdarkstudios.commons.menu.item;

import net.realdarkstudios.commons.event.MenuItemClickEvent;
import net.realdarkstudios.commons.util.MessageKeys;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GoBackMenuItem extends MenuItem {
    public GoBackMenuItem(ItemStack item, List<String> lore) {
        super(MessageKeys.Menu.GO_BACK.translate(), item, lore);
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        event.setGoBack(true);

        super.onItemClick(event);
    }
}
