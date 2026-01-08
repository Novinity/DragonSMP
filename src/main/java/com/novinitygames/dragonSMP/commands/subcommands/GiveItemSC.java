package com.novinitygames.dragonSMP.commands.subcommands;

import com.novinitygames.dragonSMP.commands.SubCommand;
import com.novinitygames.dragonSMP.utils.ItemUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveItemSC extends SubCommand {
    @Override
    public String getName() {
        return "giveitem";
    }

    @Override
    public String getDescription() {
        return "Give yourself a custom item from the Dragon SMP plugin";
    }

    @Override
    public String getSyntax() {
        return "/dragonsmp giveitem <item> [<amount>]";
    }

    @Override
    public String getRequiredPermission() {
        return "dragonsmp.giveitem";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (sender instanceof Player p) {
            int amount = 1;
            if (args.length >= 3) {
                String amountStr = args[2];
                try {
                    amount = Integer.parseInt(amountStr);
                } catch (NumberFormatException e) {}
            }

            switch (args[1].toLowerCase()) {
                case "dragonswap":
                    p.getInventory().addItem(ItemUtils.getSwapItem(amount));
                    break;
            }
        }
    }
}
