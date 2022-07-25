package dev.fumaz.plantsvszombies.zombie;

import dev.fumaz.commons.text.Strings;
import dev.fumaz.plantsvszombies.yard.Row;
import org.bukkit.ChatColor;
import org.bukkit.entity.Mob;

public enum Zombie {
    DEFAULT(Default.class, org.bukkit.entity.Zombie.class),
    CONEHEAD(Conehead.class, org.bukkit.entity.Zombie.class);

    private final Class<? extends AbstractZombie<? extends Mob>> plantClass;
    private final Class<? extends Mob> mobClass;

    Zombie(Class<? extends AbstractZombie<? extends Mob>> plantClass, Class<? extends Mob> mobClass) {
        this.plantClass = plantClass;
        this.mobClass = mobClass;
    }

    public AbstractZombie<?> spawn(Row row) {
        return AbstractZombie.spawn(plantClass, mobClass, row, row.getZombiesSpawn());
    }

    public String getDisplayName() {
        return ChatColor.GREEN + Strings.capitalizeFully(name().replace("_", " "));
    }

}
