package com.etsuni.pvpholograms;

import net.brcdev.gangs.gang.Gang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GangsInRegion {

    private Map<Gang, Integer> gangsList = new HashMap<>();
    public static GangsInRegion instance = new GangsInRegion();

    public static GangsInRegion getInstance() {
        return instance;
    }

    public Map<Gang, Integer> getGangsList() {
        return gangsList;
    }
}
