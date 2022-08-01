package dev.fumaz.plantsvszombies.plant;

import dev.fumaz.plantsvszombies.yard.Tile;
import dev.fumaz.plantsvszombies.zombie.AbstractZombie;
import org.bukkit.entity.Entity;
import org.bukkit.entity.GlowSquid;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

public class Peashooter extends AbstractPlant<GlowSquid> {

    private final Set<Snowball> projectiles;

    public Peashooter(GlowSquid entity, Tile tile) {
        super(entity, tile);

        this.projectiles = new HashSet<>();
    }

    @Override
    public void onAttack(AbstractZombie<?> zombie) {
        Vector vector = getVectorTo(zombie.getLocation(), 2);

        Snowball snowball = getWorld().spawn(getLocation().add(0, 1, 0), Snowball.class);
        snowball.setVelocity(vector);

        projectiles.add(snowball);
    }

    @Override
    public void onSpawn() {
        // entity.setRotation(-90, 0);
    }

    @Override
    public long getAttackDelay() {
        return 1425;
    }

    @Override
    public double getMaxHealth() {
        return 300;
    }

    @Override
    public double getDamage() {
        return 20;
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Snowball snowball)) {
            return;
        }

        if (!projectiles.remove(snowball)) {
            return;
        }

        Entity hit = event.getHitEntity();

        if (hit == null) {
            return;
        }

        AbstractZombie<?> zombie = getRow().getZombieFromEntity(hit);

        if (zombie == null) {
            return;
        }

        zombie.damage(getDamage());
    }

}
