package com.github.fieldpin.ConfigSystems;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class PlayerConfig extends YamlConfiguration {
    private final File file;

    public PlayerConfig(OfflinePlayer offlinePlayer) {
        this.file = new File("plugins/Configs/PlayerData/" + offlinePlayer.getUniqueId() + ".yml");
        if (!file.exists())
        {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        loadConfiguration(file);
    }

    public void setConfig(String path, Object value) {
        this.set(path, value);
        try {
            this.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
