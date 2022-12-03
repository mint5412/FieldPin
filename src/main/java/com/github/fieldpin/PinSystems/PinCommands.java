package com.github.fieldpin.PinSystems;

import com.github.fieldpin.Systems.Converters;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PinCommands implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) return false;
        if (!label.equals("pin")) return false;

        Player player = (Player) sender;

        Location pinLoc;
        PinManager pin = new PinManager(player, player.getWorld());
        Converters conv = new Converters();

        if (args.length == 0) {
            // onPlayer
            pinLoc = player.getLocation();

        }else if (args.length == 1){
            switch (args[0]) {
                case "remove":
                    pin.remove();
                    player.sendMessage("Field Pin is removed.");
                    return true;
                case "color":
                    new UIManager(player).changeColor();
                    return true;
                case "target":
                    new UIManager(player).setTarget();
                    return true;
                default:
                    return false;
            }
        }else if (args.length == 3)
        {
            // supports relative coordinates
            // X
            double x;
            if (conv.isConvertDouble(args[0]))
            {
                x = Double.parseDouble(args[0]);
            }else if (args[0].equals("~"))
            {
                x = player.getLocation().getX();
            }else if (args[0].startsWith("~")
                    && conv.isConvertDouble(args[0].replace("~", "")))
            {
                x = player.getLocation().getX() + Double.parseDouble(args[0].replace("~", ""));
            }else return false;

            // Y
            double y;
            if (conv.isConvertDouble(args[1]))
            {
                y = Double.parseDouble(args[1]);
            }else if (args[1].equals("~"))
            {
                y = player.getLocation().getY();
            }else if (args[1].startsWith("~")
                    && conv.isConvertDouble(args[1].replace("~", "")))
            {
                y = player.getLocation().getY() + Double.parseDouble(args[1].replace("~", ""));
            }else return false;

            // Z
            double z;
            if (conv.isConvertDouble(args[2]))
            {
                z = Double.parseDouble(args[2]);
            }else if (args[2].equals("~"))
            {
                z = player.getLocation().getZ();
            }else if (args[2].startsWith("~")
                    && conv.isConvertDouble(args[2].replace("~", "")))
            {
                z = player.getLocation().getZ() + Double.parseDouble(args[2].replace("~", ""));
            }else return false;

            pinLoc = new Location(player.getWorld(), x, y, z);
        }else return false;

        new PinManager(player, player.getWorld()).update(pinLoc);
        return true;

    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, Command command, @NotNull String alias, String[] args) {
        if (!command.getName().equals("pin")) return null;
        if (!(sender instanceof Player)) return null;
        Player player = (Player) sender;
        Block block = player.getTargetBlockExact(10);
        Location loc = null;
        Converters conv = new Converters();
        if (block != null) {
            loc = block.getLocation().clone();
        }
        List<String> list = new ArrayList<>();
        switch (args.length)
        {
            case 1:
                if (args[0] == null) return null;
                break;
            case 2:
                if (args[0] == null || args[1] == null) return null;
                break;
            case 3:
                if (args[0] == null || args[1] == null || args[2] == null) return null;
                break;

        }
        if (args.length == 1)
        {
            if (args[0].length() == 0)
            {
                list.addAll(Arrays.asList("color", "remove", "target"));
                if (loc != null)
                {
                    list.addAll(Arrays.asList(loc.getBlockX() + "", loc.getBlockX() + " " + loc.getBlockY(), loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ()));
                }
                return list;

            } else {
                if ("color".startsWith(args[0]))
                {
                    return Collections.singletonList("color");
                } else if ("remove".startsWith(args[0]))
                {
                    return Collections.singletonList("remove");
                } else if ("target".startsWith(args[0]))
                {
                    return Collections.singletonList("target");
                } else if (loc != null)
                {
                    if (conv.isConvertDouble(args[0])
                            || args[0].startsWith("~")
                            || conv.isConvertDouble(args[0].replace("~", "")))
                    {
                        list.addAll(Arrays.asList(args[0] + " " + loc.getBlockY(), args[0] + " " + loc.getBlockY() + " " + loc.getBlockZ()));
                        return list;
                    }
                }
            }
        } else if (args.length == 2 && loc != null)
        {
            if (args[0].startsWith("~")
                    || conv.isConvertDouble(args[0].replace("~", ""))
                    || conv.isConvertDouble(args[0]))
            {
                if (args[1].length() == 0)
                {
                    list.addAll(Arrays.asList(loc.getBlockY()+"", loc.getBlockY() + " " + loc.getBlockZ()));
                    return list;
                } else
                {
                    if (conv.isConvertDouble(args[1])
                            || args[1].startsWith("~")
                            || conv.isConvertDouble(args[1].replace("~", "")))
                    {
                        list.add(args[1] + " " + loc.getBlockZ());
                        return list;
                    }
                }

            }
        } else if (args.length == 3 && loc != null)
        {
            if ((args[0].startsWith("~")
                    || conv.isConvertDouble(args[0].replace("~", ""))
                    || conv.isConvertDouble(args[0]))
                    && (args[1].startsWith("~")
                    || conv.isConvertDouble(args[1].replace("~", ""))
                    || conv.isConvertDouble(args[1])))
            {
                if (args[2].length() == 0)
                {
                    list.add(loc.getBlockZ() + "");
                    return list;
                }
            }
        }
        return null;
    }
}
