package dev.tf2levi.safemining;

import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

public class ListenerClass implements Listener {
    private final Plugin instance;

    public ListenerClass(Plugin instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onPing(final @NotNull ServerListPingEvent e) {
        e.setMotd("§a§lKözösségi Csodafaloda\n" +
                "§fAz elvarázsolt baguette!");
    }

    @EventHandler
    public void onSleep(final @NotNull PlayerBedEnterEvent e) {
        if (e.getBedEnterResult() != PlayerBedEnterEvent.BedEnterResult.OK) {
            return;
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.isSleeping()) {
                continue;
            }

            if (!p.getUniqueId().equals(e.getPlayer().getUniqueId())) {
                p.sendTitle("§4ALVÁS!", "§7Kéne aludni!", 10, 100, 20);
            }
        }
    }

    @EventHandler
    public void onTeleport(final @NotNull PlayerTeleportEvent e) {
        Player player = e.getPlayer();
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
        player.getWorld().spawnParticle(Particle.PORTAL, player.getLocation(), 30, 0, 2, 0);
    }

    @EventHandler
    public void onBlockBreak(final @NotNull BlockDropItemEvent e) {
        if (!SafeMining.getEnabledUsers().contains(e.getPlayer().getUniqueId())) {
            return;
        }

        e.setCancelled(true);

        List<Item> items = e.getItems();
        PlayerInventory inventory = e.getPlayer().getInventory();
        items.forEach(item -> {
            HashMap<Integer, ItemStack> notFit = inventory.addItem(item.getItemStack());

            if (notFit.size() > 0) {
                e.getPlayer().sendMessage(ChatColor.RED + "A tárolód megtelt így az újonnan " +
                        "kiütött itemek a földre kerülnek!");
                notFit.forEach((index, is) -> {
                    Location loc = e.getPlayer().getLocation();
                    if (loc.getWorld() != null) {
                        loc.getWorld().dropItemNaturally(loc, is);
                    }
                });
            }
        });
    }
}
