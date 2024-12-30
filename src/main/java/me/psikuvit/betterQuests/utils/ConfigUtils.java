package me.psikuvit.betterQuests.utils;

import me.psikuvit.betterQuests.BQPlugin;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;

public class ConfigUtils {

    private final FileConfiguration configFile;

    public ConfigUtils(BQPlugin plugin) {
        configFile = plugin.getConfig();
    }

    public Type getTypeByID(int id) {
        return Type.valueOf(Objects.requireNonNull(configFile.getString("NPC-IDS." + id)).toUpperCase());
    }

    public long getHandingDelay() {
        return configFile.getLong("Handing-Delay");
    }

    public String getHost() {
        return configFile.getString("MySQL.host");
    }

    public String getDatabase() {
        return configFile.getString("MySQL.database");
    }

    public String getUsername() {
        return configFile.getString("MySQL.username");
    }

    public String getPassword() {
        return configFile.getString("MySQL.password");
    }

    public int getPort() {
        return configFile.getInt("MySQL.port");
    }
}
