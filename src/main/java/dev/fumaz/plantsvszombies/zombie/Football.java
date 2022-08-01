package dev.fumaz.plantsvszombies.zombie;

import dev.fumaz.commons.bukkit.item.ItemBuilder;
import dev.fumaz.plantsvszombies.yard.Row;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class Football extends AbstractZombie<Zombie> {

    public Football(Zombie entity, Row row) {
        super(entity, row);
    }

    @Override
    public void onSpawn() {
        entity.getEquipment().setHelmet(createArmorPiece(Material.LEATHER_HELMET));
        entity.getEquipment().setChestplate(createArmorPiece(Material.LEATHER_CHESTPLATE));
        entity.getEquipment().setLeggings(createArmorPiece(Material.LEATHER_LEGGINGS));
        entity.getEquipment().setBoots(createArmorPiece(Material.LEATHER_BOOTS));
    }

    @Override
    public long getAttackDelay() {
        return 1000;
    }

    @Override
    public double getMaxHealth() {
        return 1581;
    }

    @Override
    public double getExtraHealth() {
        return 89;
    }

    @Override
    public double getDamage() {
        return 100;
    }

    @Override
    public double getSpeed() {
        return 2.5;
    }

    private ItemStack createArmorPiece(Material material) {
        return ItemBuilder.of(material)
                .consumeCustomMeta(LeatherArmorMeta.class, meta -> meta.setColor(Color.RED))
                .build();
    }
}
