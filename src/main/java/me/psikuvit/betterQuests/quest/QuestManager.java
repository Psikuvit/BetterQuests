package me.psikuvit.betterQuests.quest;

import me.psikuvit.betterQuests.BQPlugin;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class QuestManager {

    private final Set<Quest> quests;
    private final Map<UUID, Quest> playerQuests;
    private final Map<UUID, Long> handingCooldown; // this will track the time between each handing

    public QuestManager() {
        quests = new HashSet<>();
        playerQuests = new HashMap<>();
        handingCooldown = new HashMap<>();
    }

    public void removeQuest(UUID uuid, Quest quest) {
        playerQuests.remove(uuid);
        quests.remove(quest);
    }

    public boolean hasQuest(UUID uuid) {
        return playerQuests.containsKey(uuid);
    }

    public Quest startQuest(int goal, UUID uuid, List<ItemStack> rewards, QuestType questType, Material material) {
        Quest quest = new Quest(goal, questType.name(), rewards, questType, null, material);
        quests.add(quest);
        playerQuests.put(uuid, quest);
        return quest;
    }

    public Quest startQuest(int goal, UUID uuid, List<ItemStack> rewards, QuestType questType, EntityType entity) {
        Quest quest = new Quest(goal, questType.name(), rewards, questType, entity, null);
        quests.add(quest);
        playerQuests.put(uuid, quest);
        return quest;
    }

    public void cacheHanding(UUID uuid) {
        handingCooldown.put(uuid, System.currentTimeMillis());
    }

    public boolean canHand(UUID uuid) {
        return System.currentTimeMillis() - handingCooldown.getOrDefault(uuid, 0L) > BQPlugin.getInstance().getConfigUtils().getHandingDelay() * 1000;
    }

    public long getRemainingTime(UUID uuid) {
        return BQPlugin.getInstance().getConfigUtils().getHandingDelay() - (System.currentTimeMillis() - handingCooldown.getOrDefault(uuid, 0L)) / 1000;
    }

    public void removeHanding(UUID uuid) {
        handingCooldown.remove(uuid);
    }

    public Quest getQuest(UUID uuid) {
        return playerQuests.get(uuid);
    }
}
