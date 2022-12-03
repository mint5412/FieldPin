package com.github.fieldpin.Systems;

import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FillInventory {
    public FillInventory(Inventory inventory, ItemStack fillItem)
    {
        Fill(inventory, fillItem);
    }

    private void Fill(Inventory inv, ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.LIGHT_PURPLE+"No Item");
        item.setItemMeta(meta);

        for(int i = 0; i<inv.getSize(); ++i) {
            inv.setItem(i, item);
        }
    }
}
