package com.github.fieldpin.PinSystems;

import com.github.fieldpin.ConfigSystems.PinConfig;
import com.github.fieldpin.ConfigSystems.PlayerConfig;
import com.github.fieldpin.FieldPin;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class PinManager {

    private final World world;
    private Location pinLocation;
    private Color pinColor;
    private final String configPath;
    private final PinConfig pinConfig;
    private final PlayerConfig playerConfig;
    private final OfflinePlayer pinOwner;
    private final JavaPlugin plugin;


    public PinManager(OfflinePlayer player, World world) {
        this.plugin = JavaPlugin.getPlugin(FieldPin.class);
        this.pinOwner = player;
        this.pinConfig = new PinConfig();
        this.playerConfig = new PlayerConfig(player);
        this.world = world;
        this.configPath = player.getUniqueId() + world.getName();
        this.pinColor = getColor();

        this.pinLocation = (Location) this.pinConfig.get(getConfigPath());

        World[] worlds = (World[]) this.playerConfig.get("Pins");
        if (worlds != null) {
            List<World> worldList = Arrays.asList(worlds);
            if (!worldList.contains(world)) {
                worldList.add(world);
                worlds = worldList.toArray(new World[0]);
            }
        } else worlds = new World[]{world};

        this.playerConfig.setConfig("Pins", worlds);
    }

    public void update(Location pinLoc){
        // if Player is offline, update cancel
        if (getPinOwner().getPlayer() == null) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error: " + ChatColor.LIGHT_PURPLE
                    + getPinOwner().getName() + ChatColor.RED + " is Offline");
            return;
        }

        pinLoc = pinLoc.getBlock().getLocation().add(0.5,0,0.5);

        // already exist at same point
        if (Objects.equals(getPinLocation(), pinLoc)) return;

        //既存のピンを削除
        remove();
        //新たなピンを作成
        setPinLocation(pinLoc);
        setPinMarker();
        SpawnPinParticle();

        getPinOwner().getPlayer().sendMessage(pinLoc.getX()+", "+pinLoc.getY()+", "+ pinLoc.getZ());
        getPinOwner().getPlayer().sendMessage("にピンを設置しました");
    }
    public void remove(){
        // remove Pin
        ArmorStand stand = getPinMarker();
        if (stand != null) stand.remove();
        setPinLocation(null);
    }
    public void SpawnPinParticle(){
        if (getPinLocation() == null) return;
        int range = 256 - getPinLocation().getBlockY();
        new BukkitRunnable(){

            @Override
            public void run() {
                if (getPinLocation() == null){
                    cancel();
                    return;
                }
                Color color = getColor();
                Particle.DustOptions options = new Particle.DustOptions(color, 4.0f);
                for (int add = 0; add <= range; ++add)
                {
                    world.spawnParticle(Particle.REDSTONE, getPinLocation().clone().add(0, add,0), 1, options);
                }
            }
        }.runTaskTimer(plugin,0, 20);
    }
    public ArmorStand getPinMarker() {
        World world = this.pinLocation.getWorld();
        if (world == null) return null;
        for (ArmorStand entity : world.getEntitiesByClass(ArmorStand.class)) {
            if (entity.getLocation().equals(this.pinLocation)
                    && entity.getCustomName() != null
                    && entity.getCustomName().equals(pinOwner.getName())) return entity;
        }
        return null;
    }
    public void setPinMarker() {
        if (getPinLocation().getWorld() == null) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error: World is null");
            return;
        }
        ArmorStand stand = (ArmorStand) getPinLocation().getWorld().spawnEntity(this.pinLocation, EntityType.ARMOR_STAND);
        stand.setInvisible(true);
        stand.setInvulnerable(true);
        stand.setCollidable(false);
        stand.setCustomName(getPinOwner().getName());
        stand.setGravity(false);
        stand.setCustomNameVisible(true);
        stand.setSmall(true);
    }
    public OfflinePlayer getPinOwner() {return this.pinOwner;}
    public Location getPinLocation() {return this.pinLocation;}
    public void setPinLocation(Location pinLocation) {
        this.pinLocation = pinLocation;
        this.pinConfig.setConfig(getConfigPath(), pinLocation);
    }
    public String getConfigPath() {return this.configPath;}
    public Color getColor() {
        this.pinColor = (Color) this.pinConfig.get("color");
        if (this.pinColor == null) {
            // setting default color
            setColor(Color.WHITE);
        }
        return this.pinColor;
    }
    public void setColor(Color pinColor) {
        this.pinColor = pinColor;
        this.playerConfig.setConfig("color", pinColor);
    }
}
