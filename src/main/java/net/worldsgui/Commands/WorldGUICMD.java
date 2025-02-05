package net.worldsgui.Commands;

import net.worldsgui.Inventory.WorldGUIInventory;
import net.worldsgui.Utils.PlaceholderManager;
import net.worldsgui.WorldsGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class WorldGUICMD implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            if (!player.hasPermission("worldsgui.worldguicommand.use")) {
                player.sendMessage(WorldsGUI.getConfigManager().getMessagesConfig().getTranslatedLanguage("nopermission"));
                return true;
            }

            new WorldGUIInventory().openWorldInventory(player, 1);

        } else commandSender.sendMessage(WorldsGUI.getConfigManager().getMessagesConfig().getTranslatedLanguage("onlyplayer"));

        return true;
    }
}
