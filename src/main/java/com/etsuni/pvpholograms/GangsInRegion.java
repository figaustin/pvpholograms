package com.etsuni.pvpholograms;

import net.brcdev.gangs.gang.Gang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GangsInRegion {

    private Map<Gang, Long> gangsList = new HashMap<>();
    private Map<Gang, Integer> gangsKills = new HashMap<>();
    public static GangsInRegion instance = new GangsInRegion();

    public static GangsInRegion getInstance() {
        return instance;
    }

    public Map<Gang, Long> getGangsList() {
        return gangsList;
    }

    public Map<Gang, Integer> getGangsKills() {
        return gangsKills;
    }

    public void addToMaps(Gang gang) {
        gangsList.put(gang, System.currentTimeMillis());
        gangsKills.put(gang, 0);
    }

    public void removeFromMaps(Gang gang) {
        gangsList.remove(gang);
        gangsKills.remove(gang);
    }
}
