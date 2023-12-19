package online.flowerinsnow.customscoreboard;

import online.flowerinsnow.customscoreboard.command.CommandReloadCustomScoreboard;
import online.flowerinsnow.customscoreboard.expansion.CustomScoreboardExpansions;
import online.flowerinsnow.customscoreboard.listener.JoinQuitListener;
import online.flowerinsnow.customscoreboard.manager.ConfigManager;
import online.flowerinsnow.customscoreboard.manager.PlayerPlayTimeManager;
import online.flowerinsnow.customscoreboard.manager.PlayerScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Optional;

public class CustomScoreboard extends JavaPlugin {
    private PlayerPlayTimeManager playerPlayTimeManager;
    private ConfigManager configManager;
    private PlayerScoreboardManager playerScoreboardManager;
    private CustomScoreboardExpansions expansions;
    @Override
    public void onEnable() {
        configManager = new ConfigManager();

        saveDefaultConfig();
        reloadConfig();

        File playTimeFile = new File(getDataFolder(), "playtimedata.yml");
        playerPlayTimeManager = new PlayerPlayTimeManager(playTimeFile, YamlConfiguration.loadConfiguration(playTimeFile));

        long timestamp = System.currentTimeMillis();
        Bukkit.getOnlinePlayers().forEach(p -> playerPlayTimeManager.setJoinTimeThisTime(p.getUniqueId(), timestamp));

        playerScoreboardManager = new PlayerScoreboardManager(this);

        Bukkit.getOnlinePlayers().forEach(playerScoreboardManager::create);

        getServer().getPluginManager().registerEvents(new JoinQuitListener(this), this);
        getServer().getScheduler().runTaskTimer(this, playerScoreboardManager::updateAll, 0L, configManager.getUpdateTick());

        expansions = new CustomScoreboardExpansions(this);
        expansions.register();

        Optional.ofNullable(getCommand("reloadcustomscoreboard")).ifPresent(cmd -> {
            CommandReloadCustomScoreboard executor = new CommandReloadCustomScoreboard(CustomScoreboard.this);
            cmd.setExecutor(executor);
            cmd.setTabCompleter(executor);
        });
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
        expansions.unregister();
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        configManager.setConfig(getConfig());
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public PlayerPlayTimeManager getPlayerPlayTimeManager() {
        return playerPlayTimeManager;
    }

    public PlayerScoreboardManager getPlayerScoreboardManager() {
        return playerScoreboardManager;
    }
}
