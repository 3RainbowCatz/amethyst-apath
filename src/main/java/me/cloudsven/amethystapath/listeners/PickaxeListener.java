package me.cloudsven.amethystapath.listeners;

import me.cloudsven.amethystapath.AmethystApath;
import me.cloudsven.amethystapath.items.CustomItem;
import me.cloudsven.amethystapath.items.ItemRegistry;
import me.cloudsven.amethystapath.util.CooldownUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PickaxeListener implements Listener {
    private final AmethystApath plugin;
    private static final CustomItem item = CustomItem.SCARLET_PICKAXE;

    private static final List<Material> breakable = List.of(
            Material.BEDROCK,
            Material.END_PORTAL_FRAME,
            Material.REINFORCED_DEEPSLATE,
            Material.TRIAL_SPAWNER,
            Material.VAULT
    );

    public PickaxeListener(AmethystApath plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPickaxeInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!ItemRegistry.is(event.getItem(), item)) return;

        Action action = event.getAction();

        if (action == Action.LEFT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            if (block != null && breakable.contains(block.getType())) {
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    block.setType(Material.AIR);
                }, 20 * 5L);
            }
        } else if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            String abilityKey = "scarlet_pickaxe_haste";

            if (CooldownUtil.isOnCooldown(player.getUniqueId(), abilityKey)) {
                long secondsLeft = CooldownUtil.getRemainingTime(player.getUniqueId(), abilityKey);
                player.sendMessage(ChatColor.RED + "Wait " + secondsLeft + "s before using this again!");
                return;
            }

            player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 1200, 1));
            player.sendMessage(ChatColor.GOLD + "You feel the rush of the Scarlet Pickaxe!");

            CooldownUtil.setCooldown(player.getUniqueId(), abilityKey, item.cooldown);
        }
    }
}