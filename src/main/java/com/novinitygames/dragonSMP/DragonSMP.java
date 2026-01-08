package com.novinitygames.dragonSMP;

import com.novinitygames.dragonSMP.checks.CDChecks;
import com.novinitygames.dragonSMP.commands.CommandManager;
import com.novinitygames.dragonSMP.dragons.DragonUser;
import com.novinitygames.dragonSMP.dragons.classes.WindDragon;
import com.novinitygames.dragonSMP.listeners.PlayerListeners;
import com.novinitygames.dragonSMP.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public final class DragonSMP extends JavaPlugin {

    private static DragonSMP instance;
    public static final String PREFIX = "&5[&dDragonSMP&5] ";

    private File playerDataConfigFile;
    private FileConfiguration playerDataConfig;

    private static CommandManager commandManager;

    public HashMap<Player, DragonUser> dragonUsers = new HashMap<>();

    @Override
    public void onEnable() {
        getLogger().info("Dragon SMP by Novinity.");

        instance = this;

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        createPlayerDataConfig();

        commandManager = new CommandManager();
        getCommand("dragonsmp").setExecutor(commandManager);
        getCommand("dragonsmp").setTabCompleter(commandManager);

        getServer().getPluginManager().registerEvents(new PlayerListeners(), this);

        for (Player p : Bukkit.getOnlinePlayers()) {
            DragonUser.AssignDragon(p);
        }

        CDChecks.SetupCooldownText();

        ShapedRecipe swapRecipe = new ShapedRecipe(new NamespacedKey(this, "dragon_swap_item"), ItemUtils.getSwapItem(1));
        swapRecipe.shape("AWA", "W W", "AWA");
        swapRecipe.setIngredient('A', Material.ENCHANTED_GOLDEN_APPLE);
        swapRecipe.setIngredient('W', Material.WITHER_SKELETON_SKULL);

        getServer().addRecipe(swapRecipe);
    }

    public FileConfiguration getPlayerDataConfig() {
        return this.playerDataConfig;
    }

    private void createPlayerDataConfig() {
        playerDataConfigFile = new File(getDataFolder(), "playerData.yml");
        if (!playerDataConfigFile.exists()) {
            playerDataConfigFile.getParentFile().mkdirs();
            saveResource("playerData.yml", false);
        }

        playerDataConfig = YamlConfiguration.loadConfiguration(playerDataConfigFile);
    }

    public void savePlayerDataConfig() {
        try {
            playerDataConfig.save(playerDataConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadPlayerDataConfig() {
        createPlayerDataConfig();
    }

    public static DragonSMP getInstance() {
        return instance;
    }
}
