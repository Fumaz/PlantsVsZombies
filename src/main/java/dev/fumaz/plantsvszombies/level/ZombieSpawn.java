package dev.fumaz.plantsvszombies.level;

import dev.fumaz.plantsvszombies.zombie.Zombie;

public class ZombieSpawn {

    private final Zombie zombie;
    private final int seconds;

    public ZombieSpawn(Zombie zombie, int seconds) {
        this.zombie = zombie;
        this.seconds = seconds;
    }

    public Zombie getZombie() {
        return zombie;
    }

    public int getSeconds() {
        return seconds;
    }

}
