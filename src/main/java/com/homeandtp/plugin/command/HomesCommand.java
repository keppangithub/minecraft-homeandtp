package com.homeandtp.plugin.command;

import com.homeandtp.plugin.model.Home;
import com.homeandtp.plugin.service.HomeService;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class HomesCommand implements CommandExecutor {
    private final HomeService homeService;

    public HomesCommand(HomeService homeService) {
        this.homeService = homeService;
    }

    @Override
    public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        // Command logic to list all home locations
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        UUID playerUuid = player.getUniqueId();

        // Get all homes for the player
        List<Home> homes = homeService.listHomes(playerUuid);

        if (homes.isEmpty()) {
            player.sendMessage("You don't have any homes set.");
            return true;
        }

        // Display header
        player.sendMessage("Your homes (" + homes.size() + "/5):");

        // List each home with its location
        for (Home home : homes) {
            String location = String.format("%s: %s (%.1f, %.1f, %.1f)",
                    home.getHomeName(),
                    home.getWorldName(),
                    home.getX(),
                    home.getY(),
                    home.getZ());
            player.sendMessage("  - " + location);
        }

        return true;
    }
}
