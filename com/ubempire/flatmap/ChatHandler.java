package com.ubempire.flatmap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

import java.io.File;

public class ChatHandler extends PlayerListener {

    private final FlatMap plugin;
    private final File pluginpath = new File("plugins/CreativeBanana");

    /*
      * Initiate the listener
      */
    public ChatHandler(FlatMap callbackPlugin) {
        plugin = callbackPlugin;
    }

    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock().getType() == Material.LEVER || event.getClickedBlock().getType() == Material.STONE_BUTTON || event.getClickedBlock().getTypeId() == 71 || event.getClickedBlock().getTypeId() == 64 || event.getClickedBlock().getType() == Material.CHEST || event.getClickedBlock().getType() == Material.FURNACE || event.getClickedBlock().getType() == Material.DISPENSER) {
                if (!player.isOp()) {
                    String[] whostuff = plugin.whoPlaced(event.getClickedBlock(), pluginpath);
                    String whoplaced = whostuff[0];
                    if (whoplaced.length() > 0) {
                        if (!whoplaced.equals(player.getName())) {
                            player.sendMessage(ChatColor.RED + "[Warning] " + ChatColor.LIGHT_PURPLE + "You did not place that block!");
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
        if (player.isOp() && (event.getAction() == Action.LEFT_CLICK_BLOCK) && player.getItemInHand().getType() == Material.BONE) {
            plugin.blockstuff[0] = player.getName();
            plugin.blockstuff[1] = String.valueOf(event.getClickedBlock().getLocation().getBlockX());
            plugin.blockstuff[2] = String.valueOf(event.getClickedBlock().getLocation().getBlockY());
            plugin.blockstuff[3] = String.valueOf(event.getClickedBlock().getLocation().getBlockZ());
            plugin.blockstuff[4] = String.valueOf(event.getClickedBlock().getTypeId());
            player.sendMessage(ChatColor.GREEN + "[Notice] " + ChatColor.LIGHT_PURPLE + "Cuboid location 1 set.");
        }
        if (player.isOp() && (event.getAction() == Action.RIGHT_CLICK_BLOCK) && player.getItemInHand().getType() == Material.BONE) {
            if (player.getName().equalsIgnoreCase(plugin.blockstuff[0])) {
                int typeid = Integer.valueOf(plugin.blockstuff[4]);
                int x = event.getClickedBlock().getLocation().getBlockX();
                int y = event.getClickedBlock().getLocation().getBlockY();
                int z = event.getClickedBlock().getLocation().getBlockZ();
                int Xmin = Math.min(x, Integer.valueOf(plugin.blockstuff[1]));
                int Xmax = Math.max(x, Integer.valueOf(plugin.blockstuff[1]));
                int Ymin = Math.min(y, Integer.valueOf(plugin.blockstuff[2]));
                int Ymax = Math.max(y, Integer.valueOf(plugin.blockstuff[2]));
                int Zmin = Math.min(z, Integer.valueOf(plugin.blockstuff[3]));
                int Zmax = Math.max(z, Integer.valueOf(plugin.blockstuff[3]));
                int count = 0;
                for (int X = Xmin; X <= Xmax; X++) {
                    for (int Y = Ymin; Y <= Ymax; Y++) {
                        for (int Z = Zmin; Z <= Zmax; Z++) {
                            if (count <= 20) {
                                Block block = player.getWorld().getBlockAt(X, Y, Z);
                                if (!(block.getTypeId() == typeid)) {
                                    plugin.setPlaced(player, block, pluginpath);//event.getBlock().getLocation().getBlock(), new File(pluginpath+"/");
                                    block.setTypeId(typeid);
                                    count++;
                                }
                                block = null;
                            }
                        }
                    }
                }
                player.sendMessage(ChatColor.GREEN + "[Notice] " + ChatColor.LIGHT_PURPLE + "Cuboid created.");
            } else {
                player.sendMessage(ChatColor.RED + "[Warning] " + ChatColor.LIGHT_PURPLE + "You have not set the first location!");
            }
        }
        if (player.isOp() && (event.getAction() == Action.LEFT_CLICK_BLOCK) && player.getItemInHand().getType() == Material.GOLD_SWORD) {
            player.sendBlockChange(event.getClickedBlock().getLocation(), 20, (byte) 0);
        }
        if (player.isOp() && (event.getAction() == Action.LEFT_CLICK_BLOCK) && player.getItemInHand().getType() == Material.STICK) {
            String[] whostuff = plugin.whoPlaced(event.getClickedBlock(), new File(pluginpath + "/"));
            String whoplaced = whostuff[0];
            if (whoplaced.length() == 0)
                player.sendMessage(ChatColor.GREEN + "[Notice] " + ChatColor.LIGHT_PURPLE + "The block at " + event.getClickedBlock().getLocation().getBlockX() + "," + event.getClickedBlock().getLocation().getBlockY() + "," + event.getClickedBlock().getLocation().getBlockZ() + " has no history.");
            else {
                player.sendMessage(ChatColor.GREEN + "[Notice] " + ChatColor.LIGHT_PURPLE + "The " + event.getClickedBlock().getType().name().toLowerCase().replace("_", " ") + " at " + event.getClickedBlock().getLocation().getBlockX() + "," + event.getClickedBlock().getLocation().getBlockY() + "," + event.getClickedBlock().getLocation().getBlockZ() + " was placed by " + whoplaced + ".");
            }
        }

    }

    public void onPlayerChat(PlayerChatEvent event) {
        if (!event.getMessage().equalsIgnoreCase("!help")) {
            Player player = event.getPlayer();
            String message = event.getMessage();
            String prefix = ChatColor.AQUA + "[Chat]";
            String playername = ChatColor.WHITE + player.getName() + ": ";
            if (player.isOp()) {
                playername = ChatColor.GOLD + player.getName() + ": ";
            }
            String postmessage = prefix + " " + playername + ChatColor.WHITE + message;
            System.out.println(postmessage);
            plugin.getServer().broadcastMessage(postmessage);
        } else {
            Player player = event.getPlayer();
            player.sendMessage(ChatColor.RED + "[Warning] " + ChatColor.LIGHT_PURPLE + "You have summoned an admin!");
            player.sendMessage(ChatColor.RED + "[Warning] " + ChatColor.LIGHT_PURPLE + "Abusing this WILL get you banned!");
            for (Player op : plugin.getServer().getOnlinePlayers()) {
                if (op.isOp()) op.teleport(player);
            }
        }

        event.setCancelled(true);
    }


}
	