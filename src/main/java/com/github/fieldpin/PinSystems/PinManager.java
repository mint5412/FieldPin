package com.github.fieldpin.PinSystems;

import com.github.fieldpin.ConfigSystems.PinConfig;
import com.github.fieldpin.ConfigSystems.PlayerConfig;
import com.github.fieldpin.FieldPin;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class PinManager {

    private final World world;
    private final PinConfig pinConfig;
    private final OfflinePlayer pinOwner;
    private final JavaPlugin plugin;


    public PinManager(OfflinePlayer player, World world) {
        this.plugin = JavaPlugin.getPlugin(FieldPin.class);
        this.pinOwner = player;
        this.pinConfig = new PinConfig();
        this.world = world;

    }

    public void update(Location pinLoc) {

        Player player = getPinOwner().getPlayer();
        assert player != null;

        pinLoc = pinLoc.getBlock().getLocation().add(0.5,0,0.5);

        // already exist at same point
        if (Objects.equals(getPinLocation(), pinLoc)) return;

        remove();

        create(pinLoc);

        player.sendMessage(pinLoc.getX()+", "+pinLoc.getY()+", "+ pinLoc.getZ());
        player.sendMessage("Field Pin is placed there.");
    }
    public void create(Location pinLoc) {
        setPinLocation(pinLoc);
        setPinMarker();
        SpawnPinParticle();
    }
    public void remove() {
        // remove Pin
        ArmorStand stand = getPinMarker();
        if (stand != null) stand.remove();
        setPinLocation(null);
    }
    public void SpawnPinParticle() {
        if (getPinLocation() == null) return;
        int range = 256 - getPinLocation().getBlockY();
        new BukkitRunnable(){

            @Override
            public void run() {
                if (getPinLocation() == null ||getPinMarker() == null
                        || !getPinLocation().equals(getPinMarker().getLocation())) {
                    cancel();
                    return;
                }
                Color color = getColor();
                Particle.DustOptions options = new Particle.DustOptions(color, 4.0f);
                for (int add = 0; add <= range; add++) {
                    world.spawnParticle(Particle.REDSTONE, getPinLocation().clone().add(0, add,0), 1, options);
                }
            }
        }.runTaskTimer(plugin,0, 20);
    }
    public ArmorStand getPinMarker() {
        if (getPinLocation() == null) return null;
        World world = getPinLocation().getWorld();
        if (world == null) return null;
        for (ArmorStand entity : world.getEntitiesByClass(ArmorStand.class)) {
            if (entity.getLocation().equals(getPinLocation())
                    && entity.getCustomName() != null
                    && entity.getCustomName().equals(pinOwner.getName())) return entity;
        }
        return null;
    }
    public void setPinMarker() {
        if (getPinLocation() == null || getPinLocation().getWorld() == null) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error: World is null");
            return;
        }
        ArmorStand stand = (ArmorStand) getPinLocation().getWorld().spawnEntity(getPinLocation(), EntityType.ARMOR_STAND);
        stand.setInvisible(true);
        stand.setInvulnerable(true);
        stand.setCollidable(false);
        stand.setCustomName(getPinOwner().getName());
        stand.setGravity(false);
        stand.setCustomNameVisible(true);
        stand.setSmall(true);
    }
    public OfflinePlayer getPinOwner() {return this.pinOwner;}
    public Location getPinLocation() {return this.pinConfig.getLocation(this.pinOwner.getUniqueId() + "." + world.getName());}
    public void setPinLocation(Location pinLocation) {this.pinConfig.setConfig(this.pinOwner.getUniqueId() + "." + world.getName(), pinLocation);}
    public Color getColor() {
        Color color = new PlayerConfig(pinOwner).getColor("color");
        if (color == null) {
            // setting default color
            setColor(Color.WHITE);
        }
        return color;
    }
    public void setColor(Color pinColor) {
        new PlayerConfig(pinOwner).setConfig("color", pinColor);
    }
}
