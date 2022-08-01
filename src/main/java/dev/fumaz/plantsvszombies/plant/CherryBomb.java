package dev.fumaz.plantsvszombies.plant;

import dev.fumaz.commons.bukkit.misc.Scheduler;
import dev.fumaz.plantsvszombies.yard.Tile;
import dev.fumaz.plantsvszombies.zombie.AbstractZombie;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Strider;

public class CherryBomb extends AbstractPlant<Strider> {

    public CherryBomb(Strider entity, Tile tile) {
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
    public void onTick(long tick) {
        entity.setShivering(false);
    }

    @Override
    public boolean isInvincible() {
        return true;
    }

    @Override
    public void onSpawn() {
        getWorld().playSound(getLocation(), Sound.ENTITY_TNT_PRIMED, 1f, 1f);

        Scheduler.of(plugin).runTaskLater(task -> {
            die();

            World world = getWorld();
            Location location = getLocation();

            world.getNearbyEntitiesByType(Mob.class, location, 4).forEach(mob -> {
                getRow().getYard().getRows()
                        .stream()
                        .filter(row -> row.getZombieFromEntity(mob) != null)
                        .map(row -> row.getZombieFromEntity(mob))
                        .forEach(zombie -> zombie.damage(getDamage()));
            });

            world.spawnParticle(Particle.EXPLOSION_HUGE, location, 3);
            world.playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 1f, 1f);
        }, (long) (1.2 * 20));
    }

}
