package com.novinitygames.dragonSMP.commands.subcommands;

import com.novinitygames.dragonSMP.DragonSMP;
import com.novinitygames.dragonSMP.commands.SubCommand;
import com.novinitygames.dragonSMP.dragons.Dragon;
import com.novinitygames.dragonSMP.dragons.DragonUser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetDragonSC extends SubCommand {
    @Override
    public String getName() {
        return "setdragon";
    }

    @Override
    public String getDescription() {
        return "Set the dragon of the given player";
    }

    @Override
    public String getSyntax() {
        return "/dragonsmp setdragon <player> <dragon>";
    }

    @Override
    public String getRequiredPermission() {
        return "dragonsmp.set";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Player name required.");
            return;
        }
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Dragon id required.");
            return;
        }

        String playerName = args[1];
        String dragonId = args[2].toLowerCase();

        Player player = Bukkit.getPlayer(playerName);
        if (player == null || !player.isOnline()) {
            sender.sendMessage(ChatColor.RED + "Player not online!");
            return;
        }

        if (DragonSMP.getInstance().dragonUsers.containsKey(player) && DragonSMP.getInstance().dragonUsers.get(player).dragon.getId().equals(dragonId)) {
            sender.sendMessage(ChatColor.RED + "Player already assigned to this dragon!");
            return;
        }

        Dragon newDragon = Dragon.getDragonById(dragonId);
        if (DragonSMP.getInstance().dragonUsers.containsKey(player)) {
            DragonSMP.getInstance().dragonUsers.get(player).dragon.Uninit();
        }

        DragonUser dragonUser = new DragonUser(player, newDragon);
        dragonUser.dragon.Init(dragonUser);
        DragonSMP.getInstance().dragonUsers.put(player, dragonUser);

        DragonSMP.getInstance().getPlayerDataConfig().set(player.getUniqueId().toString() + ".dragon", dragonId);
        DragonSMP.getInstance().savePlayerDataConfig();

        sender.sendMessage(ChatColor.GREEN + "Set " + player.getName() + "'s dragon to " + newDragon.getId() + ".");
    }
}
