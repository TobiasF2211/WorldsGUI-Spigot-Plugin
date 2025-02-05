package net.worldsgui.Configuration.customconfigs;

import lombok.Getter;
import net.worldsgui.WorldsGUI;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public abstract class CustomConfig {
    private final File file;
    @Getter
    protected FileConfiguration fileConfiguration;

    public CustomConfig(String name) {
        this.file = new File(WorldsGUI.getInstance().getDataFolder(), name);
        if (!(file.exists())) {
            WorldsGUI.getInstance().saveResource(name, true);
        }
        loadConfiguration();
    }

    public void loadConfiguration() {
        this.fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    protected void save() {
        try {
            fileConfiguration.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
