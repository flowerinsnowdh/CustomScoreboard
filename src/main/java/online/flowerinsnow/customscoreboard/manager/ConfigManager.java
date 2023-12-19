package online.flowerinsnow.customscoreboard.manager;

import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class ConfigManager {
    private ConfigurationSection config;

    public void setConfig(ConfigurationSection config) {
        this.config = config;
    }

    public String getScoreboardTitle() {
        return config.getString("scoreboard.title");
    }

    public List<String> getScoreboardLines() {
        return config.getStringList("scoreboard.lines");
    }

    public long getUpdateTick() {
        return config.getLong("scoreboard.update-tick");
    }
}
