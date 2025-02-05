package net.worldsgui.Configuration.customconfigs;

import java.util.List;

public class PluginConfig extends CustomConfig {
    public PluginConfig(String name) {
        super(name);
    }

    public String getType() {
        String type = fileConfiguration.getString("type");

        if (type == null) {
            return null;
        }

        if (!type.equalsIgnoreCase("whitelist") && !type.equalsIgnoreCase("blacklist")) {
            return null;
        }

        return type.toLowerCase();
    }

    public List<String> getListItems() {
        return fileConfiguration.getStringList("worlds");
    }
}
