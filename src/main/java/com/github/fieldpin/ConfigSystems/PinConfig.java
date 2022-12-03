package com.github.fieldpin.ConfigSystems;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class PinConfig extends YamlConfiguration {

    private final File file = new File("plugins/Configs/PinData.yml");

    public PinConfig () {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            this.load(this.file);
        } catch (InvalidConfigurationException | IOException e) {
            throw new RuntimeException(e);
        }
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
