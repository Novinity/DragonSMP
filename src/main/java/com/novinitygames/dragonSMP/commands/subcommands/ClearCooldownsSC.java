package com.novinitygames.dragonSMP.commands.subcommands;

import com.novinitygames.dragonSMP.DragonSMP;
import com.novinitygames.dragonSMP.commands.SubCommand;
import com.novinitygames.dragonSMP.dragons.Dragon;
import com.novinitygames.dragonSMP.dragons.DragonUser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearCooldownsSC extends SubCommand {

    @Override
    public String getName() {
        return "clearcooldowns";
    }

    @Override
    public String getDescription() {
        return "Clear a player's cooldowns";
    }

    @Override
    public String getSyntax() {
        return "/dragonsmp clearcooldowns <player>";
    }

    @Override
    public String getRequiredPermission() {
        return "dragonsmp.clearcooldowns";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Player name required.");
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

        dragonUser.cooldowns.clear();
        sender.sendMessage(ChatColor.GREEN + "Cleared ability cooldowns of " + player.getName() + "!");
    }
}
