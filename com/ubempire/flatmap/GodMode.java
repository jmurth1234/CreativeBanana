package com.ubempire.flatmap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;

import java.io.File;

public class GodMode extends EntityListener {

    private final FlatMap plugin;
    @SuppressWarnings("unused")
    private final File pluginpath = new File("plugins/CreativeBanana");

    /*
      * Initiate the listener
      */
    public GodMode(FlatMap callbackPlugin) {
        plugin = callbackPlugin;
    }

    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (player.isOp()) {
                event.setCancelled(true);
            }
        }
    }

    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            plugin.getServer().broadcastMessage(ChatColor.GREEN + "[Notice] " + ChatColor.LIGHT_PURPLE + player.getName() + " has just died.");
        }

    }
}