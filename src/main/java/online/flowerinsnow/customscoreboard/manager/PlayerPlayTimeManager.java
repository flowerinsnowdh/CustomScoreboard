package online.flowerinsnow.customscoreboard.manager;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class PlayerPlayTimeManager {
    private final File file;
    private final YamlConfiguration config;
    private final HashMap<UUID, Long> joinTimeThisTime = new HashMap<>();

    public PlayerPlayTimeManager(File file, YamlConfiguration config) {
        this.file = file;
        this.config = config;
    }

    public long getJoinTimeThisTime(UUID uuid) {
        return joinTimeThisTime.getOrDefault(uuid, -1L);
    }

    public void setJoinTimeThisTime(UUID uuid, long timestamp) {
        joinTimeThisTime.put(uuid, timestamp);
    }

    public void clearJoinTimeThisTime(UUID uuid) {
        joinTimeThisTime.remove(uuid);
    }

    public long getConfigPlayerPlayTime(UUID playerUUID) {
        return config.getLong(playerUUID.toString());
    }

    public void setConfigPlayerPlayTime(UUID playerUUID, long playTime) {
        config.set(playerUUID.toString(), playTime);
    }

    public void setConfigPlayerPlayTimeAndSave(UUID playerUUID, long playTime) {
        this.setConfigPlayerPlayTime(playerUUID, playTime);
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取玩家总游玩时间，包括以往和本次
     *
     * @param uuid 玩家UUID
     * @return 玩家总游玩时间，包括以往和本次
     */
    public long getTotalPlayTime(UUID uuid) {
        long totalPlayTime = getConfigPlayerPlayTime(uuid);
        long joinTimeThisTime = getJoinTimeThisTime(uuid);
        if (joinTimeThisTime != -1L) {
            totalPlayTime += (System.currentTimeMillis() - joinTimeThisTime);
        }
        return totalPlayTime;
    }
}
