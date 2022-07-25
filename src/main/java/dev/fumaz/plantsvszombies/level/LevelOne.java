package dev.fumaz.plantsvszombies.level;

import dev.fumaz.plantsvszombies.yard.Yard;
import dev.fumaz.plantsvszombies.zombie.Zombie;

public class LevelOne extends AbstractLevel{

    public LevelOne(Yard yard) {
        super(yard);
    }

    @Override
    protected void initialize() {
        add(new ZombieSpawn(Zombie.DEFAULT, 15));
        add(new ZombieSpawn(Zombie.DEFAULT, 50));
        add(new ZombieSpawn(Zombie.DEFAULT, 75));
    }

    @Override
    protected int getWinSeconds() {
        return 75;
    }

}
