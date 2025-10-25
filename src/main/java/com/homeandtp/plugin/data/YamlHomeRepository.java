package com.homeandtp.plugin.data;

import com.homeandtp.plugin.model.Home;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import java.io.File;
import org.bukkit.configuration.file.YamlConfiguration;
import java.util.ArrayList;

public class YamlHomeRepository implements HomeRepository {
    private final File dataFolder;

    public YamlHomeRepository(File dataFolder) {
        this.dataFolder = dataFolder;
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
    }

    private File getPlayerFile(UUID playerUuid) {
        return new File(dataFolder, playerUuid.toString() + ".yml");
    }

    @Override
    public void saveHome(Home home) {
        File playerFile = getPlayerFile(home.getPlayerUuid());
        // Implementation for saving home to YAML
        YamlConfiguration config = YamlConfiguration.loadConfiguration(playerFile);
        String homeName = home.getHomeName();
        config.set(homeName + ".world", home.getWorldName());
        config.set(homeName + ".x", home.getX());
        config.set(homeName + ".y", home.getY());
        config.set(homeName + ".z", home.getZ());
        config.set(homeName + ".yaw", home.getYaw());
        config.set(homeName + ".pitch", home.getPitch());
        try {
            config.save(playerFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean deleteHome(UUID playerUuid, String homeName) {
        // Implementation for deleting home from YAML
        File playerFile = getPlayerFile(playerUuid);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(playerFile);
        if (config.contains(homeName)) {
            config.set(homeName, null);
            try {
                config.save(playerFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    @Override
    public Optional<Home> getHome(UUID playerUuid, String homeName) {
        File playerFile = getPlayerFile(playerUuid);
        
        if (!playerFile.exists()) {
            return Optional.empty();
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(playerFile);
        if (config.contains(homeName)) {
            Home home = createHomeFromConfig(config, playerUuid, homeName);
            return Optional.of(home);
        }
        return Optional.empty();
    }

    @Override
    public List<Home> listHomes(UUID playerUuid) {
        File playerFile = getPlayerFile(playerUuid);

        if (!playerFile.exists()) {
            return List.of();
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(playerFile);
        List<Home> homes = new ArrayList<>();
        for (String homeName : config.getKeys(false)) {
            Home home = createHomeFromConfig(config, playerUuid, homeName);
            homes.add(home);
        }
        return homes;
    }

    private Home createHomeFromConfig(YamlConfiguration config, UUID playerUuid, String homeName) {
        String world = config.getString(homeName + ".world");
        double x = config.getDouble(homeName + ".x");
        double y = config.getDouble(homeName + ".y");
        double z = config.getDouble(homeName + ".z");
        float yaw = (float) config.getDouble(homeName + ".yaw");
        float pitch = (float) config.getDouble(homeName + ".pitch");
        return new Home(playerUuid, homeName, world, x, y, z, yaw, pitch);
    }
}