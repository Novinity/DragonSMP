package com.novinitygames.dragonSMP.commands.subcommands;

import com.novinitygames.dragonSMP.DragonSMP;
import com.novinitygames.dragonSMP.commands.SubCommand;
import com.novinitygames.dragonSMP.dragons.Dragon;
import com.novinitygames.dragonSMP.dragons.DragonUser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CheckDragonSC extends SubCommand {
    @Override
    public String getName() {
        return "checkdragon";
    }

    @Override
    public String getDescription() {
        return "See what dragon you have";
    }

    @Override
    public String getSyntax() {
        return "/dragonsmp checkdragon [<player>]";
    }

    @Override
    public String getRequiredPermission() {
        return "";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length < 2) {
            if (sender instanceof Player player) {
                DragonUser dragonUser = DragonSMP.getInstance().dragonUsers.get(player);
                if (dragonUser == null) {
                    player.kickPlayer("Failed to initialize.");
                    return;
                }

                sender.sendMessage(ChatColor.GREEN + "You are assigned to the " + ChatColor.GOLD + dragonUser.dragon.getId() + ChatColor.GREEN + " dragon.");
            } else {
                sender.sendMessage(ChatColor.RED + "Player name required.");
                return;
            }
        } else {
            if (!sender.hasPermission("dragonsmp.checkothers")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
                return;
            }

            String playerName = args[1];
            Player player = Bukkit.getPlayer(playerName);
            if (player == null || !player.isOnline()) {
                sender.sendMessage(ChatColor.RED + "Player not online!");
                return;
            }

            DragonUser dragonUser = DragonSMP.getInstance().dragonUsers.get(player);
            if (dragonUser == null) {
                sender.sendMessage(ChatColor.RED + "Player is not initialized.");
                return;
            }
            sender.sendMessage(ChatColor.GREEN + player.getName() + " is assigned to the " + ChatColor.GOLD + dragonUser.dragon.getId() + ChatColor.GREEN + " dragon.");

        }
    }
}
