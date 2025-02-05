package net.worldsgui.Configuration;

import lombok.Getter;
import net.worldsgui.Configuration.customconfigs.InventoryConfig;
import net.worldsgui.Configuration.customconfigs.MessagesConfig;
import net.worldsgui.Configuration.customconfigs.PluginConfig;

@Getter
public class ConfigManager {
    private InventoryConfig inventoryConfig;
    private MessagesConfig messagesConfig;
    private PluginConfig pluginConfig;

    public ConfigManager() {
        this.registerConfigs();
    }

    private void registerConfigs() {
        this.inventoryConfig = new InventoryConfig("inventory.yml");
        this.messagesConfig = new MessagesConfig("messages.yml");
        this.pluginConfig = new PluginConfig("config.yml");
    }

    public void reloadConfigs() {
        inventoryConfig.loadConfiguration();
        messagesConfig.loadConfiguration();
        pluginConfig.loadConfiguration();
    }
}
