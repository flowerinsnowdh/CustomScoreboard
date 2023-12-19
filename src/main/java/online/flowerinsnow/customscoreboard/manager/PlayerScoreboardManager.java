package online.flowerinsnow.customscoreboard.manager;

import me.clip.placeholderapi.PlaceholderAPI;
import online.flowerinsnow.customscoreboard.CustomScoreboard;
import online.flowerinsnow.customscoreboard.util.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.*;

public class PlayerScoreboardManager {
    private final HashMap<UUID, Scoreboard> playerScoreboard = new HashMap<>();
    private final CustomScoreboard plugin;

    private final HashMap<UUID, Set<Integer>> playerLinesSet = new HashMap<>();

    public PlayerScoreboardManager(CustomScoreboard plugin) {
        this.plugin = plugin;
    }

    public void create(Player player) {
        @SuppressWarnings("DataFlowIssue")
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        playerScoreboard.put(player.getUniqueId(), scoreboard);
        player.setScoreboard(scoreboard);

        init(scoreboard);
    }

    private void init(Scoreboard scoreboard) {
        Objective objective = scoreboard.registerNewObjective("sidebar", Criteria.DUMMY, "", RenderType.INTEGER);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        for (int i = 1; i < 15; i++) {
            Team team = scoreboard.registerNewTeam("sb_" + i);
            team.addEntry("§" + (char) i);
        }
    }

    private void setScoreboardLine(Player player, int lineNumber, String content) {
        Set<Integer> linesSet = playerLinesSet.computeIfAbsent(player.getUniqueId(), k -> new HashSet<>());
        Scoreboard scoreboard = playerScoreboard.get(player.getUniqueId());
        Objective objective = scoreboard.getObjective("sidebar");
        // 如果没有分数，就代表它没有显示在记分项上，就需要给它设置一个分数来让它显示在应有的位置
        @SuppressWarnings("DataFlowIssue")
        Score score = objective.getScore("§" + (char) lineNumber);
        if (linesSet.add(lineNumber)) {
            score.setScore(lineNumber);
        }
        Team team = scoreboard.getTeam("sb_" + lineNumber);
        // 当内容不同时，才有必要更新内容
        //noinspection DataFlowIssue
        if (!team.getPrefix().equals(content)) {
            team.setPrefix(content);
        }
    }

    private void hideScoreboardLine(Player player, int lineNumber) {
        Set<Integer> linesSet = playerLinesSet.computeIfAbsent(player.getUniqueId(), k -> new HashSet<>());
        Scoreboard scoreboard = playerScoreboard.get(player.getUniqueId());
        // 如果有分数，就代表它显示在记分项上了，就需要给它取消设置分数来让它在记分项上消失
        if (linesSet.remove(lineNumber)) {
            scoreboard.resetScores("§" + (char) lineNumber);
        }
    }

    public void update(Player player) {
        String title = plugin.getConfigManager().getScoreboardTitle();
        Scoreboard scoreboard = playerScoreboard.get(player.getUniqueId());
        Objective objective = scoreboard.getObjective("sidebar");
        //noinspection DataFlowIssue
        if (!objective.getDisplayName().equals(title)) {
            objective.setDisplayName(TextUtils.parseColour(PlaceholderAPI.setPlaceholders(player, title)));
        }

        List<String> lines = plugin.getConfigManager().getScoreboardLines();
        for (int i = 0; i < lines.size(); i++) {
            int lineNumber = lines.size() - i;
            setScoreboardLine(player, lineNumber, TextUtils.parseColour(PlaceholderAPI.setPlaceholders(player, lines.get(i))));
        }

        for (int i = lines.size() + 1; i < 15; i++) {
            hideScoreboardLine(player, i);
        }
    }

    public void updateAll() {
        Bukkit.getOnlinePlayers().forEach(this::update);
    }

    public void clearPlayer(UUID uuid) {
        playerScoreboard.remove(uuid);
        playerLinesSet.remove(uuid);
    }
}
