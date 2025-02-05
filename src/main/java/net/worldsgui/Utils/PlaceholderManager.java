package net.worldsgui.Utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlaceholderManager {

    public static boolean isPlaceholderAPILoaded() {
        return Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
    }

    public static String parse(Player player, String text) {
        if (text == null) {
            return "";
        }

        if (!isPlaceholderAPILoaded() || player == null) {
            return text;
        }

        return PlaceholderAPI.setPlaceholders(player, text);
    }
}
