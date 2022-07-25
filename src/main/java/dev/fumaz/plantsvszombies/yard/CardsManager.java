package dev.fumaz.plantsvszombies.yard;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import dev.fumaz.commons.bukkit.interfaces.FListener;
import dev.fumaz.commons.bukkit.item.ItemBuilder;
import dev.fumaz.commons.cache.Cooldown;
import dev.fumaz.plantsvszombies.plant.AbstractPlant;
import dev.fumaz.plantsvszombies.plant.Peashooter;
import dev.fumaz.plantsvszombies.plant.Plant;
import dev.fumaz.plantsvszombies.plant.Sunflower;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Bee;
import org.bukkit.entity.GlowSquid;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class CardsManager implements FListener {

    private final Yard yard;
    private final Map<Plant, Long> cooldown;

    public CardsManager(JavaPlugin plugin, Yard yard) {
        this.yard = yard;
        this.cooldown = new HashMap<>();

        register(plugin);
    }

    public void giveItems(Player player) {
        for (Plant plant : Plant.values()) {
            plant.give(player);
        }

        player.getInventory().setItem(8, ItemBuilder.of(Material.WOODEN_SHOVEL)
                .displayName(ChatColor.LIGHT_PURPLE + "Remove plants")
                .build());
    }

    @EventHandler
    public void onPlace(PlayerInteractEvent event) {
        if (!event.getAction().isRightClick()) {
            return;
        }

        Plant plant = Plant.getByItemStack(event.getItem());

        if (plant == null) {
            return;
        }

        event.setCancelled(true);

        Player player = event.getPlayer();
        Block block = player.getTargetBlock(100);

        if (block == null) {
            return;
        }

        Tile tile = yard.getTile(block.getLocation());

        if (tile == null) {
            return;
        }

        if (tile.getPlant() != null) {
            player.sendMessage(ChatColor.RED + "There is already a plant on this tile.");
            return;
        }

        if (yard.getSuns() < plant.getPrice()) {
            player.sendMessage(ChatColor.RED + "You don't have enough suns to buy this plant.");
            return;
        }

        if (cooldown.containsKey(plant) && System.currentTimeMillis() < cooldown.get(plant)) {
            player.sendMessage(ChatColor.RED + "You must wait " + (cooldown.get(plant) - System.currentTimeMillis()) / 1000 + " seconds before you can place this plant.");
            return;
        }

        cooldown.put(plant, System.currentTimeMillis() + plant.getRecharge());
        yard.addSuns(-plant.getPrice());

        AbstractPlant<?> abstractPlant = plant.spawn(tile);
        tile.setPlant(abstractPlant);
    }

    @EventHandler
    public void onRemove(PlayerInteractEvent event) {
        if (!event.getAction().isRightClick()) {
            return;
        }

        if (!event.hasItem() || event.getItem().getType() != Material.WOODEN_SHOVEL) {
            return;
        }

        event.setCancelled(true);

        Player player = event.getPlayer();
        Block block = player.getTargetBlock(100);

        if (block == null) {
            return;
        }

        Tile tile = yard.getTile(block.getLocation());

        if (tile == null) {
            return;
        }

        if (tile.getPlant() == null) {
            player.sendMessage(ChatColor.RED + "There is no plant on this tile.");
            return;
        }

        tile.getPlant().die();
    }

}
