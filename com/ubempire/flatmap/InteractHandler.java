package com.ubempire.flatmap;

import org.bukkit.entity.CreatureType;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityListener;

public class InteractHandler extends EntityListener {

    @SuppressWarnings("unused")
    private final FlatMap plugin;

    /*
      * Initiate the listener
      */
    public InteractHandler(FlatMap callbackPlugin) {
        plugin = callbackPlugin;
    }

    @Override
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        //Entity entity = event.getEntity();
        if (event.getLocation().getY() >= 60) {
            if (event.getCreatureType() == CreatureType.CREEPER || event.getCreatureType() == CreatureType.SKELETON || event.getCreatureType() == CreatureType.SPIDER || event.getCreatureType() == CreatureType.ZOMBIE) {
                //event.getLocation().getWorld().spawnCreature(event.getLocation(), CreatureType.CHICKEN);
                event.setCancelled(true);
            }
        }


    }
}
