package me.cloudsven.amethystapath.items;

import me.cloudsven.amethystapath.util.TextUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;

import java.util.List;

public enum CustomItem {
    AMIRITE_SHARD(
            "amirite_shard",
            Material.AMETHYST_SHARD,
            "Amirite Shard",
            NamedTextColor.LIGHT_PURPLE
    ),

    AMIRITE_TOTEM(
            "amirite_totem",
            Material.TOTEM_OF_UNDYING,
            "Totem of Amirite",
            NamedTextColor.LIGHT_PURPLE,
            List.of(
                    TextUtil.text("Death's adversary."),
                    TextUtil.text("• Explodes with true damage", NamedTextColor.DARK_PURPLE)
            )
    ),

    AMIRITE_CROSSBOW(
            "amirite_crossbow",
            Material.CROSSBOW,
            "Amirite Crossbow",
            NamedTextColor.LIGHT_PURPLE,
            List.of(
                    TextUtil.text("A pillager's dream."),
                    TextUtil.text("• Infuses arrows with amethyst", NamedTextColor.DARK_PURPLE),
                    TextUtil.text("• Explodes on impact", NamedTextColor.DARK_PURPLE)
            )
    ),

    AMIRITE_CLEAVER(
            "amirite_cleaver",
            Material.NETHERITE_AXE,
            "Amirite Cleaver",
            NamedTextColor.LIGHT_PURPLE,
            List.of(
                    TextUtil.text("Wanna do some taxonomy?"),
                    TextUtil.text("• Guarantees a head on kill", NamedTextColor.DARK_PURPLE)
            ),
            null,
            16,
            0.4
    ),

    AMIRITE_SHIELD(
            "amirite_shield",
            Material.SHIELD,
            "Amirite Shield",
            NamedTextColor.LIGHT_PURPLE,
            List.of(
                    TextUtil.text("Captain America?"),
                    TextUtil.text("• Reduces damage taken by 60%", NamedTextColor.DARK_PURPLE),
                    TextUtil.text("• Reflects 25% of damage", NamedTextColor.DARK_PURPLE)
            ),
            10
    ),

    SHARDED_TNT(
            "sharded_tnt",
            Material.TNT,
            "Sharded TNT",
            NamedTextColor.RED,
            List.of(
                    TextUtil.text("Highly unstable dynamite."),
                    TextUtil.text("• Ignites immediately on placement", NamedTextColor.DARK_PURPLE),
                    TextUtil.text("• 4x Explosion Power", NamedTextColor.DARK_PURPLE)
            )
    ),

    SCARLET_HEART(
            "scarlet_heart",
            Material.HEART_OF_THE_SEA,
            "Scarlet Heart",
            NamedTextColor.RED
    ),

    SCARLET_HEART_CHARGED(
            "scarlet_heart_charged",
            Material.HEART_OF_THE_SEA,
            "Charged Scarlet Heart",
            NamedTextColor.RED
    ),

    SCARLET_BLADE(
            "scarlet_blade",
            Material.NETHERITE_SWORD,
            "Scarlet Blade",
            NamedTextColor.RED,
            List.of(
                    TextUtil.text("Kill! Kill! KILL!"),
                    TextUtil.text("• 10% Lifesteal", NamedTextColor.DARK_RED),
                    TextUtil.text("• Deals Wither II for 3 seconds", NamedTextColor.DARK_RED)
            ),
            null,
            10,
            1.6
    ),

    SCARLET_MACE(
            "scarlet_mace",
            Material.MACE,
            "Scarlet Mace",
            NamedTextColor.RED,
            List.of(
                    TextUtil.text("Smash everyone and everything!"),
                    TextUtil.text("• Ruptures target's armor", NamedTextColor.DARK_RED),
                    TextUtil.text("• Deals Wither II for 3 seconds", NamedTextColor.DARK_RED)
            ),
            20
    ),

    SCARLET_PICKAXE(
            "scarlet_pickaxe",
            Material.NETHERITE_PICKAXE,
            "Scarlet Pickaxe",
            NamedTextColor.RED,
            List.of(
                    TextUtil.text("Break the barrier!"),
                    TextUtil.text("• Can break normally unbreakable blocks", NamedTextColor.DARK_RED)
            ),
            5 * 60
    );

    public final String id;
    public final Material material;
    public final String name;
    public final NamedTextColor color;
    public final List<Component> baseLore;

    public final Integer cooldown;
    public final Integer attackDamage;
    public final Double attackSpeed;

    // full constructor
    CustomItem(String id,
               Material material,
               String name,
               NamedTextColor color,
               List<Component> baseLore,
               Integer cooldown,
               Integer attackDamage,
               Double attackSpeed) {

        this.id = id;
        this.material = material;
        this.name = name;
        this.color = color;
        this.baseLore = baseLore == null ? List.of() : baseLore;
        this.cooldown = cooldown;
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
    }

    // without stats
    CustomItem(String id,
               Material material,
               String name,
               NamedTextColor color,
               List<Component> baseLore,
               Integer cooldown) {

        this(id, material, name, color, baseLore, cooldown, null, null);
    }

    // without cooldown
    CustomItem(String id,
               Material material,
               String name,
               NamedTextColor color,
               List<Component> baseLore) {

        this(id, material, name, color, baseLore, null, null, null);
    }

    // minimal
    CustomItem(String id,
               Material material,
               String name,
               NamedTextColor color) {

        this(id, material, name, color, List.of(), null, null, null);
    }
}
