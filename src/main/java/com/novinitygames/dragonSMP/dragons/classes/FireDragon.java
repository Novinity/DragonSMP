package com.novinitygames.dragonSMP.dragons.classes;

import com.novinitygames.dragonSMP.DragonSMP;
import com.novinitygames.dragonSMP.dragons.Dragon;
import com.novinitygames.dragonSMP.dragons.DragonUser;
import org.bukkit.*;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class FireDragon extends Dragon {
    int fireRes = 0;

    public void Init(DragonUser dragonUser) {
        this.dragonUser = dragonUser;
        ability1Cooldown = 120;

        fireRes = Bukkit.getScheduler().runTaskTimer(DragonSMP.getInstance(), new Runnable() {

            @Override
            public void run() {
                dragonUser.player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20, 0, true, false));
            }
        }, 0L, 20L).getTaskId();
    }

    @Override
    public void Uninit() {
        Bukkit.getScheduler().cancelTask(fireRes);
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
        Location initialLocation = player.getEyeLocation();
        World world = player.getWorld();

        Fireball fireball = player.launchProjectile(Fireball.class, initialLocation.getDirection());
        fireball.setCustomName(ChatColor.RED + "AbilityFireball");
        fireball.setCustomNameVisible(false);

        world.playSound(player.getLocation(), Sound.ITEM_FIRECHARGE_USE, 1, 1);
    }

    @Override
    public String getId() {
        return "fire";
    }
}
