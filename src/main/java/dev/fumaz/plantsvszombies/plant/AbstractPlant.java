package dev.fumaz.plantsvszombies.plant;

import dev.fumaz.commons.bukkit.interfaces.FListener;
import dev.fumaz.plantsvszombies.PlantsVsZombies;
import dev.fumaz.plantsvszombies.yard.Row;
import dev.fumaz.plantsvszombies.yard.Tile;
import dev.fumaz.plantsvszombies.zombie.AbstractZombie;
import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Mob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public abstract class AbstractPlant<T extends Mob> implements FListener {

    protected final JavaPlugin plugin;
    protected final T entity;
    protected final Tile tile;
    protected long lastAttack;
    protected long tick;

    public AbstractPlant(T entity, Tile tile) {
        this.plugin = JavaPlugin.getPlugin(PlantsVsZombies.class);
        this.entity = entity;
        this.tile = tile;
        this.lastAttack = -1;
        this.tick = 0;

        register(JavaPlugin.getPlugin(PlantsVsZombies.class));
        onSpawn();

        tile.setPlant(this);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Mob> AbstractPlant<T> spawn(Class<? extends AbstractPlant<? extends Mob>> plantClass, Class<? extends Mob> entityClass, Tile tile) {
        try {
            World world = tile.getRow().getYard().getWorld();
            T entity = (T) world.spawn(tile.getCenter(90), entityClass, false, null);
            entity.setAI(false);
            entity.setSilent(true);
            entity.setCollidable(false);

            return (AbstractPlant<T>) plantClass.getConstructor(entityClass, Tile.class).newInstance(entity, tile);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isDead() {
        return entity.isDead();
    }

    public World getWorld() {
        return entity.getWorld();
    }

    public Location getLocation() {
        return entity.getLocation();
    }

    public T getEntity() {
        return entity;
    }

    public Tile getTile() {
        return tile;
    }

    public Row getRow() {
        return tile.getRow();
    }

    public void onTick(long tick) {
    }

    public void onDamage(double damage) {
    }

    public void onAttack(AbstractZombie<?> zombie) {
    }

    public void onSpawn() {
    }

    public void onDeath() {
    }

    public long getAttackDelay() {
        return Integer.MAX_VALUE;
    }

    public boolean isInvincible() {
        return false;
    }

    public boolean canAttackBehind() {
        return false;
    }

    public abstract double getMaxHealth();

    public abstract double getDamage();

    public void damage(double damage) {
        if (isDead()) {
            return;
        }

        if (isInvincible()) {
            return;
        }

        entity.damage(damage);
        entity.setNoDamageTicks(0);
        entity.setMaximumNoDamageTicks(0);

        onDamage(damage);
    }

    public void tick() {
        if (isDead()) {
            return;
        }

        tick++;

        onTick(tick);

        entity.teleport(tile.getCenter(90));
        entity.setVelocity(new Vector(0, 0, 0));

        Row row = tile.getRow();
        AbstractZombie<?> zombie = row.getClosestZombie(getLocation());

        if (zombie != null) {
            if (zombie.getLocation().getBlockX() > getLocation().getBlockX() && !canAttackBehind()) {
                return;
            }

            attack(zombie);
        }
    }

    public void attack(AbstractZombie<?> zombie) {
        if (isDead()) {
            return;
        }

        if (!canAttack()) {
            return;
        }

        onAttack(zombie);
    }

    public void die() {
        entity.remove();
        tile.setPlant(null);
        HandlerList.unregisterAll(this);

        onDeath();
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

    @EventHandler
    public void onMove(EntityMoveEvent event) {
        if (isDead()) {
            return;
        }

        if (event.getEntity() != entity) {
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

    protected Vector getVectorTo(Location location, double speed) {
        return location.toVector().subtract(getLocation().toVector()).normalize().multiply(speed);
    }

}
