package dev.tf2levi.safemining.commands;

import dev.tf2levi.safemining.SafeMining;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Dictionary;
import java.util.HashMap;

public class MainCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Ez a parancs csak játékosoknak elérhető!");
            return true;
        }

        Player player = ((Player) sender);
        if (args.length > 0) {
            String expectedArg = args[0];

            if (expectedArg.equalsIgnoreCase("enable")) {
                if (hasEnabledFeature(player)) {
                    player.sendMessage(ChatColor.RED + "Ez a funkció már aktív!");
                    return true;
                }

                SafeMining.getEnabledUsers().add(player.getUniqueId());
                player.sendMessage(ChatColor.GREEN + "Funkció aktiválva!");
                return true;
            } else if (expectedArg.equalsIgnoreCase("disable")) {
                if (!hasEnabledFeature(player)) {
                    player.sendMessage(ChatColor.RED + "Ez a funkció már inaktív!");
                    return true;
                }

                SafeMining.getEnabledUsers().remove(player.getUniqueId());
                player.sendMessage(ChatColor.GREEN + "Funció kikapcsolva!");
                return true;
            } else {
                player.sendMessage(ChatColor.RED + "Ez az argumentum nem érvényes ebben a konextusban!");
                return true;
            }
        }

        player.sendMessage(ChatColor.RED + "Nincs ilyen ragumentum!");
        return true;
    }

    private boolean hasEnabledFeature(Player player) {
        return SafeMining.getEnabledUsers().contains(player.getUniqueId());
    }
}
