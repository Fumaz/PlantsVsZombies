package dev.fumaz.plantsvszombies.plant;

import dev.fumaz.plantsvszombies.yard.Tile;
import org.bukkit.entity.Blaze;

public class Wallnut extends AbstractPlant<Blaze> {

    public Wallnut(Blaze entity, Tile tile) {
        super(entity, tile);
    }

    @Override
    public double getMaxHealth() {
        return 4000;
    }

    @Override
    public double getDamage() {
        return 0;
    }

}
