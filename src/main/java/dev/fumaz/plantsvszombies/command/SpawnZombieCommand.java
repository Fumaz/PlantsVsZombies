package dev.fumaz.plantsvszombies.command;

import dev.fumaz.commons.bukkit.command.PlayerCommandExecutor;
import dev.fumaz.commons.math.Randoms;
import dev.fumaz.plantsvszombies.yard.Yard;
import dev.fumaz.plantsvszombies.zombie.Zombie;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpawnZombieCommand implements PlayerCommandExecutor {

    @Override
    public void onCommand(@NotNull Player player, @NotNull Command command, @NotNull String[] strings) {
        Yard yard = Yard.getInstance();

        if (yard == null) {
            player.sendMessage("Yard is null");
            return;
        }

        Zombie zombie = Zombie.valueOf(strings[0].toUpperCase());
        Randoms.choice(yard.getRows()).addZombie(zombie);

        player.sendMessage(ChatColor.GREEN + "You have spawned a " + ChatColor.LIGHT_PURPLE + zombie.getDisplayName() + ChatColor.GREEN + "!");
    }

}
