package me.cloudsven.amethystapath.listeners;

import me.cloudsven.amethystapath.AmethystApath;
import me.cloudsven.amethystapath.items.CustomItem;
import me.cloudsven.amethystapath.items.ItemFactory;
import me.cloudsven.amethystapath.items.ItemRegistry;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.Damageable;

public class CraftingListener implements Listener {
    private final AmethystApath plugin;

    private ItemStack getBaseItem(PrepareItemCraftEvent event, String key, Material material) {
        ItemStack[] matrix = event.getInventory().getMatrix();

        ItemStack found = null;

        for (ItemStack item : matrix) {
            if (item == null) continue;

            if (item.getType() == material) {
                found = item;

                // block crafting
                CustomItem custom = ItemRegistry.getCustomItem(key);

                if (ItemRegistry.is(item, custom)) {
                    event.getInventory().setResult(null);
                    return null;
                }
            }
        }

        return found;
    }

    private ItemStack copyItemProperties(ItemStack result, ItemStack item) {
        // copy enchants
        result.addUnsafeEnchantments(item.getEnchantments());

        // copy durability
        if (item.getItemMeta() instanceof Damageable dmgMeta &&
                result.getItemMeta() instanceof Damageable resultMeta) {

            resultMeta.setDamage(dmgMeta.getDamage());
            result.setItemMeta(resultMeta);
        }

        return result;
    }

    private void copyItemAttributes(PrepareItemCraftEvent event, CustomItem item) {
        if (item == null) return;
        ItemStack originalItem = getBaseItem(event, item.id, item.material);

        if (originalItem == null) return;
        ItemStack result = ItemFactory.create(item);

        event.getInventory().setResult(copyItemProperties(result, originalItem));
    }

    public CraftingListener(AmethystApath plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        if (!(event.getRecipe() instanceof ShapedRecipe recipe)) return;

        String key = recipe.getKey().getKey();
        copyItemAttributes(event, ItemRegistry.getCustomItem(key));
    }
}