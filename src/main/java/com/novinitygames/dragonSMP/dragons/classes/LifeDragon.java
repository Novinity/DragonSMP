package com.novinitygames.dragonSMP.dragons.classes;

import com.novinitygames.dragonSMP.DragonSMP;
import com.novinitygames.dragonSMP.dragons.Dragon;
import com.novinitygames.dragonSMP.dragons.DragonUser;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Random;

public class LifeDragon extends Dragon {
    public void Init(DragonUser dragonUser) {
        this.dragonUser = dragonUser;
        ability1Cooldown = 240;
    }

    @Override
    public void Uninit() {

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
        Location eyeLoc = player.getEyeLocation();

        DragonUser target = null;
        world.playSound(player.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 0.75f, 1.0f);

        int range = 6;
        boolean foundTarget = false;
        for (int i = 0; i <= range; i++) {
            Location checkLoc = eyeLoc.clone().add(eyeLoc.getDirection().clone().multiply(i));
            world.spawnParticle(Particle.HEART, checkLoc, 3, 0, 0, 0);

            if (!foundTarget) {
                for (Entity entity : world.getNearbyEntities(checkLoc, 0.5, 0.5, 0.5)) {
                    if (entity instanceof Player t && entity != player) {
                        DragonUser dragonUser1 = DragonSMP.getInstance().dragonUsers.get(t);
                        if (dragonUser1 == null) continue;

                        target = dragonUser1;

                        Random rand = new Random();
                        t.setMaxHealth(t.getMaxHealth() - (rand.nextInt(3, 6) * 2));
                        t.damage(1);

                        foundTarget = true;
                    }
                }
            }
        }

        if (target != null) {
            DragonUser finalTarget = target;
            Bukkit.getScheduler().scheduleSyncDelayedTask(DragonSMP.getInstance(), new Runnable() {
                @Override
                public void run() {
                    finalTarget.player.setMaxHealth(20);
                }
            }, 10*20L);
        }
    }

    @Override
    public String getId() {
        return "life";
    }
}
