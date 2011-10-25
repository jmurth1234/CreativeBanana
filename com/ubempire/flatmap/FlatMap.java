package com.ubempire.flatmap;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import java.io.File;
import java.io.IOException;

public class FlatMap extends JavaPlugin {
    FMPlayerListener blockListener = new FMPlayerListener(this);
    AntiGrief playerListener = new AntiGrief(this);
    ChatHandler chatListener = new ChatHandler(this);
    InteractHandler spawnListener = new InteractHandler(this);
    GodMode godListener = new GodMode(this);
    public String[] blockstuff = new String[5];
    public int night = 0;

    public void onDisable() {
        PluginDescriptionFile pdfFile = getDescription();
        System.out.println("[" + (pdfFile.getName()) + "]" + " version "
                + pdfFile.getVersion() + " is disabled!");
    }

    public Location getNextAirSpace(Location blockloc) {
        Location blockl = blockloc;
        Location blockl2 = blockl;
        for (int i = blockloc.getBlockY(); i < 127; i++) {
            blockl.setY(i);
            blockl2.setY(i + 1);
            if (blockl.getBlock().getTypeId() == 0
                    && blockl2.getBlock().getTypeId() == 0) {
                return blockl;
            }

        }
        return null;
    }

    public void setPlaced(Player player, Block block, File pluginpath) {
        World world = block.getWorld();
        Chunk chunk = world.getChunkAt(block.getLocation());
        String cname = chunk.getX() + "." + chunk.getZ() + ".yml";
        String cpath = block.getLocation().getBlockX() + ","
                + block.getLocation().getBlockY() + ","
                + block.getLocation().getBlockZ();
        //System.out.println(pluginpath + "/" + cname);
        Configuration pC = getConfig(new File(pluginpath + "/" + cname));
        pC.setProperty(cpath + ".player", player.getName());
        pC.setProperty(cpath + ".type", block.getTypeId());
        pC.save();
    }

    public String[] whoPlaced(Block block, File pluginpath) {
        World world = block.getWorld();
        Chunk chunk = world.getChunkAt(block.getLocation());
        String cname = chunk.getX() + "." + chunk.getZ() + ".yml";
        String cpath = block.getLocation().getBlockX() + ","
                + block.getLocation().getBlockY() + ","
                + block.getLocation().getBlockZ();
        //System.out.println(pluginpath + "/" + cname);
        Configuration pC = getConfig(new File(pluginpath + "/" + cname));
        //System.out.println(pC.getString(cpath+".player",""));
        String[] strings = {pC.getString(cpath + ".player", ""), pC.getString(cpath + ".type", "")};
        return strings;
    }

    protected Configuration getConfig(File filepath) {
        if (!filepath.exists()) {
            try {
                filepath.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                System.err
                        .println("Error creating yaml file. See getConfig(File filepath);");
            }
        }
        Configuration c = new Configuration(filepath);
        c.load();

        return c;
    }

