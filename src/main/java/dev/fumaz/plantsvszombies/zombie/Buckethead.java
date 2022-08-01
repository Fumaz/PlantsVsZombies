package dev.fumaz.plantsvszombies.zombie;

import dev.fumaz.plantsvszombies.plant.AbstractPlant;
import dev.fumaz.plantsvszombies.yard.Row;
import org.bukkit.Material;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;

public class Buckethead extends AbstractZombie<Zombie>{

    public Buckethead(Zombie entity, Row row) {
        super(entity, row);
    }

    @Override
    public void onSpawn() {
        entity.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
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
        return 1281;
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
