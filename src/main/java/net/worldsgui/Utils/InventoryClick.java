package net.worldsgui.Utils;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public interface InventoryClick {

    void onClick(InventoryClickEvent inventoryClickEvent, ItemStack itemStack, int slot);
}
