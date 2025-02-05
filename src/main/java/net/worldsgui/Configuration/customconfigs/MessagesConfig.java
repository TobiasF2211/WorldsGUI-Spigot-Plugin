package net.worldsgui.Configuration.customconfigs;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MessagesConfig extends CustomConfig {

    public MessagesConfig(String name) {
        super(name);
    }

    public String getTranslatedLanguage(String key) {
        if (!(fileConfiguration.contains(key))) {
            return key;
        }
        String language = ChatColor.translateAlternateColorCodes('&', fileConfiguration.getString(key));

        language = language.replace("%prefix%", getPrefix());

        language = translate(language);
        return language;
    }

    public String[] getTranslatedLanguages(String section) {
        ArrayList<String> languages = new ArrayList<>();
        ConfigurationSection configurationSection = fileConfiguration.getConfigurationSection(section);
        if (configurationSection == null) {
            return languages.toArray(new String[0]);
        }
        for (String language : configurationSection.getKeys(false)) {
            languages.add(getTranslatedLanguage(section + "." + language));
        }
        return languages.toArray(new String[0]);
    }

    public String getLanguageWithPrefix(String key) {
        return getTranslatedLanguage("prefix") + getTranslatedLanguage(key);
    }

    private String translate(String s) {
        s = s.replace("%Ae%", "Ä");
        s = s.replace("%ae%", "ä");
        s = s.replace("%Oe%", "Ö");
        s = s.replace("%oe%", "ö");
        s = s.replace("%Ue%", "Ü");
        s = s.replace("%ue%", "ü");
        s = s.replace("%sz%", "ß");
        s = s.replace("%>%", "»");
        s = s.replace("%<%", "«");
        s = s.replace("%*%", "×");
        s = s.replace("%|%", "┃");
        s = s.replace("%->%", "→");
        s = s.replace("%_>%", "➥");

        return s;
    }

    public String getPrefix() {
        return translate(ChatColor.translateAlternateColorCodes('&', fileConfiguration.getString("prefix")));
    }

    public String formatTime(String language, int time) {
        int hours = (time % 86400) / 3600;
        int minutes = ((time % 86400) % 3600) / 60;
        int seconds = ((time % 86400) % 3600) % 60;
        return language.replace("%time%", String.format("%02d:%02d:%02d", hours, minutes, seconds));
    }

    public String formatNumber(Long number) {
        if (number < 1000) {
            return String.valueOf(number);
        } else {
            return new DecimalFormat("0,000").format(number);
        }
    }
}
