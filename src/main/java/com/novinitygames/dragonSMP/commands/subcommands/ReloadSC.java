package com.novinitygames.dragonSMP.commands.subcommands;

import com.novinitygames.dragonSMP.DragonSMP;
import com.novinitygames.dragonSMP.commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadSC extends SubCommand {
    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "Reload the config";
    }

    @Override
    public String getSyntax() {
        return "/dragonsmp reload";
    }

    @Override
    public String getRequiredPermission() {
        return "dragonsmp.reload";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        DragonSMP.getInstance().reloadConfig();
        DragonSMP.getInstance().reloadConfig();
        for (Player plr : Bukkit.getOnlinePlayers()) {
            plr.kickPlayer(ChatColor.RED + "Reloading config. Please rejoin.");
        }
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', DragonSMP.PREFIX + "&aReloaded config!"));
    }
}
