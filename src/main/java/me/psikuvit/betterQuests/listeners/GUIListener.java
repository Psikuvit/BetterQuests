package me.psikuvit.betterQuests.listeners;

import me.psikuvit.betterQuests.BQPlugin;
import me.psikuvit.betterQuests.guis.MainInventory;
import me.psikuvit.betterQuests.guis.SubMenu;
import me.psikuvit.betterQuests.quest.Quest;
import me.psikuvit.betterQuests.quest.QuestManager;
import me.psikuvit.betterQuests.quest.QuestType;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.UUID;

public class GUIListener implements Listener {

    private final NamespacedKey QUEST_KEY = BQPlugin.getInstance().getQuestKey();
    private final QuestManager questManager;

    public GUIListener(BQPlugin plugin) {
        this.questManager = plugin.getQuestManager();
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory inv = event.getClickedInventory();
        if (inv == null || inv.getHolder() == null) return;

        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();

        if (inv.getHolder() instanceof MainInventory) {
            handleMainInventoryClick(event, player, uuid);
            event.setCancelled(true);
        } else if (inv.getHolder() instanceof SubMenu subMenu) {
            subMenu.handleClick(event);
            event.setCancelled(true);
        }
    }

    private void handleMainInventoryClick(InventoryClickEvent event, Player player, UUID uuid) {
        QuestType questType = extractQuestType(event.getCurrentItem());
        if (questType == null) return;

        if (BQPlugin.getInstance().getMySQL().getPlayerLevel(uuid.toString()) < 5) {
            player.sendMessage("You need to be level 5 to start a quest.");
            return;
        }
        Quest quest = null;
        if (questManager.hasQuest(uuid)) {
            quest = questManager.getQuest(uuid);
        } else {
            if (questType == QuestType.BRING_ITEMS_FROM_BOSS || questType == QuestType.BRING_ITEMS_FROM_MOBS) {
                quest = questManager.startQuest(5, uuid, List.of(new ItemStack(Material.PAPER)), questType, Material.STONE);
            } else if (questType == QuestType.KILL_BOSS || questType == QuestType.KILL_MOBS) {
                quest = questManager.startQuest(5, uuid, List.of(new ItemStack(Material.PAPER)), questType, EntityType.ZOMBIE);
            }
        }

        if (quest != null && quest.getQuestType() == questType) {
            player.openInventory(new SubMenu(quest).getInventory());
        }
    }

    private QuestType extractQuestType(ItemStack itemStack) {
        if (itemStack == null || itemStack.getItemMeta() == null) return null;
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta.getPersistentDataContainer().has(QUEST_KEY, PersistentDataType.STRING)) {
            String type = itemMeta.getPersistentDataContainer().get(QUEST_KEY, PersistentDataType.STRING);
            return QuestType.valueOf(type);
        }
        return null;
    }
}