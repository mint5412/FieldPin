package com.github.fieldpin.PinSystems;

import com.github.fieldpin.ConfigSystems.PinConfig;
import com.github.fieldpin.ConfigSystems.PlayerConfig;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ChoiceColor implements Listener {
    @EventHandler
    public void ClickInventoryEvent(InventoryClickEvent e) {
        Inventory clicked = e.getClickedInventory();
        if (clicked == null) return;
        ItemStack clickedItem = clicked.getItem(e.getSlot());
        if (clickedItem == null) return;

        Player player = (Player) e.getView().getPlayer();
        PinManager manager = new PinManager(player, player.getWorld());
        String title = e.getView().getTitle();

        if (!title.equalsIgnoreCase(net.md_5.bungee.api.ChatColor.LIGHT_PURPLE + "now: " +
                ChatColor.of(new java.awt.Color(manager.getColor().getRed(), manager.getColor().getGreen(), manager.getColor().getBlue())) + "this color")) return;
        e.setCancelled(true);

        PlayerConfig playerConfig = new PlayerConfig(player);

        Color color =
        switch (clickedItem.getType()) {
            case RED_STAINED_GLASS_PANE -> Color.RED;
            case BLUE_STAINED_GLASS_PANE -> Color.BLUE;
            case GREEN_STAINED_GLASS_PANE -> Color.GREEN;
            case WHITE_STAINED_GLASS_PANE -> Color.WHITE;
            case BLACK_STAINED_GLASS_PANE -> Color.BLACK;
            case PURPLE_STAINED_GLASS_PANE -> Color.PURPLE;
            case YELLOW_STAINED_GLASS_PANE -> Color.YELLOW;
            default -> null;
        };

        if (color == null) return;
        playerConfig.setConfig("color", color);

        player.closeInventory();
    }
}
