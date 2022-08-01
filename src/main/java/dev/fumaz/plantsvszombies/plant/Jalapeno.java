package dev.fumaz.plantsvszombies.plant;

import dev.fumaz.commons.bukkit.misc.Scheduler;
import dev.fumaz.plantsvszombies.yard.Tile;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Cat;

public class Jalapeno extends AbstractPlant<Cat> {

    public Jalapeno(Cat entity, Tile tile) {
        super(entity, tile);
    }

    @Override
    public double getMaxHealth() {
        return 2000;
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
    public void onSpawn() {
        Scheduler.of(plugin).runTaskLater(() -> {
            entity.remove();

            getRow().getZombies().forEach(zombie -> {
                zombie.getLocation().getWorld().spawnParticle(Particle.EXPLOSION_HUGE, zombie.getLocation(), 3);
                zombie.getLocation().getWorld().playSound(zombie.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1f, 1f);
                zombie.damage(getDamage());
            });
        }, 20 * 3);
    }

}
