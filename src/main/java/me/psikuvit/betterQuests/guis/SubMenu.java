package me.psikuvit.betterQuests.guis;

import me.psikuvit.betterQuests.BQPlugin;
import me.psikuvit.betterQuests.quest.Quest;
import me.psikuvit.betterQuests.quest.QuestManager;
import me.psikuvit.betterQuests.quest.QuestType;
import me.psikuvit.betterQuests.utils.Type;
import me.psikuvit.betterQuests.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class SubMenu implements InventoryHolder {

    private final int INVENTORY_SIZE = 45;
    private final int BACK_SLOT = 31;
    private final int HAND_SLOT = 14;
    private final int CLAIM_SLOT = 12;
    private final Inventory inventory;
    private final Quest quest;
    private final QuestManager questManager = BQPlugin.getInstance().getQuestManager();
    private final Map<Integer, Consumer<Player>> slotActions = new HashMap<>();

    public SubMenu(Quest quest) {
        this.quest = quest;
        this.inventory = Bukkit.createInventory(this, INVENTORY_SIZE, quest.getQuestType().name());
        initializeSlotActions();
        decorate();
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void handleClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        slotActions.get(event.getSlot()).accept(player);
    }

    private void initializeSlotActions() {
        slotActions.put(BACK_SLOT, player -> goBack(player, quest.getQuestType()));
        slotActions.put(HAND_SLOT, this::handleHanding);
        slotActions.put(CLAIM_SLOT, this::handleClaim);
    }

    private void handleClaim(Player player) {
        if (quest.progress == quest.getGoal()) {
            player.sendMessage(Utils.color("&bQuest Completed!"));
            questManager.removeQuest(player.getUniqueId(), quest);
            questManager.removeHanding(player.getUniqueId());

            for (ItemStack reward : quest.getRewards()) {
                player.getInventory().addItem(reward);
            }
            player.closeInventory();
        }
    }

    private void handleHanding(Player player) {
        ItemStack item = player.getItemOnCursor();
        if (quest.getQuestType() == QuestType.BRING_ITEMS_FROM_BOSS || quest.getQuestType() == QuestType.BRING_ITEMS_FROM_MOBS) {
            if (item.getType() == quest.getMaterial()) {
                if (!questManager.canHand(player.getUniqueId())) {
                    player.sendMessage(Utils.color("&cYou have to wait before handing another item! " + questManager.getRemainingTime(player.getUniqueId()) + " seconds"));
                    return;
                }
                if (quest.progress == quest.getGoal()) {
                    player.sendMessage(Utils.color("&cYou have handed all needed Items!"));
                    return;
                }

                item.setAmount(item.getAmount() - 1);
                quest.incrementTotalAttempts();
                if (Math.random() < 0.5) {
                    quest.incrementProgress();
                    quest.incrementSuccessfulAttempts();
                    questManager.cacheHanding(player.getUniqueId());
                } else {
                    player.sendMessage(Utils.color("&cYou have failed to hand the item!"));
                }
                player.openInventory(new SubMenu(quest).getInventory());
            }
        }
    }

    private void goBack(Player player, QuestType questType) {
        if (questType == QuestType.BRING_ITEMS_FROM_BOSS || questType == QuestType.BRING_ITEMS_FROM_MOBS) {
            player.openInventory(new MainInventory(player, Type.BIOLOGIST).getInventory());
        } else if (questType == QuestType.KILL_BOSS || questType == QuestType.KILL_MOBS) {
            player.openInventory(new MainInventory(player, Type.HUNTER).getInventory());
        }
    }

    private void decorate() {
        fillWithGrayPanes();
        if (quest.getQuestType() == QuestType.BRING_ITEMS_FROM_BOSS || quest.getQuestType() == QuestType.BRING_ITEMS_FROM_MOBS) {
            addItemToSlot(CLAIM_SLOT, Material.BOOK, "&aInformation", List.of(" ", "&7Your success rate: " + quest.getSuccessRate()));
        } else if (quest.getQuestType() == QuestType.KILL_BOSS || quest.getQuestType() == QuestType.KILL_MOBS) {
            addItemToSlot(CLAIM_SLOT, Material.BOOK, "&aInformation", List.of(" "));
        }
        addItemToSlot(HAND_SLOT, Material.PAPER, "&aProgress: " + quest.progress + "/" + quest.getGoal(), null);
        addItemToSlot(BACK_SLOT, Material.BARRIER, "&cBACK", null);
    }

    private void fillWithGrayPanes() {
        for (int i = 0; i < INVENTORY_SIZE; i++) {
            ItemStack itemStack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(" ");

            itemStack.setItemMeta(itemMeta);
            inventory.setItem(i, itemStack);
        }
    }

    private void addItemToSlot(int slot, Material material, String displayName, List<String> lore) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(Utils.color(displayName));
        if (lore != null) {
            itemMeta.setLore(Utils.color(lore));
        }
        itemStack.setItemMeta(itemMeta);
        inventory.setItem(slot, itemStack);
    }
}