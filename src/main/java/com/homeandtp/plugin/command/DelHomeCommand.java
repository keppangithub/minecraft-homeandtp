package com.homeandtp.plugin.command;

import com.homeandtp.plugin.service.HomeResult;
import com.homeandtp.plugin.service.HomeService;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class DelHomeCommand implements CommandExecutor {
    private final HomeService homeService;

    public DelHomeCommand(HomeService homeService) {
        this.homeService = homeService;
    }

    @Override
    public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        // Command logic to delete a home location
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

        // Call service and handle result
        HomeResult result = homeService.deleteHome(playerUuid, homeName);

        if (result.isSuccess()) {
            player.sendMessage("Home '" + homeName + "' has been deleted successfully.");
        } else {
            player.sendMessage(result.getMessage());
        }

        return true;
    }
}
