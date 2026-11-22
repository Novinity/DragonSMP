package com.novinitygames.dragonSMP.listeners;

import com.novinitygames.dragonSMP.DragonSMP;
import com.novinitygames.dragonSMP.dragons.DragonUser;
import com.novinitygames.dragonSMP.dragons.classes.EarthDragon;
import com.novinitygames.dragonSMP.dragons.classes.IceDragon;
import com.novinitygames.dragonSMP.dragons.classes.LifeDragon;
import com.novinitygames.dragonSMP.utils.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.*;

public class PlayerListeners implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        DragonUser.AssignDragon(player);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        DragonSMP.getInstance().dragonUsers.remove(player);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            DragonUser dragonUser = DragonSMP.getInstance().dragonUsers.get(player);
            if (dragonUser != null) {
                if (event.getDamageSource().getDamageType() == DamageType.FALL && !dragonUser.getCanTakeFallDamage()) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            if (event.getDamager() instanceof Fireball fireball && fireball.hasMetadata("ability")) {
                event.setDamage(6.0);
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Fireball fireball)) return;

        if (!fireball.hasMetadata("ability")) return;

        if (event.getHitEntity() instanceof Player player) {
            player.damage(6.0, fireball.getShooter() instanceof Entity e ? e : null);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (DragonSMP.getInstance().dragonUsers.containsKey(player) &&
                DragonSMP.getInstance().dragonUsers.get(player).dragon.getClass() == IceDragon.class) {
            Location loc = player.getWorld().getHighestBlockAt(player.getLocation()).getLocation();
            if ((loc.getBlock().getType() == Material.ICE || loc.getBlock().getType() == Material.PACKED_ICE || loc.getBlock().getType() == Material.BLUE_ICE)) {
                player.setWalkSpeed(0.5f);
            } else {
                player.setWalkSpeed(0.2f);
            }
        }

        if (DragonSMP.getInstance().dragonUsers.containsKey(player) && DragonSMP.getInstance().dragonUsers.get(player).frozen) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerEat(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        DragonUser dragonUser = DragonSMP.getInstance().dragonUsers.get(player);
        if (dragonUser != null) {
            if (dragonUser.dragon.getClass() == LifeDragon.class) {
                player.setSaturation(player.getSaturation() + 10);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player =  event.getPlayer();
        if (player.getInventory().getItemInMainHand().getType() == Material.AIR) return;

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            if (ItemUtils.isDragonSwap(event.getPlayer().getInventory().getItemInMainHand())) {
                if (DragonSMP.getInstance().dragonUsers.containsKey(player)) {
                    DragonSMP.getInstance().dragonUsers.get(player).dragon.Uninit();
                }
                DragonSMP.getInstance().dragonUsers.remove(player);

                DragonSMP.getInstance().getPlayerDataConfig().set(player.getUniqueId().toString() + ".dragon", null);
                DragonSMP.getInstance().savePlayerDataConfig();

                DragonUser.AssignDragon(player);

                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                player.getWorld().playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 1, 1);
                event.getPlayer().getInventory().getItemInMainHand().setAmount(event.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
            }
        }
    }

    @EventHandler
    public void onBreakBlock(BlockBreakEvent event) {
        for (Location loc : EarthDragon.unbreakableBlocks) {
            if (loc.getBlockX() == event.getBlock().getLocation().getBlockX() &&
                    loc.getBlockZ() == event.getBlock().getLocation().getBlockZ() &&
                    loc.getBlockY() == event.getBlock().getLocation().getBlockY()) {
                event.setCancelled(true);
                break;
            }
        }
    }
}
