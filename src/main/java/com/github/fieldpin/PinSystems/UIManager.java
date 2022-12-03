package com.github.fieldpin.PinSystems;

import com.github.fieldpin.Systems.FillInventory;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class UIManager extends PinManager{

    public UIManager(Player player) {
        super(player, player.getWorld());
    }

    public void changeColor(){

        Player player = getPinOwner().getPlayer();
        assert player != null;

        Inventory inventory = Bukkit.createInventory(player, 27, ChatColor.LIGHT_PURPLE + "now: " + ChatColor.of("#" + getColor().asRGB()) + getColor().toString());
        ItemStack fill = new ItemStack(Material.BARRIER);
        ItemMeta meta = fill.getItemMeta();
        assert meta != null;
        meta.setDisplayName("No Item");
        fill.setItemMeta(meta);
        new FillInventory(inventory, fill);

        List<ItemStack> items = new ArrayList<>();
        items.add(new ItemStack(Material.RED_STAINED_GLASS_PANE));
        items.add(new ItemStack(Material.BLUE_STAINED_GLASS_PANE));
        items.add(new ItemStack(Material.GREEN_STAINED_GLASS_PANE));
        items.add(new ItemStack(Material.PURPLE_STAINED_GLASS_PANE));
        items.add(new ItemStack(Material.YELLOW_STAINED_GLASS_PANE));
        items.add(new ItemStack(Material.WHITE_STAINED_GLASS_PANE));
        items.add(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));

        int ind = 10;
        for (ItemStack item : items)
        {
            meta.setDisplayName(item.getType().name().replace("_STAINED_GLASS_PANE", ""));
            item.setItemMeta(meta);
            inventory.setItem(ind, item);
            ++ind;
        }


        player.openInventory(inventory);

    }
    public void setTarget(){

        Player player = getPinOwner().getPlayer();
        assert player != null;

        Inventory inv = Bukkit.createInventory(player, 27, ChatColor.LIGHT_PURPLE+"Select Target");

        for (OfflinePlayer offlinePlayer : Bukkit.getServer().getOfflinePlayers())
        {
            ItemStack Head = new ItemStack(Material.PLAYER_HEAD);

            SkullMeta meta = (SkullMeta) Head.getItemMeta();
            assert meta != null;
            meta.setDisplayName(offlinePlayer.getName());
            meta.setOwningPlayer(offlinePlayer);

            Head.setItemMeta(meta);
            inv.addItem(Head);
        }

        ItemStack item = new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName("OFF");
        item.setItemMeta(meta);

        inv.setItem(inv.getSize()-1, item);

        player.openInventory(inv);
    }
}
