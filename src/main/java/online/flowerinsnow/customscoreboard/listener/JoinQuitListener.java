package online.flowerinsnow.customscoreboard.listener;

import online.flowerinsnow.customscoreboard.CustomScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitListener implements Listener {
    private final CustomScoreboard plugin;

    public JoinQuitListener(CustomScoreboard plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.getPlayerPlayTimeManager().setJoinTimeThisTime(player.getUniqueId(), System.currentTimeMillis());

        plugin.getPlayerScoreboardManager().create(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        plugin.getPlayerPlayTimeManager().setConfigPlayerPlayTimeAndSave(player.getUniqueId(), plugin.getPlayerPlayTimeManager().getTotalPlayTime(player.getUniqueId()));
        plugin.getPlayerPlayTimeManager().clearJoinTimeThisTime(player.getUniqueId());

        plugin.getPlayerScoreboardManager().clearPlayer(player.getUniqueId());
    }
}
