package com.novinitygames.dragonSMP.dragons.classes;

import com.novinitygames.dragonSMP.DragonSMP;
import com.novinitygames.dragonSMP.dragons.Dragon;
import com.novinitygames.dragonSMP.dragons.DragonUser;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;

public class EarthDragon extends Dragon {
    int rejuvination = 0; // holder for the ID of the timer to be created later
    public static ArrayList<Location> unbreakableBlocks = new ArrayList<>(); // List of blocks that players can't break; populated upon box creation
    // Blocks that the player can stand on to gain regeneration
    List<Material> rejuvinatingBlocks = new ArrayList<>() {{
       add(Material.GRASS_BLOCK);
       add(Material.DIRT);
       add(Material.PODZOL);
       add(Material.COARSE_DIRT);
       add(Material.ROOTED_DIRT);
       add(Material.MYCELIUM);
       add(Material.MUD);
       add(Material.MUDDY_MANGROVE_ROOTS);
       add(Material.SAND);
       add(Material.RED_SAND);
       add(Material.SNOW);
       add(Material.SNOW_BLOCK);
       add(Material.MOSS_BLOCK);
       add(Material.GRAVEL);
       add(Material.STONE);
       add(Material.ANDESITE);
       add(Material.TUFF);
       add(Material.GRANITE);
       add(Material.DIORITE);
    }};

