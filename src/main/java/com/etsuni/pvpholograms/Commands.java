package com.etsuni.pvpholograms;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            if(command.getName().equalsIgnoreCase("pvpholograms")) {
                if(args.length > 0) {
                    if(args[0].equalsIgnoreCase("create")) {

                        if(args.length > 1) {
                            String name = args[1];

                            HologramUtils hologramUtils = new HologramUtils();
                            hologramUtils.createHolo(name, ((Player) sender).getLocation());
                        }
                    }
                }
            }
        }
        return false;
    }
}
