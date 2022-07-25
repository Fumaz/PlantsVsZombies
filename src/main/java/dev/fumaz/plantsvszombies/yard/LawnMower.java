package dev.fumaz.plantsvszombies.yard;


import dev.fumaz.commons.bukkit.misc.Scheduler;
import dev.fumaz.plantsvszombies.PlantsVsZombies;
import dev.fumaz.plantsvszombies.zombie.AbstractZombie;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.minecart.ExplosiveMinecart;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BoundingBox;

import java.util.concurrent.atomic.AtomicInteger;

public class LawnMower {

    private final Row row;
    private final BoundingBox box;

    private ExplosiveMinecart entity;
    private boolean used;

    public LawnMower(Row row, BoundingBox box) {
        this.row = row;
        this.box = box;

        spawn();
    }

    public void tick() {
        if (isUsed()) {
            return;
        }

        entity.teleport(box.getCenter().toLocation(row.getYard().getWorld(), 0, 0).add(0.5, 1, 0.5));
        entity.getNearbyEntities(0.25, 0.25, 0.25)
                .stream()
                .filter(e -> e instanceof Mob)
                .findFirst()
                .ifPresent(e -> use());
    }

    public Row getRow() {
        return row;
    }

    public BoundingBox getBox() {
        return box;
    }

    public Location getLocation() {
        return entity.getLocation();
    }

    public Entity getEntity() {
        return entity;
    }

    public boolean isUsed() {
        return entity.isDead() || used;
    }

    public void spawn() {
        World world = row.getYard().getWorld();

        entity = (ExplosiveMinecart) world.spawnEntity(box.getCenter().toLocation(world, 0, 0).add(0.5, 1, 0.5), EntityType.MINECART_TNT, false);
        entity.setInvulnerable(true);
        entity.setSilent(true);
        entity.setMaxSpeed(0);
        entity.setSlowWhenEmpty(true);
    }

    public void use() {
        used = true;
        AtomicInteger amount = new AtomicInteger(22 * 10);

        Scheduler.of(JavaPlugin.getPlugin(PlantsVsZombies.class)).runTaskTimer(() -> {
            if (amount.get() <= 0) {
                entity.remove();
                return;
            }

            amount.decrementAndGet();
            entity.teleport(entity.getLocation().add(-0.5, 0, 0));
            entity.getNearbyEntities(0.25, 0.25, 0.25).forEach(e -> {
                if (!(e instanceof LivingEntity livingEntity)) {
                    return;
                }

                AbstractZombie<?> zombie = row.getZombieFromEntity(livingEntity);

                if (zombie == null) {
                    return;
                }

                livingEntity.damage(2000);
            });
        }, 0, 1);
    }

}