    // Code that runs when a player gains this dragon
    public void Init(DragonUser dragonUser) {
        // Set the dragon user (player) for this dragon
        this.dragonUser = dragonUser;
        // Sets the cooldown to use later
        ability1Cooldown = 300;

        // Create a timer to run every 20 ticks (1 second) and assign rejuvination to its ID
        rejuvination = Bukkit.getScheduler().runTaskTimer(DragonSMP.getInstance(), new Runnable() {

            @Override
            public void run() {
                // Get the block the player is standing on
                Location blockStandingOn = dragonUser.player.getLocation().clone().subtract(0, 0.1, 0);
                // Get the block's material
                Material blockMaterial = blockStandingOn.getBlock().getType();

                // Check if it's in the rejuvinating blocks list
                if (rejuvinatingBlocks.contains(blockMaterial)) {
                    // If so, give the player regeration for 1 second (20 ticks)
                    dragonUser.player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20, 0, true, false));
                }
            }
        }, 0L, 20L).getTaskId();
    }

    // Code that runs when a player's dragon is changed from this one
    @Override
    public void Uninit() {
        // Cancel the rejuvination check timer
        Bukkit.getScheduler().cancelTask(rejuvination);
    }

    // Ability 1 code
    @Override
    public void UseAbility1() {
        // Check if they are still on cooldown
        int cdDiff = getAbility1Cooldown(dragonUser);
        if (cdDiff > 0) {
            // If they are, send a message
            dragonUser.player.sendMessage(ChatColor.RED + "This is on cooldown for another " + cdDiff + " seconds!");
            return;
        }
        // If not, add them to cooldown
        dragonUser.cooldowns.put(1,System.currentTimeMillis());

        // Get the player, world, and where the player is standing when starting this ability
        Player player = dragonUser.player;
        World world = player.getWorld();
        Location initialLocation = player.getLocation().clone();

        // Play the firecharge sound
        world.playSound(player.getLocation(), Sound.ITEM_FIRECHARGE_USE, 0.5f, 1.0f);

        // 4x4 box
        int range = 4;
        int height = 4;
        HashMap<Location, Material> changedBlocks = new HashMap<>(); // Track the blocks replaced by stone
        ArrayList<Location> placedBlocks = new ArrayList<>(); // Track all blocks changed (for removing from air blocks

        // Go through all x and z coordinates (basically every block in a square range) -- this is for the floor of the box
        for (int x = -range; x <= range; x++) {
            for (int z = -range; z <= range; z++) {
                // Check if we're on the edge of the square (make sure we're not
                if (x == -range || z == -range || x == range || z == range) continue;
                // Get the target location, should be 1 block below the player's location (hence negating 1)
                Location targetLoc =  initialLocation.clone().add(x, -1, z);
                // If it's not air, add it to the changed blocks list
                if (!targetLoc.getBlock().getType().equals(Material.AIR)) {
                    changedBlocks.put(targetLoc, targetLoc.getBlock().getType());
                }
                // Track that it was placed and that it should be unbreakable
                placedBlocks.add(targetLoc);
                unbreakableBlocks.add(targetLoc);
                // Set the block to stone
                targetLoc.getBlock().setType(Material.STONE);
                // Play the stone place sound
                world.playSound(targetLoc, Sound.BLOCK_STONE_PLACE, 1f, 1.0f);
            }
        }

        // Run this code for every block in the height
        for (int y = 0; y <= height; y++) {
            // Set a non-changing variable
            int finalY = y;
            // Run this code later, the amount of ticks is equal to the current iteration of the loop
            Bukkit.getScheduler().scheduleSyncDelayedTask(DragonSMP.getInstance(), new Runnable() {
                @Override
                public void run() {
                    // Go through all x and z coordinates (basically every block in a square range)
                    for (int x = -range; x <= range; x++) {
                        for (int z = -range; z <= range; z++) {
                            // Check if we're on the edge of the square
                            if (x == -range || z == -range || x == range || z == range) {
                                // If so, get the location we're changing
                                // Get the target location
                                Location targetLoc =  initialLocation.clone().add(x, finalY, z);
                                // If it's not air, add it to the changed blocks list
                                if (!targetLoc.getBlock().getType().equals(Material.AIR)) {
                                    changedBlocks.put(targetLoc, targetLoc.getBlock().getType());
                                }
                                // Track that it was placed and that it should be unbreakable
                                placedBlocks.add(targetLoc);
                                unbreakableBlocks.add(targetLoc);
                                // Set the block to stone
                                targetLoc.getBlock().setType(Material.STONE);
                                // Play the stone place sound
                                world.playSound(targetLoc, Sound.BLOCK_STONE_PLACE, 1f, 1.0f);
                            }
                        }
                    }
                }
            }, y);
        }

        // Run this later, height + 1 ticks in the future
        Bukkit.getScheduler().scheduleSyncDelayedTask(DragonSMP.getInstance(), new Runnable() {
            @Override
            public void run() {
                // Go through all x and z coordinates (basically every block in a square range) -- this is for the floor of the box
                for (int x = -range; x <= range; x++) {
                    for (int z = -range; z <= range; z++) {
                        // Check if we're on the edge of the square (make sure we're not
                        if (x == -range || z == -range || x == range || z == range) continue;
                        // Get the target location
                        Location targetLoc =  initialLocation.clone().add(x, height, z);
                        // If it's not air, add it to the changed blocks list
                        if (!targetLoc.getBlock().getType().equals(Material.AIR)) {
                            changedBlocks.put(targetLoc, targetLoc.getBlock().getType());
                        }
                        // Track that it was placed and that it should be unbreakable
                        placedBlocks.add(targetLoc);
                        unbreakableBlocks.add(targetLoc);
                        // Set the block to stone
                        targetLoc.getBlock().setType(Material.STONE);
                        // Play the stone place sound
                        world.playSound(targetLoc, Sound.BLOCK_STONE_PLACE, 1f, 1.0f);
                    }
                }
            }
        }, height+1);

        // Run this later, 200 ticks (10 seconds) in the future
        Bukkit.getScheduler().scheduleSyncDelayedTask(DragonSMP.getInstance(), new Runnable() {
            @Override
            public void run() {
                // Go through all of the placed blocks
                for (Location loc : placedBlocks) {
                    // Prevent errors from stopping the loop with a try-catch
                    try {
                        // Set the block to air and play a stone break sound
                        loc.getBlock().setType(Material.AIR);
                        world.playSound(loc, Sound.BLOCK_STONE_BREAK, 0.5f, 1.0f);
                        // 1 tick later, make it breakable again
                        Bukkit.getScheduler().scheduleSyncDelayedTask(DragonSMP.getInstance(), new Runnable() {
                            @Override
                            public void run() {
                                try {unbreakableBlocks.remove(loc);}
                                catch (Exception e) {}
                            }
                        }, 1L);
                    } catch (Exception e) {}
                }
                // Go through all the changed blocks and attempt to set it back to its original state
                for (Location loc : changedBlocks.keySet()) {
                    try {
                        loc.getBlock().setType(changedBlocks.get(loc));
                    }
                    catch (Exception e) {}
                }
            }
        }, 10*20L);
    }

    @Override
    public String getId() {
        return "earth";
    }
}
