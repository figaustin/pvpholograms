package com.etsuni.pvpholograms;

import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramLine;
import eu.decentsoftware.holograms.api.holograms.HologramPage;
import net.brcdev.gangs.GangsPlugin;
import net.brcdev.gangs.GangsPlusApi;
import net.brcdev.gangs.gang.Gang;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.*;

import static com.etsuni.pvpholograms.PvPHolograms.plugin;

public class HologramUtils implements Listener {

    @EventHandler
    public void onKill(PlayerDeathEvent event) {
        Player killed = event.getEntity();
        Player killer = killed.getKiller();

        if(killer == null) {
            return;
        }

        if(GangsPlusApi.getPlayersGang(killer) == null) {
            return;
        }

        if(isLocationInPvpRegion(killed.getLocation())) {
            if(GangsInRegion.getInstance().getGangsKills().containsKey(GangsPlusApi.getPlayersGang(killer))) {
                //Add one to player's gangs score
                GangsInRegion.getInstance().getGangsKills().replace(GangsPlusApi.getPlayersGang(killer),
                        GangsInRegion.getInstance().getGangsKills().get(GangsPlusApi.getPlayersGang(killer)) + 1);
                try{
                    updateHolos();
                }catch (Exception e){}

            }
        }

    }

    public void updateHolos() {


        for(String s : plugin.getHologramsConfig().getStringList("holograms")) {
            Configuration config = plugin.getHologramsConfig();
            if(DHAPI.getHologram(s) != null) {

                Hologram hologram = DHAPI.getHologram(s);
                //Location location = (Location) plugin.getHologramsConfig().get("cached_locations." + s + ".location");
                //DHAPI.removeHologram(s);

                List<String> lines = new ArrayList<>();


                Map<Gang, Integer> gangsKills = sortByValue();

                int count = 0;
                for(Map.Entry<Gang, Integer> entry : gangsKills.entrySet()) {
                    if(count < config.getInt("max_size")) {

                        String line = config.getString("hologram_lines")
                                .replace("%number%", Integer.toString(gangsKills.size() - count))
                                .replace("%gang_name%", GangsPlugin.getInstance().getGangManager().getGang(entry.getKey().getName()).getName())
                                .replace("%kill_count%", entry.getValue().toString());
                        lines.add(line);
                        count++;
                    }
                }
                Collections.reverse(lines);
                lines.add(0, plugin.getHologramsConfig().getString("hologram_title"));
                //createHolo(s, location, lines);
                try {
                    DHAPI.setHologramLines(hologram, lines);
                } catch (Exception e) {}

            }
        }
    }

    public HashMap<Gang, Integer> sortByValue() {
        List<Map.Entry<Gang, Integer>> list = new LinkedList<Map.Entry<Gang, Integer>>(GangsInRegion.getInstance().getGangsKills().entrySet());

        Collections.sort(list, new Comparator<Map.Entry<Gang, Integer>>() {
            @Override
            public int compare(Map.Entry<Gang, Integer> o1, Map.Entry<Gang, Integer> o2) {
                return (o1.getValue().compareTo(o2.getValue()));
            }
        });

        HashMap<Gang, Integer> temp = new LinkedHashMap<>();
        for(Map.Entry<Gang, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    public void hologramLoop() {

        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();

        //Check if new gang in region
        scheduler.runTaskTimerAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                if(Bukkit.getWorld(plugin.getHologramsConfig().getString("world_name")).getPlayers() != null) {
                    for (Player player : Bukkit.getWorld(plugin.getHologramsConfig().getString("world_name")).getPlayers()) {
                            if (isLocationInPvpRegion(player.getLocation())) {

                                Gang gang = GangsPlusApi.getPlayersGang(player);
                                if (!GangsInRegion.getInstance().getGangsList().containsKey(gang)) {
                                    GangsInRegion.getInstance().addToMaps(gang);
                                    try {
                                        updateHolos();
                                    } catch (Exception e) {

                                    }
                                }
                            }
                        }
                    }
                }
        },0, 20);

        scheduler.runTaskTimerAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                for(Gang gang : GangsInRegion.getInstance().getGangsList().keySet()) {
                    if(gang == null) {
                        continue;
                    }
                    if(!isGangInRegion(gang)) {
                        GangsInRegion.getInstance().removeFromMaps(gang);
                        try{
                            updateHolos();
                        } catch (Exception e){

                        }
                    }
                }
            }
        },0, plugin.getHologramsConfig().getInt("gang_remove_time") * 20L);
    }

    public Boolean isLocationInPvpRegion(Location location) {
        RegionContainer container = WorldGuardPlugin.inst().getRegionContainer();
        World bukkitWorld = Bukkit.getWorld("world");
        RegionManager manager = container.get(bukkitWorld);
        ProtectedRegion region = manager.getRegion("pvp");

        return region.contains(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public Boolean isGangInRegion(Gang gang) {
        Set<Player> gangMembers = gang.getOnlineMembers();
        boolean gangInRegion = false;

        for(Player player : gangMembers) {
            Location location = player.getLocation();
            if(isLocationInPvpRegion(location)) {
                gangInRegion = true;
            }
        }
        return gangInRegion;
    }
}
