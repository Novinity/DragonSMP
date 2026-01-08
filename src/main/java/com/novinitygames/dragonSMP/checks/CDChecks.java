package com.novinitygames.dragonSMP.checks;

import com.novinitygames.dragonSMP.DragonSMP;
import com.novinitygames.dragonSMP.dragons.DragonUser;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CDChecks {
    static int cooldownTextTimer;

    public static void SetupCooldownText() {
        int id = Bukkit.getScheduler().runTaskTimer(DragonSMP.getInstance(), new Runnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    DragonUser dragonUser = DragonSMP.getInstance().dragonUsers.get(player);
                    if (dragonUser == null) return;

                    int cooldown = dragonUser.dragon.getAbility1Cooldown(dragonUser);
                    if (cooldown > 0 && !dragonUser.frozen) {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy(ChatColor.translateAlternateColorCodes('&',
                                "&c[&4⏱&c] &4" + cooldown + " seconds")));
                    } else if (dragonUser.frozen) {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy(ChatColor.translateAlternateColorCodes('&',
                                "&9[&b❄&9] You are frozen!")));
                    }
                }

            }
        }, 0L, 10L).getTaskId();
        cooldownTextTimer = id;
    }

    public static void ClearCooldownTextTimer() {
        if (cooldownTextTimer == 0) return;
        Bukkit.getScheduler().cancelTask(cooldownTextTimer);
        cooldownTextTimer = 0;
    }
}
