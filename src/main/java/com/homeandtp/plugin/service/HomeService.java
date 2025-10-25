package com.homeandtp.plugin.service;

import com.homeandtp.plugin.data.HomeRepository;
import com.homeandtp.plugin.model.Home;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

public class HomeService {

    private final HomeRepository repository;
    private static final int MAX_HOMES = 5;
    /**
     * this.playerUuid = playerUuid;
        this.homeName = homeName;
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
     * @param repository
     */

    public HomeService(HomeRepository repository) {
        this.repository = repository;
    }
    public HomeResult setHome(UUID playerUuid, String homeName, String worldName, double x, double y, double z, float yaw, float pitch){
        if (!isValidHomeName(homeName)) {
            return HomeResult.failure("Invalid home name. It must be 3-16 characters long and contain only letters, numbers, and underscores.");
        }

        // Check if home already exists
        Optional<Home> existingHome = repository.getHome(playerUuid, homeName);
        if (existingHome.isPresent()) {
            // Update existing home
            Home home = new Home(playerUuid, homeName, worldName, x, y, z, yaw, pitch);
            repository.saveHome(home);
            return HomeResult.success("Home '" + homeName + "' updated successfully.", home);
        }

        // Check limit only for new homes
        if (hasReachedLimit(playerUuid)) {
            return HomeResult.failure("You have reached the maximum number of homes allowed (" + MAX_HOMES + ").");
        }

        // Create new home
        Home home = new Home(playerUuid, homeName, worldName, x, y, z, yaw, pitch);
        repository.saveHome(home);
        return HomeResult.success("Home '" + homeName + "' set successfully.", home);

    }
    public HomeResult deleteHome(UUID playerUuid, String homeName){
        boolean deleted = repository.deleteHome(playerUuid, homeName);
        if (deleted) {
            return HomeResult.success("Home deleted successfully.", null);
        } else {
            return HomeResult.failure("Home not found.");
        }
    }

    public Optional<Home> getHome(UUID playerUuid, String homeName){
        return repository.getHome(playerUuid, homeName);
    }
    public List<Home> listHomes(UUID playerUuid){
        return repository.listHomes(playerUuid);
    }
    private boolean isValidHomeName(String homeName) {
        return homeName != null && homeName.matches("^[a-zA-Z0-9_]{3,16}$");
    }
    private boolean hasReachedLimit(UUID playerUuid) {
        return repository.listHomes(playerUuid).size() >= MAX_HOMES;
    }

}

    
        


