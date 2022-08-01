package dev.fumaz.plantsvszombies.command;

import dev.fumaz.commons.bukkit.command.PlayerCommandExecutor;
import dev.fumaz.commons.math.Randoms;
import dev.fumaz.plantsvszombies.yard.Yard;
import dev.fumaz.plantsvszombies.zombie.Zombie;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SpawnHordeCommand implements PlayerCommandExecutor {


    @Override
    public void onCommand(@NotNull Player player, @NotNull Command command, @NotNull String[] strings) {
        Yard yard = Yard.getInstance();

        if (yard == null) {
            player.sendMessage("Yard is null");
            return;
        }

        Block target = player.getTargetBlock(100);

        List<Zombie> zombies = new ArrayList<>();
        zombies.add(Zombie.FLAG);

        for (String string : strings) {
            zombies.add(Zombie.valueOf(string.toUpperCase()));
        }

        zombies.forEach(zombie -> {
            if (zombie == Zombie.FLAG) {
                yard.getRow(target.getLocation()).addZombie(zombie);
            } else {
                Randoms.choice(yard.getRows()).addZombie(zombie);
            }
        });

        player.sendMessage(ChatColor.GREEN + "You have spawned a horde of zombies!");
    }

}
