package dev.fumaz.plantsvszombies;

import dev.fumaz.plantsvszombies.command.SpawnHordeCommand;
import dev.fumaz.plantsvszombies.command.SpawnZombieCommand;
import dev.fumaz.plantsvszombies.command.YardCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlantsVsZombies extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("yard").setExecutor(new YardCommand(this));
        getCommand("spawnzombie").setExecutor(new SpawnZombieCommand());
        getCommand("spawnhorde").setExecutor(new SpawnHordeCommand());
    }

}
