package com.github.fieldpin.PinSystems;

import com.github.fieldpin.ConfigSystems.PlayerConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;

import java.util.UUID;

public class SearchPin implements Listener {
    @EventHandler
    public void PlayerUpdate(PlayerMoveEvent e) {
        Player player = e.getPlayer();

        String path = "target";

        PlayerConfig playerConfig = new PlayerConfig(player);

        // scoreboard setting
        Scoreboard board = player.getScoreboard();
        Objective objective = board.getObjective("pin");

        if (objective == null) {
            objective = board.registerNewObjective("pin", Criteria.DUMMY, "chase");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        }

        // target missing
        Object uid = playerConfig.get(path);
        if (uid == null) {
            objective.unregister();
            return;
        }

        UUID targetUID = UUID.fromString((String) uid);

        Location playerLoc = player.getLocation();

        OfflinePlayer targetPlayer = Bukkit.getServer().getOfflinePlayer(targetUID);
        PlayerConfig targetConfig = new PlayerConfig(targetPlayer);

        String worldName = player.getWorld().getName();

        // whether is there Pin in the player's world
        Location targetLoc = (Location) targetConfig.get(worldName + "Pin");
        if (targetLoc == null) {
            player.sendMessage(targetPlayer.getName() + " is not setting Field Pin in this world.");
            playerConfig.setConfig(path, null);
            objective.unregister();
            return;
        }


        Vector subtract = targetLoc.toVector().subtract(playerLoc.toVector()).normalize();
        int distance = (int) playerLoc.toVector().distance(targetLoc.toVector());

        // reached goal point
        if (distance == 0) {
            player.sendMessage("you reached targeting Field Pin");
            playerConfig.setConfig(path, null);
            objective.unregister();
            return;
        }

        String[] direction = getDirection(subtract);
        String arrow = getDirectingArrow(player, subtract);

        // update scoreboard
        for (String entry : board.getEntries()) {
            if (objective.getScore(entry).getScore() == -1) board.resetScores(entry);
        }

        objective.getScore(ChatColor.GREEN + "Distance" + ChatColor.WHITE + ":" + ChatColor.YELLOW + distance + ChatColor.GREEN + "m").setScore(-1);
        objective.getScore("Direction" + ChatColor.WHITE + ":" + ChatColor.YELLOW + direction[0] + direction[1] + arrow).setScore(-1);

    }

    private String[] getDirection(Vector subtract) {

        subtract = subtract.normalize();

        String news, ud;

        double x = subtract.getX();
        double y = subtract.getY();
        double z = subtract.getZ();

        // up, down
        if (y > 1) ud = "(UP)";
        else if (y < -1) ud = "(DOWN)";
        else ud = "(-)";

        // directions
        final double absSin22 = Math.abs(Math.sin(22.5));
        final double absCos22 = Math.abs(Math.cos(22.5));

        boolean NoS = Math.abs(x) <= absSin22 && Math.abs(z) >= Math.abs(x*absCos22/absSin22);
        boolean EoW = Math.abs(z) <= absSin22 && Math.abs(x) <= Math.abs(z*absCos22/absSin22);

        if (NoS) {
            // N or S
            if (z < 0) news = "N";
            else if (z > 0) news = "S";
            else news = "-";
        } else if (EoW) {
            // E or W
            if (x < 0) news = "W";
            else if (x > 0) news = "E";
            else news = "-";
        } else if (x > 0){
            // NE or SE
            if (z < 0) news = "NE";
            else if (z > 0) news = "SE";
            else news = "-";
        } else {
            // NW or SW
            if (z < 0) news = "NW";
            else if (z > 0) news = "SW";
            else news = "-";
        }

        return new String[]{news, ud};
    }

    private String getDirectingArrow(Player player, Vector subtract) {
        Vector faceDirection = player.getFacing().getDirection();
        return ChatColor.BLACK + "(" + ChatColor.AQUA +
                switch (getDirection(subtract.subtract(faceDirection))[0]) {
                    case "N" -> "↓";
                    case "NE" -> "↘";
                    case "E" -> "→";
                    case "SE" -> "↗";
                    case "S" -> "↑";
                    case "SW" -> "↙";
                    case "W" -> "←";
                    case "NW" -> "↖";
                    default -> "";
                }
                + ChatColor.BLACK + ")";
    }
}
