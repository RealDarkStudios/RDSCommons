package net.realdarkstudios.commons.menu.item;

import net.realdarkstudios.commons.event.MenuItemClickEvent;
import net.realdarkstudios.commons.util.MessageKeys;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CloseMenuItem extends MenuItem {
    public CloseMenuItem() {
        super(MessageKeys.Menu.CLOSE.translate(), new ItemStack(Material.RED_CONCRETE), List.of());
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        event.setClose(true);
    }
}
