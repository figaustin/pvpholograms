package com.etsuni.pvpholograms;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import org.bukkit.Location;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class PvPHolograms extends JavaPlugin {

    private File customConfigFile;
    private FileConfiguration customConfig;

    protected static PvPHolograms plugin;

    @Override
    public void onEnable() {
        plugin = this;
        createHologramsConfig();
        HologramUtils hologramUtils = new HologramUtils();
        hologramUtils.hologramLoop();

        this.getServer().getPluginManager().registerEvents(hologramUtils, this);
        this.getCommand("pvpholograms").setExecutor(new Commands());
    }

    @Override
    public void onDisable() {
    }

    private void createHologramsConfig() {
        customConfigFile = new File(getDataFolder(), "config.yml");
        if(!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }

        customConfig = new YamlConfiguration();

        try {
            customConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

    }

    public void saveCfg() {
        try {
            customConfig.save(customConfigFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public FileConfiguration getHologramsConfig() {
        return this.customConfig;
    }


}
