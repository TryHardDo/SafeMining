package dev.tf2levi.safemining;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dev.tf2levi.safemining.commands.MainCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public final class SafeMining extends JavaPlugin {
    public static String pluginPrefix = "§a§lSafeMining §7● ";
    private static SafeMining instance;
    private static List<UUID> enabledUsers = new ArrayList<>();
    private static Logger pluginLogger;
    private static File dataFile;

    public static SafeMining getInstance() {
        return instance;
    }

    public static List<UUID> getEnabledUsers() {
        return enabledUsers;
    }

    public static Logger getPluginLogger() {
        return pluginLogger;
    }

    public static String getPluginPrefix() {
        return pluginPrefix;
    }

    @Override
    public void onEnable() {
        instance = this;
        pluginLogger = this.getLogger();
        dataFile = new File(this.getDataFolder(), "data.json");

        Bukkit.getConsoleSender().sendMessage(
                "§8+",
                "§8| §eSafeMining §6" + this.getDescription().getVersion(),
                "§8| §eCreated by: §6tf2levi",
                "§8+"
        );

        init();
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
            getPluginLogger().severe("Hiba történt a I/O művelet közben!");
            e.printStackTrace();
        }
    }

    private void loadData() {
        if (!dataFile.exists()) {
            if (dataFile.getParentFile().mkdirs()) {
                getPluginLogger().info("Parent directories are generated successfully!");
            }

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
            enabledUsers = gson.fromJson(contentBuilder.toString(), new TypeToken<ArrayList<UUID>>() {
            }.getType());
            getPluginLogger().info("Loaded " + enabledUsers.size() + " enabled user UUIDs from " + dataFile.getName());
        } catch (IOException e) {
            getPluginLogger().severe("Hiba történt a I/O művelet közben!");
            e.printStackTrace();
        }
    }
}
