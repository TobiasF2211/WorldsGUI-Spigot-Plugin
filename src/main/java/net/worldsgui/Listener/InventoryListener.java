package net.worldsgui.Listener;

import net.worldsgui.Inventory.WorldGUIInventory;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        Player player = (Player) event.getWhoClicked();

        if (inventory == null) return;

        if (player.getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);
        }

        if (inventory.getHolder() instanceof WorldGUIInventory) {
            WorldGUIInventory worldGUIInventory = (WorldGUIInventory) inventory.getHolder();
            event.setCancelled(true);
            worldGUIInventory.onClick(event, event.getCurrentItem(), event.getSlot());
        }
    }
}
