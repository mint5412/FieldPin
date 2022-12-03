package com.github.fieldpin;

import com.github.fieldpin.Events.SearchPin;
import org.bukkit.plugin.java.JavaPlugin;

public final class FieldPin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        // registerEventListener
        getServer().getPluginManager().registerEvents(new SearchPin(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
