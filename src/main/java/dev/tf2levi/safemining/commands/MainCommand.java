package dev.tf2levi.safemining.commands;

import dev.tf2levi.safemining.SafeMining;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MainCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(SafeMining.getPluginPrefix() + "§4Ez a parancs csak játékosoknak elérhető!");
            return true;
        }

        if (args.length > 0) {
            String expectedArg = args[0];

            if (expectedArg.equalsIgnoreCase("enable")) {
                if (hasEnabledFeature(player)) {
                    player.sendMessage(SafeMining.getPluginPrefix() + "§4Ez a funkció már aktív!");
                    return true;
                }

                SafeMining.getEnabledUsers().add(player.getUniqueId());
                player.sendMessage(SafeMining.getPluginPrefix() + "§aFunkció aktiválva! A kikapcsoláshoz használd a §l/" + label + " disable §aparancsot.");
                return true;
            } else if (expectedArg.equalsIgnoreCase("disable")) {
                if (!hasEnabledFeature(player)) {
                    player.sendMessage(SafeMining.getPluginPrefix() + "§4Ez a funkció már inaktív! Bekapcsolhatod újra a §l/" + label + " enable §4parancsal.");
                    return true;
                }

                SafeMining.getEnabledUsers().remove(player.getUniqueId());
                player.sendMessage(SafeMining.getPluginPrefix() + "§aFunció kikapcsolva!");
                return true;
            } else {
                player.sendMessage(SafeMining.getPluginPrefix() + "§4Ez az argumentum nem érvényes ebben a konextusban!");
                return true;
            }
        }

        player.sendMessage(SafeMining.getPluginPrefix() + "§4Nincs ilyen ragumentum!");
        return true;
    }

    private boolean hasEnabledFeature(Player player) {
        return SafeMining.getEnabledUsers().contains(player.getUniqueId());
    }
}
