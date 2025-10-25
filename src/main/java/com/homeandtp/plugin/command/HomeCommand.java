package com.homeandtp.plugin.command;

import com.homeandtp.plugin.model.Home;
import com.homeandtp.plugin.service.HomeService;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

public class HomeCommand implements CommandExecutor {
    private final HomeService homeService;

    public HomeCommand(HomeService homeService) {
        this.homeService = homeService;
    }

    @Override
    public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        // Command logic to teleport to a home location
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        UUID playerUuid = player.getUniqueId();
        String homeName;

        // Get home name from args or default to "home"
        if (args.length == 0) {
            homeName = "home";
        } else {
            homeName = args[0];
        }

        // Try to get the home
        Optional<Home> homeOptional = homeService.getHome(playerUuid, homeName);

        if (!homeOptional.isPresent()) {
            player.sendMessage("Home '" + homeName + "' not found.");
            return true;
        }

        Home home = homeOptional.get();

        // Get the world
        World world = Bukkit.getWorld(home.getWorldName());
        if (world == null) {
            player.sendMessage("The world '" + home.getWorldName() + "' for home '" + homeName + "' no longer exists.");
            return true;
        }

        // Create location and teleport
        Location location = new Location(world, home.getX(), home.getY(), home.getZ(), home.getYaw(), home.getPitch());
        player.teleport(location);
        player.sendMessage("Teleported to home '" + homeName + "'.");

        return true;
    }
}