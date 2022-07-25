package dev.fumaz.plantsvszombies.yard;

import dev.fumaz.plantsvszombies.zombie.AbstractZombie;
import dev.fumaz.plantsvszombies.zombie.Zombie;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.BoundingBox;

import java.util.ArrayList;
import java.util.List;

public class Row {

    private final Yard yard;
    private final List<Tile> tiles;
    private final List<AbstractZombie<?>> zombies;
    private final BoundingBox zombiesSpawn;
    private final LawnMower mower;

    public Row(Yard yard, BoundingBox mower, BoundingBox zombiesSpawn, BoundingBox... tiles) {
        this.yard = yard;
        this.tiles = new ArrayList<>();
        this.zombies = new ArrayList<>();
        this.zombiesSpawn = zombiesSpawn;
        this.mower = new LawnMower(this, mower);

        for (BoundingBox tile : tiles) {
            this.tiles.add(new Tile(this, tile));
        }
    }

    public void tick() {
        zombies.forEach(AbstractZombie::tick);
        tiles.forEach(Tile::tick);
        mower.tick();
    }

    public Yard getYard() {
        return yard;
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public List<AbstractZombie<?>> getZombies() {
        return zombies;
    }

    public AbstractZombie<?> getClosestZombie(Location location) {
        AbstractZombie<?> closest = null;
        double distance = Double.MAX_VALUE;

        for (AbstractZombie<?> zombie : zombies) {
            if (zombie.isDead()) {
                continue;
            }

            double zombieDistance = zombie.getLocation().distance(location);
            if (zombieDistance < distance) {
                closest = zombie;
                distance = zombieDistance;
            }
        }

        return closest;
    }

    public AbstractZombie<?> getZombieFromEntity(Entity entity) {
        for (AbstractZombie<?> zombie : zombies) {
            if (zombie.getEntity().equals(entity)) {
                return zombie;
            }
        }

        return null;
    }

    public LawnMower getMower() {
        return mower;
    }

    public BoundingBox getZombiesSpawn() {
        return zombiesSpawn;
    }

    public Tile getTile(Location location) {
        for (Tile tile : tiles) {
            if (location.getX() >= tile.getBox().getMinX() && location.getX() <= tile.getBox().getMaxX() && location.getZ() >= tile.getBox().getMinZ() && location.getZ() <= tile.getBox().getMaxZ()) {
                return tile;
            }
        }

        return null;
    }

    public void addZombie(Zombie zombie) {
        addZombie(zombie.spawn(this));
    }

    public void addZombie(AbstractZombie<?> zombie) {
        zombies.add(zombie);
    }

    public void removeZombie(AbstractZombie<?> zombie) {
        zombies.remove(zombie);
    }

}
