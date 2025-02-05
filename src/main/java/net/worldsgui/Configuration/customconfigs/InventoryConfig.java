package net.worldsgui.Configuration.customconfigs;

import net.worldsgui.Utils.PlaceholderManager;
import net.worldsgui.WorldsGUI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryConfig extends CustomConfig {
    public InventoryConfig(String name) {
        super(name);
    }

    public boolean isCustomInventory() {
        return fileConfiguration.getBoolean("inventorytexture");
    }

    public String getInventoryName(int page) {
        String language = ChatColor.translateAlternateColorCodes('&', fileConfiguration.getString("inventoryname"));

        language = language.replace("%Ae%", "Ä");
        language = language.replace("%ae%", "ä");
        language = language.replace("%Oe%", "Ö");
        language = language.replace("%oe%", "ö");
        language = language.replace("%Ue%", "Ü");
        language = language.replace("%ue%", "ü");
        language = language.replace("%sz%", "ß");
        language = language.replace("%>%", "»");
        language = language.replace("%<%", "«");
        language = language.replace("%*%", "×");
        language = language.replace("%|%", "┃");
        language = language.replace("%->%", "➜");
        language = language.replace("%_>%", "➥");

        return language.replace("{page}", "" + page);
    }

    public String getCustomInventory(int page, int maxpages) {
        String pagepath;

        if (page == 1) {
            if (maxpages == 1) {
                pagepath = "singlePage";
            } else pagepath = "firstPage";
        } else if (page == maxpages) {
            pagepath = "lastPage";
        } else pagepath = "generellPage";

        return fileConfiguration.getString("custominventory_page_" + pagepath);
    }

    public int getSlots() {
        return fileConfiguration.getInt("slots");
    }

    public @NotNull List<Integer> getWorldSlots() {
        return fileConfiguration.getIntegerList("worldslots");
    }

    public ItemStack getWorldItem(Player player, World world) {
        ConfigurationSection itemsSection = fileConfiguration.getConfigurationSection("items");

        if (itemsSection != null) {
            for (String key : itemsSection.getKeys(false)) {
                if (key.equalsIgnoreCase("world_item")) {
                    ConfigurationSection itemSection = itemsSection.getConfigurationSection(key);
                    if (itemSection != null) {
                        return createWorldFromConfig(player, itemSection, world);
                    }
                }
            }
        }
        return null;
    }

    public Integer getButtonSlot(String button) {
        ConfigurationSection itemsSection = fileConfiguration.getConfigurationSection("items");

        if (itemsSection != null) {
            ConfigurationSection itemSection = itemsSection.getConfigurationSection(button);
            if (itemSection != null) {
                return itemSection.getInt("slot", -1);
            }
        }
        return null;
    }

    public List<ItemStack> getButtonStacks(Player player, String button, String page) {
        ConfigurationSection itemsSection = fileConfiguration.getConfigurationSection("items");
        List<ItemStack> buttons = new ArrayList<>();

        if (itemsSection != null) {
            ConfigurationSection itemSection = itemsSection.getConfigurationSection(button);
            if (itemSection != null) {
                buttons.add(createItemFromConfig(player, itemSection, page));
            }
        }

        return buttons;
    }

    public Map<Integer, ItemStack> getItems(Player player, String page) {
        ConfigurationSection itemsSection = fileConfiguration.getConfigurationSection("items");
        Map<Integer, ItemStack> items = new HashMap<>();

        if (itemsSection != null) {
            for (String key : itemsSection.getKeys(false)) {
                int slot;
                try {
                    slot = Integer.parseInt(key);
                } catch (NumberFormatException e) {
                    continue;
                }

                ConfigurationSection itemSection = itemsSection.getConfigurationSection(key);
                if (itemSection != null) {
                    ItemStack item = createItemFromConfig(player, itemSection, page);
                    items.put(slot, item);
                }
            }
        }

        return items;
    }

    private ItemStack createItemFromConfig(Player player, ConfigurationSection section, String page) {
        String materialName = section.getString("material", "BARRIER");
        Material material = Material.matchMaterial(materialName);
        if (material == null) {
            material = Material.BARRIER;
        }

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            if (section.contains("display-name")) {
                String displayName = PlaceholderManager.parse(player, section.getString("display-name"));
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName).replace("{page}", page));
            }

            if (section.contains("lore")) {
                List<String> lore = section.getStringList("lore");
                if (!lore.isEmpty()) {
                    List<String> coloredLore = new ArrayList<>();
                    for (String line : lore) {
                        coloredLore.add(ChatColor.translateAlternateColorCodes('&', PlaceholderManager.parse(player, line.replace("{page}", page))));
                    }
                    meta.setLore(coloredLore);
                }
            }

            if (section.contains("customModelData")) {
                meta.setCustomModelData(section.getInt("customModelData"));
            }

            item.setItemMeta(meta);
        }

        return item;
    }

    private ItemStack createWorldFromConfig(Player player, ConfigurationSection section, World world) {
        MessagesConfig messagesConfig = WorldsGUI.getConfigManager().getMessagesConfig();

        String overworldMaterialName = section.getString("overworld_material");
        Material overworldMaterial = Material.matchMaterial(overworldMaterialName);
        if (overworldMaterial == null) {
            overworldMaterial = Material.BARRIER;
        }

        String netherMaterialName = section.getString("nether_material");
        Material netherMaterial = Material.matchMaterial(netherMaterialName);
        if (netherMaterial == null) {
            netherMaterial = Material.BARRIER;
        }

        String endMaterialName = section.getString("end_material");
        Material endMaterial = Material.matchMaterial(endMaterialName);
        if (endMaterial == null) {
            endMaterial = Material.BARRIER;
        }

        String customMaterialName = section.getString("custom_material");
        Material customMaterial = Material.matchMaterial(customMaterialName);
        if (customMaterial == null) {
            customMaterial = Material.BARRIER;
        }

        String dimension;
        Material finalMaterial;

        if (world.getEnvironment() == World.Environment.NORMAL) {
            finalMaterial = overworldMaterial;
            dimension = messagesConfig.getTranslatedLanguage("dimension_overworld");
        } else if (world.getEnvironment() == World.Environment.NETHER) {
            finalMaterial = netherMaterial;
            dimension = messagesConfig.getTranslatedLanguage("dimension_nether");
        } else if (world.getEnvironment() == World.Environment.THE_END) {
            finalMaterial = endMaterial;
            dimension = messagesConfig.getTranslatedLanguage("dimension_end");
        } else if (world.getEnvironment() == World.Environment.CUSTOM) {
            finalMaterial = customMaterial;
            dimension = messagesConfig.getTranslatedLanguage("dimension_custom");
        } else {
            finalMaterial = Material.BARRIER;
            dimension = messagesConfig.getTranslatedLanguage("dimension_unknown");
        }

        ItemStack item = new ItemStack(finalMaterial);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            String displayName = PlaceholderManager.parse(player, section.getString("display-name"));
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', translateWorldInformation(player, PlaceholderManager.parse(player, displayName), dimension, world)));

            List<String> lore = section.getStringList("lore");
            if (!lore.isEmpty()) {
                List<String> coloredLore = new ArrayList<>();
                for (String line : lore) {
                    coloredLore.add(ChatColor.translateAlternateColorCodes('&', translateWorldInformation(player, PlaceholderManager.parse(player, line), dimension, world)));
                }
                meta.setLore(coloredLore);
            }

            int customModelData;
            try {
                customModelData = section.getInt("customModelData");
            } catch (Exception e) {
                customModelData = -1;
            }
            if (customModelData != -1) {
                meta.setCustomModelData(customModelData);
            }
        }

        item.setItemMeta(meta);

        return item;
    }

    private String translateWorldInformation(Player player, String s, String dimension, World world) {
        String generator;

        if (world.getGenerator() == null) {
            generator = WorldsGUI.getConfigManager().getMessagesConfig().getTranslatedLanguage("generatorifnull");
        } else {
            generator = world.getGenerator().toString();
        }

        String worldType = world.getWorldType() != null ? world.getWorldType().toString() : "Unknown";
        String seed = String.valueOf(world.getSeed());
        String hardcore = world.isHardcore() ? "true" : "false";
        String worldTime = String.valueOf(world.getTime());
        String spawnLocation = "X: " + world.getSpawnLocation().getBlockX() +
                ", Y: " + world.getSpawnLocation().getBlockY() +
                ", Z: " + world.getSpawnLocation().getBlockZ();

        return PlaceholderManager.parse(player, s).replace("{dimension}", dimension)
                .replace("{world_name}", world.getName())
                .replace("{generator}", generator)
                .replace("{difficulty}", world.getDifficulty().toString())
                .replace("{player_count}", "" + world.getPlayers().size())
                .replace("{world_type}", worldType)
                .replace("{seed}", seed)
                .replace("{hardcore}", hardcore)
                .replace("{world_time}", worldTime)
                .replace("{spawn_location}", spawnLocation);
    }

    public void saveItem(int slot, ItemStack item) {
        ConfigurationSection itemsSection = fileConfiguration.getConfigurationSection("items");
        if (itemsSection == null) {
            itemsSection = fileConfiguration.createSection("items");
        }

        ConfigurationSection itemSection = itemsSection.createSection(String.valueOf(slot));
        itemSection.set("material", item.getType().name());

        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            itemSection.set("display-name", meta.getDisplayName());
            itemSection.set("lore", meta.getLore());
        }

        save();
    }
}
