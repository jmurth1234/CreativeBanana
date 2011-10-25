package com.ubempire.flatmap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class FMPlayerListener extends PlayerListener {

    private final FlatMap plugin;

    /*
          * Initiate the listener
          */
    public FMPlayerListener(FlatMap callbackPlugin) {
        plugin = callbackPlugin;
    }

    public void onPlayerJoin(PlayerJoinEvent event) {
        try {
            Player player = event.getPlayer();
            event.setJoinMessage(ChatColor.GREEN + "[Notice] " + ChatColor.LIGHT_PURPLE + player.getName() + " has just joined the game.");
            //System.out.print(player.getWorld().getName());
            if (!player.getWorld().getName().equals("flat")) {
                player.teleport(plugin.getServer().getWorld("flat").getSpawnLocation());

                event.getPlayer().kickPlayer("Changing world... please log in again!");
                event.setJoinMessage(ChatColor.GREEN + "[Notice] " + ChatColor.LIGHT_PURPLE + player.getName() + " may be joining soon.");
            }

        } catch (Exception e) {
            System.err.println("Failed to change world, kicking player");
        }

    }

    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage(ChatColor.GREEN + "[Notice] " + ChatColor.LIGHT_PURPLE + player.getName() + " has just left the game.");
    }

    public void onPlayerRespawn(PlayerRespawnEvent event) {
        event.setRespawnLocation(plugin.getServer().getWorld("flat").getSpawnLocation());
    }


}