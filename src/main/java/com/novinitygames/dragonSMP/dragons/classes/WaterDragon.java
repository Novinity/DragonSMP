package com.novinitygames.dragonSMP.dragons.classes;

import com.novinitygames.dragonSMP.DragonSMP;
import com.novinitygames.dragonSMP.dragons.Dragon;
import com.novinitygames.dragonSMP.dragons.DragonUser;
import org.bukkit.*;
import org.bukkit.Color;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.awt.*;

public class WaterDragon extends Dragon {
    int waterBreathing = 0;

    public void Init(DragonUser dragonUser) {
        this.dragonUser = dragonUser;
        ability1Cooldown = 120;

        waterBreathing = Bukkit.getScheduler().runTaskTimer(DragonSMP.getInstance(), new Runnable() {

            @Override
            public void run() {
                dragonUser.player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 20, 0, true, false));
            }
        }, 0L, 20L).getTaskId();
    }

    @Override
    public void Uninit() {
        Bukkit.getScheduler().cancelTask(waterBreathing);
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

        int forwardRange = 4;
        int horizontalRange = 3;
        for (int i = 0; i <= forwardRange; i++) {
            int finalI = i;
            Bukkit.getScheduler().scheduleSyncDelayedTask(DragonSMP.getInstance(), new Runnable() {
                @Override
                public void run() {
                    for (int x = -horizontalRange; x <= horizontalRange; x++) {
                        for (int z = -horizontalRange; z <= horizontalRange; z++) {
                            if (x == -horizontalRange || z == -horizontalRange || x == horizontalRange || z == horizontalRange) {
                                Location checkLoc = initialLocation.clone().add(x * finalI, 0, z * finalI);
                                world.spawnParticle(Particle.DUST, checkLoc, 1, new Particle.DustOptions(Color.BLUE, 1f));
                                world.playSound(checkLoc, Sound.ENTITY_BOAT_PADDLE_WATER, 0.5f, 1.0f);

                                for (Entity entity : world.getNearbyEntities(checkLoc, 1, 1, 1)) {
                                    if (entity instanceof Player t && entity != player) {
                                        world.playSound(t.getLocation(), Sound.ENTITY_PLAYER_SPLASH, 1, 1);
                                        t.damage(3);
                                    }
                                }
                            }
                        }
                    }
                }
            }, 5L*i);
        }
    }

    @Override
    public String getId() {
        return "water";
    }

    Vector getLeft(Location loc) {
        float yaw = loc.getYaw() + 90f;
        double yawRad = yaw * (Math.PI / 180d);
        double z = Math.cos(yawRad);
        double x = -Math.sin(yawRad);
        return new Vector(x, 0d, z);
    }
}
