package com.homeandtp;

import com.homeandtp.plugin.command.HomeCommand;
import com.homeandtp.plugin.command.setHomeCommand;
import com.homeandtp.plugin.data.YamlHomeRepository;
import com.homeandtp.plugin.service.HomeService;
import org.bukkit.plugin.java.JavaPlugin;

public class HomeAndTPPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Initialize data folder
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        // Create repository and service
        YamlHomeRepository homeRepository = new YamlHomeRepository(getDataFolder());
        HomeService homeService = new HomeService(homeRepository);

        // Register commands
        getCommand("sethome").setExecutor(new setHomeCommand(homeService));
        getCommand("home").setExecutor(new HomeCommand(homeService));

        // Log success
        getLogger().info("HomeAndTP Plugin has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("HomeAndTP Plugin has been disabled!");
    }
}
