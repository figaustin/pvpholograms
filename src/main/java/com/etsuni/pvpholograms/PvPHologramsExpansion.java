package com.etsuni.pvpholograms;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class PvPHologramsExpansion extends PlaceholderExpansion {
    private final PvPHolograms plugin;

    public PvPHologramsExpansion(PvPHolograms plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "dominatinggangs";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Etsuni";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if(params.equalsIgnoreCase("first")) {
            return plugin.getGangString().get(0);
        }

        if(params.equalsIgnoreCase("second")) {
            return plugin.getGangString().get(1);
        }

        if(params.equalsIgnoreCase("third")) {
            return plugin.getGangString().get(2);
        }

        if(params.equalsIgnoreCase("fourth")) {
            return plugin.getGangString().get(3);
        }

        if(params.equalsIgnoreCase("fifth")) {
            return plugin.getGangString().get(4);
        }
        return null;
    }
}
