package dev.fumaz.plantsvszombies.yard;

import dev.fumaz.commons.bukkit.interfaces.FListener;
import dev.fumaz.commons.bukkit.misc.Scheduler;
import dev.fumaz.commons.math.Randoms;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.BoundingBox;

import java.util.ArrayList;
import java.util.List;

public class Yard implements FListener {

    private static Yard instance;

    private final JavaPlugin plugin;
    private final World world;
    private final List<Row> rows;

    private final BukkitTask tickTask;
    private final CardsManager cardsManager;

    private final Player player;

    private int suns;
    private int level;


    public Yard(JavaPlugin plugin, Player player) {
        instance = this;

        this.plugin = plugin;
        this.player = player;
        this.world = Bukkit.getWorld("plantsvszombies");
        this.cardsManager = new CardsManager(plugin, this);
        this.rows = new ArrayList<>();
        this.suns = 100;
        this.level = 0;

        addRow(new Row(this,
                new BoundingBox(11, 64, 4, 10, 64, 3),
                new BoundingBox(-9, 64, 4, -10, 64, 3),
                new BoundingBox(9, 64, 4, 8, 64, 3),
                new BoundingBox(7, 64, 4, 6, 64, 3),
                new BoundingBox(5, 64, 4, 4, 64, 3),
                new BoundingBox(3, 64, 4, 2, 64, 3),
                new BoundingBox(1, 64, 4, 0, 64, 3),
                new BoundingBox(-1, 64, 4, -2, 64, 3),
                new BoundingBox(-3, 64, 4, -4, 64, 3),
                new BoundingBox(-5, 64, 4, -6, 64, 3),
                new BoundingBox(-7, 64, 4, -8, 64, 3))
        );

        addRow(new Row(this,
                new BoundingBox(11, 64, 2, 10, 64, 1),
                new BoundingBox(-9, 64, 2, -10, 64, 1),
                new BoundingBox(9, 64, 2, 8, 64, 1),
                new BoundingBox(7, 64, 2, 6, 64, 1),
                new BoundingBox(5, 64, 2, 4, 64, 1),
                new BoundingBox(3, 64, 2, 2, 64, 1),
                new BoundingBox(1, 64, 2, 0, 64, 1),
                new BoundingBox(-1, 64, 2, -2, 64, 1),
                new BoundingBox(-3, 64, 2, -4, 64, 1),
                new BoundingBox(-5, 64, 2, -6, 64, 1),
                new BoundingBox(-7, 64, 2, -8, 64, 1))
        );

        addRow(new Row(this,
                new BoundingBox(11, 64, 0, 10, 64, -1),
                new BoundingBox(-9, 64, 0, -10, 64, -1),
                new BoundingBox(9, 64, 0, 8, 64, -1),
                new BoundingBox(7, 64, 0, 6, 64, -1),
                new BoundingBox(5, 64, 0, 4, 64, -1),
                new BoundingBox(3, 64, 0, 2, 64, -1),
                new BoundingBox(1, 64, 0, 0, 64, -1),
                new BoundingBox(-1, 64, 0, -2, 64, -1),
                new BoundingBox(-3, 64, 0, -4, 64, -1),
                new BoundingBox(-5, 64, 0, -6, 64, -1),
                new BoundingBox(-7, 64, 0, -8, 64, -1))
        );

        addRow(new Row(this,
                new BoundingBox(11, 64, -2, 10, 64, -3),
                new BoundingBox(-9, 64, -2, -10, 64, -3),
                new BoundingBox(9, 64, -2, 8, 64, -3),
                new BoundingBox(7, 64, -2, 6, 64, -3),
                new BoundingBox(5, 64, -2, 4, 64, -3),
                new BoundingBox(3, 64, -2, 2, 64, -3),
                new BoundingBox(1, 64, -2, 0, 64, -3),
                new BoundingBox(-1, 64, -2, -2, 64, -3),
                new BoundingBox(-3, 64, -2, -4, 64, -3),
                new BoundingBox(-5, 64, -2, -6, 64, -3),
                new BoundingBox(-7, 64, -2, -8, 64, -3))
        );

        addRow(new Row(this,
                new BoundingBox(11, 64, -4, 10, 64, -5),
                new BoundingBox(-9, 64, -4, -10, 64, -5),
                new BoundingBox(9, 64, -4, 8, 64, -5),
                new BoundingBox(7, 64, -4, 6, 64, -5),
                new BoundingBox(5, 64, -4, 4, 64, -5),
                new BoundingBox(3, 64, -4, 2, 64, -5),
                new BoundingBox(1, 64, -4, 0, 64, -5),
                new BoundingBox(-1, 64, -4, -2, 64, -5),
                new BoundingBox(-3, 64, -4, -4, 64, -5),
                new BoundingBox(-5, 64, -4, -6, 64, -5),
                new BoundingBox(-7, 64, -4, -8, 64, -5))
        );

        cardsManager.giveItems(player);
        nextLevel();

        this.tickTask = Scheduler.of(plugin).runTaskTimer(() -> {
            rows.forEach(Row::tick);
            player.sendActionBar(ChatColor.YELLOW + "" + ChatColor.BOLD + suns + " ☀");
        }, 0, 1);

        createNextSunTask();
        register(plugin);
    }

