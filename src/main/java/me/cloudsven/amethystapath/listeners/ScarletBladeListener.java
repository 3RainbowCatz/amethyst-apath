package me.cloudsven.amethystapath.listeners;

import me.cloudsven.amethystapath.AmethystApath;
import me.cloudsven.amethystapath.items.CustomItem;
import me.cloudsven.amethystapath.items.ItemRegistry;
import me.cloudsven.amethystapath.util.DamageUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ScarletBladeListener implements Listener {
    private final AmethystApath plugin;

    public ScarletBladeListener(AmethystApath plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        if (!(event.getEntity() instanceof LivingEntity target)) return;

        ItemStack item = player.getInventory().getItemInMainHand();
        if (!ItemRegistry.is(item, CustomItem.SCARLET_BLADE)) return;

        double finalDamage = event.getFinalDamage();

        DamageUtil.applyTrueDamage(target, 2);
        DamageUtil.applyWithering(target, 60, 1);

        double healAmount = finalDamage * 0.10;
        player.setHealth(Math.min(
                player.getHealth() + healAmount,
                player.getAttribute(Attribute.MAX_HEALTH).getValue()
        ));

        DamageUtil.damageArmor(target, 0.03);
        DamageUtil.damageArmor(player, 0.05);

        if (isCritical(player)) {
            target.setFireTicks(160); // 8 seconds
        }

        if (target.getHealth() - event.getFinalDamage() <= 0) {
            Location loc = target.getLocation();

            target.getWorld().spawnParticle(
                    Particle.CRIMSON_SPORE,
                    loc,
                    80,
                    0.5, 0.5, 0.5
            );

            target.getWorld().playSound(
                    loc,
                    Sound.ENTITY_WITHER_HURT,
                    1f,
                    0.6f
            );

            double finishingHealAmount = target.getHealth();
            player.setHealth(Math.min(
                    player.getHealth() + finishingHealAmount,
                    player.getAttribute(Attribute.MAX_HEALTH).getValue()
            ));
        }
    }

    private boolean isCritical(Player player) {
        return player.getFallDistance() > 0
                && !player.isOnGround()
                && !player.isClimbing()
                && !player.isInWater()
                && !player.hasPotionEffect(PotionEffectType.BLINDNESS);
    }
}
