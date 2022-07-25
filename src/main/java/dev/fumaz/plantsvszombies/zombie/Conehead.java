package dev.fumaz.plantsvszombies.zombie;

import dev.fumaz.commons.bukkit.item.ItemBuilder;
import dev.fumaz.plantsvszombies.plant.AbstractPlant;
import dev.fumaz.plantsvszombies.yard.Row;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class Conehead extends AbstractZombie<Zombie> {

    public Conehead(Zombie entity, Row row) {
        super(entity, row);
    }

    @Override
    public void onSpawn() {
        entity.getEquipment().setHelmet(ItemBuilder.of(Material.LEATHER_HELMET)
                .consumeCustomMeta(LeatherArmorMeta.class, meta -> meta.setColor(Color.ORANGE))
                .build());
    }

    @Override
    public void onAttack(AbstractPlant<?> plant) {
        plant.damage(getDamage());
    }

    @Override
    public long getAttackDelay() {
        return 1000;
    }

    @Override
    public double getMaxHealth() {
        return 551;
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
        return 4.7;
    }

}
