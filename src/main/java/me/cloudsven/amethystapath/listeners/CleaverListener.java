package me.cloudsven.amethystapath.listeners;

import me.cloudsven.amethystapath.AmethystApath;
import me.cloudsven.amethystapath.items.CustomItem;
import me.cloudsven.amethystapath.items.ItemRegistry;
import me.cloudsven.amethystapath.util.DamageUtil;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class CleaverListener implements Listener {
    private final AmethystApath plugin;

    private ItemStack getHeadForEntity(LivingEntity victim) {
        if (victim instanceof Player player) {
            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) head.getItemMeta();
            meta.setOwningPlayer(player);
            head.setItemMeta(meta);
            return head;
        }

        return switch (victim.getType()) {
            case ZOMBIE -> new ItemStack(Material.ZOMBIE_HEAD);
            case PIGLIN, PIGLIN_BRUTE -> new ItemStack(Material.PIGLIN_HEAD);
            case SKELETON -> new ItemStack(Material.SKELETON_SKULL);
            case CREEPER -> new ItemStack(Material.CREEPER_HEAD);
            case WITHER_SKELETON -> new ItemStack(Material.WITHER_SKELETON_SKULL);
            case ENDER_DRAGON -> new ItemStack(Material.DRAGON_HEAD);
            default -> null;
        };
    }

    public CleaverListener(AmethystApath plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity victim = event.getEntity();
        Player killer = victim.getKiller();

        if (killer == null) return;

        ItemStack item = killer.getInventory().getItemInMainHand();
        if (!ItemRegistry.is(item, CustomItem.AMIRITE_CLEAVER)) return;

        ItemStack head = getHeadForEntity(victim);
        if (head == null) return;

        event.getDrops().add(head);
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || !item.hasItemMeta()) return;
        if (!ItemRegistry.is(item, CustomItem.AMIRITE_CLEAVER)) return;

        Entity target = event.getEntity();
        if (!(target instanceof LivingEntity living)) return;

        DamageUtil.applyTrueDamage(living, 2);
        living.setFireTicks(100);
    }
}
