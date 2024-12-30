package me.psikuvit.betterQuests;

import me.psikuvit.betterQuests.listeners.GUIListener;
import me.psikuvit.betterQuests.listeners.KillQuestListener;
import me.psikuvit.betterQuests.listeners.NPCClickListener;
import me.psikuvit.betterQuests.quest.QuestManager;
import me.psikuvit.betterQuests.utils.ConfigUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public final class BQPlugin extends JavaPlugin {

    private static BQPlugin instance;
    private ConfigUtils configUtils;
    private NamespacedKey questKey;
    private QuestManager questManager;
    private MySQL mySQL;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        instance = this;
        configUtils = new ConfigUtils(this);
        questKey = new NamespacedKey(this, "quest");
        questManager = new QuestManager();
        mySQL = new MySQL(this);

        getServer().getPluginManager().registerEvents(new NPCClickListener(this), this);
        getServer().getPluginManager().registerEvents(new GUIListener(this), this);
        getServer().getPluginManager().registerEvents(new KillQuestListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public ConfigUtils getConfigUtils() {
        return configUtils;
    }

    public NamespacedKey getQuestKey() {
        return questKey;
    }

    public QuestManager getQuestManager() {
        return questManager;
    }

    public MySQL getMySQL() {
        return mySQL;
    }

    public static BQPlugin getInstance() {
        return instance;
    }
}
