package dev.fumaz.plantsvszombies.zombie;

import dev.fumaz.commons.bukkit.interfaces.FListener;
import dev.fumaz.commons.collection.Pair;
import dev.fumaz.plantsvszombies.PlantsVsZombies;
import dev.fumaz.plantsvszombies.plant.AbstractPlant;
import dev.fumaz.plantsvszombies.yard.Row;
import dev.fumaz.plantsvszombies.yard.Tile;
import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

public abstract class AbstractZombie<T extends Mob> implements FListener {

    protected final T entity;
    protected final Row row;
    protected long lastAttack;

    public AbstractZombie(T entity, Row row) {
        this.entity = entity;
        this.row = row;
        this.lastAttack = -1;

        entity.setMaxHealth(getMaxHealth() / 100);
        entity.setHealth(getMaxHealth() / 100);

        register(JavaPlugin.getPlugin(PlantsVsZombies.class));
        onSpawn();

        row.addZombie(this);
    }

    public static <T extends Mob> AbstractZombie<T> spawn(Class<? extends AbstractZombie<?>> plantClass, Class<? extends Mob> entityClass, Row row, BoundingBox box) {
        try {
            World world = row.getYard().getWorld();
            T entity = (T) world.spawn(box.getCenter().toLocation(world, -90, 0).add(0.5, 1, 0.5), entityClass, false, null);
            entity.setSilent(true);

            if (entity instanceof Zombie) {
                ((Zombie) entity).setShouldBurnInDay(false);
            }

            return (AbstractZombie<T>) plantClass.getConstructor(entityClass, Row.class).newInstance(entity, row);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Location getLocation() {
        return entity.getLocation();
    }

    public T getEntity() {
        return entity;
    }

    public Row getRow() {
        return row;
    }

    public boolean isDead() {
        return entity.isDead();
    }

    public void onTick() {
    }

    public void onDamage(double damage) {
    }

    public void onAttack(AbstractPlant<?> plant) {
    }

    public void onSpawn() {
    }

    public void onDeath() {
    }

    public abstract long getAttackDelay();

    public abstract double getMaxHealth();

    public abstract double getExtraHealth();

    public abstract double getDamage();

    public abstract double getSpeed();

    public void damage(double damage) {
        if (isDead()) {
            return;
        }

        entity.damage(damage / 100);
        entity.setNoDamageTicks(0);
        entity.setMaximumNoDamageTicks(0);

        onDamage(damage);
    }

    public void tick() {
        if (isDead()) {
            return;
        }

        onTick();

        Pair<AbstractPlant<?>, Double> closest = row.getClosestPlant(getLocation());
        if (closest != null && closest.getSecond() <= 1.5) {
            attack(closest.getFirst());
            entity.setVelocity(new Vector(0, 0, 0));
            return;
        }

        entity.setVelocity(new Vector((4.7 / getSpeed()) * 0.02, 0, 0));

        if (entity.getLocation().getBlockX() >= 13) {
            row.getYard().lose();
        }
    }

    public void die() {
        row.removeZombie(this);

        onDeath();
        HandlerList.unregisterAll(this);
    }

    public void attack(AbstractPlant<?> plant) {
        if (!canAttack()) {
            return;
        }

        onAttack(plant);
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        if (event.getEntity() != entity) {
            return;
        }

        event.getDrops().clear();
        event.setDroppedExp(0);
        die();
    }

    @EventHandler
    public void onTarget(EntityTargetEvent event) {
        if (isDead()) {
            return;
        }

        if (event.getEntity() != entity) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onMove(EntityMoveEvent event) {
        if (isDead()) {
            return;
        }

        if (event.getEntity() != entity) {
            return;
        }

        Location to = event.getTo();
        to.setZ(event.getFrom().getZ());
        to.setYaw(event.getFrom().getYaw());
        to.setPitch(event.getFrom().getPitch());
        event.setTo(to);

        Tile tile = row.getTile(getLocation());
        if (tile != null && tile.getPlant() != null) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (isDead()) {
            return;
        }

        if (event.getEntity() != entity) {
            return;
        }

        if (event.getCause() == EntityDamageEvent.DamageCause.CUSTOM) {
            return;
        }

        event.setCancelled(true);
    }

    private boolean canAttack() {
        long now = System.currentTimeMillis();
        if (now - lastAttack < getAttackDelay()) {
            return false;
        }

        lastAttack = now;
        return true;
    }

}
