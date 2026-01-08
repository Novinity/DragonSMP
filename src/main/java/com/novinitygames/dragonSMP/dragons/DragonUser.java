package com.novinitygames.dragonSMP.dragons;

import com.novinitygames.dragonSMP.DragonSMP;
import com.novinitygames.dragonSMP.dragons.classes.FireDragon;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class DragonUser {
    public Player player;
    public Dragon dragon;

    public HashMap<Integer, Long> cooldowns = new HashMap<>();

    public boolean frozen = false;

    public DragonUser(Player player, Dragon dragon) {
        this.player = player;
        this.dragon = dragon;
    }

    public void UseAbility1()
    {
        dragon.UseAbility1();
    }

    public boolean getCanTakeFallDamage() {
        return dragon.fallDamageEnabled;
    }

    public static void AssignDragon(Player player) {
        String existingDragon = DragonSMP.getInstance().getPlayerDataConfig().getString(player.getUniqueId().toString() + ".dragon", null);
        Dragon dragon = null;

        if (existingDragon != null) {

            dragon = Dragon.getDragonById(existingDragon);

            DragonUser dragonUser = new DragonUser(player, dragon);
            dragonUser.dragon.Init(dragonUser);
            DragonSMP.getInstance().dragonUsers.put(player, dragonUser);

            player.sendMessage(ChatColor.GREEN + "Welcome back, " + ChatColor.GOLD + dragon.getId() + ChatColor.GREEN + " user.");

        } else {

            dragon = Dragon.getRandomDragon();
            if (dragon == null) {
                dragon = new FireDragon();
            }

            DragonUser dragonUser = new DragonUser(player, dragon);
            dragonUser.dragon.Init(dragonUser);
            DragonSMP.getInstance().dragonUsers.put(player, dragonUser);

            DragonSMP.getInstance().getPlayerDataConfig().set(player.getUniqueId().toString() + ".dragon", dragon.getId());
            DragonSMP.getInstance().savePlayerDataConfig();

            player.sendMessage(ChatColor.GREEN + "You were assigned to the " + ChatColor.GOLD + dragon.getId() + ChatColor.GREEN + " dragon.");

        }
    }
}
