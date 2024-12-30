package me.psikuvit.betterQuests.listeners;

import me.psikuvit.betterQuests.BQPlugin;
import me.psikuvit.betterQuests.guis.MainInventory;
import me.psikuvit.betterQuests.utils.Type;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class NPCClickListener implements Listener {

    private final BQPlugin plugin;

    public NPCClickListener(BQPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onNPCClick(NPCRightClickEvent event) {
        Player player = event.getClicker();
        NPC npc = event.getNPC();
        Type type = plugin.getConfigUtils().getTypeByID(npc.getId());

        player.openInventory(new MainInventory(player, type).getInventory());

    }
}
