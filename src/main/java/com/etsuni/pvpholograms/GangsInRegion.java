package com.etsuni.pvpholograms;

import net.brcdev.gangs.gang.Gang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GangsInRegion {

    private Map<Gang, Long> gangsList = new HashMap<>();
    private Map<Gang, Integer> gangsKills = new HashMap<>();
    private Map<Gang, Integer> taskIds = new HashMap<>();
    private List<String> gangsStrings = new ArrayList<>();
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
        HologramUtils utils = new HologramUtils();
        utils.gangChecker(gang);
    }

    public void removeFromMaps(Gang gang) {
        gangsList.remove(gang);
        gangsKills.remove(gang);
    }

    public Map<Gang, Integer> getTaskIds() {
        return taskIds;
    }

    public List<String> getGangsStrings() {
        return gangsStrings;
    }

    public void setGangsStrings(List<String> list) {
        gangsStrings = list;
    }
}
