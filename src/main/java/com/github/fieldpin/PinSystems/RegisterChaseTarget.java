package com.github.fieldpin.PinSystems;

import com.github.fieldpin.ConfigSystems.PlayerConfig;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class RegisterChaseTarget implements Listener {
    @EventHandler
    public void ClickInventoryEvent(InventoryClickEvent e) {
        Inventory clicked = e.getClickedInventory();
        if (clicked == null) return;
        ItemStack clickedItem = clicked.getItem(e.getSlot());
        if (clickedItem == null) return;

        Player player = (Player) e.getView().getPlayer();
        String title = e.getView().getTitle();
        if (!title.equalsIgnoreCase(ChatColor.LIGHT_PURPLE+"Select Target")) return;
        e.setCancelled(true);

        ItemMeta meta = clickedItem.getItemMeta();
        assert meta != null;

        String path = "target";
        PlayerConfig config = new PlayerConfig(player);

        if (meta instanceof SkullMeta && ((SkullMeta) meta).getOwningPlayer() != null) {
            OfflinePlayer offlinePlayer = ((SkullMeta) meta).getOwningPlayer();
            config.setConfig(path, offlinePlayer.getUniqueId().toString());
            player.sendMessage("Chase Target: " + offlinePlayer.getName());
        }else {
            player.sendMessage("Chase Target: OFF");
            config.setConfig(path, null);
        }

        player.closeInventory();

    }
}
