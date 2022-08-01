package dev.fumaz.plantsvszombies.zombie;

import dev.fumaz.plantsvszombies.plant.AbstractPlant;
import dev.fumaz.plantsvszombies.yard.Row;
import org.bukkit.Material;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;

public class Newspaper extends AbstractZombie<Zombie> {

    public Newspaper(Zombie entity, Row row) {
        super(entity, row);
    }

    @Override
    public void onSpawn() {
        entity.getEquipment().setItemInMainHand(new ItemStack(Material.MAP));
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
        return 331;
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
        return entity.getHealth() > 181 ? 4.7 : 1.8;
    }

}
