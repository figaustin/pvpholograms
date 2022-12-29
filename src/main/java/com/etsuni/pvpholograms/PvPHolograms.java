package com.etsuni.pvpholograms;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public final class PvPHolograms extends JavaPlugin {

    private File customConfigFile;
    private FileConfiguration customConfig;

    protected static PvPHolograms plugin;

    private static final Logger log = Logger.getLogger("Minecraft");

    @Override
    public void onEnable() {
        plugin = this;
        createHologramsConfig();
        cacheHolos();
        HologramUtils hologramUtils = new HologramUtils();
        hologramUtils.updateHolos();
        hologramUtils.hologramLoop();

        this.getServer().getPluginManager().registerEvents(hologramUtils, this);
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

    public void cacheHolos() {
        log.info(String.format("[%s] Trying to cache holograms...", getDescription().getName()));
        BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                for(String s : customConfig.getStringList("holograms")) {
                    Hologram hologram = DHAPI.getHologram(s);

                    if(hologram == null) {
                        continue;
                    }
                    log.info(String.format("[%s] Cached " + s + " hologram", getDescription().getName()));
                    HologramPositions.getInstance().getPositions().put(hologram, hologram.getLocation());
                }
            }
        }, 60);
    }

}
