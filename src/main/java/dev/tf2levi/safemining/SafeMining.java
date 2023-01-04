package dev.tf2levi.safemining;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dev.tf2levi.safemining.commands.MainCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Stream;

public final class SafeMining extends JavaPlugin {
    private static SafeMining instance;
    private static List<UUID> enabledUsers = new ArrayList<>();
    private static Logger pluginLogger;
    private static File dataFile;
    @Override
    public void onEnable() {
        instance = this;
        pluginLogger = this.getLogger();
        dataFile = new File(this.getDataFolder() ,"data.json");

        Bukkit.getConsoleSender().sendMessage(
                ChatColor.AQUA + "SafeMining" + this.getDescription().getVersion(),
                ChatColor.GOLD + "Created by: tf2levi"
        );

        this.init();
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        saveData();
    }

    private void init() {
        Bukkit.getPluginManager().registerEvents(new ListenerClass(this), this);
        this.getCommand("safemining").setExecutor(new MainCommand());
        loadData();
    }

    private void saveData() {
        try (final BufferedWriter writer = Files.newBufferedWriter(dataFile.toPath(), StandardCharsets.UTF_8, StandardOpenOption.CREATE)) {
            Gson gson = new Gson();
            String jsonString = gson.toJson(enabledUsers);
            writer.write(jsonString, 0, jsonString.length());
            writer.flush();

            getPluginLogger().info("Data saved to " + dataFile.getPath() + " wrote " + dataFile.length() + " bytes.");
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }

    private void loadData() {
        if (!dataFile.exists()) {
            saveData();
            return;
        }

        try (final BufferedReader reader = Files.newBufferedReader(dataFile.toPath())) {
            StringBuilder contentBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line);
            }

            Gson gson = new Gson();
            enabledUsers = gson.fromJson(contentBuilder.toString(), new TypeToken<ArrayList<UUID>>(){}.getType());
            getPluginLogger().info("Loaded " + enabledUsers.size() + " enabled user UUIDs from " + dataFile.getName());
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }

    /*
     * Static
     * GETTERS / SETTERS
     */
    public static SafeMining getInstance() {
        return instance;
    }

    public static List<UUID> getEnabledUsers() {
        return enabledUsers;
    }

    public static Logger getPluginLogger() {
        return pluginLogger;
    }
}
