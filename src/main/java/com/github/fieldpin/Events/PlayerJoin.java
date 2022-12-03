package com.github.fieldpin.Events;

import com.github.fieldpin.ConfigSystems.PlayerConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class PlayerJoin implements Listener {
    @EventHandler
    public void PlayerJoin(PlayerJoinEvent e)
    {
        Player player = e.getPlayer();
        // setting player config
        new PlayerConfig(player).setConfig("name", player.getName());

        //setting scoreboard
        ScoreboardManager mng = Bukkit.getScoreboardManager();
        assert mng != null;
        Scoreboard board = mng.getNewScoreboard();
        player.setScoreboard(board);
    }
}
