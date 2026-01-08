package com.novinitygames.dragonSMP.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

import java.util.ArrayList;

public class ItemUtils {
    public static ItemStack getSwapItem(int amt) {
        ItemStack stack = new ItemStack(Material.POPPED_CHORUS_FRUIT, amt);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                "&4Dragon Swap"));
        meta.setLore(new ArrayList<>() {{
            add(ChatColor.translateAlternateColorCodes('&', "&cReroll your dragon."));
        }});

        CustomModelDataComponent modelDataComponent = meta.getCustomModelDataComponent();
        modelDataComponent.setStrings(new ArrayList<>() {{add("dragon_swap");}});
        meta.setCustomModelDataComponent(modelDataComponent);

        stack.setItemMeta(meta);
        return stack;
    }

    public static boolean isDragonSwap(ItemStack stack) {
        if (stack.getItemMeta().hasCustomModelDataComponent()) {
            if (stack.getItemMeta().getCustomModelDataComponent().getStrings().contains("dragon_swap")) {
                return true;
            }
        }
        return false;
    }
}
