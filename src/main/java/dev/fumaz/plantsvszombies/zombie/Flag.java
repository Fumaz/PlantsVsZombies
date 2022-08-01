package dev.fumaz.plantsvszombies.zombie;

import dev.fumaz.plantsvszombies.plant.AbstractPlant;
import dev.fumaz.plantsvszombies.yard.Row;
import org.bukkit.entity.Zombie;

public class Flag extends AbstractZombie<Zombie> {

    public Flag(Zombie entity, Row row) {
        super(entity, row);
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
        return 181;
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
        return 3.7;
    }

}
