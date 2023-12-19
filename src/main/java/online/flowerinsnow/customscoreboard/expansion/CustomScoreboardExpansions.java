package online.flowerinsnow.customscoreboard.expansion;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import online.flowerinsnow.customscoreboard.CustomScoreboard;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class CustomScoreboardExpansions extends PlaceholderExpansion {
    private final CustomScoreboard plugin;

    public CustomScoreboardExpansions(CustomScoreboard plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "customscoreboard";
    }

    @Override
    public @NotNull String getAuthor() {
        return "flowerinsnow";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        return switch (params.toLowerCase(Locale.ROOT)) {
            case "locx" -> Integer.toString(player.getLocation().getBlockX());
            case "locy" -> Integer.toString(player.getLocation().getBlockY());
            case "locz" -> Integer.toString(player.getLocation().getBlockZ());
            case "playtime" -> Long.toString(TimeUnit.MILLISECONDS.toSeconds(plugin.getPlayerPlayTimeManager().getTotalPlayTime(player.getUniqueId())));
            default -> null;
        };
    }
}
