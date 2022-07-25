package dev.fumaz.plantsvszombies.plant;

import dev.fumaz.commons.bukkit.item.ItemBuilder;
import dev.fumaz.commons.text.Strings;
import dev.fumaz.plantsvszombies.yard.Tile;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

public enum Plant {
    SUNFLOWER(Sunflower.class, Bee.class, Material.BEE_SPAWN_EGG, 50, 7500),
    PEASHOOTER(Peashooter.class, GlowSquid.class, Material.GLOW_SQUID_SPAWN_EGG, 100, 7500),
    CHERRY_BOMB(CherryBomb.class, Strider.class, Material.STRIDER_SPAWN_EGG, 150, 50 * 1000);

    private final Class<? extends AbstractPlant<? extends Mob>> plantClass;
    private final Class<? extends Mob> mobClass;
    private final Material material;
    private final int price;
    private final int recharge;

    Plant(Class<? extends AbstractPlant<? extends Mob>> plantClass, Class<? extends Mob> mobClass, Material material, int price, int recharge) {
        this.plantClass = plantClass;
        this.mobClass = mobClass;
        this.material = material;
        this.price = price;
        this.recharge = recharge;
    }

    public static Plant getByItemStack(ItemStack item) {
        for (Plant plant : values()) {
            if (plant.getItemStack().isSimilar(item)) {
                return plant;
            }
        }

        return null;
    }

    public AbstractPlant<?> spawn(Tile tile) {
        return AbstractPlant.spawn(plantClass, mobClass, tile);
    }

    public int getPrice() {
        return price;
    }

    public int getRecharge() {
        return recharge;
    }

    public String getDisplayName() {
        return ChatColor.GREEN + Strings.capitalizeFully(name().replace("_", " "));
    }

    public ItemStack getItemStack() {
        return ItemBuilder.of(material)
                .displayName(getDisplayName() + ChatColor.GRAY + " (" + ChatColor.YELLOW + price + ChatColor.GRAY + ")")
                .glow()
                .build();
    }

    public void give(Player player) {
        player.getInventory().addItem(getItemStack());
    }

}
