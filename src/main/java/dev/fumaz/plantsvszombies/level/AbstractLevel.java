package dev.fumaz.plantsvszombies.level;

import dev.fumaz.commons.math.Randoms;
import dev.fumaz.plantsvszombies.yard.Row;
import dev.fumaz.plantsvszombies.yard.Yard;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractLevel {

    private final Yard yard;
    private final List<ZombieSpawn> zombies;

    private int seconds;

    public AbstractLevel(Yard yard) {
        this.yard = yard;
        this.zombies = new ArrayList<>();
        this.seconds = 0;

        initialize();
    }

    public void tick() {
        seconds++;

        if (seconds >= getWinSeconds() && !yard.hasZombies()) {
            yard.win();
        }

        zombies.stream()
                .filter(spawn -> spawn.getSeconds() == seconds)
                .forEach(spawn -> {
                    Row row = Randoms.choice(yard.getRows());
                    spawn.getZombie().spawn(row);
                });
    }

    protected abstract void initialize();

    protected abstract int getWinSeconds();

    protected void add(ZombieSpawn spawn) {
        zombies.add(spawn);
    }

}
