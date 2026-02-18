package me.cloudsven.amethystapath.listeners;

import me.cloudsven.amethystapath.AmethystApath;
import me.cloudsven.amethystapath.items.CustomItem;
import me.cloudsven.amethystapath.items.ItemRegistry;
import me.cloudsven.amethystapath.util.CooldownUtil;
import me.cloudsven.amethystapath.util.DamageUtil;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.Map;

public class MaceListener implements Listener {
    private final AmethystApath plugin;

    private static final CustomItem mace = CustomItem.SCARLET_MACE;
    private final String MACE_BOOST_TAG = "scarlet_mace_boosted";
    private static final NamespacedKey RUPTURE_KEY =
            new NamespacedKey("amethystapath", "rupture");

    public static int getRuptureLevel(ItemStack item) {
        if (item == null) return 0;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return 0;

        for (Map.Entry<Enchantment, Integer> entry : meta.getEnchants().entrySet()) {
            if (entry.getKey().getKey().equals(RUPTURE_KEY)) {
                return entry.getValue();
            }
        }
        return 0;
    }

    private double getArmorDamagePercent(ItemStack mace) {
        int level = getRuptureLevel(mace);

        return (level * 0.025) + 0.5;
    }

    public MaceListener(AmethystApath plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        if (!(event.getEntity() instanceof LivingEntity target)) return;

        ItemStack item = player.getInventory().getItemInMainHand();
        if (!ItemRegistry.is(item, CustomItem.SCARLET_MACE)) return;

        double percent = getArmorDamagePercent(item);

        DamageUtil.damageArmor(target, percent);
        DamageUtil.applyTrueDamage(target, 2);
        DamageUtil.applyWithering(target, 60, 1);
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        // check if item is mace and action is right click
        if (!ItemRegistry.is(item, mace)) return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        String abilityKey = "scarlet_mace_launch";

        // check cooldown (i.e. 8 seconds)
        if (CooldownUtil.isOnCooldown(player.getUniqueId(), abilityKey)) {
            long secondsLeft = CooldownUtil.getRemainingTime(player.getUniqueId(), abilityKey);
            player.sendMessage(ChatColor.RED + "Wait " + secondsLeft + "s before using this again!");
            return;
        }

        // apply velocity boost
        Vector boost = player.getLocation().getDirection().multiply(1.5).setY(1.0);
        player.setVelocity(boost);

        // tag player for fall damage immunity
        player.setMetadata(MACE_BOOST_TAG, new FixedMetadataValue(plugin, true));

        // play wind charge sound
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WIND_CHARGE_THROW, 1.0f, 1.0f);
        player.sendMessage(ChatColor.GOLD + "You feel the launch of the Scarlet Mace!");

        // set cooldown (i.e. 8 seconds)
        CooldownUtil.setCooldown(player.getUniqueId(), "mace_launch", mace.cooldown);
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        // check if player is falling and has the boost tag
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            if (player.hasMetadata(MACE_BOOST_TAG)) {
                event.setCancelled(true);
                // remove tag after landing
                player.removeMetadata(MACE_BOOST_TAG, plugin);
            }
        }
    }
}