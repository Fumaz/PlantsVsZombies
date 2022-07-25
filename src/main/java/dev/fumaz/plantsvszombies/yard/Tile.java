package dev.fumaz.plantsvszombies.yard;

import dev.fumaz.plantsvszombies.plant.AbstractPlant;
import org.bukkit.Location;
import org.bukkit.util.BoundingBox;

public class Tile {

    private final Row row;
    private final BoundingBox box;

    private AbstractPlant<?> plant;

    public Tile(Row row, BoundingBox box) {
        this.row = row;
        this.box = box;
    }

    public void tick() {
        if (plant == null) {
            return;
        }

        plant.tick();
    }

    public Row getRow() {
        return row;
    }

    public BoundingBox getBox() {
        return box;
    }

    public Location getCenter(float yaw) {
        return box.getCenter().toLocation(getRow().getYard().getWorld(), yaw, 0).add(0.5, 1, 0.5);
    }

    public AbstractPlant<?> getPlant() {
        return plant;
    }

    public void setPlant(AbstractPlant<?> plant) {
        this.plant = plant;
    }

}
