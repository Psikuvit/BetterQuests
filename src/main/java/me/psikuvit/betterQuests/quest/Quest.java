package me.psikuvit.betterQuests.quest;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Quest {

    private final int goal;
    private final String description;
    private final List<ItemStack> rewards;
    private final QuestType questType;
    private final EntityType entity;
    private final Material material;
    public int progress = 0;
    private int totalAttempts = 0;
    private int successfulAttempts = 0;

    public Quest(int goal, String description, List<ItemStack> rewards, QuestType questType, EntityType entity, Material material) {
        this.goal = goal;
        this.description = description;
        this.rewards = rewards;
        this.questType = questType;
        this.entity = entity;
        this.material = material;
    }

    public int getGoal() {
        return goal;
    }

    public String getDescription() {
        return description;
    }

    public List<ItemStack> getRewards() {
        return rewards;
    }

    public QuestType getQuestType() {
        return questType;
    }

    public EntityType getEntity() {
        return entity;
    }

    public Material getMaterial() {
        return material;
    }

    public void incrementProgress() {
        progress++;
    }

    public void incrementTotalAttempts() {
        totalAttempts++;
    }

    public void incrementSuccessfulAttempts() {
        successfulAttempts++;
    }

    public String getSuccessRate() {
        return totalAttempts == 0 ? "0.00" : String.format("%.2f", (double) successfulAttempts / totalAttempts * 100);
    }
}