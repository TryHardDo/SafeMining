package dev.tf2levi.safemining.commands;

import dev.tf2levi.safemining.SafeMining;
import org.bukkit.Bukkit;
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
                if (args.length == 2) {
                    Player targetPlayer = Bukkit.getPlayer(args[1]);

                    if (targetPlayer == null) {
                        player.sendMessage(SafeMining.getPluginPrefix() + "§4Nincs ilyen játékos!");
                        return true;
                    }

                    if (hasEnabledFeature(targetPlayer)) {
                        player.sendMessage(SafeMining.getPluginPrefix() + "§4A funkció már aktív a céljátékosnál!");
                        return true;
                    }

                    SafeMining.getEnabledUsers().add(targetPlayer.getUniqueId());
                    targetPlayer.sendMessage(SafeMining.getPluginPrefix() + "§aEgy adminisztrátor bekapcsolta a biztonságos bányászat opciót!");
                    return true;
                }

                if (hasEnabledFeature(player)) {
                    player.sendMessage(SafeMining.getPluginPrefix() + "§4Ez a funkció már aktív!");
                    return true;
                }

                SafeMining.getEnabledUsers().add(player.getUniqueId());
                player.sendMessage(SafeMining.getPluginPrefix() + "§aFunkció aktiválva! A kikapcsoláshoz használd a §l/" + label + " disable §aparancsot.");
                return true;
            } else if (expectedArg.equalsIgnoreCase("disable")) {
                if (args.length == 2) {
                    Player targetPlayer = Bukkit.getPlayer(args[1]);

                    if (targetPlayer == null) {
                        player.sendMessage(SafeMining.getPluginPrefix() + "§4Nincs ilyen játékos!");
                        return true;
                    }

                    if (!hasEnabledFeature(targetPlayer)) {
                        player.sendMessage(SafeMining.getPluginPrefix() + "§4A funkció már inaktív a céljátékosnál!");
                        return true;
                    }

                    SafeMining.getEnabledUsers().remove(targetPlayer.getUniqueId());
                    targetPlayer.sendMessage(SafeMining.getPluginPrefix() + "§aEgy adminisztrátor kikapcsolta a biztonságos bányászat opciót!");
                    return true;
                }

                if (!hasEnabledFeature(player)) {
                    player.sendMessage(SafeMining.getPluginPrefix() + "§4Ez a funkció már inaktív! Bekapcsolhatod újra a §l/" + label + " enable §4parancsal.");
                    return true;
                }

                SafeMining.getEnabledUsers().remove(player.getUniqueId());
                player.sendMessage(SafeMining.getPluginPrefix() + "§aFunció kikapcsolva!");
                return true;
            } else {
                player.sendMessage(SafeMining.getPluginPrefix() + "§4Ez az argumentum nem érvényes ebben a kontextusban!");
                return true;
            }
        }

        player.sendMessage(SafeMining.getPluginPrefix() + "§4Nincs ilyen argumentum!");
        return true;
    }

    private boolean hasEnabledFeature(Player player) {
        return SafeMining.getEnabledUsers().contains(player.getUniqueId());
    }
}
