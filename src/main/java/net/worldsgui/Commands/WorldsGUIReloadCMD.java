package net.worldsgui.Commands;

import net.worldsgui.Utils.WorldCache;
import net.worldsgui.WorldsGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class WorldsGUIReloadCMD implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (!commandSender.hasPermission("worldsgui.worldsguireloadcommand.use")) {
            commandSender.sendMessage(WorldsGUI.getConfigManager().getMessagesConfig().getTranslatedLanguage("nopermission"));
            return true;
        }

        WorldsGUI.getConfigManager().reloadConfigs();
        WorldCache.updateCachedWorlds();
        commandSender.sendMessage(WorldsGUI.getConfigManager().getMessagesConfig().getTranslatedLanguage("reloadedsuccessfully"));

        return true;
    }
}