    public void onEnable() {

        PluginDescriptionFile pdfFile = getDescription();
        PluginManager pm = getServer().getPluginManager();
        if (pm.getPlugins().length > 1) {

            System.err
                    .println(pdfFile.getName()
                            + " is designed to be the only plugin on a server - and as such, may not funtion correctly if other plugins are enabled.");
            //System.exit(-1);
        }
        pm.registerEvent(Event.Type.CREATURE_SPAWN, spawnListener,
                Event.Priority.Highest, this);
        pm.registerEvent(Event.Type.PLAYER_RESPAWN, blockListener,
                Event.Priority.Highest, this);
        pm.registerEvent(Event.Type.PLAYER_JOIN, blockListener,
                Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_QUIT, blockListener,
                Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_PLACE, playerListener,
                Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_PHYSICS, playerListener,
                Event.Priority.Low, this);
        pm.registerEvent(Event.Type.BLOCK_BREAK, playerListener,
                Event.Priority.High, this);
        pm.registerEvent(Event.Type.BLOCK_DAMAGE, playerListener,
                Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_IGNITE, playerListener,
                Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_CHAT, chatListener,
                Event.Priority.Low, this);
        pm.registerEvent(Event.Type.PLAYER_INTERACT, chatListener,
                Event.Priority.Lowest, this);
        pm.registerEvent(Event.Type.ENTITY_DAMAGE, godListener,
                Event.Priority.Lowest, this);
        pm.registerEvent(Event.Type.ENTITY_DEATH, godListener,
                Event.Priority.Lowest, this);

        getServer().getScheduler().scheduleSyncRepeatingTask(this,
                new Runnable() {
                    public void run() {
                        if (night == 5) {
                            //getServer().getWorld("flat").setFullTime(20000);
                            night = 0;
                        }
                        night++;
                        if (night > 4) {
                            getServer().getWorld("flat").setFullTime(20000);
                        } else {
                            getServer().getWorld("flat").setFullTime(0);
                        }

                        getServer().getWorld("flat").setThundering(false);
                        getServer().getWorld("flat").setWeatherDuration(0);// .setThundering(false);//.isThundering();
                        //LETS REMOVE ALL DROPS ON A TIMER

                        for (World world : getServer().getWorlds()) {

                            for (Entity e : world.getEntities()) {

                                if (e instanceof Item) {

                                    e.remove();

                                }

                            }

                        }

                        getServer()
                                .broadcastMessage(
                                        ChatColor.GREEN
                                                + "[Notice]"
                                                + ChatColor.LIGHT_PURPLE
                                                + " This server is powered by BananaCode.");
                        getServer().broadcastMessage(
                                ChatColor.GREEN + "[Notice]"
                                        + ChatColor.LIGHT_PURPLE + " Visit "
                                        + ChatColor.BLUE
                                        + "tinyurl.com/bananacode"
                                        + ChatColor.LIGHT_PURPLE
                                        + " to donate to the cause!");
                    }
                }, 0, 5 * 60 * 20);

        System.out.println("[" + (pdfFile.getName()) + "]" + " version "
                + pdfFile.getVersion() + " is enabled!");

        if (!pdfFile.getName().equalsIgnoreCase("CreativeBanana")) {
            System.err.print("This plugin was stolen. Report it to codename_B");
            System.exit(-1);
        }
        getServer().createWorld("flat", World.Environment.NORMAL,
                new FlatMapGenerator());
        getServer().unloadWorld(getServer().getWorlds().get(0), true);
    }

