package dev.fumaz.plantsvszombies.plant;

import dev.fumaz.commons.bukkit.misc.Scheduler;
import dev.fumaz.plantsvszombies.yard.Tile;
import dev.fumaz.plantsvszombies.zombie.AbstractZombie;
import org.bukkit.entity.Creeper;

public class Squash extends AbstractPlant<Creeper> {

    public Squash(Creeper entity, Tile tile) {
        super(entity, tile);
    }

    @Override
    public double getMaxHealth() {
        return 300;
    }

    @Override
    public double getDamage() {
        return 1800;
    }

    @Override
    public boolean isInvincible() {
        return true;
    }

    @Override
    public void attack(AbstractZombie<?> zombie) {
        if (zombie.getLocation().distanceSquared(getLocation()) > 4) {
            return;
        }

        entity.setVelocity(zombie.getLocation().subtract(getLocation()).add(0, 0.5, 0).toVector().normalize().multiply(0.5));

        Scheduler.of(plugin).runTaskLater(task -> {
            entity.setVelocity(zombie.getLocation().subtract(getLocation()).toVector().normalize().multiply(0.5));

            Scheduler.of(plugin).runTaskLater(() -> {
                zombie.damage(getDamage());
                die();
            }, 10);
        }, 20);
    }

}
