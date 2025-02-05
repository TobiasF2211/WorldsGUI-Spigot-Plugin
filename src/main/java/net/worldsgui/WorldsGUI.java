package net.worldsgui;

import lombok.Getter;
import net.worldsgui.Commands.WorldGUICMD;
import net.worldsgui.Commands.WorldsGUIReloadCMD;
import net.worldsgui.Configuration.ConfigManager;
import net.worldsgui.Listener.InventoryListener;
import net.worldsgui.Utils.PlaceholderManager;
import net.worldsgui.Utils.WorldCache;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class WorldsGUI extends JavaPlugin {

    @Getter
    static WorldsGUI instance;
    @Getter
    static ConfigManager configManager;

    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getLogger().info("Loading...");

        configManager = new ConfigManager();

        Bukkit.getPluginManager().registerEvents(new InventoryListener(), this);

        Bukkit.getLogger().info("Worlds are loading...");

        WorldCache.updateCachedWorlds();

        Bukkit.getLogger().info("Hooking into PlaceholderAPI...");

        if (PlaceholderManager.isPlaceholderAPILoaded()) {
            Bukkit.getLogger().info("Successfully hooked in PlaceholderAPI!");
        } else Bukkit.getLogger().warning("Failed to hook in PlaceholderAPI! Load without Placeholder Support... If you don't wan't placeholders anyway you can ignore this message.");

        getCommand("worldsgui").setExecutor(new WorldGUICMD());
        getCommand("worldsguireload").setExecutor(new WorldsGUIReloadCMD());

        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage(configManager.getMessagesConfig().getPrefix() + this.getName() + " successfully loaded!");
        Bukkit.getConsoleSender().sendMessage("");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("Unloading...");

        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage(configManager.getMessagesConfig().getPrefix() + this.getName() + " successfully unloaded!");
        Bukkit.getConsoleSender().sendMessage("");
    }
}
