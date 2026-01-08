package com.novinitygames.dragonSMP.commands.subcommands;

import com.novinitygames.dragonSMP.DragonSMP;
import com.novinitygames.dragonSMP.commands.SubCommand;
import com.novinitygames.dragonSMP.dragons.DragonUser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class RerollAllSC extends SubCommand {
    @Override
    public String getName() {
        return "rerollall";
    }

    @Override
    public String getDescription() {
        return "Reroll all player dragons";
    }

    @Override
    public String getSyntax() {
        return "/dragonsmp rerollall";
    }

    @Override
    public String getRequiredPermission() {
        return "dragonsmp.rerollall";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        for (DragonUser dragonUser : DragonSMP.getInstance().dragonUsers.values()) {
            dragonUser.dragon.Uninit();
        }
        DragonSMP.getInstance().dragonUsers.clear();

        ConfigurationSection section = DragonSMP.getInstance().getPlayerDataConfig().getConfigurationSection("");
        for (String key : section.getKeys(false)) {
            DragonSMP.getInstance().getPlayerDataConfig().set(key, null);
        }
        DragonSMP.getInstance().savePlayerDataConfig();

        for (Player player : Bukkit.getOnlinePlayers()) {
            DragonUser.AssignDragon(player);
        }

        sender.sendMessage(ChatColor.GREEN + "Rerolled all dragons!");
    }
}