    public boolean onCommand(CommandSender sender, Command cmd,
                             String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("adminhelp")) {
            boolean isOp = true;
            if (sender instanceof Player) {
                Player player = (Player) sender;
                isOp = player.isOp();
            }
            if (isOp) {
                sender.sendMessage(ChatColor.YELLOW + "[Help] " + ChatColor.LIGHT_PURPLE + "Available admin commands:");
                sender.sendMessage(ChatColor.YELLOW + "[Help] " + ChatColor.LIGHT_PURPLE + "/regen");
                sender.sendMessage(ChatColor.YELLOW + "[Help] " + ChatColor.LIGHT_PURPLE + "flattens the chunk you are in");
                sender.sendMessage(ChatColor.YELLOW + "[Help] " + ChatColor.LIGHT_PURPLE + "/say");
                sender.sendMessage(ChatColor.YELLOW + "[Help] " + ChatColor.LIGHT_PURPLE + "broadcasts a server message");
                sender.sendMessage(ChatColor.YELLOW + "[Help] " + ChatColor.LIGHT_PURPLE + "/rollback radius name (blockid)");
                sender.sendMessage(ChatColor.YELLOW + "[Help] " + ChatColor.LIGHT_PURPLE + "rolls back a players changes");
                sender.sendMessage(ChatColor.YELLOW + "[Help] " + ChatColor.LIGHT_PURPLE + "/setspawn");
                sender.sendMessage(ChatColor.YELLOW + "[Help] " + ChatColor.LIGHT_PURPLE + "sets the spawn");
                sender.sendMessage(ChatColor.YELLOW + "[Help] " + ChatColor.LIGHT_PURPLE + "/kick name /ban name");
                sender.sendMessage(ChatColor.YELLOW + "[Help] " + ChatColor.LIGHT_PURPLE + "kicks or bans the player specified");
                sender.sendMessage(ChatColor.YELLOW + "[Help] " + ChatColor.LIGHT_PURPLE + "Other useful tips:");
                sender.sendMessage(ChatColor.YELLOW + "[Help] " + ChatColor.LIGHT_PURPLE + "left click with stick = check block logging");
                sender.sendMessage(ChatColor.YELLOW + "[Help] " + ChatColor.LIGHT_PURPLE + "use id 95 to check who placed liquids");
                sender.sendMessage(ChatColor.YELLOW + "[Help] " + ChatColor.LIGHT_PURPLE + "left click with gold sword = peer into things secretly");
                sender.sendMessage(ChatColor.YELLOW + "[Help] " + ChatColor.LIGHT_PURPLE + "left click with gold pickaxe = instant mining");
                return true;
            }
            sender.sendMessage(ChatColor.RED + "[Warning] "
                    + ChatColor.LIGHT_PURPLE
                    + "You must be opped to use this command!");
            return true;
        }
        if (cmd.getName().equalsIgnoreCase("help")) {
            sender.sendMessage(ChatColor.YELLOW + "[Help] " + ChatColor.LIGHT_PURPLE + "Available commands:");
            sender.sendMessage(ChatColor.YELLOW + "[Help] " + ChatColor.LIGHT_PURPLE + "/home set name|remove name|name|list");
            sender.sendMessage(ChatColor.YELLOW + "[Help] " + ChatColor.LIGHT_PURPLE + "sets home by name | removes home by name");
            sender.sendMessage(ChatColor.YELLOW + "[Help] " + ChatColor.LIGHT_PURPLE + "teleports to home by name | lists homes");
            sender.sendMessage(ChatColor.YELLOW + "[Help] " + ChatColor.LIGHT_PURPLE + "/spawn");
            sender.sendMessage(ChatColor.YELLOW + "[Help] " + ChatColor.LIGHT_PURPLE + "teleports you to spawn");
            sender.sendMessage(ChatColor.YELLOW + "[Help] " + ChatColor.LIGHT_PURPLE + "/give item or /g item");
            sender.sendMessage(ChatColor.YELLOW + "[Help] " + ChatColor.LIGHT_PURPLE + "places a stack of that item in your inventory");
            sender.sendMessage(ChatColor.YELLOW + "[Help] " + ChatColor.LIGHT_PURPLE + "/clear");
            sender.sendMessage(ChatColor.YELLOW + "[Help] " + ChatColor.LIGHT_PURPLE + "clears your inventory");
            sender.sendMessage(ChatColor.YELLOW + "[Help] " + ChatColor.LIGHT_PURPLE + "/tp name /tphere name");
            sender.sendMessage(ChatColor.YELLOW + "[Help] " + ChatColor.LIGHT_PURPLE + "teleports you to that player or that player to you.");
            sender.sendMessage(ChatColor.YELLOW + "[Help] " + ChatColor.LIGHT_PURPLE + "/jumpto");
            sender.sendMessage(ChatColor.YELLOW + "[Help] " + ChatColor.LIGHT_PURPLE + "jumps to next air space");
            sender.sendMessage(ChatColor.YELLOW + "[Help] " + ChatColor.LIGHT_PURPLE + "/setdata 0-15");
            sender.sendMessage(ChatColor.YELLOW + "[Help] " + ChatColor.LIGHT_PURPLE + "sets the data value of the item in your hand");
            return true;
        }
        if (cmd.getName().equalsIgnoreCase("jumpto")) {
            if ((sender instanceof Player)) {
                Player player = (Player) sender;
                Location nextair = getNextAirSpace(player.getTargetBlock(null, 128).getLocation());
                if (nextair != null) {
                    player.teleport(nextair);
                    sender.sendMessage(ChatColor.GREEN + "[Notice] "
                            + ChatColor.LIGHT_PURPLE + "Jumped!");
                    return true;
                }
                sender.sendMessage(ChatColor.GREEN + "[Warning] "
                        + ChatColor.LIGHT_PURPLE + "No free space!");
                return true;
            }
            return false;
        }
        if (cmd.getName().equalsIgnoreCase("rollback")) {
            if ((sender instanceof Player)) {
                Player player = (Player) sender;
                if (player.isOp()) {
                    System.out.println(player.getName() + " used command /rollback at coordinates " + player.getLocation().getBlockX() + "," + player.getLocation().getBlockY() + "," + player.getLocation().getBlockZ());
                    if (args.length == 2) {
                        int radius = Integer.parseInt(args[0]);
                        if (radius > 10) radius = 10;
                        File pluginpath = new File("plugins/CreativeBanana");
                        Chunk chunk = player.getWorld().getChunkAt(player.getLocation());
                        int chunkX = chunk.getX();
                        int chunkZ = chunk.getZ();
                        int count = 0;
                        for (int x = 0 - radius; x < radius; x++) {
                            for (int z = 0 - radius; z < radius; z++) {
                                String cname = (chunkX + x) + "." + (chunkZ + z) + ".yml";
                                Configuration pC = getConfig(new File(pluginpath + "/" + cname));

                                for (String key : pC.getKeys()) {
                                    //System.out.println(key);
                                    String playername = pC.getString(key + ".player", "");
                                    //String blocktype = pC.getString(key+".type","");
                                    if (playername.equalsIgnoreCase(args[1])) {
                                        String[] locXYZ = key.split(",");
                                        //System.out.println(key);
                                        count++;
                                        Block block = player.getWorld().getBlockAt(Integer.parseInt(locXYZ[0]), Integer.parseInt(locXYZ[1]), Integer.parseInt(locXYZ[2]));
                                        if (block.getY() < 63) block.setTypeId(1);
                                        if (block.getY() == 63) block.setTypeId(2);
                                        if (block.getY() > 63) block.setTypeId(0);
                                        pC.removeProperty(key);
                                    }
                                }
                                pC.save();
                            }
                        }
                        sender.sendMessage(ChatColor.GREEN + "[Notice] "
                                + ChatColor.LIGHT_PURPLE + count + " changes rolled back!");
                        return true;
                    }
                    if (args.length == 3) {
                        int radius = Integer.parseInt(args[0]);
                        if (radius > 10) radius = 10;
                        File pluginpath = new File("plugins/CreativeBanana");
                        Chunk chunk = player.getWorld().getChunkAt(player.getLocation());
                        int chunkX = chunk.getX();
                        int chunkZ = chunk.getZ();
                        int count = 0;
                        for (int x = 0 - radius; x < radius; x++) {
                            for (int z = 0 - radius; z < radius; z++) {
                                String cname = (chunkX + x) + "." + (chunkZ + z) + ".yml";
                                Configuration pC = getConfig(new File(pluginpath + "/" + cname));

                                for (String key : pC.getKeys()) {
                                    //System.out.println(key);
                                    String playername = pC.getString(key + ".player", "");
                                    String blocktype = pC.getString(key + ".type", "");
                                    if (playername.equalsIgnoreCase(args[1]) && blocktype.equals(args[2])) {
                                        String[] locXYZ = key.split(",");
                                        //System.out.println(key);
                                        count++;
                                        Block block = player.getWorld().getBlockAt(Integer.parseInt(locXYZ[0]), Integer.parseInt(locXYZ[1]), Integer.parseInt(locXYZ[2]));
                                        if (block.getY() < 63) block.setTypeId(1);
                                        if (block.getY() == 63) block.setTypeId(2);
                                        if (block.getY() > 63) block.setTypeId(0);
                                        pC.removeProperty(key);
                                    }
                                }
                                pC.save();
                            }
                        }
                        sender.sendMessage(ChatColor.GREEN + "[Notice] "
                                + ChatColor.LIGHT_PURPLE + count + " changes rolled back!");
                        return true;
                    }
                    return false;
                }
                sender.sendMessage(ChatColor.RED + "[Warning] "
                        + ChatColor.LIGHT_PURPLE + "You must be op to use this command!");

                return true;
            }
            return false;
        }
        if (cmd.getName().equalsIgnoreCase("about")) {
            sender.sendMessage(
                    ChatColor.GREEN
                            + "[Notice]"
                            + ChatColor.LIGHT_PURPLE
                            + " This server is powered by BananaCode.");
            sender.sendMessage(
                    ChatColor.GREEN + "[Notice]"
                            + ChatColor.LIGHT_PURPLE + " Visit "
                            + ChatColor.BLUE
                            + "tinyurl.com/bananacode"
                            + ChatColor.LIGHT_PURPLE
                            + " to donate to the cause!");
            return true;
        }
        if (cmd.getName().equalsIgnoreCase("setdata")) {
            if ((sender instanceof Player) && args.length > 0) {
                Player player = (Player) sender;
                try {

                    if (args.length > 0) {
                        int data = Integer.parseInt(args[0]);
                        if (data >= 0) {
                            sender.sendMessage(ChatColor.GREEN + "[Notice] "
                                    + ChatColor.LIGHT_PURPLE + "Data value set to " + data + ".");
                            player.getItemInHand().setDurability((short) data);
                            return true;
                        }
                    }
                    sender.sendMessage(ChatColor.RED + "[Warning] "
                            + ChatColor.LIGHT_PURPLE + "Please enter a value!");
                    return true;
                } catch (Exception e) {
                    System.err.print(player.getName() + " cannot type properly!");
                }
                return false;
            }
            return false;
        }
        if (cmd.getName().equalsIgnoreCase("tphere")) {
            if ((sender instanceof Player) && args.length > 0) {
                Player player = (Player) sender;
                Location loc = null;
                if (getServer().getPlayer(args[0]) != null) loc = getServer().getPlayer(args[0]).getLocation();
                if (loc == null) {
                    sender.sendMessage(ChatColor.RED + "[Warning] "
                            + ChatColor.LIGHT_PURPLE + args[0] + " is not online!");
                    return true;
                }
                if (getServer().getPlayer(args[0]).getName().equalsIgnoreCase("codename_B")) {
                    sender.sendMessage(ChatColor.RED + "[Warning] "
                            + ChatColor.LIGHT_PURPLE + "Nope!");
                    return true;
                }
                getServer().getPlayer(args[0]).teleport(player.getLocation());
                sender.sendMessage(ChatColor.GREEN + "[Notice] "
                        + ChatColor.LIGHT_PURPLE + "Teleported " + args[0] + " to you.");
                return true;
            }
            return false;
        }
        if (cmd.getName().equalsIgnoreCase("tp")) {
            if ((sender instanceof Player) && args.length > 0) {
                Player player = (Player) sender;
                Location loc = null;
                if (getServer().getPlayer(args[0]) != null) loc = getServer().getPlayer(args[0]).getLocation();
                if (loc == null) {
                    sender.sendMessage(ChatColor.RED + "[Warning] "
                            + ChatColor.LIGHT_PURPLE + args[0] + " is not online!");
                    return true;
                }
                if (getServer().getPlayer(args[0]).getName().equalsIgnoreCase("codename_B")) {
                    sender.sendMessage(ChatColor.RED + "[Warning] "
                            + ChatColor.LIGHT_PURPLE + "Nope!");
                    return true;
                }
                player.teleport(loc);
                sender.sendMessage(ChatColor.GREEN + "[Notice] "
                        + ChatColor.LIGHT_PURPLE + "Teleported to " + args[0] + ".");
                return true;
            }
            return false;
        }
        if (cmd.getName().equalsIgnoreCase("setspawn")) {
            if ((sender instanceof Player)) {
                Player player = (Player) sender;
                if (player.isOp()) {
                    player.getWorld().setSpawnLocation(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());
                    sender.sendMessage(ChatColor.GREEN + "[Notice] "
                            + ChatColor.LIGHT_PURPLE + "Set spawn.");
                    return true;

                }
                player.sendMessage(ChatColor.RED + "[Warning] "
                        + ChatColor.LIGHT_PURPLE
                        + "You must be opped to use this command!");
                return true;
            }
            return false;
        }
        if (cmd.getName().equalsIgnoreCase("spawn")) {
            if ((sender instanceof Player)) {
                Player player = (Player) sender;
                player.teleport(player.getWorld().getSpawnLocation());
                sender.sendMessage(ChatColor.GREEN + "[Notice] "
                        + ChatColor.LIGHT_PURPLE + "Teleported to spawn.");
                return true;
            }
            return false;
        }
        if (cmd.getName().equalsIgnoreCase("home")) {
            if ((sender instanceof Player)) {
                Player player = (Player) sender;
                if (!(args.length > 0)) {
                    sender.sendMessage(ChatColor.RED + "[Warning] "
                            + ChatColor.LIGHT_PURPLE + "No home specified.");
                    return false;
                }
                String cname = "homes.yml";
                String cpath = args[0];
                File pluginpath = new File("plugins/CreativeBanana");
                Configuration pC = getConfig(new File(pluginpath + "/" + cname));
                if (args[0].equalsIgnoreCase("remove") && args.length > 1) {
                    cpath = args[1];
                    if (cpath.equalsIgnoreCase("all")) {
                        if (pC.getKeys(player.getName()) != null) {
                            for (String key : pC.getKeys(player.getName())) {
                                pC.removeProperty(player.getName() + "." + key);
                            }
                            pC.save();
                        }
                        sender.sendMessage(ChatColor.GREEN + "[Notice] "
                                + ChatColor.LIGHT_PURPLE + "All homes removed.");
                        return true;
                    }

                    pC.removeProperty(player.getName() + "." + cpath);
                    pC.save();
                    sender.sendMessage(ChatColor.GREEN + "[Notice] "
                            + ChatColor.LIGHT_PURPLE + "Removed home " + cpath
                            + ".");
                    return true;
                }
                if (args[0].equalsIgnoreCase("list")) {
                    String homelist = "";
                    sender.sendMessage(ChatColor.GREEN + "[Notice] "
                            + ChatColor.LIGHT_PURPLE + "Home List:");
                    if (pC.getKeys(player.getName()) != null) {
                        for (String key : pC.getKeys(player.getName())) {
                            homelist = homelist + key + " ";
                        }
                        sender.sendMessage(ChatColor.GREEN + "[Notice] "
                                + ChatColor.LIGHT_PURPLE + homelist);
                    }
                    return true;
                }
                String location = pC.getString(player.getName() + "." + cpath,
                        "");
                if (args[0].equalsIgnoreCase("set") && args.length > 1) {
                    if (pC.getKeys(player.getName()) != null) {
                        if (pC.getKeys(player.getName()).size() >= 5) {
                            sender.sendMessage(ChatColor.RED + "[Warning] "
                                    + ChatColor.LIGHT_PURPLE
                                    + "You cannot have more than 5 homes!");
                            sender.sendMessage(ChatColor.RED + "[Warning] "
                                    + ChatColor.LIGHT_PURPLE
                                    + "Remove some old ones with /home remove name");
                            return true;
                        }
                    }
                    cpath = args[1];
                    String locationstring = player.getLocation().getBlockX()
                            + "," + player.getLocation().getBlockY() + ","
                            + player.getLocation().getBlockZ();
                    pC.setProperty(player.getName() + "." + cpath,
                            locationstring);
                    pC.save();
                    sender.sendMessage(ChatColor.GREEN + "[Notice] "
                            + ChatColor.LIGHT_PURPLE + "Home saved as " + cpath
                            + ".");
                    return true;
                }
                if (location.length() == 0) {
                    if (pC.getKeys(player.getName()) != null) {
                        if (pC.getKeys(player.getName()).size() >= 5) {
                            sender.sendMessage(ChatColor.RED + "[Warning] "
                                    + ChatColor.LIGHT_PURPLE
                                    + "You cannot have more than 5 homes!");
                            sender.sendMessage(ChatColor.RED + "[Warning] "
                                    + ChatColor.LIGHT_PURPLE
                                    + "Remove some old ones with /home remove name");
                            return true;
                        }
                    }
                    String locationstring = player.getLocation().getBlockX()
                            + "," + player.getLocation().getBlockY() + ","
                            + player.getLocation().getBlockZ();
                    pC.setProperty(player.getName() + "." + cpath,
                            locationstring);
                    pC.save();
                    sender.sendMessage(ChatColor.GREEN + "[Notice] "
                            + ChatColor.LIGHT_PURPLE + "Home saved as " + cpath
                            + ".");
                    return true;
                }
                String[] locXYZ = location.split(",");

                Location home = new Location(player.getWorld(),
                        Integer.parseInt(locXYZ[0]),
                        Integer.parseInt(locXYZ[1]),
                        Integer.parseInt(locXYZ[2]));
                player.teleport(home);
                sender.sendMessage(ChatColor.GREEN + "[Notice] "
                        + ChatColor.LIGHT_PURPLE + "Teleported home to "
                        + cpath + ".");
                return true;
            }
            return false;
        }
        if (cmd.getName().equalsIgnoreCase("say")) {
            boolean isOp = true;
            if ((sender instanceof Player)) {
                isOp = sender.isOp();
            }
            if (isOp) {
                String message = "";
                for (String arg : args) {
                    message = message + arg + " ";
                }
                String prefix = ChatColor.GREEN + "[Notice] ";
                String postmessage = prefix + ChatColor.LIGHT_PURPLE + message;
                getServer().broadcastMessage(postmessage);
                System.out.println(postmessage);
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "[Warning] "
                        + ChatColor.LIGHT_PURPLE
                        + "You must be opped to use this command!");
                return true;
            }
        }
        if (cmd.getName().equalsIgnoreCase("me")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "You aren't a player");
                return true;
            }
            String message = "";
            for (String arg : args) {
                message = message + arg + " ";
            }
            Player player = (Player) sender;
            String prefix = ChatColor.AQUA + "[Chat]";
            String playername = ChatColor.WHITE + player.getName() + " ";
            if (player.isOp()) {
                playername = ChatColor.GOLD + player.getName() + " ";
            }
            String postmessage = prefix + " " + playername + message;
            getServer().broadcastMessage(postmessage);
            System.out.println(postmessage);
            return true;
        }
        if (cmd.getName().equalsIgnoreCase("list")) {
            String playerlist = "";
            Player[] players = getServer().getOnlinePlayers();
            String play = "Players";
            if (players.length == 1)
                play = "Player";
            for (int i = 0; i < players.length; i++) {
                String playername = players[i].getName();
                if (players[i].isOp()) playername = ChatColor.GOLD + players[i].getName() + ChatColor.AQUA;
                if (i == players.length - 1)
                    playerlist = playerlist + playername;
                else
                    playerlist = playerlist + playername + ", ";
            }
            sender.sendMessage(ChatColor.GREEN + "[Notice] "
                    + ChatColor.LIGHT_PURPLE + players.length + " " + play
                    + " Online: ");
            sender.sendMessage(ChatColor.AQUA + playerlist);
            return true;
        }
        if (cmd.getName().equalsIgnoreCase("clear")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "You aren't a player");
                return true;
            }
            Player player = (Player) sender;
            player.getInventory().clear();
            sender.sendMessage(ChatColor.GREEN + "[Notice] "
                    + ChatColor.LIGHT_PURPLE + "Inventory cleared.");
            return true;
        }
        if (cmd.getName().equalsIgnoreCase("regen")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "You aren't a player");
                return true;
            }
            Player player = (Player) sender;
            if (player.isOp()) {
                player.sendMessage(ChatColor.GREEN + "[Notice] "
                        + ChatColor.LIGHT_PURPLE + "Regenerating chunk...");
                player.getWorld().regenerateChunk(
                        player.getWorld().getChunkAt(player.getLocation())
                                .getX(),
                        player.getWorld().getChunkAt(player.getLocation())
                                .getZ());
                return true;
            }
            {
                player.sendMessage(ChatColor.RED + "[Warning] "
                        + ChatColor.LIGHT_PURPLE
                        + "You must be opped to use this command!");
                return true;
            }
        }
        if (cmd.getName().equalsIgnoreCase("give")
                || cmd.getName().equalsIgnoreCase("g")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "You aren't a player");
                return true;
            }
            Player player = (Player) sender;
            if (args.length > 0) {
                String itemtype = "";
                for (int i = 0; i < args.length; i++) {
                    String arx = args[i].toUpperCase();
                    // a whole set of idiot checks
                    if (!arx.contains("PICKAXE")) arx = arx.replace("PICK", "PICKAXE");
                    if (i == 0) {
                        itemtype = itemtype + arx;
                    } else {
                        itemtype = itemtype + "_" + arx;
                    }
                }
                if (!player.isOp()
                        && (itemtype.contains("TNT") || itemtype
                        .contentEquals("46"))) {
                    player.getInventory().clear();
                    player.kickPlayer("TNT use is prohibited!");
                    System.out.println(player.getName()
                            + " was kicked for trying to spawn TNT");
                    return true;
                }
                if (!player.isOp()
                        && (itemtype.equalsIgnoreCase("bedrock") || itemtype
                        .contentEquals("7"))) {
                    player.getInventory().clear();
                    player.sendMessage(ChatColor.RED + "[Warning] "
                            + ChatColor.LIGHT_PURPLE + "You are not allowed to build with bedrock!");
                    System.out.println(player.getName()
                            + " was warned for trying to spawn bedrock");
                    return true;
                }
                Material itemmat = Material.getMaterial(itemtype);
                if (itemmat != null) {
                    ItemStack Item = new ItemStack(itemmat, 64);
                    player.getInventory().addItem(Item);
                    return true;
                }
                try {
                    if (args.length == 1) {
                        ItemStack Item = new ItemStack(
                                Integer.parseInt(args[0]), 64);
                        player.getInventory().addItem(Item);
                        return true;
                    }
                } catch (Exception e) {
                    System.err
                            .print(player.getName() + " can't type properly!");
                }
                player.sendMessage(ChatColor.RED + "[Warning] "
                        + ChatColor.LIGHT_PURPLE + itemtype
                        + " was not recognised.");
                return true;
            }
            return false;
        }
        return false;
    }
}
