package com.homeandtp.plugin.command;

import org.bukkit.command.CommandExecutor;

import com.homeandtp.plugin.service.HomeService;
import org.bukkit.entity.Player;
import java.util.UUID;
import com.homeandtp.plugin.service.HomeResult;

public class setHomeCommand implements CommandExecutor {
    private final HomeService homeService;

    public setHomeCommand(HomeService homeService) {
        this.homeService = homeService;
    }

    @Override
    public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String label,String[] args) {
        // Command logic to set a home location

        if (!(sender instanceof Player)) {
            sender.sendMessage("A non player can't set home");
            return true;
        }

        Player player = (Player) sender;
        UUID playerUuid = player.getUniqueId();
        String homeName;

        if (args.length == 0 ) {
            homeName = "home";
        } else {
            homeName = args[0];
        }

        // Check if world is null
        if (player.getLocation().getWorld() == null) {
            player.sendMessage("Unable to determine your current world.");
            return true;
        }

        String world = player.getLocation().getWorld().getName();

        double x = player.getLocation().getX();
        double y = player.getLocation().getY();
        double z = player.getLocation().getZ();
        float yaw = (float) player.getLocation().getYaw();
        float pitch = (float) player.getLocation().getPitch();

        // Call service and handle result
        HomeResult result = homeService.setHome(playerUuid, homeName, world, x, y, z, yaw, pitch);
        player.sendMessage(result.getMessage());

        return true; 
    }
}
