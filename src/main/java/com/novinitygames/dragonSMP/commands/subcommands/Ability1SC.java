package com.novinitygames.dragonSMP.commands.subcommands;

import com.novinitygames.dragonSMP.DragonSMP;
import com.novinitygames.dragonSMP.commands.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Ability1SC extends SubCommand {

    @Override
    public String getName() {
        return "ability1";
    }

    @Override
    public String getDescription() {
        return "Use ability 1 of your dragon.";
    }

    @Override
    public String getSyntax() {
        return "/dragonsmp ability1";
    }

    @Override
    public String getRequiredPermission() {
        return "";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            DragonSMP.getInstance().dragonUsers.get(player).UseAbility1();
        } else {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
        }
    }
}
