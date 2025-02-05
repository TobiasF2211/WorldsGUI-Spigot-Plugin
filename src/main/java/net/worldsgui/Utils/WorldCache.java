package net.worldsgui.Utils;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class WorldCache {
    @Getter
    static List<World> cachedWorlds = new ArrayList<>();

    public static void updateCachedWorlds() {
        cachedWorlds = new ArrayList<>(Bukkit.getWorlds());
    }

}
