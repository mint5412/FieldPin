package com.github.fieldpin;

import com.github.fieldpin.PinSystems.SearchPin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class FieldPin extends JavaPlugin {

    @Override
    public void onEnable() {
        // registerEventListener
        getServer().getPluginManager().registerEvents(new SearchPin(), this);

        // mkdir
        File file = new File("plugins/Configs");
        if (!file.exists()) file.mkdir();
        file = new File(file.getPath()+"/PlayerData");
        if (!file.exists()) file.mkdir();


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
