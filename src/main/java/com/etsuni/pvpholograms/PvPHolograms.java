package com.etsuni.pvpholograms;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public final class PvPHolograms extends JavaPlugin {

    private File customConfigFile;
    private FileConfiguration customConfig;

    protected static PvPHolograms plugin;

    private static final Logger log = Logger.getLogger("Minecraft");

    private final HologramUtils hologramUtils = new HologramUtils();

    @Override
    public void onEnable() {
        if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PvPHologramsExpansion(this).register();
        }
        plugin = this;
        createHologramsConfig();
        hologramUtils.updateList();
        hologramUtils.hologramLoop();

        this.getServer().getPluginManager().registerEvents(hologramUtils, this);
    }

    @Override
    public void onDisable() {
    }

    public List<String> getGangString() {
        return GangsInRegion.getInstance().getGangsStrings();
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
