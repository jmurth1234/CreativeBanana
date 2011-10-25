package com.ubempire.flatmap;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.*;
import org.bukkit.util.config.Configuration;

import java.io.File;

public class AntiGrief extends BlockListener {

    private final FlatMap plugin;
    private final File pluginpath = new File("plugins/CreativeBanana");

    /*
          * Initiate the listener
          */
    public AntiGrief(FlatMap callbackPlugin) {
        plugin = callbackPlugin;
    }

    public void onBlockIgnite(BlockIgniteEvent event) {
        event.setCancelled(true);
    }

    public void onBlockPhysics(BlockPhysicsEvent event) {
        Material block = event.getBlock().getType();
        if (block == Material.GRAVEL || block == Material.SAND) {
            event.setCancelled(true);
        }
    }

    public void onBlockDamage(BlockDamageEvent event) {
        Player player = event.getPlayer();
        if (player.isOp() && player.getItemInHand().getType() == Material.GOLD_PICKAXE) {
            //event.setInstaBreak(true);
            event.setCancelled(true);
            event.getBlock().setType(Material.AIR);
        }
    }

    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (!player.isOp()) {
            Block block = event.getBlock();
            World world = block.getWorld();
            Chunk chunk = world.getChunkAt(block.getLocation());
            String cname = chunk.getX() + "." + chunk.getZ() + ".yml";
            String cpath = block.getLocation().getBlockX() + "," + block.getLocation().getBlockY() + "," + block.getLocation().getBlockZ();
            String cpath2 = block.getLocation().getBlockX() + "," + (block.getLocation().getBlockY() + 1) + "," + block.getLocation().getBlockZ();
            Configuration pC = plugin.getConfig(new File(pluginpath + "/" + cname));
            //pC.setHeader(player.getName());
            if (!pC.getString(cpath + ".player", "").equals(player.getName()) && pC.getString(cpath + ".player", "").length() != 0) {
                event.setCancelled(true);
            }
            if (block.getRelative(0, 1, 0).getTypeId() == 71 || block.getRelative(0, 1, 0).getTypeId() == 64)
                if (!pC.getString(cpath2 + ".player", "").equals(player.getName()) && pC.getString(cpath2 + ".player", "").length() != 0) {
                    event.setCancelled(true);
                } else {
                    pC.removeProperty(cpath);
                    pC.save();
                }
        }

    }

    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (player.isOp() && (event.getBlock().getTypeId() == 95)) {
            event.setCancelled(true);
            String[] whostuff = plugin.whoPlaced(event.getBlock().getLocation().getBlock(), new File(pluginpath + "/"));
            String whoplaced = whostuff[0];
            if (whoplaced.length() == 0)
                player.sendMessage(ChatColor.GREEN + "[Notice] " + ChatColor.LIGHT_PURPLE + "The block at " + event.getBlock().getLocation().getBlockX() + "," + event.getBlock().getLocation().getBlockY() + "," + event.getBlock().getLocation().getBlockZ() + " has no history.");
            else {
                player.sendMessage(ChatColor.GREEN + "[Notice] " + ChatColor.LIGHT_PURPLE + "The " + Material.getMaterial(Integer.parseInt(whostuff[1])).name().toLowerCase().replace("_", " ") + " at " + event.getBlock().getLocation().getBlockX() + "," + event.getBlock().getLocation().getBlockY() + "," + event.getBlock().getLocation().getBlockZ() + " was placed by " + whoplaced + ".");
            }
        } else if (event.getBlock().getTypeId() == 46 || event.getBlock().getTypeId() == 79 || event.getBlock().getTypeId() == 8 || event.getBlock().getTypeId() == 9 || event.getBlock().getTypeId() == 10 || event.getBlock().getTypeId() == 11 || event.getBlock().getTypeId() == 51 || event.getBlock().getTypeId() == 52) {
            if (!player.isOp()) {
                if (event.getBlock().getTypeId() == 52 || event.getBlock().getTypeId() == 46) event.setCancelled(true);
                int onlineops = 0;
                for (Player players : plugin.getServer().getOnlinePlayers()) {
                    if (players.isOp()) {
                        onlineops++;
                    }
                    if (onlineops == 0) {
                        event.setCancelled(true);
                    } else {
                        plugin.getServer().broadcastMessage(ChatColor.GREEN + "[Notice] " + ChatColor.LIGHT_PURPLE + player.getName() + " has placed a restricted block!");
                        System.out.print(player.getName() + " has placed a restricted block");
                    }
                }

            }
        } else {
            Block block = event.getBlock();
            World world = block.getWorld();
            Chunk chunk = world.getChunkAt(block.getLocation());
            String cname = chunk.getX() + "." + chunk.getZ() + ".yml";
            String cpath = block.getLocation().getBlockX() + "," + block.getLocation().getBlockY() + "," + block.getLocation().getBlockZ();
            if (!pluginpath.exists()) {
                pluginpath.mkdir();
            }
            Configuration pC = plugin.getConfig(new File(pluginpath + "/" + cname));
            pC.setProperty(cpath + ".player", player.getName());
            pC.setProperty(cpath + ".type", event.getBlock().getTypeId());
            if (block.getTypeId() == 71 || block.getTypeId() == 64) {
                String cpath2 = block.getLocation().getBlockX() + "," + (block.getLocation().getBlockY() + 1) + "," + block.getLocation().getBlockZ();
                pC.setProperty(cpath2 + ".player", player.getName());
                pC.setProperty(cpath2 + ".type", event.getBlock().getTypeId());
            }
            pC.save();
        }
    }
}