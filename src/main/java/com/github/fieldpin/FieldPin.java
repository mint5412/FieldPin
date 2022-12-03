package com.github.fieldpin;

import com.github.fieldpin.ConfigSystems.PinConfig;
import com.github.fieldpin.Events.PlayerJoin;
import com.github.fieldpin.PinSystems.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;
import java.util.UUID;

public final class FieldPin extends JavaPlugin {

    @Override
    public void onEnable() {
        // registerEventListener
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new SearchPin(), this);
        getServer().getPluginManager().registerEvents(new ChoiceColor(), this);
        getServer().getPluginManager().registerEvents(new RegisterChaseTarget(), this);

        Objects.requireNonNull(getCommand("pin")).setExecutor(new PinCommands());

        Mkdir();
        SetUp();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void Mkdir() {
        File file = new File("plugins/Configs");
        if (!file.exists()) file.mkdir();
        file = new File(file.getPath()+"/PlayerData");
        if (!file.exists()) file.mkdir();
    }
    public void SetUp() {
        PinConfig pinConfig = new PinConfig();
        for (String key : pinConfig.getKeys(false)) {
            Location pinLocation = (Location) pinConfig.get(key);
            if (pinLocation == null) continue;

            World world = pinLocation.getWorld();
            assert world != null;

            OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(key.replace(world.getName(), "")));
            new PinManager(player, world).SpawnPinParticle();
        }
        for (Player player : Bukkit.getOnlinePlayers())
        {
            Bukkit.getServer().getPluginManager().callEvent(new PlayerJoinEvent(player, ""));
        }
    }
}
