package me.psikuvit.betterQuests.guis;

import me.psikuvit.betterQuests.BQPlugin;
import me.psikuvit.betterQuests.quest.QuestType;
import me.psikuvit.betterQuests.utils.Type;
import me.psikuvit.betterQuests.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class MainInventory implements InventoryHolder {

    private final int INVENTORY_SIZE = 45;
    private final Inventory inventory;
    private final Type type;
    private final Player player;

    public MainInventory(Player player, Type type) {
        this.type = type;
        this.player = player;
        this.inventory = Bukkit.createInventory(this, INVENTORY_SIZE, type.getId());
        decorateInventory();
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    private void decorateInventory() {
        fillWithGrayPanes();
        int QUEST_SLOT_1 = 21;
        int QUEST_SLOT_2 = 23;
        if (type == Type.BIOLOGIST) {
            addQuestItem(QUEST_SLOT_1, "&aBring Items From Mobs", QuestType.BRING_ITEMS_FROM_MOBS);
            addQuestItem(QUEST_SLOT_2, "&aBring Items From Boss", QuestType.BRING_ITEMS_FROM_BOSS);
        } else if (type == Type.HUNTER) {
            addQuestItem(QUEST_SLOT_1, "&aKill Mobs", QuestType.KILL_MOBS);
            addQuestItem(QUEST_SLOT_2, "&aKill Boss", QuestType.KILL_BOSS);
        }
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

    private void addQuestItem(int slot, String displayName, QuestType questType) {
        ItemStack itemStack = new ItemStack(Material.BOOK);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(Utils.color(displayName));
        itemMeta.setLore(List.of(" ", checkStatus(questType)));
        itemMeta.getPersistentDataContainer().set(BQPlugin.getInstance().getQuestKey(), PersistentDataType.STRING, questType.name());
        itemStack.setItemMeta(itemMeta);
        inventory.setItem(slot, itemStack);
    }

    public String checkStatus(QuestType questType) {
        if (BQPlugin.getInstance().getQuestManager().hasQuest(player.getUniqueId())) {
            if (BQPlugin.getInstance().getQuestManager().getQuest(player.getUniqueId()).getQuestType() == questType) {
                return Utils.color("&eThis quest is active!");
            } else {
                return Utils.color("&cOther quest is active!");
            }
        } else {
            return Utils.color("&eQuest is available!");
        }
    }
}