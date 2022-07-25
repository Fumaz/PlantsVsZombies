package dev.fumaz.plantsvszombies.plant;

import dev.fumaz.commons.bukkit.misc.Scheduler;
import dev.fumaz.plantsvszombies.yard.Tile;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Shulker;

public class PotatoMine extends AbstractPlant<Shulker> {

    private boolean armed = false;

    public PotatoMine(Shulker entity, Tile tile) {
        super(entity, tile);

        ArmorStand stand = getWorld().spawn(entity.getLocation().add(0.5, 0, 0.5), ArmorStand.class);
        stand.setMarker(true);
        stand.setInvisible(true);
        stand.setCollidable(false);
        stand.setInvulnerable(true);

        stand.addPassenger(entity);
        entity.setColor(DyeColor.YELLOW);
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
    public long getAttackDelay() {
        return 15 * 1000;
    }

    @Override
    public void onTick(long tick) {
        if (tick < ((getAttackDelay() / 1000) * 20)) {
            entity.setPeek(0);
            return;
        }

        entity.setPeek(1.0f);

        if (armed) {
            return;
        }

        armed = true;
        entity.setMaxHealth(2000);
        entity.setHealth(2000);
        getWorld().playSound(getLocation(), Sound.ENTITY_TNT_PRIMED, 1f, 1f);
    }

    @Override
    public void onDamage(double damage) {
        if (!armed) {
            return;
        }

        die();
        Scheduler.of(plugin).runTaskLater(task -> {
            die();

            World world = getWorld();
            Location location = getLocation();

            world.getNearbyEntitiesByType(Mob.class, location, 2).forEach(mob -> {
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
