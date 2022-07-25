package dev.fumaz.plantsvszombies.command;

import dev.fumaz.commons.bukkit.command.PlayerCommandExecutor;
import dev.fumaz.plantsvszombies.plant.AbstractPlant;
import dev.fumaz.plantsvszombies.plant.Peashooter;
import dev.fumaz.plantsvszombies.plant.Sunflower;
import dev.fumaz.plantsvszombies.yard.Tile;
import dev.fumaz.plantsvszombies.yard.Yard;
import dev.fumaz.plantsvszombies.zombie.AbstractZombie;
import dev.fumaz.plantsvszombies.zombie.Conehead;
import dev.fumaz.plantsvszombies.zombie.Default;
import org.bukkit.command.Command;
import org.bukkit.entity.Bee;
import org.bukkit.entity.GlowSquid;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

public class YardCommand implements PlayerCommandExecutor {

    private final JavaPlugin plugin;

    public YardCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onCommand(@NotNull Player player, @NotNull Command command, @NotNull String[] strings) {
        Yard yard = new Yard(plugin, player);
    }

}
