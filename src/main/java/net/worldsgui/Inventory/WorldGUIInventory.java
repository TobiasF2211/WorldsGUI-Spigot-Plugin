package net.worldsgui.Inventory;

import net.worldsgui.Utils.InventoryClick;
import net.worldsgui.Utils.PlaceholderManager;
import net.worldsgui.Utils.WorldCache;
import net.worldsgui.WorldsGUI;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorldGUIInventory implements InventoryHolder, InventoryClick {

    private Inventory gui;
    private final Map<Integer, World> slotToWorldMap = new HashMap<>();
    private static final Map<Player, Integer> playerPages = new HashMap<>();

    public void openWorldInventory(Player player, int page) {
        if (WorldCache.getCachedWorlds().isEmpty()) {
            WorldCache.updateCachedWorlds();
        }
        List<World> worlds = WorldCache.getCachedWorlds();
        slotToWorldMap.clear();

        int itemsPerPage = WorldsGUI.getConfigManager().getInventoryConfig().getWorldSlots().size();
        int totalPages = (int) Math.ceil((double) worlds.size() / itemsPerPage);

        page = Math.max(1, Math.min(page, totalPages));
        playerPages.put(player, page);

        String title = WorldsGUI.getConfigManager().getInventoryConfig().isCustomInventory()
                ? WorldsGUI.getConfigManager().getInventoryConfig().getCustomInventory(page, totalPages)
                : WorldsGUI.getConfigManager().getInventoryConfig().getInventoryName(page);

        title = PlaceholderManager.parse(player, title);

        this.gui = Bukkit.createInventory(this, WorldsGUI.getConfigManager().getInventoryConfig().getSlots(), title);

        List<Integer> slots = WorldsGUI.getConfigManager().getInventoryConfig().getWorldSlots();
        int worldIndex = (page - 1) * itemsPerPage;

        for (int slot : slots) {
            if (worldIndex >= worlds.size()) break;

            World world = worlds.get(worldIndex);
            if (isWorldBlocked(world)) {
                worldIndex++;
                if (worldIndex >= worlds.size()) break;
                world = worlds.get(worldIndex);
            }

            ItemStack worldItem = WorldsGUI.getConfigManager().getInventoryConfig().getWorldItem(player, world);
            if (worldItem != null) {
                gui.setItem(slot, worldItem);
                slotToWorldMap.put(slot, world);
            }

            worldIndex++;
        }

        addStaticItems(player, page);
        setNavigationButtons(player, page, totalPages);
        Bukkit.getScheduler().runTaskLater(WorldsGUI.getInstance(), () -> {
            player.openInventory(gui);
        }, 1L);
    }

    private boolean isWorldBlocked(World world) {
        String type = WorldsGUI.getConfigManager().getPluginConfig().getType();
        if (type != null) {
            switch (type) {
                case "blacklist":
                    return WorldsGUI.getConfigManager().getPluginConfig().getListItems().contains(world.getName());
                case "whitelist":
                    return !WorldsGUI.getConfigManager().getPluginConfig().getListItems().contains(world.getName());
            }
        }
        return false;
    }

    private void addStaticItems(Player player, int page) {
        Map<Integer, ItemStack> map = WorldsGUI.getConfigManager().getInventoryConfig().getItems(player, String.valueOf(page));
        for (Map.Entry<Integer, ItemStack> entry : map.entrySet()) {
            gui.setItem(entry.getKey(), entry.getValue());
        }
    }

    private void setNavigationButtons(Player player, int page, int totalPages) {
        if (page > 1) {
            Integer prevSlot = WorldsGUI.getConfigManager().getInventoryConfig().getButtonSlot("back_button");
            if (prevSlot != null) {
                List<ItemStack> prevButtons = WorldsGUI.getConfigManager().getInventoryConfig().getButtonStacks(player, "back_button", String.valueOf(page));
                if (!prevButtons.isEmpty()) {
                    gui.setItem(prevSlot, prevButtons.get(0));
                }
            }
        }

        if (page < totalPages) {
            Integer nextSlot = WorldsGUI.getConfigManager().getInventoryConfig().getButtonSlot("next_button");
            if (nextSlot != null) {
                List<ItemStack> nextButtons = WorldsGUI.getConfigManager().getInventoryConfig().getButtonStacks(player, "next_button", String.valueOf(page));
                if (!nextButtons.isEmpty()) {
                    gui.setItem(nextSlot, nextButtons.get(0));
                }
            }
        }
    }

    @Override
    public void onClick(InventoryClickEvent event, ItemStack itemStack, int slot) {
        Player player = (Player) event.getWhoClicked();
        int currentPage = playerPages.getOrDefault(player, 1);

        if (slotToWorldMap.containsKey(slot)) {
            World world = slotToWorldMap.get(slot);
            if (world != null) {
                player.teleport(world.getSpawnLocation());
                player.sendMessage(WorldsGUI.getConfigManager().getMessagesConfig().getTranslatedLanguage("teleportedSuccessfully"));
            } else {
                player.sendMessage(WorldsGUI.getConfigManager().getMessagesConfig().getTranslatedLanguage("worldNotFound"));
            }
            return;
        }

        Integer backSlot = WorldsGUI.getConfigManager().getInventoryConfig().getButtonSlot("back_button");
        Integer nextSlot = WorldsGUI.getConfigManager().getInventoryConfig().getButtonSlot("next_button");

        if (backSlot != null && slot == backSlot) {
            if (currentPage > 1) {
                openWorldInventory(player, currentPage - 1);
                playerPages.put(player, currentPage - 1);
            }
        } else if (nextSlot != null && slot == nextSlot) {
            int totalPages = (int) Math.ceil((double) Bukkit.getWorlds().size() / WorldsGUI.getConfigManager().getInventoryConfig().getWorldSlots().size());
            if (currentPage < totalPages) {
                openWorldInventory(player, currentPage + 1);
                playerPages.put(player, currentPage + 1);
            }
        }
    }

    @Override
    public @NotNull Inventory getInventory() {
        return gui;
    }
}
