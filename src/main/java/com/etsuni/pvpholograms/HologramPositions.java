package com.etsuni.pvpholograms;

import eu.decentsoftware.holograms.api.holograms.Hologram;
import org.bukkit.Location;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HologramPositions {

    private Map<Hologram, Location> positions = new HashMap<>();
    private static HologramPositions instance = new HologramPositions();

    public static HologramPositions getInstance() {
        return instance;
    }

    public Map<Hologram, Location> getPositions() {
        return positions;
    }


}
