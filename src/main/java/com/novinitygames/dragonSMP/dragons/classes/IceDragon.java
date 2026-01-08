package com.novinitygames.dragonSMP.dragons.classes;

import com.novinitygames.dragonSMP.DragonSMP;
import com.novinitygames.dragonSMP.dragons.Dragon;
import com.novinitygames.dragonSMP.dragons.DragonUser;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class IceDragon extends Dragon {
    public void Init(DragonUser dragonUser) {
        this.dragonUser = dragonUser;
        ability1Cooldown = 150;
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
        world.playSound(player.getLocation(), Sound.ITEM_FIRECHARGE_USE, 0.5f, 1.0f);

        int range = 4;
        for (int i = 0; i <= range; i++) {
            Location checkLoc = eyeLoc.clone().add(eyeLoc.getDirection().clone().multiply(i));
            world.spawnParticle(Particle.CLOUD, checkLoc, 5, 0, 0, 0);

            for (Entity entity : world.getNearbyEntities(checkLoc, 0.5, 0.5, 0.5)) {
                if (entity instanceof Player t && entity != player) {
                    DragonUser dragonUser1 = DragonSMP.getInstance().dragonUsers.get(t);
                    if (dragonUser1 == null) continue;

                    target = dragonUser1;
                    dragonUser1.frozen = true;
                    world.playSound(t.getLocation(), Sound.ENTITY_CREAKING_FREEZE, 1, 1);

                    break;
                }
            }
        }

        if (target != null) {
            DragonUser finalTarget = target;
            Bukkit.getScheduler().scheduleSyncDelayedTask(DragonSMP.getInstance(), new Runnable() {
                @Override
                public void run() {
                    finalTarget.frozen = false;
                    world.playSound(finalTarget.player.getLocation(), Sound.BLOCK_GLASS_BREAK, 1, 1);
                }
            }, 7*20L);
        }
    }

    @Override
    public String getId() {
        return "ice";
    }
}
