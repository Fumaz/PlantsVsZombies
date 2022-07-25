package dev.fumaz.plantsvszombies.level;

import dev.fumaz.plantsvszombies.yard.Yard;
import dev.fumaz.plantsvszombies.zombie.Zombie;

public class LevelTwo extends AbstractLevel {

    public LevelTwo(Yard yard) {
        super(yard);
    }

    @Override
    protected void initialize() {
        add(new ZombieSpawn(Zombie.DEFAULT, 25));
        add(new ZombieSpawn(Zombie.CONEHEAD, 35));
        add(new ZombieSpawn(Zombie.CONEHEAD, 50));
    }

    @Override
    protected int getWinSeconds() {
        return 60;
    }

}
