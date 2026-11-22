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
    int rejuvination = 0;
    public static ArrayList<Location> unbreakableBlocks = new ArrayList<>();
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

    public void Init(DragonUser dragonUser) {
        this.dragonUser = dragonUser;
        ability1Cooldown = 300;

        rejuvination = Bukkit.getScheduler().runTaskTimer(DragonSMP.getInstance(), new Runnable() {

            @Override
            public void run() {
                Location blockStandingOn = dragonUser.player.getLocation().clone().subtract(0, 0.1, 0);
                Material blockMaterial = blockStandingOn.getBlock().getType();

                if (rejuvinatingBlocks.contains(blockMaterial)) {
                    dragonUser.player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20, 0, true, false));
                }
            }
        }, 0L, 20L).getTaskId();
    }

    @Override
    public void Uninit() {
        Bukkit.getScheduler().cancelTask(rejuvination);
    }

    @Override
    public void UseAbility1() {
        int cdDiff = getAbility1Cooldown(dragonUser);
        if (cdDiff > 0) {
            dragonUser.player.sendMessage(ChatColor.RED + "This is on cooldown for another " + cdDiff + " seconds!");
            return;
        }
        dragonUser.cooldowns.put(1,System.currentTimeMillis());

        Player player = dragonUser.player;
        World world = player.getWorld();
        Location initialLocation = player.getLocation().clone();

        world.playSound(player.getLocation(), Sound.ITEM_FIRECHARGE_USE, 0.5f, 1.0f);

        int range = 4;
        int height = 4;
        HashMap<Location, Material> changedBlocks = new HashMap<>();
        ArrayList<Location> placedBlocks = new ArrayList<>();

        for (int x = -range; x <= range; x++) {
            for (int z = -range; z <= range; z++) {
                if (x == -range || z == -range || x == range || z == range) continue;
                Location targetLoc =  initialLocation.clone().add(x, -1, z);
                if (!targetLoc.getBlock().getType().equals(Material.AIR)) {
                    changedBlocks.put(targetLoc, targetLoc.getBlock().getType());
                }
                placedBlocks.add(targetLoc);
                unbreakableBlocks.add(targetLoc);
                targetLoc.getBlock().setType(Material.STONE);
                world.playSound(targetLoc, Sound.BLOCK_STONE_PLACE, 1f, 1.0f);
            }
        }

        for (int y = 0; y <= height; y++) {
            int finalY = y;
            Bukkit.getScheduler().scheduleSyncDelayedTask(DragonSMP.getInstance(), new Runnable() {
                @Override
                public void run() {
                    for (int x = -range; x <= range; x++) {
                        for (int z = -range; z <= range; z++) {
                            if (x == -range || z == -range || x == range || z == range) {
                                Location targetLoc =  initialLocation.clone().add(x, finalY, z);
                                if (!targetLoc.getBlock().getType().equals(Material.AIR)) {
                                    changedBlocks.put(targetLoc, targetLoc.getBlock().getType());
                                }
                                placedBlocks.add(targetLoc);
                                unbreakableBlocks.add(targetLoc);
                                targetLoc.getBlock().setType(Material.STONE);
                                world.playSound(targetLoc, Sound.BLOCK_STONE_PLACE, 1f, 1.0f);
                            }
                        }
                    }
                }
            }, y);
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(DragonSMP.getInstance(), new Runnable() {
            @Override
            public void run() {
                for (int x = -range; x <= range; x++) {
                    for (int z = -range; z <= range; z++) {
                        if (x == -range || z == -range || x == range || z == range) continue;
                        Location targetLoc =  initialLocation.clone().add(x, height, z);
                        if (!targetLoc.getBlock().getType().equals(Material.AIR)) {
                            changedBlocks.put(targetLoc, targetLoc.getBlock().getType());
                        }
                        placedBlocks.add(targetLoc);
                        unbreakableBlocks.add(targetLoc);
                        targetLoc.getBlock().setType(Material.STONE);
                        world.playSound(targetLoc, Sound.BLOCK_STONE_PLACE, 1f, 1.0f);
                    }
                }
            }
        }, height+1);

        Bukkit.getScheduler().scheduleSyncDelayedTask(DragonSMP.getInstance(), new Runnable() {
            @Override
            public void run() {
                for (Location loc : placedBlocks) {
                    loc.getBlock().setType(Material.AIR);
                    world.playSound(loc, Sound.BLOCK_STONE_BREAK, 0.5f, 1.0f);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(DragonSMP.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            unbreakableBlocks.remove(loc);
                        }
                    }, 1L);
                }
                for (Location loc : changedBlocks.keySet()) {
                    loc.getBlock().setType(changedBlocks.get(loc));
                }
            }
        }, 10*20L);
    }

    @Override
    public String getId() {
        return "earth";
    }
}
