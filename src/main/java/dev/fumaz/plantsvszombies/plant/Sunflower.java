package dev.fumaz.plantsvszombies.plant;

import dev.fumaz.plantsvszombies.yard.Tile;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Bee;
import org.bukkit.inventory.ItemStack;

public class Sunflower extends AbstractPlant<Bee> {

    public Sunflower(Bee entity, Tile tile) {
        super(entity, tile);
    }

    @Override
    public void onTick(long tick) {
        if ((tick != (20 * 7)) && (tick % (24 * 20) != 0)) {
            return;
        }

        tile.getRow().getYard().createSuns(tile, 1);
    }

    @Override
    public double getMaxHealth() {
        return 300;
    }

    @Override
    public double getDamage() {
        return 0;
    }

}
