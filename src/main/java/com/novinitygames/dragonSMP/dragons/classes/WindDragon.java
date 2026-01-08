package com.novinitygames.dragonSMP.dragons.classes;

import com.novinitygames.dragonSMP.dragons.Dragon;
import com.novinitygames.dragonSMP.dragons.DragonUser;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class WindDragon extends Dragon {
    public void Init(DragonUser dragonUser) {
        this.dragonUser = dragonUser;
        ability1Cooldown = 60;
        fallDamageEnabled = false;
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
        Location initialLocation = player.getLocation();
        World world = player.getWorld();

        dragonUser.player.setVelocity(dragonUser.player.getVelocity().add(new Vector(0, 3.5, 0)));

        world.playSound(player.getLocation(), Sound.ENTITY_BREEZE_WIND_BURST, 1.0f, 1.0f);
        world.playSound(player.getLocation(), Sound.ITEM_TRIDENT_RIPTIDE_1, 1.0f, 1.0f);
        for (int i = 1; i < 3; i++) {
            world.spawnParticle(Particle.POOF, initialLocation.add(i, 0, i), 10, 0, 0, 0);
            world.spawnParticle(Particle.POOF, initialLocation.add(-i, 0, -i), 10, 0, 0, 0);
            world.spawnParticle(Particle.POOF, initialLocation.add(-i, 0, i), 10, 0, 0, 0);
            world.spawnParticle(Particle.POOF, initialLocation.add(i, 0, -i), 10, 0, 0, 0);
        }
    }

    @Override
    public String getId() {
        return "wind";
    }
}
