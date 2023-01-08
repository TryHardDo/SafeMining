package dev.tf2levi.safemining;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
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
    public void onTeleport(final @NotNull PlayerTeleportEvent e) {
        Player player = e.getPlayer();
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
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
