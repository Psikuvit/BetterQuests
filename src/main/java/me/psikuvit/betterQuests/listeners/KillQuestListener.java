package me.psikuvit.betterQuests.listeners;

import me.psikuvit.betterQuests.BQPlugin;
import me.psikuvit.betterQuests.quest.Quest;
import me.psikuvit.betterQuests.quest.QuestManager;
import me.psikuvit.betterQuests.quest.QuestType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class KillQuestListener implements Listener {

    private final QuestManager questManager;

    public KillQuestListener(BQPlugin plugin) {
        this.questManager = plugin.getQuestManager();
    }

    @EventHandler
    public void onKill(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        Player player = entity.getKiller();
        if (player == null) return;

        Quest quest = questManager.getQuest(player.getUniqueId());

        if (quest == null) return;

        if (quest.getQuestType() == QuestType.KILL_BOSS || quest.getQuestType() == QuestType.KILL_MOBS) {
            if (entity.getType() == quest.getEntity() && quest.progress != quest.getGoal()) {
                quest.incrementProgress();
            }
        }
    }
}