package online.flowerinsnow.customscoreboard.command;

import online.flowerinsnow.customscoreboard.CustomScoreboard;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CommandReloadCustomScoreboard implements TabExecutor {
    private final CustomScoreboard plugin;

    public CommandReloadCustomScoreboard(CustomScoreboard plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        plugin.reloadConfig();
        plugin.getPlayerScoreboardManager().updateAll();
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        return new ArrayList<>();
    }
}
