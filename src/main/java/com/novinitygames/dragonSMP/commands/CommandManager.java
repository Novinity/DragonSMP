package com.novinitygames.dragonSMP.commands;

import com.novinitygames.dragonSMP.DragonSMP;
import com.novinitygames.dragonSMP.Properties;
import com.novinitygames.dragonSMP.commands.subcommands.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class CommandManager implements TabCompleter, CommandExecutor {

    public ArrayList<SubCommand> subCommands = new ArrayList<>();

    public CommandManager() {
        subCommands.add(new ReloadSC());
        subCommands.add(new Ability1SC());
        subCommands.add(new SetDragonSC());
        subCommands.add(new ClearCooldownsSC());
        subCommands.add(new CheckDragonSC());
        subCommands.add(new ResetDragonSC());
        subCommands.add(new RerollAllSC());
        subCommands.add(new GiveItemSC());
    }

    public ArrayList<SubCommand> getSubCommands() {
        return subCommands;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 1) {
            ArrayList<String> possibilities = new ArrayList<String>() {{
                for (SubCommand subCommand : subCommands) {
                    if (subCommand.getName().startsWith(args[0].toLowerCase())
                            && (subCommand.getRequiredPermission().isEmpty() || sender.hasPermission(subCommand.getRequiredPermission()))) {
                        add(subCommand.getName());
                    }
                }
            }};
            return new ArrayList<String>() {{
                for (String possibility : possibilities) {
                    if (possibility.startsWith(args[0].toLowerCase())) {
                        add(possibility);
                    }
                }
            }};
        } else if (args.length == 2) {
            if (args[0].toLowerCase().equalsIgnoreCase("giveitem")) {
                return new ArrayList<>() {{
                    add("dragonswap");
                }};
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("setdragon")) {
                return new ArrayList<String>() {{
                    add("fire");
                    add("water");
                    add("ice");
                    add("wind");
                    add("earth");
                    add("life");
                }};
            }
        }

        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length > 0) {
            for (int i = 0; i < getSubCommands().size(); i++) {
                if (args[0].equalsIgnoreCase(getSubCommands().get(i).getName())) {
                    if (getSubCommands().get(i).getRequiredPermission().isEmpty() || sender.hasPermission(getSubCommands().get(i).getRequiredPermission())) {
                        getSubCommands().get(i).perform(sender, args);
                    } else {
                        sender.sendMessage(ChatColor.RED + "Command does not exist!");
                    }
                }
            }
        } else if (args.length == 0) {
            sender.sendMessage(DragonSMP.PREFIX + ChatColor.translateAlternateColorCodes('&',
                    "&aVersion " + Properties.getProperty("version")));
        }

        return true;
    }
}