    public static Yard getInstance() {
        return instance;
    }

    public void win() {
        nextLevel();
    }

    public void nextLevel() {
        level += 1;
        suns = 100;

        player.sendTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "LEVEL " + level, null);

        getRows().forEach(row -> row.getTiles().forEach(tile -> {
            if (tile.getPlant() == null) {
                return;
            }

            tile.getPlant().die();
        }));
    }

    public boolean hasZombies() {
        return rows.stream().anyMatch(row -> !row.getZombies().isEmpty());
    }

    public World getWorld() {
        return world;
    }

    public List<Row> getRows() {
        return rows;
    }

    public Row getRow(Location location) {
        for (Row row : rows) {
            if (row.getTile(location) != null) {
                return row;
            }
        }

        return null;
    }

    public Tile getTile(Location location) {
        for (Row row : rows) {
            if (row.getTile(location) != null) {
                return row.getTile(location);
            }
        }

        return null;
    }

    public int getSuns() {
        return suns;
    }

    public void addSuns(int amount) {
        this.suns += amount;
    }

    public CardsManager getCardsManager() {
        return cardsManager;
    }

    @EventHandler
    protected void onPickupSun(PlayerAttemptPickupItemEvent event) {
        if (event.getItem().getItemStack().getType() != Material.SUNFLOWER) {
            return;
        }

        event.setCancelled(true);
        event.getItem().remove();

        addSuns(25 * event.getItem().getItemStack().getAmount());
    }

    @EventHandler
    public void onSunLeftClick(PlayerInteractEvent event) {
        if (!event.getAction().isLeftClick()) {
            return;
        }

        Player player = event.getPlayer();
        Block block = player.getTargetBlock(100);

        if (block == null) {
            return;
        }

        block.getWorld().getNearbyEntities(block.getBoundingBox().expand(1))
                .stream()
                .filter(entity -> entity instanceof Item)
                .map(entity -> (Item) entity)
                .forEach(item -> {
                    if (item.getItemStack().getType() != Material.SUNFLOWER) {
                        return;
                    }

                    item.remove();

                    addSuns(25 * item.getItemStack().getAmount());
                });
    }

    public void createSuns(Tile tile, int amount) {
        Item item = tile.getCenter(0).getWorld().dropItemNaturally(tile.getCenter(0), new ItemStack(Material.SUNFLOWER, amount));
        item.setGlowing(true);

        Team team = player.getScoreboard().getTeam("sunflowers");

        if (team == null) {
            team = player.getScoreboard().registerNewTeam("sunflowers");
            team.setColor(ChatColor.YELLOW);
        }

        team.addEntity(item);
    }

    public void lose() {
        player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "GAME OVER", ChatColor.WHITE + "The zombies ate your brains!");
        tickTask.cancel();
        unregister();
    }

    private void addRow(Row row) {
        rows.add(row);
    }

    private void createNextSunTask() {
        Bukkit.getScheduler().runTaskLater(plugin, task -> {
            createNextSun();
            createNextSunTask();
        }, Randoms.nextInt(1, 10) * 20L);
    }

    private void createNextSun() {
        Tile tile = Randoms.choice(Randoms.choice(getRows()).getTiles());
        createSuns(tile, 1);
    }

}
