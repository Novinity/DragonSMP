package com.novinitygames.dragonSMP.commands.subcommands;

import com.novinitygames.dragonSMP.DragonSMP;
import com.novinitygames.dragonSMP.commands.SubCommand;
import com.novinitygames.dragonSMP.dragons.Dragon;
import com.novinitygames.dragonSMP.dragons.DragonUser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ResetDragonSC extends SubCommand {
    @Override
    public String getName() {
        return "resetdragon";
    }

    @Override
    public String getDescription() {
        return "Reroll a player's dragon";
    }

    @Override
    public String getSyntax() {
        return "/dragonsmp resetdragon <player>";
    }

    @Override
    public String getRequiredPermission() {
        return "dragonsmp.reset";
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

        if (DragonSMP.getInstance().dragonUsers.containsKey(player)) {
            DragonSMP.getInstance().dragonUsers.get(player).dragon.Uninit();
        }

        DragonSMP.getInstance().dragonUsers.remove(player);
        DragonSMP.getInstance().getPlayerDataConfig().set(player.getUniqueId().toString() + ".dragon", null);
        DragonSMP.getInstance().savePlayerDataConfig();
        DragonUser.AssignDragon(player);

        sender.sendMessage(ChatColor.GREEN + "Set " + player.getName() + "'s dragon to a random dragon!");
    }
}
